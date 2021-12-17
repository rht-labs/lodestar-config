package com.redhat.labs.lodestar.service;

import com.redhat.labs.lodestar.config.rest.client.EngagementApiRestClient;
import com.redhat.labs.lodestar.model.GitlabHook;
import com.redhat.labs.lodestar.model.GitlabHookConfiguration;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import java.util.List;

/**
 * Takes a config map and creates a list of webhooks that need to be maintained
 * by each gitlab project representing an engagement. Notifies lodestar-engagements
 * when a new version is created so engagements (projects) can be updated
 */
@ApplicationScoped
public class GitlabHookService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabHookService.class);

    @ConfigProperty(name = "configs.webhook.base.file")
    String gitlabWebhookConfigMapPath;

    @Inject
    @RestClient
    EngagementApiRestClient engagementApiRestClient;

    GitlabHookConfiguration gitlabHookConfiguration;


    /**
     * Create {@link com.redhat.labs.lodestar.model.GitlabHook}s at startup.
     */
    void onStart(@Observes StartupEvent ev) {
        LOGGER.debug("starting webhooks");
        gitlabHookConfiguration = GitlabHookConfiguration.builder().filePath(gitlabWebhookConfigMapPath).build();
        loadModifiedWebhooks();
    }

    /**
     * Can't trust modification of config maps meta-data. need to read the data
     */
    @Scheduled(every = "60s", delayed = "20s")
    void loadModifiedWebhooks() {
        boolean isNew = gitlabHookConfiguration.readAndUpdateMountedFile();

        if(isNew) { //If they are the same then don't bother
            gitlabHookConfiguration.loadFromConfigMap();
            try {
                engagementApiRestClient.updateWebhooks(gitlabHookConfiguration.getHooks());
                LOGGER.debug("gitlab webhooks updated.");
            } catch (ProcessingException | WebApplicationException ex) { //Only needed until engagements comes online
                LOGGER.error("This should occur before v2", ex.getMessage());
            }
        }
    }

    public List<GitlabHook> getWebhooks() {
        return gitlabHookConfiguration.getHooks();
    }
}
