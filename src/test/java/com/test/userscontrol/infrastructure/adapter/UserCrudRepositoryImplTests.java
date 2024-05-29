package com.test.userscontrol.infrastructure.adapter;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.entity.UserEntity;
import com.test.userscontrol.infrastructure.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCrudRepositoryImpTests {

    @Mock
    private IUserCrudRepository userCrudRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserCrudRepositoryImp userCrudRepositoryImp;

    private LocalDateTime now;
    private User newUser;
    private User user1;
    private User user2;
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private List<UserEntity> userEntities;
    private List<User> users;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        now = LocalDateTime.now();

        newUser = new User(null, "John Doe", "john.doe@example.com", "password123", "ADMIN", "default.png", null);

        user1 = new User(1, "John Doe", "john.doe@example.com", "password123", "ADMIN", "default.png", now);
        user2 = new User(2, "Jane Doe", "jane.doe@example.com", "password123", "USER", "default.png", now);

        userEntity1 = new UserEntity();
        userEntity1.setName("John Doe");
        userEntity1.setEmail("john.doe@example.com");
        userEntity1.setPassword("password123");
        userEntity1.setRole("ADMIN");
        userEntity1.setImage("default.png");

        userEntity2 = new UserEntity();
        userEntity2.setName("Jane Doe");
        userEntity2.setEmail("jane.doe@example.com");
        userEntity2.setPassword("password123");
        userEntity2.setRole("USER");
        userEntity2.setImage("default.png");

        userEntities = Arrays.asList(userEntity1, userEntity2);
        users = Arrays.asList(user1, user2);
    }

    @Test
    @DirtiesContext
    void shouldSaveANewUser() {
        // Configura el comportamiento esperado del mock 'userMapper' cuando se llama al método 'toUserEntity()'
        // Cuando se llama al método 'toUserEntity()' con 'newUser' como argumento, devuelve 'userEntity1'
        when(userMapper.toUserEntity(newUser)).thenReturn(userEntity1);
        // Configura el comportamiento esperado del mock 'userCrudRepository' cuando se llama al método 'save()'
        // Cuando se llama al método 'save()' con 'userEntity1' como argumento, devuelve 'userEntity1'
        when(userCrudRepository.save(userEntity1)).thenReturn(userEntity1);
        // Configura el comportamiento esperado del mock 'userMapper' cuando se llama al método 'toUser()'
        // Cuando se llama al método 'toUser()' con 'userEntity1' como argumento, devuelve 'user1'
        when(userMapper.toUser(userEntity1)).thenReturn(user1);
        // Llama al método que se está probando, en este caso, 'save()' de la implementación de 'UserCrudRepository'
        User savedUser = userCrudRepositoryImp.save(newUser);
        // Verifica que el objeto de usuario devuelto por el método 'save()' tenga los valores esperados
        assertEquals(user1, savedUser);
        // Verifica que los métodos del mock 'userMapper' y del mock 'userCrudRepository' se hayan llamado correctamente
        verify(userMapper, times(1)).toUserEntity(newUser);
        verify(userCrudRepository, times(1)).save(userEntity1);
        verify(userMapper, times(1)).toUser(userEntity1);
    }

    @Test
    @DirtiesContext
    public void shouldReturnAllUsers() {
        // Configura el comportamiento esperado del mock 'userCrudRepository' cuando se llama al método 'findAll()'
        // Cuando se llama al método 'findAll()', devuelve 'userEntities'
        when(userCrudRepository.findAll()).thenReturn(userEntities);
        // Configura el comportamiento esperado del mock 'userMapper' cuando se llama al método 'toUsers()'
        // Cuando se llama al método 'toUsers()' con 'userEntities' como argumento, devuelve 'users'
        when(userMapper.toUsers(userEntities)).thenReturn(users);
        // Llama al método que se está probando, en este caso, 'findAll()' de la implementación de 'UserCrudRepository'
        Iterable<User> result = userCrudRepositoryImp.findAll();
        // Comprueba que el resultado devuelto por el método 'findAll()' es igual al valor esperado 'users'
        assertEquals(users, result);
        // Verifica que el método 'findAll()' del mock 'userCrudRepository' se haya llamado exactamente una vez
        verify(userCrudRepository, times(1)).findAll();
        // Verifica que el método 'toUsers()' del mock 'userMapper' se haya llamado exactamente una vez con 'userEntities' como argumento
        verify(userMapper, times(1)).toUsers(userEntities);
    }

    @Test
    void shouldFindUserById() {
        // Configuración del comportamiento esperado del mock 'userCrudRepository'
        when(userCrudRepository.findById(1)).thenReturn(Optional.of(userEntity1));
        // Configuración del comportamiento esperado del mock 'userMapper'
        when(userMapper.toUser(userEntity1)).thenReturn(user1);
        // Llamada al método a probar
        User foundUser = userCrudRepositoryImp.findById(1);
        // Verificación de que el usuario encontrado es el esperado
        assertEquals(user1, foundUser);
        // Verificación de que los métodos del mock fueron llamados correctamente
        verify(userCrudRepository, times(1)).findById(1);
        verify(userMapper, times(1)).toUser(userEntity1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Configuración del comportamiento esperado del mock 'userCrudRepository' para el caso de usuario no encontrado
        when(userCrudRepository.findById(4)).thenReturn(Optional.empty());
        // Verificación de que se lanza una RuntimeException cuando el usuario no se encuentra
        assertThrows(RuntimeException.class, () -> {
            userCrudRepositoryImp.findById(4);
        });
        // Verificación de que el método del mock fue llamado correctamente
        verify(userCrudRepository, times(1)).findById(4);
        verify(userMapper, never()).toUser(any(UserEntity.class));
    }

    @Test
    void shouldDeleteUserById() {
        // Configuración del comportamiento esperado del mock iUserCrudRepository
        doNothing().when(userCrudRepository).deleteById(2); // Configuración del comportamiento esperado del método deleteById
        // Llamada al método a probar
        userCrudRepositoryImp.deleteById(2); // Eliminamos el usuario con ID 2
        // Verificación de que se llamó al método deleteById del mock iUserCrudRepository con el ID proporcionado
        verify(userCrudRepository, times(1)).deleteById(2);
        // Verificación de que el usuario ya no existe en la base de datos
        Optional<UserEntity> deletedUser = userCrudRepository.findById(2);
        assertFalse(deletedUser.isPresent());
    }


}