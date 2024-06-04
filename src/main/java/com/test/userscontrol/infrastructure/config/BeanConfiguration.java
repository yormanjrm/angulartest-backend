package com.test.userscontrol.infrastructure.config;

import com.test.userscontrol.application.*;
import com.test.userscontrol.domain.ports.IUserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public UserService userService(IUserRepository iUserRepository, UploadFileService uploadFileService) {
        return new UserService(iUserRepository, uploadFileService);
    }

    @Bean
    public UploadFileService uploadFile(){
        return new UploadFileService();
    }
}