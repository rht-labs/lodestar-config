package com.redhat.labs.lodestar.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtils.class);

    private static ObjectMapper om = new ObjectMapper(new YAMLFactory());

    private YamlUtils() {
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

        try {
            return Optional.of(om.readValue(yaml, Map.class));
        } catch (Exception e) {
            LOGGER.warn("could not parse string, {}", yaml);
            return Optional.empty();
        }

    }

    /**
     * Creates a new {@link Map} containing the base {@link Map} placing any
     * corresponding attributes from the override {@link Map}.
     * 
     * @param mergedMap
     * @param base
     * @param override
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> merge(Map<String, Object> base, Map<String, Object> override) {

        Map<String, Object> mergedMap = new HashMap<>();

        for (Entry<String, Object> entry : base.entrySet()) {

            Object bValue = entry.getValue();
            Object oValue = override.get(entry.getKey());

            Object mergedValue = null;

            if (null != bValue && null != oValue) {

                if (bValue instanceof Map && oValue instanceof Map) {
                    mergedValue = merge((Map<String, Object>) bValue, (Map<String, Object>) oValue);
                } else if (bValue instanceof List && oValue instanceof List) {
                    mergedValue = oValue;
                } else {
                    mergedValue = oValue;
                }

            } else if (null != bValue) {
                mergedValue = bValue;
            } else if (null != oValue) {
                mergedValue = oValue;
            }

            // add to merged map
            if (null != mergedValue) {
                mergedMap.put(entry.getKey(), mergedValue);
            }

        }

        return mergedMap;

    }

}
