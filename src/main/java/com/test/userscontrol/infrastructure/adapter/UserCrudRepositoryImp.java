package com.test.userscontrol.infrastructure.adapter;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;
import com.test.userscontrol.infrastructure.entity.UserEntity;
import com.test.userscontrol.infrastructure.exception.UserNotFoundException;
import com.test.userscontrol.infrastructure.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Anotación que indica que esta clase es un componente de repositorio gestionado por Spring.
public class UserCrudRepositoryImp implements IUserRepository {

    private final IUserCrudRepository iUserCrudRepository; // Repositorio CRUD para la entidad de usuario.
    private final UserMapper userMapper; // Mapper para mapear entre entidades de usuario y modelos de dominio.

    // Constructor que recibe las dependencias necesarias.
    public UserCrudRepositoryImp(IUserCrudRepository iUserCrudRepository, UserMapper userMapper) {
        this.iUserCrudRepository = iUserCrudRepository; // Inicialización del repositorio CRUD.
        this.userMapper = userMapper; // Inicialización del mapper.
    }

    // Método para guardar un usuario en la base de datos.
    @Override
    public User save(User user) {
        return userMapper.toUser(iUserCrudRepository.save(userMapper.toUserEntity(user)));
    }

    // Método para recuperar todos los usuarios de la base de datos.
    @Override
    public Iterable<User> findAll() {
        return userMapper.toUsers(iUserCrudRepository.findAll());
    }

    // Método para buscar un usuario por su ID en la base de datos.
    @Override
    public User findById(Integer id) {
        // Se intenta encontrar el usuario por su ID, si no se encuentra, se lanza una excepción.
        return userMapper.toUser(iUserCrudRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found")));
    }

    // Método para buscar un usuario por su email en la base de datos.
    @Override
    public User findByEmail(String email) {
        // Se busca un usuario por su email. Si no se encuentra, se lanza una excepción.
        Optional<UserEntity> userEntityOptional = iUserCrudRepository.findByEmail(email);
        if (userEntityOptional.isEmpty()) {
            throw new UserNotFoundException("User with email " + email + " not found");
        } else {
            // Retorna un objeto User mapeado a partir del Optional userEntityOptional; si está vacío, lanza una excepción UserNotFoundException
            return userMapper.toUser(userEntityOptional.orElseThrow(() -> new UserNotFoundException("User not found")));
        }
    }

    // Método para eliminar un usuario por su ID de la base de datos.
    @Override
    public void deleteById(Integer id) {
        iUserCrudRepository.deleteById(id); // Se llama al método de eliminación por ID del repositorio CRUD.
    }
}