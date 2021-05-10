package com.redhat.labs.lodestar.resource;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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

}
