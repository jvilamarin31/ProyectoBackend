package com.example.demo;

import com.example.demo.exception.JwtInvalidTokenException;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "9f82a1c7b4d9e6f0a3c5d7e8f9b0c1d2e3f4a5b6c7d8e9f0a1b2c3d4e5f6a7b8";

    private JwtService buildJwtService(long expirationMinutes) {
        JwtService jwtService = new JwtService();
        setField(jwtService, "secret", SECRET);
        setField(jwtService, "expirationMinutes", expirationMinutes);
        return jwtService;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("No se pudo setear el campo: " + fieldName, e);
        }
    }

    @Test
    void generateToken_and_extractUserId_shouldReturnSameUserId() {
        JwtService jwtService = buildJwtService(60);
        String userId = "123";

        String token = jwtService.generateToken(userId);
        String extracted = jwtService.extractUserId(token);

        assertNotNull(token);
        assertEquals(userId, extracted);
    }

    @Test
    void isValid_shouldReturnTrue_whenTokenIsValid() {
        JwtService jwtService = buildJwtService(60);
        String token = jwtService.generateToken("abc");

        boolean valid = jwtService.isValid(token);

        assertTrue(valid);
    }

    @Test
    void isValid_shouldThrowJwtInvalidTokenException_whenTokenIsInvalid() {
        JwtService jwtService = buildJwtService(60);
        String fakeToken = "esto.no.es.un.jwt";

        assertThrows(JwtInvalidTokenException.class, () -> jwtService.isValid(fakeToken));
    }

    @Test
    void isValid_shouldThrowJwtInvalidTokenException_whenTokenIsExpired() {
        JwtService jwtService = buildJwtService(-1);
        String tokenExpired = jwtService.generateToken("123");

        assertThrows(JwtInvalidTokenException.class, () -> jwtService.isValid(tokenExpired));
    }
}
