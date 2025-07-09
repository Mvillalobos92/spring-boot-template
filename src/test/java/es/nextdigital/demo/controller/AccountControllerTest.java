package es.nextdigital.demo.controller;

import es.nextdigital.demo.model.request.DepositMoneyRequest;
import es.nextdigital.demo.model.request.ExtractMoneyRequest;
import es.nextdigital.demo.model.response.MovementsResponse;
import es.nextdigital.demo.model.response.OperationResponse;
import es.nextdigital.demo.service.AccountsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountsService accountsService;

    @InjectMocks
    private AccountsController accountsController;

    @Test
    void shouldReturnOkWithMovementsList() {
        Long accountId = 1L;

        List<MovementsResponse> mockMovements = List.of(
                new MovementsResponse(1L, 2L, 52L, "INGRESO", new BigDecimal("50.00")),
                new MovementsResponse(1L, 3L, 27L, "GASTO", new BigDecimal("25.00"))
        );
        Mockito.when(accountsService.getAccountTransactions(Mockito.any())).thenReturn(mockMovements);
        ResponseEntity<List<MovementsResponse>> response = accountsController.getAccountMovements(accountId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void shouldReturnNoContentWhenListIsEmpty() {
        Long accountId = 2L;
        Mockito.when(accountsService.getAccountTransactions(Mockito.any())).thenReturn(Collections.emptyList());
        ResponseEntity<List<MovementsResponse>> response = accountsController.getAccountMovements(accountId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnNoContentWhenListIsNull() {
        Long accountId = 3L;
        Mockito.when(accountsService.getAccountTransactions(accountId)).thenReturn(null);
        ResponseEntity<List<MovementsResponse>> response = accountsController.getAccountMovements(accountId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void extractMoney_ok() {
        ExtractMoneyRequest request = new ExtractMoneyRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setAtmId(2L);
        request.setCardId(3L);
        Mockito.when(accountsService.extractMoney(Mockito.any())).thenReturn(true);
        ResponseEntity<OperationResponse> response = accountsController.extractMoney(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deposit_ok() {
        DepositMoneyRequest request = new DepositMoneyRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setAtmId(2L);
        request.setCardId(3L);
        Mockito.when(accountsService.depositMoney(Mockito.any())).thenReturn(true);
        ResponseEntity<OperationResponse> response = accountsController.deposit(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deposit_ko() {
        DepositMoneyRequest request = new DepositMoneyRequest();
        request.setAmount(new BigDecimal("100.00"));
        request.setAtmId(2L);
        request.setCardId(3L);
        Mockito.when(accountsService.depositMoney(Mockito.any())).thenReturn(false);
        ResponseEntity<OperationResponse> response = accountsController.deposit(request);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
