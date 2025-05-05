package com.flowbank.service;

import com.flowbank.entity.Account;
import com.flowbank.repository.AccountRepository;
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
public class AccountService {

    private final AccountRepository accountRepository;
    private final LogService logService;

    public List<Account> findByUserId(UUID userId) {
        return accountRepository.findByUserId(userId);
    }

    @Transactional
    public void blockAccount(UUID accountId) {
        accountRepository.findById(accountId).ifPresent(account -> {
            account.setBlocked(true);
            accountRepository.save(account);
            logService.logAction(account.getUser(), "Blocked account " + account.getAccountNumber());
            log.info("Blocked account {}", account.getAccountNumber());
        });
    }

    @Transactional
    public void unblockAccount(UUID accountId) {
        accountRepository.findById(accountId).ifPresent(account -> {
            account.setBlocked(false);
            accountRepository.save(account);
            logService.logAction(account.getUser(), "Unblocked account " + account.getAccountNumber());
            log.info("Admin unblocked account {}", account.getAccountNumber());
        });
    }

    public boolean blockAccountIfOwner(UUID accountId, UUID userId) {
        return accountRepository.findById(accountId)
                .filter(acc -> acc.getUser().getId().equals(userId))
                .map(acc -> {
                    acc.setBlocked(true);
                    accountRepository.save(acc);
                    logService.logAction(acc.getUser(), "Client blocked own account " + acc.getAccountNumber());
                    log.info("Client blocked own account {}", acc.getAccountNumber());
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void topUpAccount(String accountNumber, BigDecimal amount) {
        accountRepository.findByAccountNumber(accountNumber).ifPresent(account -> {
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            logService.logAction(account.getUser(), "Topped up account " + accountNumber + " by " + amount);
            log.info("Topped up account {}", account.getAccountNumber());
        });
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
}
