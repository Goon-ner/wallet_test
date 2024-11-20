package com.agapov.wallet.api.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class WalletOperationRequest {
    private UUID walletId;
    private String operationType;
    private BigDecimal amount;

    public WalletOperationRequest() {

    }
}
