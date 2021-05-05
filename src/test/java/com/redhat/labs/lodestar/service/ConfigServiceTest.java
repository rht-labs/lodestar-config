package com.redhat.labs.lodestar.service;

import java.util.Optional;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyNamingStrategy;

import org.junit.jupiter.api.Test;

class ConfigServiceTest {

    JsonbConfig config = new JsonbConfig().withFormatting(true).withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);
    Jsonb jsonb = JsonbBuilder.create(config);
//    jsonbConfig.withFormatting(true).withPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CASE_WITH_UNDERSCORES);

    @Test
    void testGetBaseConfiguration() {

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.loadRuntimeConfigurationData();

        String configuration = service.getRuntimeConfiguration(Optional.empty());

        System.out.println(configuration);

    }

    @Test
    void testGetTypeOneConfiguration() {

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.loadRuntimeConfigurationData();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeOne"));

        System.out.println(configuration);

    }

    @Test
    void testGetTypeTwoConfiguration() {

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.loadRuntimeConfigurationData();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeTwo"));

        System.out.println(configuration);

    }

    @Test
    void testGetTypeThreeConfigurationNotFound() {

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/base-config.yaml";

        service.loadRuntimeConfigurationData();

        String configuration = service.getRuntimeConfiguration(Optional.of("TypeThree"));

        System.out.println(configuration);

    }

}
