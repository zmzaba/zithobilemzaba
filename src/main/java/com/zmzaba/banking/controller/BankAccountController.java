package com.zmzaba.banking.controller;

import com.zmzaba.banking.service.BankAccountService;
import com.zmzaba.banking.exception.InsufficientFundsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bank")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestParam Long accountId, @RequestParam BigDecimal amount) {
        try {
            bankAccountService.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body("Insufficient funds for withdrawal");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Withdrawal failed due to system error");
        }
    }
}