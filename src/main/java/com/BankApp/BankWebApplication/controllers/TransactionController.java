package com.BankApp.BankWebApplication.controllers;

import com.BankApp.BankWebApplication.models.AccountHolder;
import com.BankApp.BankWebApplication.models.Balance;
import com.BankApp.BankWebApplication.models.Transaction;
import com.BankApp.BankWebApplication.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin(origins = "http://localhost:3000",methods = {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE,RequestMethod.PUT})
public class TransactionController {
    private final TransactionService transactionService;
    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }
    @GetMapping("/history")
    @PreAuthorize("hasAuthority('USER')")
    public List<Transaction> viewTransactionsHistory(Authentication auth){
        return transactionService.viewTransactionsHistory(auth.getName());
    }
    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('USER')")
    public void depositCash(Authentication auth, @RequestBody Balance balance){
        transactionService.depositCash(auth.getName(), balance);
    }
    @PostMapping("/withdraw")
    @PreAuthorize("hasAuthority('USER')")
    public void withdrawCash(Authentication auth, @RequestBody Balance balance){
        transactionService.withdrawCash(auth.getName(), balance);
    }
    @PostMapping("/transfer")
    @PreAuthorize("hasAuthority('USER')")
    public void transferCash(Authentication sender, @RequestParam String receiver, @RequestBody Balance balance){
        if(sender.getName().equals(receiver))
            throw new IllegalArgumentException("You cannot transfer money to yourself");
        transactionService.transferCash(sender.getName(), receiver, balance);
    }
}
