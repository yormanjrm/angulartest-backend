package com.test.userscontrol.infrastructure.exception;

// Define una clase pública llamada DuplicateUserException que extiende RuntimeException.
public class DuplicateUserException extends RuntimeException {
    // Constructor que toma un mensaje como parámetro.
    public DuplicateUserException(String message){
        // Llama al constructor de la superclase (RuntimeException) pasando el mensaje.
        super(message);
    }
}
