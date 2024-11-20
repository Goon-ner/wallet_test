package com.agapov.wallet.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "wallets")
@Data
public class WalletEntity {
    @Id
    private UUID walletId;

    private BigDecimal balance;

}

