package com.redhat.labs.lodestar.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Optional;

import lombok.ToString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@ToString(callSuper = true)
public class ConfigMap {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigMap.class);

    private Path path;
    private String filePath;
    @Getter
    @Builder.Default
    private Optional<String> content = Optional.empty();

    /**
     * The last modified date is flakely in-container. Instead, read file
     * each time and update when changed. Limited reads via scheduler
     * @return true if the content changed
     */
    public boolean readAndUpdateMountedFile() {
        String currentContent = content.isPresent() ? content.get() : "";

        if (!checkPath()) {
            LOGGER.warn("Unable to read file {}", filePath);
            return false;
        }

        try {
            String newContent = Files.readString(path, StandardCharsets.UTF_8);
            content = Optional.of(newContent);
            LOGGER.trace("content match ({}) = {} ", filePath, currentContent.equals(newContent));

            return !currentContent.equals(newContent);
        } catch (IOException e) {
            LOGGER.error("Error updating mounted file %{} {} ", path, filePath);
            content = Optional.empty();
        }

        return false;
    }

    public boolean checkPath() {
        if (null == path) {
            path = Paths.get(filePath);
        }

        return Files.isReadable(path);
    }

}
