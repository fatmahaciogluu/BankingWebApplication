package com.BankApp.BankWebApplication.services;

import com.BankApp.BankWebApplication.models.AccountHolder;
import com.BankApp.BankWebApplication.models.Balance;
import com.BankApp.BankWebApplication.repositories.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;

    @Autowired
    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    public void createBalance(AccountHolder accountHolder, Long amount, String transType){

        Balance balance = new Balance();

        if(accountHolder.getBalances()==null){
            List<Balance> balanceList = new ArrayList<>();
            balance.setAmount(amount);
            balanceList.add(balance);
            accountHolder.setBalances(balanceList);
        }
        else {
            int latestBalRecIdx = accountHolder.getBalances().size() - 1;
            if (transType.equals("DB")) {
                balance.setAmount(accountHolder.getBalances().get(latestBalRecIdx).getAmount() - amount);
            } else {
                balance.setAmount(accountHolder.getBalances().get(latestBalRecIdx).getAmount() + amount);
            }
        }

        balance.setDate(LocalDateTime.now());
        balance.setDb_cr_indicator(transType);

        balance.setAccountHolders(accountHolder);
        accountHolder.getBalances().add(balance);

        balanceRepository.save(balance);
    }
    public List<Balance> findBalanceHistoryByUsername(String username) {
        return balanceRepository.findBalanceHistoryByUsername(username);
    }

    public Balance updateBalance(Balance balance, Long amount, String transType) {

        if(transType.equals("DB")){
            balance.setAmount(balance.getAmount() - amount);
        }
        else{
            balance.setAmount(balance.getAmount() + amount);
        }
        balance.setDb_cr_indicator(transType);
        return balanceRepository.save(balance);
    }

    public Balance showBalance(String username) {
        return balanceRepository.findBalanceByAccountHolderUsername(username);
    }

    public Balance getUsernameByAccountHolderId(Long id) {

        return balanceRepository.findUsernameByAccountHolderId(id);
    }
}
