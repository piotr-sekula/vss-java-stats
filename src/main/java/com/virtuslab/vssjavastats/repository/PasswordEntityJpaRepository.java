package com.virtuslab.vssjavastats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

interface PasswordEntityJpaRepository extends JpaRepository<PasswordEntity, UUID> {

    PasswordEntity findByPasswordHash(String passwordHash);

    @Query(value = "select hp.hashType as hashType, count(*) as amount from hashed_passwords hp group by hp.hashType")
    List<HashTypeAmountDto> findByHashType();
}
