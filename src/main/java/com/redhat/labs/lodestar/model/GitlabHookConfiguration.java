package com.redhat.labs.lodestar.model;

import com.redhat.labs.lodestar.utils.MarshalUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
public class GitlabHookConfiguration extends ConfigMap {

    @Builder.Default
    private List<GitlabHook> hooks = new ArrayList<>();

    public void loadFromConfigMap() {
        hooks = MarshalUtils.convertToHookList(getContent());
    }
}
