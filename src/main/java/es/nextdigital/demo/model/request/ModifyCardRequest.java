package es.nextdigital.demo.model.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ModifyCardRequest {

    @NotNull(message = "You must indicate the card to activate.")
    private Long idCard;

    @NotNull(message = "You must enter the new PIN associated with the card")

    @DecimalMin(value = "500.00", message = "The withdrawal limit must be at least 500")
    @DecimalMax(value = "6000.00", message = "The withdrawal limit must not exceed 6000")
    private BigDecimal withdrawalLimit;

}
