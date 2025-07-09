package es.nextdigital.demo.controller;

import es.nextdigital.demo.model.request.ActivateCardRequest;
import es.nextdigital.demo.model.request.ChangePinRequest;
import es.nextdigital.demo.model.request.ModifyCardRequest;
import es.nextdigital.demo.model.response.OperationResponse;
import es.nextdigital.demo.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/card")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/activate")
    public ResponseEntity<OperationResponse> activateCard(@Valid @NotNull @RequestBody ActivateCardRequest request) {
        cardService.activateCard(request);
        return ResponseEntity.ok(new OperationResponse("Your card has been activated successfully"));
    }

    @PostMapping("/change-pin")
    public ResponseEntity<OperationResponse> changePin(@Valid @NotNull @RequestBody ChangePinRequest request) {
        cardService.changePin(request);
        return ResponseEntity.ok(new OperationResponse("The PIN has been changed successfully."));
    }

    @PostMapping("/modify")
    public ResponseEntity<OperationResponse> modify(@Valid @NotNull @RequestBody ModifyCardRequest request) {
        cardService.modify(request);
        return ResponseEntity.ok(new OperationResponse("The PIN has been changed successfully."));
    }


}
