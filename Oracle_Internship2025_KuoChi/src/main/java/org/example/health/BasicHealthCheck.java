package org.example.health;

import com.codahale.metrics.health.HealthCheck;
import javax.xml.transform.Result;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class BasicHealthCheck extends HealthCheck {

    @GET
    @Path("/health")
    protected Response healthCheck() {
        return Response.ok("{\"status\": \"healthy\"}").build();
    }
}
