package es.nextdigital.demo.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExtractMoneyRequest {

    @NotNull(message = "You must inform the card from which you want to withdraw the money.")
    private Long cardId;

    @NotNull(message = "You must inform the card from which you want to withdraw the money.")
    private BigDecimal amount;

    @NotNull(message = "You must indicate the ATM from which the withdrawal will be made.")
    private Long atmId;
}
