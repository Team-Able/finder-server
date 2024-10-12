package com.finder.global.config.upload;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "spring.upload")
@RequiredArgsConstructor
public class UploadProperties {
    private final String path;
}
