package com.BankApp.BankWebApplication.services;

import com.BankApp.BankWebApplication.models.AccountHolder;
import com.BankApp.BankWebApplication.repositories.AccountHolderRepository;
import com.BankApp.BankWebApplication.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AccountHolderService implements UserDetailsService {

    private final AccountHolderRepository accountHolderRepository;

    private final BalanceService balanceService;

    @Autowired
    public AccountHolderService(AccountHolderRepository accountHolderRepository, BalanceService balanceService) {
        this.accountHolderRepository = accountHolderRepository;
        this.balanceService = balanceService;
    }

    public AccountHolder findByUsername(String username){
        return accountHolderRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AccountHolder accHolder = findByUsername(username);
        if(accHolder == null){
            System.out.println(username);
            throw new UsernameNotFoundException(username + "not found");
        }
        return new User(accHolder.getUsername(), accHolder.getPassword(), true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(accHolder.getRoles()));
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);

    public void createAccountHolder(AccountHolder user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        accountHolderRepository.save(user);
        if(user.getRoles().equalsIgnoreCase("USER"))
            balanceService.createBalance(user, 0L, "CR");
    }

    public List<AccountHolder> getAllAccountHolders() {
        return accountHolderRepository.findAll();
    }


    public AccountHolder updateAccountHolder(String username, AccountHolder accountHolder) {
        AccountHolder foundUser = accountHolderRepository.findByUsername(username).orElse(null);
        if(foundUser == null){
            System.out.println("I am here");
            throw new UsernameNotFoundException(username + "not found");
        }
        System.out.println(foundUser.getUsername());
        foundUser.setEmail(accountHolder.getEmail());
        foundUser.setAddress(accountHolder.getAddress());

        return accountHolderRepository.save(foundUser);
    }


    public AccountHolder getAccountHolderById(Long id) {
        return accountHolderRepository.findById(id).orElse(null);
    }

    public List<String> getRolesByUsername(String username) {
        return accountHolderRepository.getRolesByUsername(username);
    }

    public ResponseEntity<ApiResponse<AccountHolder>> getAccountHolderByUsername(String name) {
        Optional<AccountHolder> foundUser = accountHolderRepository.getAccountHolderByUsername(name);
        if(foundUser.isEmpty())
            throw new NoSuchElementException(name + "not found");
        return ResponseEntity.ok(ApiResponse.of(foundUser.get()));
    }

    public ResponseEntity<ApiResponse<Boolean>> deleteAccountHolderByUsername(String username) {

        AccountHolder foundUser = accountHolderRepository.findByUsername(username).orElse(null);
        if(foundUser != null){
            accountHolderRepository.deleteById(foundUser.getId());
            return ResponseEntity.ok(ApiResponse.of(true));
        }
        throw new NoSuchElementException(username + "not found");
    }
}