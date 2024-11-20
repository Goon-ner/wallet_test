package com.agapov.wallet.controller;

import com.agapov.wallet.api.request.WalletOperationRequest;
import com.agapov.wallet.exeption.UnknownOperationExeption;
import org.springframework.http.ResponseEntity;
import com.agapov.wallet.exeption.InsufficientFundsException;
import com.agapov.wallet.exeption.ResourceNotFoundException;
import com.agapov.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/wallet")
    public ResponseEntity<String> performOperation(@RequestBody WalletOperationRequest request) {
        try {
            walletService.performOperation(request.getWalletId(), request.getOperationType(), request.getAmount());
            return ResponseEntity.ok("Operation successful");
        } catch (IllegalArgumentException | UnknownOperationExeption |
                 ResourceNotFoundException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/wallets/{walletId}")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable String walletId) {
        try {
            BigDecimal balance = walletService.getBalance(walletId);
            return ResponseEntity.ok(balance);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

