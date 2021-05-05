package com.redhat.labs.lodestar.utils;

class YamlUtilsTest {

//    @Test
//    void testConvertToMapNull() {
//        assertTrue(YamlUtils.convertToMap(null).isEmpty());
//    }
//
//    @Test
//    void testConvertToMapNotParsable() {
//        assertTrue(YamlUtils.convertToMap("not\n\tparsable").isEmpty());
//    }
//
//    @Test
//    void testConverToMapSuccess() {
//
//        String yaml = ResourceLoader.load("base-config.yaml");
//        Optional<Map> optional = YamlUtils.convertToMap(yaml);
//
//        assertTrue(optional.isPresent());
//        System.out.println(optional.get());
//
//    }
//
//    @Test
//    void testMergeBaseAndOverrideYaml() {
//
//        String baseYaml = ResourceLoader.load("base-config.yaml");
//        Optional<Map> base = YamlUtils.convertToMap(baseYaml);
//        assertTrue(base.isPresent());
//
//        String overrideYaml = ResourceLoader.load("override-1-config.yaml");
//        Optional<Map> override = YamlUtils.convertToMap(overrideYaml);
//        assertTrue(override.isPresent());
//
//        Map<String, Object> map = new HashMap<>();
//
//        YamlUtils.merge(map, base.get(), override.get());
//
//        Map<String, Object> mInfoMap = (Map<String, Object>)map.get("more_information");
//        assertEquals("b", mInfoMap.get("b_value"));
//
//        Map otherOptions = (Map) base.get().get("other_options");
//        Map types = (Map) otherOptions.get("types");
//        List options = (List) types.get("options");
//        assertEquals(1, options.size());
//
//        Map optionOne = (Map) options.get(0);
//        assertEquals("option1", optionOne.get("value"));
//
//    }
//
//    private static ObjectMapper om = new ObjectMapper(new YAMLFactory());
//
//    @Test
//    void test() {
//
////        String bYaml = ResourceLoader.load("base-list-of-map.yaml");
//        String bYaml = ResourceLoader.load("base-config.yaml");
//        Map base = YamlUtils.convertToMap(bYaml).orElse(null);
//        
////        String oYaml = ResourceLoader.load("override-list-of-map.yaml");
//        String oYaml = ResourceLoader.load("override-1-config.yaml");
//        Map override = YamlUtils.convertToMap(oYaml).orElse(null);
//
//        Map<String, Object> map = new HashMap<>();
//
//        YamlUtils.merge(map, base, override);
//        
//        map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + "\n\t" + e.getValue()));
//
//    }
//
//    void tryMerge(Map<String, Object> mergedMap, Map<String, Object> base, Map<String, Object> override) {
//
//        for(Entry<String, Object> entry : base.entrySet()) {
//
//            Object bValue = entry.getValue();
//            Object oValue = override.get(entry.getKey());
//
//            // skip if both values are null
//            if(null == bValue && null == oValue) {
//                continue;
//            } else if(null != bValue && null == oValue) {
//                mergedMap.put(entry.getKey(), bValue);
//            } else if(null == bValue && null != oValue) {
//                mergedMap.put(entry.getKey(), oValue);
//            } else {
//
//                if(bValue instanceof Map && oValue instanceof Map) {
//                    Map<String, Object> m = new HashMap<>();
//                    tryMerge(m, (Map<String, Object>)bValue, (Map<String, Object>)oValue);
//                    mergedMap.put(entry.getKey(), m);
//                } else if(bValue instanceof List && oValue instanceof List) {
//
//                    // TODO: Can we merge these instead of just replacing?
//                    mergedMap.put(entry.getKey(), oValue);
//
//                } else {
//                    mergedMap.put(entry.getKey(), oValue);
//                }
//
//            }
//
//        }
//
//    }
//
//
//    
//
//    void m(Map<String, Object> base, Map<String, Object> override) {
//
//        HashMap<String, Object> merged = new HashMap<>();
//
//        for(Entry<String, Object> e : base.entrySet()) {
//
//            // if maps 
//            
//            // if lists
//            
//            // if found in override use
//            
//            // else add
//
//        }
//
//
//    }
//
//    void merge(Map<String, Object> base, Map<String, Object> override) {
//        for (Entry<String, Object> e : override.entrySet()) {
//
//            Object overrideValue = override.get(e.getKey());
//
//            if (base.containsKey(e.getKey())) {
//
//                Object baseValue = base.get(e.getKey());
//                if (baseValue instanceof Map && overrideValue instanceof Map) {
//                    merge((Map<String, Object>) baseValue, (Map<String, Object>) overrideValue);
//                } else if (baseValue instanceof List && overrideValue instanceof List) {
//                    base.put(e.getKey(), merge((List<Object>) baseValue, (List<Object>) overrideValue));
//                } else {
//                    base.put(e.getKey(), overrideValue);
//                }
//
//            }
//
//        }
//    }
//
//    
//    
////    ---
////    basic_information:
////      engagement_types:
////        options:
////          - label: Type One
////            value: TypeOne
////          - label: TypeTwo
////            value: TypeTwo
////          - label: TypeThree
////            value: TypeThree
////            default: true
////    more_information:
////      a_value: a
////      b_value: Q
////    other_options:
////      types:
////        options:
////          - label: OptionOne
////            value: optionOne
////          - label: OptionTwo
////            value: optionTwo
//    
//    List<Object> merge(List<Object> list1, List<Object> list2) {
//
//        
//
//        list2.removeAll(list1);
//        list1.addAll(list2);
//        return list1;
//    }

    

}
