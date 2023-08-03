package com.chris.money.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chris.money.domain.Transfer;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}