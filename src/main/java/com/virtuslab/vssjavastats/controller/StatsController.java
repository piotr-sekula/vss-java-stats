package com.virtuslab.vssjavastats.controller;

import com.virtuslab.vssjavastats.domain.StatsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

    private final StatsRepository repository;

    public StatsController(StatsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/stats")
    HashTypesView getAmountOfHashTypes() {
        return repository.countHashTypes();
    }
}
