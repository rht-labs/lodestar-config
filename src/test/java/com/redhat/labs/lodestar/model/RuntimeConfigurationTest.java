package com.redhat.labs.lodestar.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

class RuntimeConfigurationTest {

    @Test
    void testGetConfigurationUnreadableFile() {

        RuntimeConfiguration rc = RuntimeConfiguration.builder().filePath("unknown-config").build();
        assertNotNull(rc);

        Map<String, Object> config = rc.getConfiguration();
        assertNotNull(config);

        assertTrue(config.keySet().isEmpty());

    }

    @Test
    void testGetConfigurationLoadSuccessful() {

        RuntimeConfiguration rc = RuntimeConfiguration.builder().filePath("src/test/resources/simple-config.yaml")
                .build();
        assertNotNull(rc);

        Map<String, Object> config = rc.getConfiguration();
        assertNotNull(config);

        assertEquals(2, config.keySet().size());

    }

    @Test
    void testGetConfigurationAlreadyLoaded() {

        RuntimeConfiguration rc = RuntimeConfiguration.builder().filePath("src/test/resources/simple-config.yaml")
                .build();
        assertNotNull(rc);

        Map<String, Object> config = rc.getConfiguration();
        assertNotNull(config);

        assertEquals(2, config.keySet().size());

        assertEquals(2, rc.getConfiguration().keySet().size());

    }

}
