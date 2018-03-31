package com.dream.spring.redis.service.user;

import com.dream.spring.redis.dto.UserDTO;

public interface IUserService {

    boolean addUser(UserDTO user);

    UserDTO getById(int id);
}
