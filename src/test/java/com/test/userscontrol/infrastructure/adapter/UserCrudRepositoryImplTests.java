package com.test.userscontrol.infrastructure.adapter;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.entity.UserEntity;
import com.test.userscontrol.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserCrudRepositoryImpTests {

    @Mock
    private IUserCrudRepository userCrudRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserCrudRepositoryImp userCrudRepositoryImp;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DirtiesContext
    void shouldSaveUser() {
        // Crear un User y UserEntity de prueba
        LocalDateTime now = LocalDateTime.now();
        User user = new User(null, "John Doe", "john.doe@example.com", "password", "ADMIN", "default.png", now, now);
        UserEntity userEntity = new UserEntity(1, "John Doe", "john.doe@example.com", "password", "ADMIN", "default.png", now, now);

        // Configurar el comportamiento simulado del mapeador y del repositorio
        when(userMapper.toUserEntity(user)).thenReturn(userEntity);
        when(userCrudRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUser(userEntity)).thenReturn(new User(1, "John Doe", "john.doe@example.com", "password", "ADMIN", "default.png", now, now));

        // Llamar al método save del UserCrudRepositoryImp
        User savedUser = userCrudRepositoryImp.save(user);

        // Verificar que los métodos del mapeador y del repositorio se llamaron correctamente
        verify(userMapper, times(1)).toUserEntity(user);
        verify(userCrudRepository, times(1)).save(userEntity);
        verify(userMapper, times(1)).toUser(userEntity);

        // Verificar que el ID se ha generado y asignado correctamente
        assertEquals(1, savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("password", savedUser.getPassword());
        assertEquals("ADMIN", savedUser.getRole());
        assertEquals("default.png", savedUser.getImage());
        assertEquals(now, savedUser.getDateCreated());
        assertEquals(now, savedUser.getDateUpdated());
    }
}