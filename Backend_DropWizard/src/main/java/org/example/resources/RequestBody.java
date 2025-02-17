package org.example.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.util.List;

public class RequestBody {
    @JsonProperty("targetAmount")
    @NotNull(message = "Target amount is required.")
    @Range(min = 0, max = 10000, message = "Target amount must be within the range between 0 and 10,000.00")
    private Double targetAmount;

    @JsonProperty("coinDenominations")
    @NotEmpty(message = "Coin denominator can not be empty.")
    private List<Number> coinDenominations;

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public List<Number> getCoinDenominations() {
        return coinDenominations;
    }

    public void setCoinDenominations(List<Number> coinDenominations) {
        this.coinDenominations = coinDenominations;
    }
}
