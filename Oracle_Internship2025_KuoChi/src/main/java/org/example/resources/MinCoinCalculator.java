package org.example.resources;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MinCoinCalculator {

    @POST
    @Path("/submit")
    public Response handleRequest(@Valid RequestBody requestBody) {
        double targetAmount = requestBody.getTargetAmount();
        List<Double> allowedDenominations = Arrays.<Double>asList(0.01, 0.05, 0.1, 0.2, 0.5, 1., 2., 5., 10., 50., 100., 1000.);
        List<Double> coinDenominations = requestBody.getCoinDenominations().stream()
                .map(Number::doubleValue)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        try {
            validate(targetAmount, allowedDenominations, coinDenominations);
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Unable to validate due to unknown causes.").build();
        }

        return calculate(targetAmount, allowedDenominations, coinDenominations);
    }

    private void validate(double targetAmount, List<Double> allowedDenominations, List<Double> coinDenominations) throws ValidationException {
        BigDecimal bigTargetAmount = new BigDecimal(String.valueOf(targetAmount));
        BigDecimal min = new BigDecimal("0.01");
        if (bigTargetAmount.remainder(min).compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException("Target amount cannot have more than 2d.p.");
        }

        double minCoin = coinDenominations.stream()
                .min(Double::compare)
                .orElseThrow(() -> new ValidationException("Coin denominator list cannot be empty."));
        if (bigTargetAmount.remainder(BigDecimal.valueOf(minCoin)).compareTo(BigDecimal.ZERO) != 0) {
            throw new ValidationException("Target amount is not divisible without remainder.");
        }
        for (Double coinDenomination : coinDenominations) {
            if (!allowedDenominations.contains(coinDenomination)) {
                throw new ValidationException("Coin denominator must be one of the following values [0.01, 0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 50, 100, 1000].");
            }
        }
    }

    private Response calculate(double targetAmount, List<Double> allowedDenominations, List<Double> coinDenominations) {
        HashMap<Double, Integer> requiredCoins = new HashMap<>();
        double remainingAmount = targetAmount;
        System.out.println(remainingAmount);
        for (Double coinDenomination : coinDenominations) {
            remainingAmount = Math.round(remainingAmount * 100.0) / 100.0;
            coinDenomination = Math.round(coinDenomination * 100.0) / 100.0;
            int num = (int) (remainingAmount / coinDenomination);
            System.out.println(num);
            if (num > 0) {
                requiredCoins.put(coinDenomination, num);
                remainingAmount -= num * coinDenomination;
            }
        }

        List<String> coinList = new ArrayList<>();
        requiredCoins.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Double denomination = entry.getKey();
                    Integer num = entry.getValue();
                    for (int i = 0; i < num; i++) {
                        String formattedDenomination = (denomination % 1 == 0)
                                ? String.valueOf(denomination.intValue())
                                : String.valueOf(denomination);
                        coinList.add(formattedDenomination);
                    }
                });

        return Response.ok(coinList).build();
    }
}
