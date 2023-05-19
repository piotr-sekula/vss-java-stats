package com.virtuslab.vssjavastats.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PasswordDeserializerTest {

    @Test
    void shouldDeserializePasswordEvent() {
        // when
        final var result = PasswordDeserializer.deserializePassword("{\"hashType\":\"MD5\",\"password\":\"abc\",\"hash\":\"hash123\"}");

        // then
        assertEquals("MD5", result.hashType());
        assertEquals("abc", result.password());
        assertEquals("hash123", result.hash());
    }
}