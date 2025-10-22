package com.example.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {
    @NotBlank(message = "Card number must not be blank")
    private String maskedNumber;

    @NotNull(message = "Owner ID must not be null")
    private Long ownerId;

    @NotBlank(message = "Owner name must not be blank")
    private String ownerName;

    private String status;

    @NotNull(message = "Expiry date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expireDate;

    @NotNull(message = "Balance is required")
    private BigDecimal balance;
}
