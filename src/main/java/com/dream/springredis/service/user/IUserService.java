package com.dream.springredis.service.user;

import com.dream.springredis.dto.UserDTO;

public interface IUserService {

    boolean addUser(UserDTO user);

    UserDTO getById(int id);
}
