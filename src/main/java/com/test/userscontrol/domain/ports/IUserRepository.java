package com.test.userscontrol.domain.ports;

import com.test.userscontrol.domain.model.User;

public interface IUserRepository {
    User save(User user);
    Iterable<User> findAll();
    User findById(Integer id);
    User findByEmail(String email);
    void deleteById(Integer id);
}