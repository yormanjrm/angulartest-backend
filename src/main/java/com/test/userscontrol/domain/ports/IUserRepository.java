package com.test.userscontrol.domain.ports;

import com.test.userscontrol.domain.model.User;

public interface IUserRepository {
    User save(User user);
}