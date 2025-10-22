package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardDto toDto(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        if (card.getOwner() == null) {
            throw new IllegalArgumentException("CardDto need to be made during transaction");
        }

        return CardDto.builder()
                .maskedNumber(maskCardNumber(card.getNumber()))
                .ownerId(card.getOwner().getId())
                .ownerName(card.getOwner().getUsername())
                .status(card.getStatus().name())
                .expireDate(card.getExpiryDate())
                .balance(card.getBalance())
                .build();
    }

    public Card toEntity(CardDto dto, User owner) {
        if (dto == null) {
            throw new IllegalArgumentException("CardDto cannot be null");
        }

        return Card.builder()
                .number(unmaskCardNumber(dto.getMaskedNumber()))
                .owner(owner)
                .status(CardStatus.valueOf(dto.getStatus()))
                .expiryDate(dto.getExpireDate())
                .balance(dto.getBalance())
                .build();
    }

    private String maskCardNumber(String number) {
        if (number == null || number.length() < 4) return number;
        int length = number.length();
        String masked = "*".repeat(length - 4) + number.substring(length - 4);
        return masked;
    }

    private String unmaskCardNumber(String maskedNumber) {
        // В реальной жизни тут должно быть получение полного номера карты, например, из базы.
        // Для примера просто вернем maskedNumber без маски
        return maskedNumber.replace("*", "0");
    }
}