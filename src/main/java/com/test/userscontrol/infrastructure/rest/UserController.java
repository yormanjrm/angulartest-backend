package com.test.userscontrol.infrastructure.rest;

import com.test.userscontrol.application.UserService;
import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.dto.ApiResponseDTO;
import com.test.userscontrol.infrastructure.exception.DuplicateUserException;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200/")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<Object>> save(
            @RequestParam("id") Integer id,
            @RequestParam("name") String  name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("image") String image,
            @RequestParam(value = "imageFile", required = false) MultipartFile multipartFile) {
        try {
            User user = new User(id, name, email, password, role, image, null);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User savedUser = userService.save(user, multipartFile);
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(201, false, "Created user", savedUser);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DuplicateUserException e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(409, true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (Exception e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(500, true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDTO<Object>> findAll() {
        try {
            String message = "There are no registered users";
            Integer code = 204;
            Iterable<User> list = userService.findAll();
            if (IterableUtils.size(list) > 0) {
                message = "Users found";
                code = 200;
            }
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(code, false, message, list);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(500, true, "Internal server errror, try again.", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/byId")
    public ResponseEntity<ApiResponseDTO<Object>> findById(@RequestParam Integer id) {
        try {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(200, false, "User found", userService.findById(id));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(500, true, "Internal server errror, try again.", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<Object>> update(
            @RequestParam("id") Integer id,
            @RequestParam("name") String  name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") String role,
            @RequestParam("image") String image,
            @RequestParam(value = "imageFile", required = false) MultipartFile multipartFile) {
        try {
            User user = new User(id, name, email, password, role, image, null);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User updatedUser = userService.updateUser(user, multipartFile);
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(200, false, "Updated user", updatedUser);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(404, true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(500, true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponseDTO<Object>> deleteById(@RequestParam Integer id) {
        try {
            userService.deleteById(id);
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(200, false, "Deleted user", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(404, true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponseDTO<Object> response = new ApiResponseDTO<>(500, true, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}