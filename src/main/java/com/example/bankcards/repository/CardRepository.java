package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    List<Card> findByOwnerId(Long ownerId);

    Optional<Card> findByNumber(String number);
    void deleteByNumber(String number);
    Boolean existsByNumber(String number);

    List<Card> findByOwnerIdAndStatus(Long ownerId, CardStatus status);
}

