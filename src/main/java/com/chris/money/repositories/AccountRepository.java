package com.chris.money.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chris.money.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}