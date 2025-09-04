package com.homs.account_rest_api.transactions.model;

import com.homs.account_rest_api.categories.model.Category;
import com.homs.account_rest_api.transactions.enums.TransactionType;
import com.homs.account_rest_api.accounts.model.Account;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cli_transaction")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(updatable = false,nullable = false)
    private String transactionId;

    @Column(name = "transaction_amount",precision = 19,scale = 2,nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_type",nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "transaction_date",nullable = false)
    private LocalDate date;

    @Column(name = "transaction_alias",length = 60)
    private String alias;

    @ManyToOne
    @JoinColumn(name = "transaction_account")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "transaction_category")
    private Category category;
}
