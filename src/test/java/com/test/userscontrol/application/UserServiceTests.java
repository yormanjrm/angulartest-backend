package com.test.userscontrol.application;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {
    @MockBean
    private IUserRepository iUserRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        User user = new User(null, "Test User", "test@example.com", "password", "ADMIN", "default.png", LocalDateTime.now());
        User savedUser = new User(1, "Test User", "test@example.com", "password", "ADMIN", "default.png", LocalDateTime.now());

        when(iUserRepository.save(any(User.class))).thenReturn(user); // Cuando el método save de iUserRepository sea llamado con cualquier instancia de la clase User, entonces debe devolver el objeto user.
        User result = userService.save(user);

        assertEquals(savedUser.getId(), result.getId());
        assertEquals(savedUser.getName(), result.getName());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(savedUser.getPassword(), result.getPassword());
        assertEquals(savedUser.getRole(), result.getRole());
        assertEquals(savedUser.getImage(), result.getImage());
        assertEquals(savedUser.getDateCreated(), result.getDateCreated());
        verify(iUserRepository, times(1)).save(user); // Verifica que el metodo save con el parametro user solo se haya ejecutado 1 vez
    }

}