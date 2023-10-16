package com.BankApp.BankWebApplication.services;

import com.BankApp.BankWebApplication.models.AccountHolder;
import com.BankApp.BankWebApplication.models.Balance;
import com.BankApp.BankWebApplication.models.Transaction;
import com.BankApp.BankWebApplication.repositories.AccountHolderRepository;
import com.BankApp.BankWebApplication.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BalanceService balanceService;
    private final AccountHolderRepository accountHolderRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              BalanceService balanceService,
                              AccountHolderRepository accountHolderRepository) {
        this.transactionRepository = transactionRepository;
        this.balanceService = balanceService;
        this.accountHolderRepository = accountHolderRepository;
    }

    public void createTransaction(AccountHolder foundUser, Balance balance, String transType, String description){

        Transaction transaction = new Transaction();
        transaction.setDate(LocalDateTime.now());
        transaction.setDescription(description);
        transaction.setDb_cr_indicator(transType);
        transaction.setAmount(balance.getAmount());
        foundUser.getTransactions().add(transaction);
        transaction.setAccountHolders(foundUser);
        transactionRepository.save(transaction);
    }
    public List<Transaction> viewTransactionsHistory(String username){
        return transactionRepository.findTransactionByAccountHoldersUsername(username);
    }
    private static boolean isTransactionValid(Long currentBalance, Long amount, String transType){

        if(transType.equals("DB"))
        {
            return amount <= currentBalance && amount > 0;
        }
        if(transType.equals("CR"))
        {
            return amount > 0;
        }
        return false;
    }
    private void validateTransaction(Balance foundAccLatestBalanceRec, Long amount, String transType, Boolean toUpdate){
        if(!isTransactionValid(foundAccLatestBalanceRec.getAmount(), amount, transType)){
            throw new IllegalStateException("Invalid transaction");
        }

        if(toUpdate)
            balanceService.updateBalance(foundAccLatestBalanceRec, amount, transType);
        else
            balanceService.createBalance(foundAccLatestBalanceRec.getAccountHolders(), amount, transType);
    }

    public void handleTransaction(String username, Balance balance, String transType, String transDesc){

        AccountHolder foundAcc = accountHolderRepository.findByUsername(username).orElse(null);

        if(foundAcc == null)
            throw new IllegalStateException("User not found");

        int foundAccBalListSize = foundAcc.getBalances().size();
        Balance foundAccLatestBalRec = foundAcc.getBalances().get(foundAccBalListSize - 1);
        LocalDate latestDate = LocalDate.from(foundAccLatestBalRec.getDate());

        Boolean toUpdate = latestDate.isEqual(LocalDate.now());
        validateTransaction(foundAccLatestBalRec, balance.getAmount(), transType, toUpdate);

        createTransaction(foundAcc, balance, transType, transDesc);
        accountHolderRepository.save(foundAcc);
    }
    public void depositCash(String username, Balance balance) {
        handleTransaction(username, balance, "CR", "Deposit");
    }

    public void withdrawCash(String username, Balance balance) {
        handleTransaction(username, balance, "DB", "Withdrawal");
    }

    public void transferCash(String sender, String receiver, Balance balance) {
        AccountHolder receiverAcc = accountHolderRepository.getAccountHolderByUsername(receiver).orElse(null);
        if(receiverAcc == null || receiverAcc.getRoles().equals("ADMIN"))
            throw new NoSuchElementException("User not found");
        handleTransaction(sender, balance, "DB", "You" + " transferred $" + balance.getAmount() + " to @_" + receiver);
        handleTransaction(receiver, balance, "CR", "$" + balance.getAmount() + " was transferred to you by @_" + sender);
    }
}
