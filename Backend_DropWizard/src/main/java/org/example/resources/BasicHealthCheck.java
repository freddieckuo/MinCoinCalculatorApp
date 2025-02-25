package org.example.resources;

import com.codahale.metrics.health.HealthCheck;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class BasicHealthCheck extends HealthCheck {

    @GET
    @Path("/health")
    public Response healthCheck() {
        return Response.ok("{\"status\": \"healthy\"}").build();
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }
}
