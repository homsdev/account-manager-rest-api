package com.homs.account_rest_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {

    @NotNull(message = "Missing account balance")
    private BigDecimal accountBalance;

    @NotBlank(message = "Missing account alias")
    private String accountAlias;
}
