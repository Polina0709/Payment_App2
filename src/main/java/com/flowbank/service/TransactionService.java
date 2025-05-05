package com.flowbank.service;

import com.flowbank.entity.Account;
import com.flowbank.entity.Transaction;
import com.flowbank.repository.AccountRepository;
import com.flowbank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LogService logService;

    @Transactional
    public boolean transfer(String senderAccNum, String receiverAccNum, BigDecimal amount) {
        Account sender = accountRepository.findByAccountNumber(senderAccNum).orElse(null);
        Account receiver = accountRepository.findByAccountNumber(receiverAccNum).orElse(null);

        if (sender == null || receiver == null || sender.isBlocked() || receiver.isBlocked()) return false;
        if (sender.getBalance().compareTo(amount) < 0) return false;

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction tx = new Transaction();
        tx.setSenderAccount(sender);
        tx.setReceiverAccount(receiver);
        tx.setAmount(amount);
        transactionRepository.save(tx);

        logService.logAction(sender.getUser(), "Transferred " + amount + " to account " + receiverAccNum);
        log.info("Transferred " + amount + " to account " + receiverAccNum);
        logService.logAction(receiver.getUser(), "Received " + amount + " from account " + senderAccNum);
        log.info("Received " + amount + " from account " + senderAccNum);

        return true;
    }


    @Transactional
    public boolean topUp(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElse(null);
        if (account == null || account.isBlocked()) return false;

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setSenderAccount(null);
        tx.setReceiverAccount(account);
        tx.setAmount(amount);
        transactionRepository.save(tx);

        logService.logAction(account.getUser(), "Topped up account " + accountNumber + " by " + amount);
        log.info("Topped up account " + accountNumber + " by " + amount);

        return true;
    }


    public List<Transaction> getByClientId(UUID clientId) {
        return transactionRepository.findBySenderOrReceiver(clientId);
    }
}
