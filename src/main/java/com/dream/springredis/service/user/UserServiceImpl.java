package com.dream.springredis.service.user;

import com.dream.springredis.dao.UserDAO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Cacheable(value="common",key="'user_id_'+#id")
    @Override
    public UserDAO getById() {
        return null;
    }
}
