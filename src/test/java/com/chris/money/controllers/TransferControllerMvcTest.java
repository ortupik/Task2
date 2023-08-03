package com.chris.money.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.chris.money.domain.Account;
import com.chris.money.domain.Transfer;
import com.chris.money.exceptions.InsufficientBalanceException;
import com.chris.money.exceptions.InvalidTransferException;
import com.chris.money.requests.TransferRequest;
import com.chris.money.services.TransferService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransferController.class)
@AutoConfigureMockMvc
public class TransferControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransferService transferService;

    @InjectMocks
    private TransferController transferController;

    @Test
    void testCreateTransfer() throws Exception {
        TransferRequest request = new TransferRequest(1L, 2L, 500.0);
        Account sourceAccount = new Account("12345678", 1000.0);
        Account destinationAccount = new Account("87654321", 500.0);
        Transfer transfer = new Transfer(sourceAccount, destinationAccount, request.getAmount());
        
        when(transferService.transferMoney(request.getSourceAccountId(), request.getDestinationAccountId(), request.getAmount()))
            .thenReturn(transfer);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfers").contentType("application/json")
                .content("{ \"sourceAccountId\": 1, \"destinationAccountId\": 2, \"amount\": 500.0 }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(500.0));
    }

    @Test
    void testCreateTransfer_InvalidRequest() throws Exception {
        // Test creating a transfer with invalid request (e.g., missing sourceAccountId)
        mockMvc.perform(MockMvcRequestBuilders.post("/transfers").contentType("application/json")
                .content("{ \"destinationAccountId\": 2, \"amount\": 500.0 }"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testCreateTransfer_InsufficientBalance() throws Exception {
        TransferRequest request = new TransferRequest(1L, 2L, 2000.0);
        when(transferService.transferMoney(request.getSourceAccountId(), request.getDestinationAccountId(), request.getAmount()))
            .thenThrow(InsufficientBalanceException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfers").contentType("application/json")
                .content("{ \"sourceAccountId\": 1, \"destinationAccountId\": 2, \"amount\": 2000.0 }"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testCreateTransfer_InvalidTransfer() throws Exception {
        TransferRequest request = new TransferRequest(1L, 2L, -500.0);
        when(transferService.transferMoney(request.getSourceAccountId(), request.getDestinationAccountId(), request.getAmount()))
            .thenThrow(InvalidTransferException.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfers").contentType("application/json")
                .content("{ \"sourceAccountId\": 1, \"destinationAccountId\": 2, \"amount\": -500.0 }"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
