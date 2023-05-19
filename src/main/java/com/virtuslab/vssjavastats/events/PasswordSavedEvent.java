package com.virtuslab.vssjavastats.events;

import com.virtuslab.vssjavastats.domain.Password;

record PasswordSavedEvent(String hashType, String password, String hash) {

    Password toPassword() {
        return new Password(this.hashType, this.password, this.hash);
    }
}
