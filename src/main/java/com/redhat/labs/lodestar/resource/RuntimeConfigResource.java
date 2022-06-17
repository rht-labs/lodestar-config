package com.redhat.labs.lodestar.resource;

import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.redhat.labs.lodestar.service.RuntimeConfigService;

@Path("/api/v1/configs/runtime")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RuntimeConfigResource {

    @Inject
    RuntimeConfigService configService;

    @GET
    public String get(@QueryParam("type") Optional<String> type) {
        return configService.getRuntimeConfiguration(type);
    }

    @GET
    @Path("artifact/options")
    public Map<Object, Object> getArtifactOptions() {
        return configService.getArtifactOptions();
    }

    @GET
    @Path("engagement/options")
    public Map<String, String> getEngagementOptions() {
        return configService.getEngagementOptions();
    }

    @GET
    @Path("region/options")
    public Map<String, String> getRegionOptions() {
        return configService.getEngagementRegionOptions();
    }

    @GET
    @Path("participant/options")
    public Map<Object, Object> getParticipantOptions(@QueryParam("engagementType") String type) {
        return configService.getParticipantRoleOptions(type);
    }
    
    @GET
    @Path("rbac")
    public Response getPermission() {
        return Response.ok(configService.getPermission()).build();
    }

}
