package com.BankApp.BankWebApplication.controllers;

import com.BankApp.BankWebApplication.models.AccountHolder;
import com.BankApp.BankWebApplication.responses.ApiResponse;
import com.BankApp.BankWebApplication.services.AccountHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins = "http://localhost:3000")
public class AccountHolderController {
    private final AccountHolderService accountHolderService;

    @Autowired
    public AccountHolderController(AccountHolderService accountHolderService){
        this.accountHolderService = accountHolderService;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public void createAccountHolder(@RequestBody AccountHolder user){
        accountHolderService.createAccountHolder(user);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Boolean>> deleteAccountHolder(@PathVariable String username){
        return accountHolderService.deleteAccountHolderByUsername(username);
    }

    @PutMapping("/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AccountHolder updateAccountHolder(@PathVariable String username, @RequestBody AccountHolder accountHolder){
        return accountHolderService.updateAccountHolder(username, accountHolder);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AccountHolder> viewAccounts(){
        return accountHolderService.getAllAccountHolders();
    }

    @GetMapping("/search/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<AccountHolder>> getAccountHolderByUsername(@PathVariable String username){
        return accountHolderService.getAccountHolderByUsername(username);
    }

    @GetMapping("/getRoles")
    public List<String> getRoles(Authentication auth){
        return accountHolderService.getRolesByUsername(auth.getName());
    }

    @GetMapping("/getUsername")
    public String getUsername(Authentication auth){
        return auth.getName();
    }
}