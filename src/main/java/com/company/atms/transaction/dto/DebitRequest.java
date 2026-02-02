package com.company.atms.transaction.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @Size(min = 5, max = 50)
    private String reference;

    @NotNull
    @Positive
    private BigDecimal amount;

    @Size(max = 255)
    private String description;
}
