package com.redhat.labs.lodestar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
            } else if (bValue instanceof List && oValue instanceof List) {
                merged.put(key, merge((List<Object>) bValue, (List<Object>) oValue));
            } else {
                merged.put(key, oValue);
            }

        }

        return merged;

    }

    /**
     * Returns a {@link List} of {@link Object} containing the {@link Object}s in
     * list1 and list2.
     * 
     * @param list1
     * @param list2
     * @return
     */
    public static List<Object> merge(List<Object> list1, List<Object> list2) {

        List<Object> merged = new ArrayList<>();

        // find in list1, not list2
        List<Object> inList1NotList2 = list1.stream().filter(o -> !contains(list2, o)).collect(Collectors.toList());
        merged.addAll(inList1NotList2);

        // find in list 2, not list1
        List<Object> inList2NotList1 = list2.stream().filter(o -> !contains(list1, o)).collect(Collectors.toList());
        merged.addAll(inList2NotList1);

        // in both
        List<Object> inBoth = list1.stream().filter(o -> contains(list2, o)).collect(Collectors.toList());
        merged.addAll(inBoth);

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

    /**
     * Returns true if the given {@link Object} is found in the {@link List}.
     * Otherwise, returns false.
     * 
     * @param list
     * @param input
     * @return
     */
    @SuppressWarnings("unchecked")
    static boolean contains(List<Object> list, Object input) {

        return list.stream().anyMatch(o -> (o instanceof Map && input instanceof Map
                && isMapEquals((Map<String, Object>) o, (Map<String, Object>) input)) || o.equals(input));

    }

    /**
     * Return true if both maps have the same number of keys and every key/value
     * pair in map1 is found in map2. Otherwise, returns false.
     * 
     * @param map1
     * @param map2
     * @return
     */
    static boolean isMapEquals(Map<String, Object> map1, Map<String, Object> map2) {

        if (map1.keySet().size() != map2.keySet().size()) {
            return false;
        }

        for (String key : map1.keySet()) {

            if (!map1.get(key).equals(map2.get(key))) {
                return false;
            }

        }

        return true;

    }

}
