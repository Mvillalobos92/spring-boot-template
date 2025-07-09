package es.nextdigital.demo.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositMoneyRequest {

    @NotNull(message = "You must inform the card from which you want to deposit the money.")
    private Long cardId;

    @NotNull(message = "You must inform the card from which you want to deposit the money.")
    private BigDecimal amount;

    @NotNull(message = "You must indicate the ATM from which the deposit will be made.")
    private Long atmId;
}
