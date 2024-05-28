package com.test.userscontrol.application;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTests {
    @Mock
    private IUserRepository iUserRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(iUserRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("Test User", savedUser.getName());
        verify(iUserRepository, times(1)).save(user);
    }
}