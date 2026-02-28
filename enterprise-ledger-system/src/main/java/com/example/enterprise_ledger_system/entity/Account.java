package com.example.enterprise_ledger_system.entity;

import com.example.enterprise_ledger_system.enums.AccountType;
import com.example.enterprise_ledger_system.enums.NormalBalance;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name= "ACCOUNTS",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_account_number", columnNames = "ACCOUNT_NUMBER"),
                @UniqueConstraint(name = "uk_account_code", columnNames = "ACCOUNT_CODE")
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "parentAccount")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name="PUBLIC_ID", nullable = false, updatable = false, unique = true)
    private UUID publicId;

    @Column(name = "ACCOUNT_NUMBER", nullable = false, length = 36)
    private String accountNumber;

    @Column(name= "ACCOUNT_NAME", nullable = false, length = 150)
    private String accountName;

    @Column(name = "ACCOUNT_CODE", nullable = false, length = 150)
    private String accountCode;

    @Column(nullable = false, name = "ACCOUNT_TYPE")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name="DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name="NORMAL_BALANCE", nullable = false)
    private NormalBalance normalBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ACCOUNT_ID")
    private Account parentAccount;

    @Column(name = "LEVEL_DEPTH")
    private Integer levelDepth;

    @Column(name = "BALANCE")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "ALLOW_NEGATIVE_BALANCE")
    private boolean allowNegativeBalance;

    @Column(name = "RECONCILABLE")
    private boolean reconcilable;

    @Column(name = "LOCKED", nullable = false)
    private boolean locked;

    @Column(name = "SYSTEM_ACCOUNT", nullable = false)
    private boolean systemAccount;

    @Version
    private Long version;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @PrePersist
    public void prePersist() {
        this.publicId = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if(this.balance == null){
            this.balance = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null) return false;

        if(org.hibernate.Hibernate.getClass(this) != org.hibernate.Hibernate.getClass(o)) return false;

        Account that = (Account) o;

        return publicId != null && publicId.equals(that.publicId);
    }

    @Override
    public int hashCode(){
        return getClass().hashCode();
    }
}
