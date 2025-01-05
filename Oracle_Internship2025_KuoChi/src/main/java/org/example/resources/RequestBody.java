package org.example.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Getter
@Setter
public class RequestBody {
    @JsonProperty("targetAmount")
    @NotNull(message = "Target amount is required.")
    @Range(min = 0, max = 10000, message = "Target amount must be within the range between 0 and 10,000.00")
    private Double targetAmount;

    @JsonProperty("coinDenominations")
    @NotEmpty(message = "Coin denominator can not be empty.")
    private List<Number> coinDenominations;
}
