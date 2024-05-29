package com.test.userscontrol.application;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private LocalDateTime now;
    private User newUser;
    private User user1;
    private User user2;
    private List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        now = LocalDateTime.now();

        newUser = new User(null, "John Doe", "john.doe@example.com", "password123", "ADMIN", "default.png", null);

        user1 = new User(1, "John Doe", "john.doe@example.com", "password123", "ADMIN", "default.png", now);
        user2 = new User(2, "Jane Doe", "jane.doe@example.com", "password123", "USER", "default.png", now);

        users = Arrays.asList(user1, user2);
    }

    @Test
    void shouldSaveUser() {
        // Configura el comportamiento esperado del método 'save()' del mock 'userRepository'
        // Cuando se llama al método 'save()' con 'newUser' como argumento, devuelve 'newUser'
        when(userRepository.save(newUser)).thenReturn(newUser);
        // Llama al método 'save()' en el objeto 'userService' con 'newUser' como argumento y guarda el resultado en 'savedUser'
        User savedUser = userService.save(newUser);
        // Verifica que el método 'save()' del mock 'userRepository' se haya llamado exactamente una vez con 'newUser' como argumento
        verify(userRepository, times(1)).save(newUser);
        // Verifica que 'savedUser' sea igual a 'newUser'
        assertEquals(newUser, savedUser);

    }

    @Test
    void shouldFindAllUsers() {
        // Configura el comportamiento esperado del método 'findAll()' del mock 'userRepository'
        // Cuando se llama al método 'findAll()', devuelve la lista de usuarios 'users'
        when(userRepository.findAll()).thenReturn(users);
        // Llama al método 'findAll()' en el objeto 'userService' y guarda el resultado en 'result'
        Iterable<User> result = userService.findAll();
        // Verifica que el método 'findAll()' del mock 'userRepository' se haya llamado exactamente una vez
        verify(userRepository, times(1)).findAll();
        // Verifica que el número de elementos en 'result' sea igual a 2 (la cantidad de usuarios en 'users')
        assertEquals(2, ((Collection<?>) result).size());
        // Verifica que el elemento en la posición 0 sea igual a user1
        assertEquals(user1, ((List<User>) result).get(0));
        // Verifica que el elemento en la posición 1 sea igual a user2
        assertEquals(user2, ((List<User>) result).get(1));
    }

    @Test
    void shouldReturnAnUserFindedById(){
        // Configura el comportamiento esperado al llamar al método 'findById' del objeto 'userRepository'
        // Cuando se llama con el ID 1 como argumento, devuelve 'user1'
        when(userRepository.findById(1)).thenReturn(user1);
        // Llama al método findById del UserService
        User userFound = userService.findById(1);
        // Verifica que el método 'findById' del objeto 'userRepository' se llamó una vez con el ID 1
        verify(userRepository, times(1)).findById(1);
        // Verifica que el usuario devuelto es igual a 'user1'
        assertEquals(user1, userFound);
    }
}