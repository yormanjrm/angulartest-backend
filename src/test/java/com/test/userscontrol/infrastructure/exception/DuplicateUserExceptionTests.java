package com.test.userscontrol.infrastructure.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DuplicateUserExceptionTests {
    @Test
    void testDuplicateUserExceptionMessage() {
        // Define un mensaje de prueba
        String message = "User already exists";
        // Crea una instancia de DuplicateUserException con el mensaje de prueba
        DuplicateUserException exception = new DuplicateUserException(message);
        // Verifica que el mensaje almacenado en la excepción es el mismo que el mensaje de prueba
        Assertions.assertEquals(message, exception.getMessage());
    }
}
