package com.virtuslab.vssjavastats.domain;

public record Password(String hashType, String password, String hash) {
}
