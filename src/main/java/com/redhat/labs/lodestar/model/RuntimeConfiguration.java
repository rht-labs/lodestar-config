package com.redhat.labs.lodestar.model;

import java.util.HashMap;
import java.util.Map;

import com.redhat.labs.lodestar.utils.MarshalUtils;

import lombok.Builder;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class RuntimeConfiguration extends ConfigMap {

    @Builder.Default
    private Map<String, Object> configuration = new HashMap<>();

    public void loadFromConfigMapIfChanged() {
        if(readAndUpdateMountedFile()) { //changed
            configuration = MarshalUtils.convertToMap(getContent().orElse(null)).orElse(new HashMap<>());
        }
    }

    public Map<String, Object> getConfiguration() {
         return configuration;
    }

}
