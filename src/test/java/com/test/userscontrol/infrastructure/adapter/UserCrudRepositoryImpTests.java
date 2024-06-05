package com.test.userscontrol.infrastructure.adapter;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.entity.UserEntity;
import com.test.userscontrol.infrastructure.exception.DuplicateUserException;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import com.test.userscontrol.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserCrudRepositoryImpTests {

    @Mock
    private IUserCrudRepository userCrudRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserCrudRepositoryImp userCrudRepositoryImp;

    private User userMock;
    private UserEntity userEntityMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userMock = new User();
        userMock.setId(1);
        userMock.setEmail("test@mail.com");
        userEntityMock = new UserEntity();
        userEntityMock.setId(1);
        userEntityMock.setEmail("test@mail.com");
    }

    @Test
    public void testSaveUser_Success() {
        // Simula que findByEmail devuelve un Optional vacío, indicando que no se encontró ningún usuario con ese email
        when(userCrudRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.empty());
        // Simula la conversión de User a UserEntity utilizando el UserMapper
        when(userMapper.toUserEntity(userMock)).thenReturn(userEntityMock);
        // Simula que el método save del repositorio devuelve la entidad de usuario guardada
        when(userCrudRepository.save(userEntityMock)).thenReturn(userEntityMock);
        // Simula la conversión de UserEntity a User utilizando el UserMapper
        when(userMapper.toUser(userEntityMock)).thenReturn(userMock);
        // Llama al método save de UserCrudRepositoryImp con el usuario mock y guarda el resultado
        User savedUser = userCrudRepositoryImp.save(userMock);
        // Verifica que el usuario guardado no sea nulo
        assertNotNull(savedUser);
        // Verifica que el usuario guardado sea igual al usuario mock original
        assertEquals(userMock, savedUser);
        // Verifica que el método findByEmail del repositorio se haya llamado una vez con el email del usuario mock
        verify(userCrudRepository, times(1)).findByEmail(userMock.getEmail());
        // Verifica que el método save del repositorio se haya llamado una vez con la entidad de usuario mock
        verify(userCrudRepository, times(1)).save(userEntityMock);
    }

    @Test
    public void testSaveUser_DuplicateUserException() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por email
        when(userCrudRepository.findByEmail(userMock.getEmail())).thenReturn(Optional.of(userEntityMock));
        // Verificación de que al intentar guardar un usuario duplicado, se lance la excepción adecuada
        assertThrows(DuplicateUserException.class, () -> userCrudRepositoryImp.save(userMock));
        // Verificación de que el método findByEmail del repositorio se haya llamado exactamente una vez
        verify(userCrudRepository, times(1)).findByEmail(userMock.getEmail());
        // Verificación de que el método save del repositorio nunca se haya llamado con un UserEntity
        verify(userCrudRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testFindAllUsers() {
        // Configuración del comportamiento esperado del repositorio al buscar todos los usuarios
        when(userCrudRepository.findAll()).thenReturn(Collections.singletonList(userEntityMock));
        // Configuración del comportamiento esperado del mapeador al convertir la lista de entidades de usuario a usuarios
        when(userMapper.toUsers(Collections.singletonList(userEntityMock))).thenReturn(Collections.singletonList(userMock));
        // Llamada al método findAll del repositorio para obtener todos los usuarios
        Iterable<User> users = userCrudRepositoryImp.findAll();
        // Verificación de que la lista de usuarios no sea nula
        assertNotNull(users);
        // Verificación de que la lista de usuarios no esté vacía
        assertTrue(users.iterator().hasNext());
        // Verificación de que el primer usuario de la lista sea igual al usuario simulado
        assertEquals(userMock, users.iterator().next());
        // Verificación de que el método findAll del repositorio se haya llamado exactamente una vez
        verify(userCrudRepository, times(1)).findAll();
    }


    @Test
    public void testFindUserById_Success() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su ID
        when(userCrudRepository.findById(1)).thenReturn(Optional.of(userEntityMock));
        // Configuración del comportamiento esperado del mapeador al convertir la entidad de usuario a un usuario
        when(userMapper.toUser(userEntityMock)).thenReturn(userMock);
        // Llamada al método findById del repositorio para encontrar un usuario por su ID
        User foundUser = userCrudRepositoryImp.findById(1);
        // Verificación de que el usuario encontrado no sea nulo
        assertNotNull(foundUser);
        // Verificación de que el usuario encontrado sea igual al usuario simulado
        assertEquals(userMock, foundUser);
        // Verificación de que el método findById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).findById(1);
    }


    @Test
    public void testFindUserById_UserNotFoundException() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su ID
        when(userCrudRepository.findById(1)).thenReturn(Optional.empty());
        // Verificación de que al intentar encontrar un usuario por su ID y no se encuentra, se lance una excepción
        assertThrows(RuntimeException.class, () -> userCrudRepositoryImp.findById(1));
        // Verificación de que el método findById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).findById(1);
    }


    @Test
    public void testFindUserByEmail_Success() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su correo electrónico
        when(userCrudRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(userEntityMock));
        // Configuración del comportamiento esperado del mapeador al convertir la entidad de usuario a un usuario
        when(userMapper.toUser(userEntityMock)).thenReturn(userMock);
        // Llamada al método findByEmail del repositorio para encontrar un usuario por su correo electrónico
        User foundUser = userCrudRepositoryImp.findByEmail("test@mail.com");
        // Verificación de que el usuario encontrado no sea nulo
        assertNotNull(foundUser);
        // Verificación de que el usuario encontrado sea igual al usuario simulado
        assertEquals(userMock, foundUser);
        // Verificación de que el método findByEmail del repositorio se haya llamado exactamente una vez con el correo electrónico proporcionado
        verify(userCrudRepository, times(1)).findByEmail("test@mail.com");
    }


    @Test
    public void testFindUserByEmail_UserNotFoundException() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su correo electrónico
        when(userCrudRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        // Verificación de que al intentar encontrar un usuario por su correo electrónico y no se encuentra, se lance una excepción
        assertThrows(UserNotFoundException.class, () -> userCrudRepositoryImp.findByEmail("test@mail.com"));
        // Verificación de que el método findByEmail del repositorio se haya llamado exactamente una vez con el correo electrónico proporcionado
        verify(userCrudRepository, times(1)).findByEmail("test@mail.com");
    }


    @Test
    public void testUpdateUser_Success() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su ID
        when(userCrudRepository.findById(1)).thenReturn(Optional.of(userEntityMock));
        // Configuración del comportamiento esperado del mapeador al convertir el usuario a una entidad de usuario
        when(userMapper.toUserEntity(userMock)).thenReturn(userEntityMock);
        // Configuración del comportamiento esperado del repositorio al guardar la entidad de usuario actualizada
        when(userCrudRepository.save(userEntityMock)).thenReturn(userEntityMock);
        // Configuración del comportamiento esperado del mapeador al convertir la entidad de usuario a un usuario
        when(userMapper.toUser(userEntityMock)).thenReturn(userMock);
        // Llamada al método updateUser del repositorio para actualizar un usuario
        User updatedUser = userCrudRepositoryImp.updateUser(userMock);
        // Verificación de que el usuario actualizado no sea nulo
        assertNotNull(updatedUser);
        // Verificación de que el usuario actualizado sea igual al usuario simulado
        assertEquals(userMock, updatedUser);
        // Verificación de que el método findById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).findById(1);
        // Verificación de que el método save del repositorio se haya llamado exactamente una vez con la entidad de usuario actualizada
        verify(userCrudRepository, times(1)).save(userEntityMock);
    }


    @Test
    public void testUpdateUser_UserNotFoundException() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su ID (en este caso, devolverá un Optional vacío)
        when(userCrudRepository.findById(1)).thenReturn(Optional.empty());
        // Verificación de que al intentar actualizar un usuario que no existe, se lance una excepción UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userCrudRepositoryImp.updateUser(userMock));
        // Verificación de que el método findById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).findById(1);
        // Verificación de que el método save del repositorio nunca se haya llamado con cualquier objeto de tipo UserEntity
        verify(userCrudRepository, never()).save(any(UserEntity.class));
    }


    @Test
    public void testDeleteUserById_Success() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su ID
        when(userCrudRepository.findById(1)).thenReturn(Optional.of(userEntityMock));
        // Llamada al método deleteById del repositorio para eliminar un usuario por su ID
        userCrudRepositoryImp.deleteById(1);
        // Verificación de que el método findById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).findById(1);
        // Verificación de que el método deleteById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).deleteById(1);
    }


    @Test
    public void testDeleteUserById_UserNotFoundException() {
        // Configuración del comportamiento esperado del repositorio al buscar un usuario por su ID (en este caso, devolverá un Optional vacío)
        when(userCrudRepository.findById(1)).thenReturn(Optional.empty());
        // Verificación de que al intentar eliminar un usuario que no existe, se lance una excepción UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> userCrudRepositoryImp.deleteById(1));
        // Verificación de que el método findById del repositorio se haya llamado exactamente una vez con el ID 1
        verify(userCrudRepository, times(1)).findById(1);
        // Verificación de que el método deleteById del repositorio nunca se haya llamado con el ID 1
        verify(userCrudRepository, never()).deleteById(1);
    }

}
