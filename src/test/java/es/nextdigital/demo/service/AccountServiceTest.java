package es.nextdigital.demo.service;

import es.nextdigital.demo.entities.Account;
import es.nextdigital.demo.entities.Atm;
import es.nextdigital.demo.entities.Card;
import es.nextdigital.demo.entities.Movements;
import es.nextdigital.demo.model.request.DepositMoneyRequest;
import es.nextdigital.demo.model.request.ExtractMoneyRequest;
import es.nextdigital.demo.model.response.MovementsResponse;
import es.nextdigital.demo.repositories.AccountRepository;
import es.nextdigital.demo.repositories.AtmRepository;
import es.nextdigital.demo.repositories.CardRepository;
import es.nextdigital.demo.repositories.MovementsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private MovementsRepository movementsRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private AtmRepository atmRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AccountsService accountsService;


    @Test
    void testGetAccountTransactions_accountExists() {
        Account account = new Account();
        account.setId(1L);
        Movements movement = new Movements();
        when(accountRepository.findById(Mockito.any())).thenReturn(Optional.of(account));
        when(movementsRepository.findBySourceAccountOrDestinationAccount(Mockito.any(), Mockito.any())).thenReturn(
                Arrays.asList(movement));

        List<MovementsResponse> result = accountsService.getAccountTransactions(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAccountTransactions_accountDoesNotExist() {
        when(accountRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        List<MovementsResponse> result = accountsService.getAccountTransactions(1L);
        assertNull(result);
    }

    @Test
    void testExtractMoney_debitCard_success() {
        ExtractMoneyRequest request = new ExtractMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);
        request.setAmount(BigDecimal.valueOf(100));

        Card card = new Card();
        card.setCardType(1L);
        card.setLimit(BigDecimal.valueOf(200));
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(150));
        card.setAccount(account);

        Atm atm = new Atm();
        atm.setBankIdentity(1L);
        atm.setCommission(BigDecimal.valueOf(0));

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(atmRepository.findById(Mockito.any())).thenReturn(Optional.of(atm));
        when(accountRepository.save(Mockito.any())).thenReturn(account);

        boolean result = accountsService.extractMoney(request);
        assertTrue(result);
    }

    @Test
    void testExtractMoney_creditCard_success() {
        ExtractMoneyRequest request = new ExtractMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);
        request.setAmount(BigDecimal.valueOf(100));

        Card card = new Card();
        card.setCardType(2L);
        card.setLimit(BigDecimal.valueOf(200));
        card.setCredit(BigDecimal.valueOf(150));

        Atm atm = new Atm();
        atm.setBankIdentity(1L);
        atm.setCommission(BigDecimal.valueOf(0));

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(atmRepository.findById(Mockito.any())).thenReturn(Optional.of(atm));
        when(cardRepository.save(Mockito.any())).thenReturn(card);

        boolean result = accountsService.extractMoney(request);
        assertTrue(result);
    }

    @Test
    void testExtractMoney_exceedsLimit() {
        ExtractMoneyRequest request = new ExtractMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);
        request.setAmount(BigDecimal.valueOf(300));

        Card card = new Card();
        card.setCardType(1L);
        card.setLimit(BigDecimal.valueOf(200));
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(500));
        card.setAccount(account);

        Atm atm = new Atm();
        atm.setBankIdentity(1L);
        atm.setCommission(BigDecimal.valueOf(0));

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(atmRepository.findById(Mockito.any())).thenReturn(Optional.of(atm));

        boolean result = accountsService.extractMoney(request);
        assertFalse(result);
    }

    @Test
    void testExtractMoney_invalidCardType() {
        ExtractMoneyRequest request = new ExtractMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);
        request.setAmount(BigDecimal.valueOf(100));

        Card card = new Card();
        card.setCardType(3L);
        card.setLimit(BigDecimal.valueOf(200));

        Atm atm = new Atm();
        atm.setBankIdentity(1L);
        atm.setCommission(BigDecimal.valueOf(0));

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(atmRepository.findById(Mockito.any())).thenReturn(Optional.of(atm));

        boolean result = accountsService.extractMoney(request);
        assertFalse(result);
    }

    @Test
    void testDepositMoneySuccess() {
        DepositMoneyRequest request = new DepositMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);
        request.setAmount(new BigDecimal("100.00"));

        Account account = new Account();
        account.setBalance(new BigDecimal("200.00"));

        Card card = new Card();
        card.setAccount(account);

        Atm atm = new Atm();
        atm.setBankIdentity(1L);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(atmRepository.findById(1L)).thenReturn(Optional.of(atm));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        boolean result = accountsService.depositMoney(request);

        assertTrue(result);
        assertEquals(new BigDecimal("300.00"), account.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    void testDepositMoneyCardNotFound() {
        DepositMoneyRequest request = new DepositMoneyRequest();
        request.setCardId(1L);

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountsService.depositMoney(request);
        });

        assertEquals("Tarjeta no encontrada", exception.getMessage());
    }

    @Test
    void testDepositMoneyAtmNotFound() {
        DepositMoneyRequest request = new DepositMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);

        Card card = new Card();
        card.setAccount(new Account());

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(atmRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            accountsService.depositMoney(request);
        });

        assertEquals("Cajero no encontrado", exception.getMessage());
    }

    @Test
    void testDepositMoneyDifferentBank() {
        DepositMoneyRequest request = new DepositMoneyRequest();
        request.setCardId(1L);
        request.setAtmId(1L);
        request.setAmount(new BigDecimal("50.00"));

        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));

        Card card = new Card();
        card.setAccount(account);

        Atm atm = new Atm();
        atm.setBankIdentity(2L);

        when(cardRepository.findById(Mockito.any())).thenReturn(Optional.of(card));
        when(atmRepository.findById(Mockito.any())).thenReturn(Optional.of(atm));

        boolean result = accountsService.depositMoney(request);

        assertFalse(result);
        verify(accountRepository, never()).save(any());
    }

}
