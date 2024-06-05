package com.test.userscontrol.infrastructure.adapter;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import com.test.userscontrol.infrastructure.entity.UserEntity;
import com.test.userscontrol.infrastructure.exception.DuplicateUserException;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import com.test.userscontrol.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Anotación que indica que esta clase es un componente de repositorio gestionado por Spring.
public class UserCrudRepositoryImp implements IUserRepository {

    private final IUserCrudRepository iUserCrudRepository;
    private final UserMapper userMapper;

    public UserCrudRepositoryImp(IUserCrudRepository iUserCrudRepository, UserMapper userMapper) {
        this.iUserCrudRepository = iUserCrudRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        // Busca un usuario existente por correo electrónico
        Optional<UserEntity> existingUserEntityOptional = iUserCrudRepository.findByEmail(user.getEmail());
        if (existingUserEntityOptional.isPresent()) {
            // Si el usuario ya existe, lanza una excepción de usuario duplicado
            throw new DuplicateUserException("A user has been registered with email " + user.getEmail() + ", try another one.");
        } else {
            // Si el usuario no existe, guarda el nuevo usuario y lo mapea a un objeto de dominio User
            return userMapper.toUser(iUserCrudRepository.save(userMapper.toUserEntity(user)));
        }
    }

    @Override
    public Iterable<User> findAll() {
        // Encuentra todos los usuarios y los mapea a una lista de objetos de dominio User
        return userMapper.toUsers(iUserCrudRepository.findAll());
    }

    @Override
    public User findById(Integer id) {
        // Encuentra un usuario por su ID, lanza una excepción si no se encuentra
        return userMapper.toUser(iUserCrudRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public User findByEmail(String email) {
        // Encuentra un usuario por su correo electrónico
        Optional<UserEntity> userEntityOptional = iUserCrudRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            // Si no se encuentra, lanza una excepción de usuario no encontrado
            throw new UserNotFoundException("User with email " + email + " not found");
        } else {
            // Si se encuentra, lo mapea a un objeto de dominio User y lo devuelve
            return userMapper.toUser(userEntityOptional.orElseThrow(() -> new UserNotFoundException("User not found")));
        }
    }

    @Override
    public User updateUser(User user) {
        // Encuentra el usuario por su ID
        Optional<UserEntity> userEntityOptional = iUserCrudRepository.findById(user.getId());
        if (userEntityOptional.isEmpty()) {
            // Si no se encuentra, lanza una excepción de usuario no encontrado
            throw new UserNotFoundException("User with id " + user.getId() + " not found");
        } else {
            // Si se encuentra, guarda el usuario actualizado y lo mapea a un objeto de dominio User
            return userMapper.toUser(iUserCrudRepository.save(userMapper.toUserEntity(user)));
        }
    }

    @Override
    public void deleteById(Integer id) {
        // Encuentra el usuario por su ID
        Optional<UserEntity> userEntityOptional = iUserCrudRepository.findById(id);
        if (userEntityOptional.isEmpty()) {
            // Si no se encuentra, lanza una excepción de usuario no encontrado
            throw new UserNotFoundException("User with id " + id + " not found");
        } else {
            // Si se encuentra, elimina el usuario por su ID
            iUserCrudRepository.deleteById(id);
        }
    }
}