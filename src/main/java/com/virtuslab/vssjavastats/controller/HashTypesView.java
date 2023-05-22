package com.virtuslab.vssjavastats.controller;

import com.virtuslab.vssjavastats.repository.HashTypeAmountDto;

import java.util.List;
import java.util.stream.Collectors;

public class HashTypesView {
    private final List<TypeAmount> data;

    private HashTypesView(List<TypeAmount> data) {
        this.data = data;
    }

    public static HashTypesView from(List<HashTypeAmountDto> dtos) {
        final var typeAmountList = dtos.stream()
                .map(dto -> new TypeAmount(dto.getHashType(), dto.getAmount()))
                .collect(Collectors.toList());
        return new HashTypesView(typeAmountList);
    }

    public List<TypeAmount> getData() {
        return data;
    }

    private record TypeAmount(String type, int amount) {
    }
}
