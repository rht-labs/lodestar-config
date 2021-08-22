package com.redhat.labs.lodestar.config.rest.client;

import com.redhat.labs.lodestar.model.GitlabHook;
import org.apache.http.NoHttpResponseException;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.List;

@Retry(maxRetries = 5, delay = 1200, retryOn = NoHttpResponseException.class, abortOn = WebApplicationException.class)
@RegisterRestClient(configKey = "engagements.api")
@Produces("application/json")
public interface EngagementApiRestClient {

    @PUT
    @Path("/api/v1/engagements/gitlab-webhooks")
    Response updateWebhooks(List<GitlabHook> webhooks);

}
