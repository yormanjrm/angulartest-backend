package com.test.userscontrol.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class UserServiceTests {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UploadFileService uploadFileService;

    @Mock
    private User newUserMock;

    @Mock
    private User savedUserMock;

    @Mock
    private User userToUpdateMock;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        newUserMock = new User(0, "Name test", "email@mail.com", "password", "RECEP", "default.png", null);
        savedUserMock = new User(1, "Name test", "email@mail.com", "$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe", "RECEP", "http://localhost:8080/images/test.jpg", null);
        userToUpdateMock = new User(1, "Name test2", "email2@mail.com", "$2a$10$/iOt2Bx.vd4PHhWWJttE3.mNlX/jriVf4dASqOwbijXR.0goXmyRe", "RECEP", "http://localhost:8080/images/test.jpg", null);
    }


    @Test
    public void testSaveUser() throws IOException {
        // Creamos un mock de MultipartFile
        MultipartFile multipartFile = mock(MultipartFile.class);
        // Configuramos el comportamiento del mock de MultipartFile para que devuelva una URL simulada
        when(uploadFileService.upload(multipartFile)).thenReturn("http://localhost:8080/images/test.jpg");
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva el mismo usuario que se le pasa como argumento
        when(userRepository.save(newUserMock)).thenReturn(newUserMock);
        // Llamamos al método save del servicio UserService para guardar un usuario
        User savedUser = userService.save(newUserMock, multipartFile);
        // Verificamos que el usuario guardado no sea nulo
        assertNotNull(savedUser);
        // Verificamos que el usuario guardado sea el mismo que el usuario original
        assertEquals(newUserMock, savedUser);
        // Verificamos que la URL de la imagen sea la esperada
        assertEquals("http://localhost:8080/images/test.jpg", savedUser.getImage());
        // Verificamos que el método save del repositorio se llame una vez con el usuario correcto
        verify(userRepository, times(1)).save(newUserMock);
        // Verificamos que el método upload del servicio de carga de archivos se llame una vez con el archivo correcto
        verify(uploadFileService, times(1)).upload(multipartFile);
    }

    @Test
    public void testFindAllUsers() {
        // Creamos una lista de usuarios simulada con un usuario guardado previamente (savedUserMock)
        List<User> userList = Arrays.asList(savedUserMock);
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva la lista simulada
        when(userRepository.findAll()).thenReturn(userList);
        // Llamamos al método findAll del servicio UserService para buscar todos los usuarios
        Iterable<User> foundUsers = userService.findAll();
        // Verificamos que los usuarios encontrados sean iguales a la lista simulada
        assertEquals(userList, foundUsers);
        // Verificamos que el método findAll del repositorio se llame una vez
        verify(userRepository, times(1)).findAll();
    }


    @Test
    public void testFindUserById() {
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva el usuario simulado cuando se llame con el ID 1
        when(userRepository.findById(1)).thenReturn(savedUserMock);
        // Llamamos al método findById del servicio UserService para buscar un usuario por su ID
        User foundUser = userService.findById(1);
        // Verificamos que el usuario encontrado sea igual al usuario simulado
        assertEquals(savedUserMock, foundUser);
        // Verificamos que el método findById del repositorio se llame una vez con el ID 1
        verify(userRepository, times(1)).findById(1);
    }


    @Test
    public void testDeleteUserById() {
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva el usuario simulado cuando se llame con el ID 1
        when(userRepository.findById(1)).thenReturn(savedUserMock);
        // Llamamos al método deleteById del servicio UserService para eliminar un usuario por su ID
        userService.deleteById(1);
        // Verificamos que el método delete del servicio de carga de archivos (uploadFileService) se llame una vez con cualquier nombre de archivo (indicando que se eliminó la imagen)
        verify(uploadFileService, times(1)).delete(anyString());
        // Verificamos que el método deleteById del repositorio se llame una vez con el ID 1
        verify(userRepository, times(1)).deleteById(1);
    }


    @Test
    public void testUpdateUserWithoutImage() throws IOException {
        // Creamos un mock de MultipartFile para simular la ausencia de imagen
        MultipartFile multipartFile = null;
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva el usuario simulado cuando se llame con el ID 1
        when(userRepository.findById(1)).thenReturn(savedUserMock);
        // Llamamos al método updateUser del servicio UserService para actualizar un usuario sin cambiar la imagen
        User updatedUser = userService.updateUser(userToUpdateMock, multipartFile);
        // Verificamos que el método updateUser del repositorio se llame una vez con el usuario actualizado
        verify(userRepository, times(1)).updateUser(userToUpdateMock);
    }


    @Test
    public void testUpdateUserWithImage() throws IOException {
        // Creamos un mock de MultipartFile para simular la presencia de una nueva imagen
        MultipartFile multipartFile = mock(MultipartFile.class);
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva el usuario simulado cuando se llame con el ID 1
        when(userRepository.findById(1)).thenReturn(savedUserMock);
        // Llamamos al método updateUser del servicio UserService para actualizar un usuario con una nueva imagen
        User updatedUser = userService.updateUser(userToUpdateMock, multipartFile);
        // Verificamos que el método updateUser del repositorio se llame una vez con el usuario actualizado
        verify(userRepository, times(1)).updateUser(userToUpdateMock);
    }


    @Test
    public void testFindByEmail() {
        // Configuramos el comportamiento del repositorio de usuarios (userRepository) para que devuelva el usuario simulado cuando se llame con el correo electrónico "email@mail.com"
        when(userRepository.findByEmail("email@mail.com")).thenReturn(savedUserMock);
        // Llamamos al método findByEmail del servicio UserService para buscar un usuario por su correo electrónico
        User foundUser = userService.findByEmail("email@mail.com");
        // Verificamos que el usuario encontrado sea igual al usuario simulado
        assertEquals(savedUserMock, foundUser);
        // Verificamos que el método findByEmail del repositorio se llame una vez con el correo electrónico "email@mail.com"
        verify(userRepository, times(1)).findByEmail("email@mail.com");
    }

}