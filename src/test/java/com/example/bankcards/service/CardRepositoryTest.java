package com.example.bankcards.service;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CardService cardService;
    @Autowired
    private UserService userService;

    @Test
    void saveAndFindCard() {
        User owner = new User();
        owner.setUsername("alice");
        owner.setPassword("pass");
        owner.setRole(Role.USER);
        owner = userRepository.save(owner);

        Card card1 = Card.builder()
                .number("1111222233334444")
                .owner(owner)
                .expiryDate(LocalDate.of(2025, 10, 31))
                .status(CardStatus.ACTIVE)
                .balance(new BigDecimal("500"))
                .build();
        Card card2 = Card.builder()
                .number("1212212133334444")
                .owner(owner)
                .expiryDate(LocalDate.of(2025, 10, 31))
                .status(CardStatus.BLOCKED)
                .balance(new BigDecimal("600"))
                .build();

        List<Card> cards = new ArrayList<Card>();
        cards.add(card1);
        cards.add(card2);
        owner.setCards(cards);

        cardRepository.save(card1);
        cardRepository.save(card2);


        Card found = cardRepository.findByNumber("1111222233334444").orElse(null);
        assertNotNull(found);
        assertEquals("1111222233334444", found.getNumber());

        CardDto card = cardService.getCard("1111222233334444");

        UserDto dto = userService.getUser(card.getOwnerId());
    }
}
