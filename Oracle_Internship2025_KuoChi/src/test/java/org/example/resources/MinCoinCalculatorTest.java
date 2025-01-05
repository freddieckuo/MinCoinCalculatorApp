package org.example.resources;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import static org.assertj.core.api.Assertions.assertThat;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(DropwizardExtensionsSupport.class)
public class MinCoinCalculatorTest {
    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new MinCoinCalculator())
            .build();

    @Test
    void testValidRequest() {
        RequestBody requestBody = new RequestBody();
        requestBody.setTargetAmount(100.50);
        requestBody.setCoinDenominations(List.of(0.1, 5, 0.01, 0.2, 1, 0.5, 100, 10));

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(List.class)).containsExactly("0.5", "100");
    }

    @Test
    void testEmptyTargetAmount() {
        RequestBody requestBody = new RequestBody();
        requestBody.setCoinDenominations(List.of(100, 0.05, 0.5, 0.2, 1.0, 1000, 50));

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.readEntity(String.class)).contains("Target amount is required.");
    }

    @Test
    void testInvalidDPTargetAmount() {
        RequestBody requestBody = new RequestBody();
        requestBody.setTargetAmount(7560.123);
        requestBody.setCoinDenominations(List.of(100, 0.05, 0.5, 0.2, 1.0, 1000, 50));

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(String.class)).contains("Target amount cannot have more than 2d.p.");
    }

    @Test
    void testInvalidRangeTargetAmount() {
        RequestBody requestBody = new RequestBody();
        requestBody.setTargetAmount(10050.0);
        requestBody.setCoinDenominations(List.of(0.05, 0.1, 100, 50, 10, 1000));

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.readEntity(String.class)).contains("Target amount must be within the range between 0 and 10,000.00");
    }

    @Test
    void testEmptyCoinDenominations() {
        RequestBody requestBody = new RequestBody();
        requestBody.setTargetAmount(347.23);
        requestBody.setCoinDenominations(List.of());

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(422);
        assertThat(response.readEntity(String.class)).contains("Coin denominator can not be empty.");
    }

    @Test
    void testInvalidCoinDenominations() {
        RequestBody requestBody = new RequestBody();
        requestBody.setTargetAmount(500.25);
        requestBody.setCoinDenominations(List.of(0.01, 50, 0.5, 3, 100));

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(String.class))
                .contains("Coin denominator must be one of the following values [0.01, 0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 50, 100, 1000].");
    }

    @Test
    void testAmountNotDivisible() {
        RequestBody requestBody = new RequestBody();
        requestBody.setTargetAmount(301.2);
        requestBody.setCoinDenominations(List.of(0.5, 100, 1));

        Response response = RESOURCES.target("/api/submit")
                .request()
                .post(Entity.json(requestBody));

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        assertThat(response.readEntity(String.class))
                .contains("Target amount is not divisible without remainder.");
    }

}
