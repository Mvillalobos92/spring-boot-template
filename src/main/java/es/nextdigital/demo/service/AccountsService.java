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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountsService {

    private final MovementsRepository movementsRepository;

    private final AccountRepository accountRepository;

    private final CardRepository cardRepository;

    private final AtmRepository atmRepository;

    private final ModelMapper modelMapper;

    public List<MovementsResponse> getAccountTransactions(Long accountId) {

        Optional<Account> accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty()) {
            return null;
        }

        List<Movements> movementsList =
                movementsRepository.findBySourceAccountOrDestinationAccount(accountId, accountId);

        return movementsList.stream()
                .map(movement -> modelMapper.map(movement, MovementsResponse.class))
                .collect(Collectors.toList());
    }

    public boolean extractMoney(ExtractMoneyRequest request) {
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        Account account = card.getAccount();
        Atm atm = atmRepository.findById(request.getAtmId())
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        //suponemos que somos la entidad 1L(Se podría hacer con enumerados y tal)
        BigDecimal commission = atm.getBankIdentity().equals(1L) ? BigDecimal.ZERO : atm.getCommission();
        BigDecimal total = request.getAmount().add(commission);

        if (total.compareTo(card.getLimit()) > 0) {
            return false;
        }

        if (card.getCardType() == 1L) {
            return processDebit(account, total);
        } else if (card.getCardType() == 2L) {
            return processCredit(card, total);
        }

        return false;
    }

    private boolean processDebit(Account account, BigDecimal total) {
        if (account.getBalance().compareTo(total) >= 0) {
            account.setBalance(account.getBalance().subtract(total));
            accountRepository.save(account);
            return true;
        }
        return false;
    }

    private boolean processCredit(Card card, BigDecimal total) {
        if (card.getCredit().compareTo(total) >= 0) {
            card.setCredit(card.getCredit().subtract(total));
            cardRepository.save(card);
            return true;
        }
        return false;
    }

    public boolean depositMoney(DepositMoneyRequest request) {
        Card card = cardRepository.findById(request.getCardId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        Account account = card.getAccount();
        Atm atm = atmRepository.findById(request.getAtmId())
                .orElseThrow(() -> new RuntimeException("Cajero no encontrado"));

        //suponemos que somos la entidad 1L(Se podría hacer con enumerados y tal)
        if (!atm.getBankIdentity().equals(1L)) {
            return false;
        }

        BigDecimal balance = account.getBalance().add(request.getAmount());
        account.setBalance(balance);
        accountRepository.save(account);
        return true;
    }
}
