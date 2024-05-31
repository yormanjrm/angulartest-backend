package com.test.userscontrol.infrastructure.exception;

// Define una clase pública llamada UserNotFoundException que extiende RuntimeException.
public class UserNotFoundException extends RuntimeException {
    // Constructor que toma un mensaje como parámetro.
    public UserNotFoundException(String message) {
        // Llama al constructor de la superclase (RuntimeException) pasando el mensaje.
        super(message);
    }
}