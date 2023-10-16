package com.BankApp.BankWebApplication.repositories;

import com.BankApp.BankWebApplication.models.AccountHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountHolderRepository extends JpaRepository<AccountHolder,Long> {

    Optional<AccountHolder> findByUsername(String username);

    @Query(value = "SELECT roles FROM account_holders WHERE username= ?1", nativeQuery = true)
    List<String> getRolesByUsername(String username);

    @Query(value = "SELECT * FROM account_holders WHERE username= ?1", nativeQuery = true)
    Optional<AccountHolder> getAccountHolderByUsername(String name);
}
