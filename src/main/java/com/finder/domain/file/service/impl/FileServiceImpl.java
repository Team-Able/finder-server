package com.finder.domain.file.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.finder.domain.file.error.FileError;
import com.finder.domain.file.service.FileService;
import com.finder.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = generateUniqueFileName("finder/images/", originalFilename);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new CustomException(FileError.FILE_UPLOAD_FAILED);
        }
    }

    private String generateUniqueFileName(String prefix, String originalFilename) {
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = prefix + baseName + "." + extension;

        int count = 0;
        while (exists(fileName)) {
            count++;
            fileName = prefix + baseName + "(" + count + ")." + extension;
        }

        return fileName;
    }

    private boolean exists(String fileName) {
        try {
            amazonS3Client.getObjectMetadata(bucket, fileName);
            return true;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                return false;
            }

            throw e;
        }
    }
}
