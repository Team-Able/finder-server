package com.finder.domain.item.service.impl;

import com.finder.domain.item.domain.entity.ItemEntity;
import com.finder.domain.item.domain.enums.ItemStatus;
import com.finder.domain.item.dto.response.ItemResponse;
import com.finder.domain.item.repository.ItemRepository;
import com.finder.domain.item.service.ItemSearchService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemSearchServiceImpl implements ItemSearchService {
    private static final int MAX_EDIT_DISTANCE = 2;
    private static final int MAX_SUGGESTIONS = 10;
    private static final double JAMO_SIMILARITY_THRESHOLD = 0.7;
    private static final long CACHE_DURATION_MILLIS = 300000; // 5분
    private static final char[] CHOSUNG_LIST = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
            'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private static final char[] JUNGSUNG_LIST = {
            'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
            'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ', 'ㅣ'
    };
    private static final char[] JONGSUNG_LIST = {
            ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ', 'ㄻ',
            'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ', 'ㅆ', 'ㅇ',
            'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };
    private final ItemRepository itemRepository;
    private final Map<Long, SearchDocument> searchIndex = new ConcurrentHashMap<>();
    private final Map<String, CacheEntry<List<String>>> autoCompleteCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        updateIndex();
    }

    @Scheduled(fixedRate = 300000) // 5분마다 인덱스 업데이트
    public void updateIndex() {
        List<ItemEntity> items = itemRepository.findAllByStatus(ItemStatus.LOST);
        items.forEach(this::indexItem);
        cleanExpiredCache();
    }

    private void indexItem(ItemEntity item) {
        String normalizedTitle = item.getTitle().toLowerCase();
        SearchDocument doc = new SearchDocument(
                item.getId(),
                item.getTitle(),
                item.getContent(),
                normalizedTitle,
                item.getContent().toLowerCase(),
                extractChosung(normalizedTitle),
                extractWordChosungs(normalizedTitle),
                decompose(normalizedTitle),
                generateNGrams(normalizedTitle, 2),
                getInitials(normalizedTitle)
        );
        searchIndex.put(item.getId(), doc);
    }

    private void cleanExpiredCache() {
        autoCompleteCache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    @Override
    public List<ItemResponse> searchItem(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedKeyword = keyword.toLowerCase().trim();
        String keywordJamo = decompose(normalizedKeyword);

        return searchIndex.values().stream()
                .map(doc -> new AbstractMap.SimpleEntry<>(doc, calculateScore(doc, normalizedKeyword, keywordJamo)))
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.<SearchDocument, Double>comparingByValue().reversed())
                .map(entry -> ItemResponse.of(itemRepository.findById(entry.getKey().getId())
                        .orElseThrow(() -> new RuntimeException("Item not found"))))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAutoCompleteSuggestions(String prefix) {
        if (prefix == null || prefix.trim().isEmpty()) {
            return Collections.emptyList();
        }

        String normalizedPrefix = prefix.toLowerCase().trim();

        // 캐시 확인
        CacheEntry<List<String>> cacheEntry = autoCompleteCache.get(normalizedPrefix);
        if (cacheEntry != null && !cacheEntry.isExpired()) {
            return cacheEntry.getValue();
        }

        // 새로운 결과 계산
        String prefixJamo = decompose(normalizedPrefix);
        List<String> suggestions = searchIndex.values().stream()
                .map(doc -> new AbstractMap.SimpleEntry<>(
                        doc.getTitle(),
                        calculateAutoCompleteScore(doc, normalizedPrefix, prefixJamo)
                ))
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(MAX_SUGGESTIONS)
                .collect(Collectors.toList());

        // 결과 캐싱
        autoCompleteCache.put(normalizedPrefix,
                new CacheEntry<>(suggestions, System.currentTimeMillis() + CACHE_DURATION_MILLIS));

        return suggestions;
    }

    private double calculateScore(SearchDocument doc, String keyword, String keywordJamo) {
        double score = 0.0;

        // 1. 정확한 매칭 (100점)
        if (doc.getNormalizedTitle().contains(keyword)) {
            score += 100.0;
            if (startsWithWord(doc.getNormalizedTitle(), keyword)) {
                score += 50.0;
            }
        }

        // 2. 초성 매칭 (80점)
        if (isAllChosung(keyword)) {
            if (doc.getTitleChosung().contains(keyword)) {
                score += 80.0;
                if (doc.getWordChosungs().contains(keyword)) {
                    score += 40.0;
                }
            }
        }

        // 3. 자모 유사도 매칭 (70점)
        double jamoSimilarity = calculateJamoSimilarity(doc.getTitleJamo(), keywordJamo);
        if (jamoSimilarity >= JAMO_SIMILARITY_THRESHOLD) {
            score += 70.0 * jamoSimilarity;
        }

        // 4. N-gram 매칭 (60점)
        List<String> keywordNGrams = generateNGrams(keyword, 2);
        double ngramScore = calculateNGramSimilarity(doc.getTitleNGrams(), keywordNGrams);
        score += 60.0 * ngramScore;

        // 5. 편집 거리 기반 퍼지 매칭 (40점)
        int distance = calculateLevenshteinDistance(doc.getNormalizedTitle(), keyword);
        if (distance <= MAX_EDIT_DISTANCE) {
            score += 40.0 * (1.0 - (double) distance / MAX_EDIT_DISTANCE);
        }

        // 6. 내용 매칭 (20점)
        if (doc.getNormalizedContent().contains(keyword)) {
            score += 20.0;
        }

        return score;
    }

    private double calculateAutoCompleteScore(SearchDocument doc, String prefix, String prefixJamo) {
        double score = 0.0;

        // 1. 일반 prefix 매칭
        if (doc.getNormalizedTitle().startsWith(prefix)) {
            score = 100.0;
        } else if (doc.getNormalizedTitle().contains(prefix)) {
            score = 80.0;
            if (startsWithWord(doc.getNormalizedTitle(), prefix)) {
                score = 90.0;
            }
        }

        // 2. 초성 매칭
        if (isAllChosung(prefix)) {
            if (doc.getTitleChosung().startsWith(prefix)) {
                score = Math.max(score, 70.0);
            } else if (doc.getWordChosungs().stream().anyMatch(c -> c.startsWith(prefix))) {
                score = Math.max(score, 60.0);
            }
        }

        // 3. 자모 유사도
        double jamoSimilarity = calculateJamoSimilarity(
                doc.getTitleJamo().substring(0, Math.min(doc.getTitleJamo().length(), prefixJamo.length())),
                prefixJamo
        );
        if (jamoSimilarity >= JAMO_SIMILARITY_THRESHOLD) {
            score = Math.max(score, 50.0 * jamoSimilarity);
        }

        return score;
    }

    private String decompose(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (isHangul(ch)) {
                int unicode = ch - 0xAC00;
                int cho = unicode / (21 * 28);
                int jung = (unicode % (21 * 28)) / 28;
                int jong = unicode % 28;

                result.append(CHOSUNG_LIST[cho])
                        .append(JUNGSUNG_LIST[jung]);
                if (jong > 0) {
                    result.append(JONGSUNG_LIST[jong]);
                }
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String extractChosung(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (isHangul(ch)) {
                int cho = (ch - 0xAC00) / (21 * 28);
                result.append(CHOSUNG_LIST[cho]);
            }
        }
        return result.toString();
    }

    private List<String> extractWordChosungs(String text) {
        List<String> chosungs = new ArrayList<>();
        String[] words = text.split("\\s+");

        for (String word : words) {
            if (!word.isEmpty()) {
                StringBuilder wordChosung = new StringBuilder();
                for (char ch : word.toCharArray()) {
                    if (isHangul(ch)) {
                        int cho = (ch - 0xAC00) / (21 * 28);
                        wordChosung.append(CHOSUNG_LIST[cho]);
                    } else {
                        wordChosung.append(ch);
                    }
                }
                if (!wordChosung.isEmpty()) {
                    chosungs.add(wordChosung.toString());
                }
            }
        }
        return chosungs;
    }

    private String getInitials(String text) {
        return Arrays.stream(text.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .map(word -> String.valueOf(
                        Character.toLowerCase(word.charAt(0))
                ))
                .collect(Collectors.joining());
    }

    private boolean isHangul(char ch) {
        return ch >= 0xAC00 && ch <= 0xD7A3;
    }

    private boolean isAllChosung(String text) {
        return text.chars()
                .mapToObj(ch -> String.valueOf((char) ch))
                .allMatch(c -> c.matches("[ㄱ-ㅎ]"));
    }

    private List<String> generateNGrams(String text, int n) {
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i < text.length() - n + 1; i++) {
            ngrams.add(text.substring(i, i + n));
        }
        return ngrams;
    }

    private double calculateNGramSimilarity(List<String> ngrams1, List<String> ngrams2) {
        Set<String> set1 = new HashSet<>(ngrams1);
        Set<String> set2 = new HashSet<>(ngrams2);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    private double calculateJamoSimilarity(String s1, String s2) {
        int matchCount = 0;
        int totalLength = Math.max(s1.length(), s2.length());
        int minLength = Math.min(s1.length(), s2.length());

        for (int i = 0; i < minLength; i++) {
            if (s1.charAt(i) == s2.charAt(i)) {
                matchCount++;
            }
        }

        return (double) matchCount / totalLength;
    }

    private boolean startsWithWord(String text, String prefix) {
        int index = text.indexOf(prefix);
        if (index == 0) {
            return true;
        }
        if (index > 0) {
            char beforeChar = text.charAt(index - 1);
            return Character.isWhitespace(beforeChar) ||
                    !Character.isLetterOrDigit(beforeChar);
        }
        return false;
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[] prev = new int[s2.length() + 1];
        int[] curr = new int[s2.length() + 1];

        // 첫 행 초기화
        for (int j = 0; j <= s2.length(); j++) {
            prev[j] = j;
        }

        // 나머지 행 계산
        for (int i = 1; i <= s1.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(
                        Math.min(
                                curr[j - 1] + 1,     // 삽입
                                prev[j] + 1         // 삭제
                        ),
                        prev[j - 1] + cost     // 대체
                );
            }
            // prev와 curr 배열 스왑
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[s2.length()];
    }

    @Data
    @AllArgsConstructor
    private static class SearchDocument {
        private Long id;
        private String title;
        private String content;
        private String normalizedTitle;
        private String normalizedContent;
        private String titleChosung;
        private List<String> wordChosungs;
        private String titleJamo;
        private List<String> titleNGrams;
        private String initials;
    }

    @Data
    @AllArgsConstructor
    private static class CacheEntry<T> {
        private T value;
        private long expirationTime;

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}
