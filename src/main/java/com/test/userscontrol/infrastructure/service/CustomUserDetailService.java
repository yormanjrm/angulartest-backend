package com.test.userscontrol.infrastructure.service;

import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Marca esta clase como un servicio gestionado por Spring.
public class CustomUserDetailService implements UserDetailsService {
    private UserService userService; // Define una instancia de UserService para manejar la lógica de negocio relacionada con los usuarios.

    // Constructor que inyecta una instancia de UserService.
    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    // Sobrescribe el método loadUserByUsername de la interfaz UserDetailsService.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Llama al método findByEmail del servicio de usuario para obtener un usuario por su correo electrónico.
        User user = userService.findByEmail(username);
        // Construye y retorna un objeto UserDetails basado en el usuario encontrado.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Establece el nombre de usuario (correo electrónico).
                .password(user.getPassword()) // Establece la contraseña.
                .roles(user.getRole()) // Establece los roles del usuario.
                .build(); // Construye el objeto UserDetails.
    }
}
