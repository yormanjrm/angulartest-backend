package com.test.userscontrol.infrastructure.rest;

import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.exception.InvalidPasswordException;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import com.test.userscontrol.infrastructure.jwt.JWTClient;
import com.test.userscontrol.infrastructure.jwt.JWTGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@CrossOrigin(origins = "http://localhost:4200/")
public class LoginController {
    private final UserService userService;
    private final JWTGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public LoginController(UserService userService, JWTGenerator jwtGenerator, AuthenticationManager authenticationManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.authenticationManager = authenticationManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @PostMapping()
    public ResponseEntity<JWTClient> login(@RequestParam String email, @RequestParam String password) {
        try {
            User user = userService.findByEmail(email);
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {

                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtGenerator.getToken(email);
                JWTClient jwtClient = new JWTClient(user.getId(), user.getRole(), token);
                return new ResponseEntity<>(jwtClient, HttpStatus.OK);

            } else {
                throw new InvalidPasswordException("Invalid password for email " + email);
            }
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

}