package es.nextdigital.demo.service;

import es.nextdigital.demo.entities.Card;
import es.nextdigital.demo.exception.CustomException;
import es.nextdigital.demo.model.request.ActivateCardRequest;
import es.nextdigital.demo.model.request.ChangePinRequest;
import es.nextdigital.demo.model.request.ModifyCardRequest;
import es.nextdigital.demo.model.response.CardResponse;
import es.nextdigital.demo.model.response.MovementsResponse;
import es.nextdigital.demo.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardService {

    private final CardRepository cardRepository;

    private final ModelMapper modelMapper;

    public void activateCard(ActivateCardRequest request) {
        Card card =
                cardRepository.findById(request.getIdCard()).orElseThrow(() -> new CustomException("Card not found."));
        card.setActive(true);
        card.setPin(request.getPin());
        cardRepository.save(card);
    }


    public void changePin(ChangePinRequest request) {
        Card card =
                cardRepository.findById(request.getIdCard()).orElseThrow(() -> new CustomException("Card not found."));
        if (!card.isActive()) {
            throw new CustomException(
                    "The PIN cannot be changed because the card is not active. Please activate it first.");
        }

        if (!card.getPin().equals(request.getOldPin())) {
            throw new CustomException("The PIN you provided does not match the one on your card.");
        }

        card.setPin(request.getNewPin());
        cardRepository.save(card);
    }

    public CardResponse modify(ModifyCardRequest request) {
        Card card =
                cardRepository.findById(request.getIdCard()).orElseThrow(() -> new CustomException("Card not found."));

        if (!card.isActive()) {
            throw new CustomException(
                    "The PIN cannot be changed because the card is not active. Please activate it first.");
        }

        card.setLimit(request.getWithdrawalLimit());
        card = cardRepository.save(card);
        return modelMapper.map(card, CardResponse.class);
    }
}
