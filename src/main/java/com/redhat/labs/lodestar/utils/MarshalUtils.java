package com.redhat.labs.lodestar.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class MarshalUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarshalUtils.class);

    private static ObjectMapper om = new ObjectMapper(new YAMLFactory());

    private MarshalUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a Map containing the values from the given Yaml String. Otherwise, an
     * empty {@link HashMap} will be returned.
     * 
     * @param yaml
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    public static Optional<Map<String, Object>> convertToMap(String yaml) {

        if (null == yaml || yaml.isBlank()) {
            return Optional.empty();
        }

        try {
            return Optional.of(om.readValue(yaml, Map.class));
        } catch (Exception e) {
            LOGGER.warn("could not parse string, {}", yaml);
            return Optional.empty();
        }

    }

    /**
     * Returns a {@link Map} containing the merged base and override maps.
     * 
     * @param base
     * @param override
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> merge(Map<String, Object> base, Map<String, Object> override) {

        Map<String, Object> merged = new HashMap<>();

        // add all in base, not in override
        keysInMap1NotInMap2(base, override).stream().forEach(k -> merged.put(k, base.get(k)));

        // add all in override, not in base
        keysInMap1NotInMap2(override, base).stream().forEach(k -> merged.put(k, override.get(k)));

        List<String> keysInBoth = keysInMap1AndMap2(base, override);
        for (String key : keysInBoth) {

            Object bValue = base.get(key);
            Object oValue = override.get(key);

            if (bValue instanceof Map && oValue instanceof Map) {
                merged.put(key, merge((Map<String, Object>) bValue, (Map<String, Object>) oValue));
            } else {
                merged.put(key, oValue);
            }

        }

        return merged;

    }

    /**
     * Returns a {@link List} of keys that are in map1, but not in map2.
     * 
     * @param map1
     * @param map2
     * @return
     */
    static List<String> keysInMap1NotInMap2(Map<String, Object> map1, Map<String, Object> map2) {
        return map1.keySet().stream().filter(k -> !map2.containsKey(k)).collect(Collectors.toList());
    }

    /**
     * Returns a {@link List} of keys that are in both map1 and map2.
     * 
     * @param map1
     * @param map2
     * @return
     */
    static List<String> keysInMap1AndMap2(Map<String, Object> map1, Map<String, Object> map2) {
        return map1.keySet().stream().filter(k -> map2.containsKey(k)).collect(Collectors.toList());
    }

}
