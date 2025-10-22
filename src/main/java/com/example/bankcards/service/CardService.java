package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    private Card getCardEntity(String number) {
        return cardRepository.findByNumber(number)
                .orElseThrow(() -> new RuntimeException("Card not found"));
    }

    @Transactional(readOnly = true)
    public CardDto getCard(String number) {
        Card card = getCardEntity(number);
        return cardMapper.toDto(card);
    }

    @Transactional
    public void blockCard(String number) {
        Card card = getCardEntity(number);
        card.block();
    }

    @Transactional
    public void activateCard(String number) {
        Card card = getCardEntity(number);
        card.activate();
    }

    @Transactional
    public void addCard(CardDto dto, User user) {
        Card card = cardMapper.toEntity(dto, user);

        cardRepository.save(card);
    }

    @Transactional
    public void deposit(String number, BigDecimal depositAmount) {
        Card card = getCardEntity(number);
        card.deposit(depositAmount);
    }

    @Transactional
    public Boolean debit(String number, BigDecimal debitAmount) {
        Card card = getCardEntity(number);
        return card.debit(debitAmount);
    }

    @Transactional
    public void deleteCard(String cardNumber) {
        if (!cardRepository.existsByNumber(cardNumber)) {
            throw new RuntimeException("Card not found");
        }
        cardRepository.deleteByNumber(cardNumber);
    }

    @Transactional
    public Boolean Transfer(String fromCardNumber, String toCardNumber, BigDecimal amount) {
        Boolean successful = debit(fromCardNumber, amount);
        if (!successful) {
            return false;
        }
        deposit(toCardNumber, amount);
        return true;
    }

    @Transactional(readOnly = true)
    public List<CardDto> getUserCardsByStatus(Long userId, CardStatus status) {
        return cardRepository.findByOwnerIdAndStatus(userId, status)
                .stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList());
    }
}
