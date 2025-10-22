package com.example.bankcards.controller;

import com.example.bankcards.dto.CardDto;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping({"user/{number}","admin//{number}"})
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCardOwner(#number, authentication.name)")
    public ResponseEntity<CardDto> getCard(@PathVariable String number) {
        return ResponseEntity.ok(cardService.getCard(number));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createCard(@RequestBody @Valid CardDto cardDto) {
        cardService.addCard(cardDto, null); // лучше передать User в сервисе через ownerId
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{number}/block")
    @PreAuthorize("hasRole('ADMIN') or @securityService.isCardOwner(#number, authentication.name)")
    public ResponseEntity<Void> blockCard(@PathVariable String number) {
        cardService.blockCard(number);
        return ResponseEntity.ok().build();
    }

    @PostMapping("admin/{number}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateCard(@PathVariable String number) {
        cardService.activateCard(number);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("admin/{number}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCard(@PathVariable String number) {
        cardService.deleteCard(number);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("user/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> transfer(@RequestParam String fromCard,
                                           @RequestParam String toCard,
                                           @RequestParam BigDecimal amount) {
        boolean success = cardService.Transfer(fromCard, toCard, amount);
        if (success) {
            return ResponseEntity.ok("Transfer successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds");
        }
    }

    @GetMapping({"user/{userId}","admin//{userId}"})
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public ResponseEntity<List<CardDto>> getUserCards(@PathVariable Long userId,
                                                      @RequestParam(required = false) String status) {
        CardStatus cardStatus = status != null ? CardStatus.valueOf(status.toUpperCase()) : null;
        List<CardDto> cards;
        if (cardStatus != null) {
            cards = cardService.getUserCardsByStatus(userId, cardStatus);
        } else {
            cards = cardService.getUserCardsByStatus(userId, null); // метод можно доработать для null
        }
        return ResponseEntity.ok(cards);
    }
}
