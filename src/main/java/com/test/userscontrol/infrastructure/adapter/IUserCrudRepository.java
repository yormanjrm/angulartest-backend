package com.test.userscontrol.infrastructure.adapter;

import com.test.userscontrol.infrastructure.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface IUserCrudRepository extends CrudRepository<UserEntity, Integer> {
}