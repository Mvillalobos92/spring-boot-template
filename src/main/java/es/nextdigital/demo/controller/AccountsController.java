package es.nextdigital.demo.controller;

import es.nextdigital.demo.model.MovementsResponse;
import es.nextdigital.demo.service.AccountsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountsService accountsService;

    @GetMapping("/{accountId}/movements")
    public ResponseEntity<List<MovementsResponse>> getAccountMovements(@PathVariable Long accountId) {

        List<MovementsResponse> result =  accountsService.getAccountTransactions(accountId);

        return (result == null || result.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(result);
    }

}
