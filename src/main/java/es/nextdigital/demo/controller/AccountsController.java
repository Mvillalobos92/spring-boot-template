package es.nextdigital.demo.controller;

import es.nextdigital.demo.model.request.DepositMoneyRequest;
import es.nextdigital.demo.model.request.ExtractMoneyRequest;
import es.nextdigital.demo.model.request.TransferMoneyRequest;
import es.nextdigital.demo.model.response.MovementsResponse;
import es.nextdigital.demo.model.response.OperationResponse;
import es.nextdigital.demo.service.AccountsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    @GetMapping("/{accountId}/movements")
    public ResponseEntity<List<MovementsResponse>> getAccountMovements(@PathVariable Long accountId) {

        List<MovementsResponse> result = accountsService.getAccountTransactions(accountId);

        return (result == null || result.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }

    @PostMapping(path = "/extract", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationResponse> extract(@Valid @NotNull @RequestBody ExtractMoneyRequest request) {
        boolean result = accountsService.extractMoney(request);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new OperationResponse("The money could not be withdrawn. Please try again later."));
        }
        return ResponseEntity
                .ok(new OperationResponse("Extraction successful."));
    }

    @PostMapping(path = "/deposit", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationResponse> deposit(@Valid @NotNull @RequestBody DepositMoneyRequest request) {
        boolean result = accountsService.depositMoney(request);

        if (Boolean.FALSE.equals(result)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new OperationResponse(
                            "You are trying to make a deposit from another bank's ATM. Please go to your bank's ATM."));
        }
        return ResponseEntity
                .ok(new OperationResponse("Deposit successful."));
    }

    @PostMapping(path = "/transfer", produces = "application/json", consumes = "application/json")
    public ResponseEntity<OperationResponse> transfer(@Valid @NotNull @RequestBody TransferMoneyRequest request) {
        boolean result = accountsService.transferMoney(request);

        if (Boolean.FALSE.equals(result)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new OperationResponse("There was an error during the transfer. Please try again later."));
        }
        return ResponseEntity
                .ok(new OperationResponse("transfer completed."));
    }

}
