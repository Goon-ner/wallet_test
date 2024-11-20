package com.agapov.wallet.service;

import com.agapov.wallet.entity.WalletEntity;
import com.agapov.wallet.entity.WalletRepository;
import com.agapov.wallet.exeption.InsufficientFundsException;
import com.agapov.wallet.exeption.ResourceNotFoundException;
import com.agapov.wallet.exeption.UnknownOperationExeption;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public void performOperation(UUID walletId, String operationType, BigDecimal amount)
            throws ResourceNotFoundException, InsufficientFundsException, UnknownOperationExeption {
        WalletEntity wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

        synchronized (walletId.toString().intern()) {
            if ("DEPOSIT".equals(operationType)) {
                wallet.setBalance(wallet.getBalance().add(amount));
            } else if ("WITHDRAW".equals(operationType)) {
                if (wallet.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException("Insufficient funds");
                }
                wallet.setBalance(wallet.getBalance().subtract(amount));
            } else throw new UnknownOperationExeption("Unknown Operation");
            walletRepository.save(wallet);
        }
    }

    public BigDecimal getBalance(String walletId) throws ResourceNotFoundException {
        WalletEntity wallet = walletRepository.findById(UUID.fromString(walletId))
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));
        return wallet.getBalance();
    }
}

