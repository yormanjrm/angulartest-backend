package com.test.userscontrol.application;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.domain.ports.IUserRepository;

public class UserService {
    private final IUserRepository iUserRepository;

    public UserService(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    public User save(User user) {
        return iUserRepository.save(user);
    }

    public Iterable<User> findAll() {
        return iUserRepository.findAll();
    }

    public User findById(Integer id) {
        return iUserRepository.findById(id);
    }

    public void deleteById(Integer id){
        iUserRepository.deleteById(id);
    }

    public User updateUser(User user){
        return iUserRepository.updateUser(user);
    }

    public User findByEmail(String email){
        return iUserRepository.findByEmail(email);
    }
}