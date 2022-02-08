package com.redhat.labs.lodestar.service;

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
        loadModifiedConfigs();
        LOGGER.debug("runtime configurations loaded.");
    }

    /**
     * Can't trust modification of config maps meta-data. need to read the data
     */
    @Scheduled(every = "60s", delayed = "10s")
    void loadModifiedConfigs() {
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
        Collection<String> engagementTypes = getEngagementOptions().keySet();

        engagementTypes.forEach(this::createOverrideConfig);
        LOGGER.debug("override configurations: {}", overrideConfigurations.keySet());

    }

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getArtifactOptions() {
        Map<String, Object> configuration = baseConfiguration.getConfiguration();
        List<Map<String, Object>> typesList = Optional.of(configuration)
                .map(m -> (Map<String, Object>) m.get("artifact_options"))
                .map(m -> (Map<String, Object>) m.get("types"))
                .map(m -> (List<Map<String, Object>>) m.get("options")).orElse(new ArrayList<>());

        return typesList.stream().collect(Collectors.toMap(s -> s.get("value"), s -> s.get("label")));
    }


    /**
     * Returns a {@link Map} of {@link String} engagement type key / values from the
     * configured base {@link RuntimeConfiguration}. Empty if none found
     *
     * @return a map of engagements
     */
    public Map<String, String> getEngagementOptions() {
        return getEngagementOptions("engagement_types");
    }

    public Map<String, String> getEngagementRegionOptions() {
        return getEngagementOptions("engagement_regions");
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getEngagementOptions(String type) {
        Map<String, Object> configuration = baseConfiguration.getConfiguration();
        List<Map<String, Object>> typesList = Optional.of(configuration)
                .map(m -> (Map<String, Object>) m.get("basic_information"))
                .map(m -> (Map<String, Object>) m.get(type))
                .map(m -> (List<Map<String, Object>>) m.get("options")).orElse(new ArrayList<>());

        return typesList.stream().collect(Collectors.toMap(s -> (String) s.get("value"), s -> (String) s.get("label")));
    }

    /**
     * Creates and adds a {@link RuntimeConfiguration} to the override
     * configurations {@link Map} for the given engagement type.
     *
     * @param engagementType - the type of engagement
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
     * @return a map of runtime configurations
     */
    public Map<String, Object> mapRuntimeConfiguration(Optional<String> engagementType) {

        // get map for base configuration
        Map<String, Object> base = baseConfiguration.getConfiguration();

        if (engagementType.isPresent() && overrideConfigurations.containsKey(engagementType.get())) {

            Map<String, Object> override = overrideConfigurations.get(engagementType.get()).getConfiguration();
            base = MarshalUtils.merge(base, override);

        }

        return base;

    }

    public String getRuntimeConfiguration(Optional<String> engagementType) {
        return jsonb.toJson(mapRuntimeConfiguration(engagementType));
    }
    
    /**
     * Returns a mapping of engagement type to rbac access indicating the groups 
     * that can write engagements. The purpose is to inform the front end on a user's
     * ability to write data for an engagement
     * @return a map of permissions per group
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
