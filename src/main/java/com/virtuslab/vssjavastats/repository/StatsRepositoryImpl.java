package com.virtuslab.vssjavastats.repository;

import com.virtuslab.vssjavastats.domain.Password;
import com.virtuslab.vssjavastats.domain.StatsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class StatsRepositoryImpl implements StatsRepository {

    private final PasswordEntityJpaRepository jpaRepository;

    public StatsRepositoryImpl(PasswordEntityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Password password) {
        PasswordEntity entity = PasswordEntity.from(password);
        jpaRepository.save(entity);
    }

    @Override
    public Password getByHash(String hash) {
        final var foundEntity = jpaRepository.findByPasswordHash(hash);
        return new Password(foundEntity.getHashType(), foundEntity.getPassword(), foundEntity.getPasswordHash());
    }
}
