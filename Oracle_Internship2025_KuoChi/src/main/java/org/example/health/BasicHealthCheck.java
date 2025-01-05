package org.example.health;

import com.codahale.metrics.health.HealthCheck;
import javax.xml.transform.Result;

public class BasicHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        return HealthCheck.Result.healthy();
    }
}
