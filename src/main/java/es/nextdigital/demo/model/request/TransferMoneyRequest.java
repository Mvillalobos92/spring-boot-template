package es.nextdigital.demo.model.request;

import es.nextdigital.demo.validation.ValidIban;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferMoneyRequest {

    @NotNull(message = "You must indicate the source account from which you want to make the transfer.")
    @ValidIban
    private String sourceIban;

    @NotNull(message = "You must indicate the destination account from which you want to make the transfer.")
    @ValidIban
    private String destinationIban;

    @NotNull(message = "You must indicate the amount you wish to transfer.")
    private BigDecimal amount;
}
