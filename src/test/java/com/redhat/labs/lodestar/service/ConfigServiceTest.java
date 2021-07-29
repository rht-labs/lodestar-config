package com.redhat.labs.lodestar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.junit.jupiter.api.Test;

import com.redhat.labs.lodestar.utils.ResourceLoader;

class ConfigServiceTest {

    JsonbConfig config = new JsonbConfig().withFormatting(true);
    Jsonb jsonb = JsonbBuilder.create(config);

    @Test
    void testGetBaseConfiguration() {

        String expected = ResourceLoader.load("expected/service-base-config.json");

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.empty());
        assertEquals(expected, configuration);

    }

    @Test
    void testGetTypeOneConfiguration() {

        String expected = ResourceLoader.load("expected/service-get-type-one-config.json");

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeOne"));
        assertEquals(expected, configuration);

    }

    @Test
    void testGetTypeTwoConfiguration() {
        String expected = ResourceLoader.load("expected/service-get-type-two-config.json");

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeTwo"));
        assertEquals(expected, configuration);

    }

    @Test
    void testGetTypeThreeConfigurationNotFound() {

        String expected = ResourceLoader.load("expected/service-get-type-three-config.json");

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.createRuntimeConfigurations();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeThree"));
        assertEquals(expected, configuration);

    }

}
