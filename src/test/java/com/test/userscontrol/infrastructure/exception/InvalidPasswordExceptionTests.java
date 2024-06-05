package com.test.userscontrol.infrastructure.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InvalidPasswordExceptionTests {
    @Test
    void testInvalidPasswordExceptionMessage() {
        // Define un mensaje de prueba
        String message = "Invalid password";
        // Crea una instancia de InvalidPasswordException con el mensaje de prueba
        InvalidPasswordException exception = new InvalidPasswordException(message);
        // Verifica que el mensaje almacenado en la excepción es el mismo que el mensaje de prueba
        Assertions.assertEquals(message, exception.getMessage());
    }
}
