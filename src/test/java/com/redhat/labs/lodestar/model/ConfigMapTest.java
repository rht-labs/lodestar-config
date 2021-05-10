package com.redhat.labs.lodestar.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

class ConfigMapTest {

    final static String READABLE_FILE = "src/test/resources/simple-config.yaml";
    final static String UNREADABLE_FILE = "/ad;lfjasdf";

    @Test void testConfigMapNotReadable() {

        ConfigMap cm = ConfigMap.builder().filePath(UNREADABLE_FILE).build();

        boolean modified = cm.readMountedFile();
        assertFalse(modified);
        assertTrue(cm.getContent().isEmpty());
        

    }

    @Test void testConfigMapNotModified() throws IOException {

        ConfigMap cm = ConfigMap.builder().lastModifiedTime(getLastModifiedTime(READABLE_FILE)).filePath(READABLE_FILE).build();
        

        boolean modified = cm.readMountedFile();
        assertFalse(modified);
        assertTrue(cm.getContent().isEmpty());  

    }

    @Test void testConfigMapModified() {
        
        ConfigMap cm = ConfigMap.builder().filePath(READABLE_FILE).build();

        boolean modified = cm.readMountedFile();
        assertTrue(modified);
        assertTrue(cm.getContent().isPresent());
        
    }

    private long getLastModifiedTime(String file) throws IOException {
        return Files.getLastModifiedTime(Paths.get(file)).toMillis();
    }

}
