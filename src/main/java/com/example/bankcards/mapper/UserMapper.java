package com.example.bankcards.mapper;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final CardMapper cardMapper;

    public UserMapper(CardMapper cardMapper) {
        this.cardMapper = cardMapper;
    }

    public UserDto toDto(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<CardDto> cards = user.getCards() != null
                ? user.getCards().stream()
                .map(cardMapper::toDto)
                .collect(Collectors.toList())
                : List.of();

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .cards(cards)
                .build();
    }

    public User toEntity(UserDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("UserDto cannot be null");
        }

        List<Card> cards = dto.getCards() != null
                ? dto.getCards().stream()
                .map(cardDto -> cardMapper.toEntity(cardDto, null)) // Пока owner=null, потом нужно установить
                .collect(Collectors.toList())
                : List.of();

        User user = User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .role(Role.valueOf(dto.getRole()))
                .cards(cards)
                .build();

        cards.forEach(card -> card.setOwner(user));

        return user;
    }
}

