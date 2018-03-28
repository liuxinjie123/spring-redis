package com.dream.springredis.service.user;

import com.dream.springredis.dao.UserDAO;

import java.util.List;

public interface IUserService {

    public UserDAO getById();

    List<UserDAO> getAll();
}
