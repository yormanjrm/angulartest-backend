package com.test.userscontrol.infrastructure.config;

import com.test.userscontrol.application.UploadFileService;
import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BeanConfigurationTests {
    @MockBean
    private IUserRepository userRepository;
    @Test
    void userServiceBeanShouldNotBeNull() {
        // Creamos una instancia de BeanConfiguration
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        // Obtenemos el bean UserService del BeanConfiguration
        UserService userService = beanConfiguration.userService(userRepository, new UploadFileService());
        // Verificamos que el bean UserService no sea nulo
        assertNotNull(userService);
    }

    @Test
    void uploadFileServiceBeanShouldNotBeNull() {
        // Creamos una instancia de BeanConfiguration
        BeanConfiguration beanConfiguration = new BeanConfiguration();
        // Obtenemos el bean UploadFileService del BeanConfiguration
        UploadFileService uploadFileService = beanConfiguration.uploadFile();
        // Verificamos que el bean UploadFileService no sea nulo
        assertNotNull(uploadFileService);
    }
}