package com.redhat.labs.lodestar.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.labs.lodestar.utils.MarshalUtils;

import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RuntimeConfiguration extends ConfigMap {

    private Map<String, Object> configuration;

    public Map<String, Object> getConfiguration() {

        if (null != configuration && !configuration.keySet().isEmpty()) {
            return configuration;
        }

        // load content from file
        readMountedFile();

        // load map from content
        configuration = MarshalUtils.convertToMap(getContent().orElse(null)).orElse(new HashMap<>());
        return configuration;

    }

}
