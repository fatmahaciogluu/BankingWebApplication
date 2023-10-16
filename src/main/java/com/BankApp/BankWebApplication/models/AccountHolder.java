package com.BankApp.BankWebApplication.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;



import java.util.List;
@Entity
@Table(name = "account_holders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountHolder {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String roles;

    @NotNull
    private String address;

    @OneToMany(mappedBy = "accountHolders", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "accountHolders", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Balance> balances;
}
