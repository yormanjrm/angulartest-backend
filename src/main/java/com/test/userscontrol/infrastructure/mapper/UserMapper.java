package com.test.userscontrol.infrastructure.mapper;

import com.test.userscontrol.domain.model.User;
import com.test.userscontrol.infrastructure.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "email", target = "email"),
            @Mapping(source = "password", target = "password"),
            @Mapping(source = "role", target = "role"),
            @Mapping(source = "image", target = "image"),
            @Mapping(source = "date_created", target = "date_created")
    })
    User toUser(UserEntity userEntity);

    Iterable<User> toUsers(Iterable<UserEntity> userEntities);

    @InheritInverseConfiguration
    UserEntity toUserEntity(User user);

}