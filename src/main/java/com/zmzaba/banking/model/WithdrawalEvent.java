package com.zmzaba.banking.model;

import java.math.BigDecimal;

public class WithdrawalEvent {

    private final BigDecimal amount;
    private final Long accountId;
    private final String status;

    public WithdrawalEvent(BigDecimal amount, Long accountId, String status) {
        this.amount = amount;
        this.accountId = accountId;
        this.status = status;
    }
    public String toJson() {
        return String.format("{\"amount\":\"%s\",\"accountId\":%d,\"status\":\"%s\"}",
                amount, accountId, status);
    }
}