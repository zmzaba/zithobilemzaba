package com.zmzaba.banking.service;

import com.zmzaba.banking.exception.InsufficientFundsException;
import com.zmzaba.banking.model.WithdrawalEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BankAccountService{

    private final JdbcTemplate jdbcTemplate;
    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    public BankAccountService(JdbcTemplate jdbcTemplate, NotificationService notificationService) {
        this.jdbcTemplate = jdbcTemplate;
        this.notificationService = notificationService;
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        String sql = "SELECT balance FROM accounts WHERE id = ?";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, new Object[]{accountId}, BigDecimal.class);

        if (balance == null || balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }

        sql = "UPDATE accounts SET balance = balance - ? WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
        if (rowsAffected == 0) {
            throw new IllegalStateException("Withdrawal failed: no rows updated");
        }

        WithdrawalEvent event = new WithdrawalEvent(amount, accountId, "SUCCESSFUL");
        notificationService.publishWithdrawalEvent(event);
        logger.info("Withdrawal successful for account {}", accountId);
    }
}