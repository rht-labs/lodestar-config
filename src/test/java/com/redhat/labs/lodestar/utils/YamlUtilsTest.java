package com.redhat.labs.lodestar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class YamlUtilsTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", " " })
    void testConvertToMapInvalidInput(String input) {
        assertTrue(YamlUtils.convertToMap(input).isEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    void testConverToMapSuccess() {

        String yaml = ResourceLoader.load("simple-config.yaml");
        Optional<Map<String, Object>> optional = YamlUtils.convertToMap(yaml);

        assertTrue(optional.isPresent());
        System.out.println(optional.get());

        Map<String, Object> config = optional.get();
        assertNotNull(config);

        Map<String, Object> simpleMap = (Map<String, Object>) config.get("simple_map");
        assertNotNull(simpleMap);
        assertEquals("key_value_one", simpleMap.get("key_one"));

        Map<String, Object> anotherMap = (Map<String, Object>) config.get("another_map");
        assertNotNull(anotherMap);

        Map<String, Object> nestedMap = (Map<String, Object>) anotherMap.get("nested_map");
        assertNotNull(nestedMap);

        List<Object> list = (List<Object>) nestedMap.get("list");
        assertNotNull(list);
        assertEquals(2, list.size());
        assertTrue(list.contains("One"));
        assertTrue(list.contains("Two"));

    }

    @Test
    void testConvertToMapUnparsable() {
        assertTrue(YamlUtils.convertToMap("--unparsable").isEmpty());
    }

}
