package com.flowbank.repository;

import com.flowbank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t FROM Transaction t " +
            "WHERE t.senderAccount.user.id = :clientId OR t.receiverAccount.user.id = :clientId")
    List<Transaction> findBySenderOrReceiver(@Param("clientId") UUID clientId);
}
