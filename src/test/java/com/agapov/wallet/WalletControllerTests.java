package com.agapov.wallet;
import com.agapov.wallet.api.request.WalletOperationRequest;
import com.agapov.wallet.controller.WalletController;
import com.agapov.wallet.exeption.InsufficientFundsException;
import com.agapov.wallet.exeption.ResourceNotFoundException;
import com.agapov.wallet.service.WalletService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
public class WalletControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @Test
    public void testPerformOperationSuccess() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(UUID.randomUUID());
        request.setOperationType("DEPOSIT");
        request.setAmount(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Operation successful"));
    }

    @Test
    public void testPerformOperationFailsDueToException() throws Exception {
        WalletOperationRequest request = new WalletOperationRequest();
        request.setWalletId(UUID.randomUUID());
        request.setOperationType("WITHDRAW");
        request.setAmount(BigDecimal.valueOf(1000));

        doThrow(new InsufficientFundsException("Insufficient funds")).when(walletService)
                .performOperation(any(UUID.class), any(String.class), any(BigDecimal.class));

        mockMvc.perform(post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Insufficient funds"));
    }

    @Test
    public void testGetBalanceSuccess() throws Exception {
        String walletId = String.valueOf(UUID.randomUUID());
        when(walletService.getBalance(walletId)).thenReturn(BigDecimal.valueOf(200));

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isOk())
                .andExpect(content().string("200"));
    }

    @Test
    public void testGetBalanceFailsDueToResourceNotFound() throws Exception {
        String walletId = String.valueOf(UUID.randomUUID());
        doThrow(new ResourceNotFoundException("Wallet not found")).when(walletService).getBalance(walletId);

        mockMvc.perform(get("/api/v1/wallets/{walletId}", walletId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }
}

