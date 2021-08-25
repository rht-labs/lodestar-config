package com.redhat.labs.lodestar.resource;

import com.redhat.labs.lodestar.model.GitlabHook;
import com.redhat.labs.lodestar.service.GitlabHookService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/api/v1/configs/webhooks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GitlabHookConfigResource {

    @Inject
    GitlabHookService gitlabHookService;

    @GET
    public Response get() {
        List<GitlabHook> webhooks = gitlabHookService.getWebhooks();
        return Response.ok(webhooks).header("x-total-webhooks", webhooks.size()).build();
    }
}
