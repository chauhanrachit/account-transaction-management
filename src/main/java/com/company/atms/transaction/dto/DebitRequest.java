package com.company.atms.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DebitRequest {
	
    @NotNull
    private UUID accountId;

    @NotNull
    private String reference;

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;
}
