package com.virtuslab.vssjavastats.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

class PasswordDeserializer {

    static PasswordSavedEvent deserializePassword(String event) {
        ObjectMapper mapper = new ObjectMapper();
        PasswordSavedEvent passwordSavedEvent;

        try {
            passwordSavedEvent = mapper.readValue(event, PasswordSavedEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return passwordSavedEvent;
    }
}
