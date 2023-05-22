package com.virtuslab.vssjavastats.domain;

import com.virtuslab.vssjavastats.controller.HashTypesView;

public interface StatsRepository {

    void save(Password password);

    Password getByHash(String hash);

    HashTypesView countHashTypes();
}
