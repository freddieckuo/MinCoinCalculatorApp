package org.example.health;

import com.codahale.metrics.health.HealthCheck;
import javax.xml.transform.Result;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
public class BasicHealthCheck extends HealthCheck {

    @GET
    @Path("/health")
    protected Result check() throws Exception {
        return HealthCheck.Result.healthy();
    }
}
