package com.redhat.labs.lodestar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

import org.junit.jupiter.api.Test;

import com.redhat.labs.lodestar.utils.ResourceLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ConfigServiceTest {

    JsonbConfig config = new JsonbConfig().withFormatting(true);
    Jsonb jsonb = JsonbBuilder.create(config);

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "TypeOne", " TypeTwo", "TypeThree" })
    void testGetBaseConfiguration(String type) {

        String expected = ResourceLoader.load(getExpected(type == null ? "" : type));

        RuntimeConfigService service = new RuntimeConfigService();
        service.jsonb = jsonb;
        service.runtimeBaseConfig = "src/test/resources/runtime-config-base.yaml";

        service.createRuntimeConfigurations();

        Optional configOption = Optional.ofNullable(type);

        String configuration = service.getRuntimeConfiguration(configOption);
        assertEquals(expected, configuration);

    }

    private String getExpected(String type) {
        String expected;
        switch (type) {
            case "TypeOne":
                expected = "expected/service-get-type-one-config.json";
                break;
            case "TypeTwo":
                expected = "expected/service-get-type-two-config.json";
                break;
            default:
                expected = "expected/service-base-config.json";
        }

        return expected;
    }
}
