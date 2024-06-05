package com.test.userscontrol.infrastructure.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserNotFoundExceptionTests {
    @Test
    void testUserNotFoundExceptionMessage() {
        // Define un mensaje de prueba
        String message = "User not found";
        // Crea una instancia de UserNotFoundException con el mensaje de prueba
        UserNotFoundException exception = new UserNotFoundException(message);
        // Verifica que el mensaje almacenado en la excepción es el mismo que el mensaje de prueba
        Assertions.assertEquals(message, exception.getMessage());
    }
}