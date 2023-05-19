package com.virtuslab.vssjavastats.domain;

public interface StatsRepository {

    void save(Password password);

    Password getByHash(String hash);
}
