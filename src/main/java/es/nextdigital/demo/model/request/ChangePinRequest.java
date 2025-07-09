package es.nextdigital.demo.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePinRequest {

    @NotNull(message = "You must indicate the card to activate.")
    private Long idCard;

    @NotNull(message = "You must enter the new PIN associated with the card")
    private String oldPin;

    @NotNull(message = "You must enter the new PIN associated with the card")
    @Min(value = 6, message = "The PIN must have at least 6 digits")
    @Max(value = 18, message = "The PIN must not have more than 18 digits.")
    private String newPin;

}
