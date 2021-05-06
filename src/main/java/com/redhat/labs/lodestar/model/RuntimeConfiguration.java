package com.redhat.labs.lodestar.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.labs.lodestar.utils.YamlUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//@Data
@SuperBuilder
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public class RuntimeConfiguration extends ConfigMap {

    private Map<String, Object> configuration;

    public Map<String, Object> getConfiguration() {

        if (null != configuration && !configuration.keySet().isEmpty()) {
            return configuration;
        }

        // load content from file
        readMountedFile();

        // load map from content
        configuration = YamlUtils.convertToMap(getContent().orElse(null)).orElse(new HashMap<>());
        return configuration;

    }

}
