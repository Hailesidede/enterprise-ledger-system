package com.example.enterprise_ledger_system.repositories;

import com.example.enterprise_ledger_system.entity.Account;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByPublicId(UUID publicId  );

    Optional<Account> findByAccountNumber(String accountNumber);

    Optional<Account> findByAccountNumberAndStatusTrue(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.id =:id")
    Optional<Account>findByIdForUpdate(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.publicId =:publicId")
    Optional<Account> findByPublicIdForUpdate(@Param("publicId") UUID publicId);
}
