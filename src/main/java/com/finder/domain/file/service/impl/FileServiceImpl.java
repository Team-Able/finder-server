package com.finder.domain.file.service.impl;

import com.finder.domain.file.error.FileError;
import com.finder.domain.file.service.FileService;
import com.finder.global.config.upload.UploadProperties;
import com.finder.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final UploadProperties uploadProperties;

    @Override
    public String uploadFile(MultipartFile file) {
        String basename = FilenameUtils.getBaseName(file.getOriginalFilename());
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String filename = generateFilename(basename, extension);
        File directory = new File(uploadProperties.getPath());
        File f = new File(directory, filename);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            file.transferTo(f);
        } catch (IOException e) {
            throw new CustomException(FileError.FILE_UPLOAD_FAILED);
        }

        return filename;
    }

    private String generateFilename(String filename, String extension) {
        String newFilename = filename + "." + extension;
        int i = 1;

        while (Files.exists(Paths.get(uploadProperties.getPath() + newFilename))) {
            newFilename = filename + "(" + i++ + ")." + extension;
        }

        return newFilename;
    }
}
