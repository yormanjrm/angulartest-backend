package com.test.userscontrol.infrastructure.exception;

// Define una clase pública llamada InvalidPasswordException que extiende RuntimeException.
public class InvalidPasswordException extends RuntimeException {
    // Constructor que toma un mensaje como parámetro.
    public InvalidPasswordException(String message) {
        // Llama al constructor de la superclase (RuntimeException) pasando el mensaje.
        super(message);
    }
}