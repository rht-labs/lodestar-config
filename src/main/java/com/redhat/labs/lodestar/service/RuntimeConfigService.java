package com.redhat.labs.lodestar.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.Jsonb;

import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.model.RuntimeConfiguration;
import com.redhat.labs.lodestar.utils.MarshalUtils;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class RuntimeConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeConfigService.class);

    @ConfigProperty(name = "configs.runtime.base.file")
    String runtimeBaseConfig;

    RuntimeConfiguration baseConfiguration;
    Map<String, RuntimeConfiguration> overrideConfigurations = new HashMap<>();


    @Inject
    Jsonb jsonb;

    /**
     * Create {@link RuntimeConfiguration}s at startup.
     */
    void onStart(@Observes StartupEvent ev) {
        createRuntimeConfigurations();
        loadModifiedWebhooks();
        LOGGER.debug("runtime configurations loaded.");
    }

    /**
     * Can't trust modification of config maps meta-data. need to read the data
     */
    @Scheduled(every = "60s", delayed = "10s")
    void loadModifiedWebhooks() {
        baseConfiguration.loadFromConfigMapIfChanged();
        overrideConfigurations.values().forEach(RuntimeConfiguration::loadFromConfigMapIfChanged);
    }

    /**
     * Creates {@link RuntimeConfiguration}s for the configured base configuration
     * and any override configurations. Only happens on first load. New engagement type
     * customizations require a new configmap which requires a change to the deployment config
     */
    void createRuntimeConfigurations() {

        LOGGER.debug("loading runtime configurations");

        // read runtime base config
        baseConfiguration = RuntimeConfiguration.builder().filePath(runtimeBaseConfig).build();
        baseConfiguration.loadFromConfigMapIfChanged();

        // Get List of engagement types from base config
        List<String> engagementTypes = getEngagementTypes();

        engagementTypes.stream().forEach(this::createOverrideConfig);
        LOGGER.debug("override configurations: {}", overrideConfigurations.keySet());

    }

    /**
     * Returns a {@link List} of {@link String} engagement type values from the
     * configured base {@link RuntimeConfiguration}. Otherwise, an empty
     * {@link List} is returned.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    List<String> getEngagementTypes() {

        Map<String, Object> configuration = baseConfiguration.getConfiguration();
        List<Map<String, Object>> typesList = Optional.of(configuration)
                .map(m -> (Map<String, Object>) m.get("basic_information"))
                .map(m -> (Map<String, Object>) m.get("engagement_types"))
                .map(m -> (List<Map<String, Object>>) m.get("options")).orElse(new ArrayList<>());

        return typesList.stream().flatMap(m -> m.entrySet().stream()).filter(e -> e.getKey().equals("value"))
                .map(e -> e.getValue().toString()).collect(Collectors.toList());

    }

    /**
     * Creates and adds a {@link RuntimeConfiguration} to the override
     * configurations {@link Map} for the given engagement type.
     * 
     * @param engagementType
     */
    void createOverrideConfig(String engagementType) {
        String filePath = this.runtimeBaseConfig.replaceAll("base", engagementType);
        RuntimeConfiguration rc = RuntimeConfiguration.builder().filePath(filePath).build();
        if(rc.checkPath()) {
            rc.loadFromConfigMapIfChanged();
            overrideConfigurations.put(engagementType, rc);
        } else if(LOGGER.isWarnEnabled()) {
            LOGGER.warn("Unable to read file {} so it will no longer be checked", filePath);
        }
    }

    /**
     * Returns the JSON String of the runtime configuration for the engagement type
     * provided. If not provided, or no override configuration found, the base
     * configuration will be returned.
     * 
     * @param engagementType
     * @return
     */
    public String getRuntimeConfiguration(Optional<String> engagementType) {

        // get map for base configuration
        Map<String, Object> base = baseConfiguration.getConfiguration();

        if (engagementType.isPresent() && overrideConfigurations.containsKey(engagementType.get())) {

            Map<String, Object> override = overrideConfigurations.get(engagementType.get()).getConfiguration();
            return jsonb.toJson(MarshalUtils.merge(base, override));

        }

        return jsonb.toJson(base);

    }
    
    /**
     * Returns a mapping of engagement type to rbac access indicating the groups 
     * that can write engagements. The purpose is to inform the front end on a user's
     * ability to write data for an engagement
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Set<String>> getPermission() {

        Map<String, Object> configuration = baseConfiguration.getConfiguration();
        Map<String, Set<String>> typesList = Optional.of(configuration)
                .map(m -> (Map<String, Set<String>>) m.get("rbac")).orElse(new HashMap<>());

        LOGGER.debug("rbac {}", typesList);

        return typesList;
    }

}
