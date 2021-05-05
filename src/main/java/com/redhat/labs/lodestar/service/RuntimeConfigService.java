package com.redhat.labs.lodestar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.redhat.labs.lodestar.model.RuntimeConfiguration;
import com.redhat.labs.lodestar.utils.YamlUtils;

import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class RuntimeConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeConfigService.class);

    @ConfigProperty(name = "configs.runtime.base.file")
    String runtimeBaseConfig;

    @ConfigProperty(name = "configs.runtime.check.modified")
    boolean reloadConfig;

    RuntimeConfiguration baseConfiguration;
    Map<String, RuntimeConfiguration> overrideConfigurations = new HashMap<>();

    @Inject
    Jsonb jsonb;

    /**
     * Periodically reloads the web hook and configuration data if the configured
     * files have been modified.
     */
    @Scheduled(every = "30s")
    void reloadConfigMapData() {
        if (reloadConfig) {
            resetConfigurations();
            loadRuntimeConfigurationData();
        }
    }

    /**
     * Clear the configuration maps for the base and override configurations.
     */
    void resetConfigurations() {

        // reset base config
        if (null != baseConfiguration) {
            baseConfiguration.resetConfiguration();
        }

        // reset each override config
        overrideConfigurations.values().stream().filter(Objects::nonNull)
                .forEach(RuntimeConfiguration::resetConfiguration);

    }

    void loadRuntimeConfigurationData() {

        // read runtime base config
        loadBaseRuntimeConfig();

        // Get List of engagement types from base config
        List<String> engagementTypes = getEngagementTypes();

        engagementTypes.stream().forEach(this::loadOverrideConfig);

    }

    /**
     * Create {@link RuntimeConfiguration} for the configured base config file.
     */
    void loadBaseRuntimeConfig() {

        // create base runtime config
        if (null == baseConfiguration) {
            baseConfiguration = RuntimeConfiguration.builder().filePath(runtimeBaseConfig).build();
        }

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

        if (null != baseConfiguration) {

            Map<String, Object> configuration = baseConfiguration.getConfiguration();
            List<Map<String, Object>> typesList = Optional.of(configuration)
                    .map(m -> (Map<String, Object>) m.get("basic_information"))
                    .map(m -> (Map<String, Object>) m.get("engagement_types"))
                    .map(m -> (List<Map<String, Object>>) m.get("options")).orElse(new ArrayList<>());

            return typesList.stream().flatMap(m -> m.entrySet().stream()).filter(e -> e.getKey().equals("value"))
                    .map(e -> e.getValue().toString()).collect(Collectors.toList());
        }

        return new ArrayList<>();

    }

    /**
     * Builds a {@link String} representing the runtime configuration override file
     * for the given engagement type.
     * 
     * @param engagementType
     * @return
     */
    String getOverrideFileName(String engagementType) {

        int index = runtimeBaseConfig.indexOf(".");
        String baseFileName = runtimeBaseConfig.substring(0, index);
        String baseFileType = runtimeBaseConfig.substring(index + 1);

        return new StringBuilder(baseFileName).append("-").append(engagementType.toLowerCase()).append(".")
                .append(baseFileType).toString();

    }

    /**
     * Adds a {@link RuntimeConfiguration} to the override configurations
     * {@link Map} for the given engagement type.
     * 
     * @param engagementType
     */
    void loadOverrideConfig(String engagementType) {
        overrideConfigurations.put(engagementType,
                RuntimeConfiguration.builder().filePath(getOverrideFileName(engagementType)).build());
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
            return jsonb.toJson(YamlUtils.merge(base, override));

        }

        return jsonb.toJson(base);

    }

}
