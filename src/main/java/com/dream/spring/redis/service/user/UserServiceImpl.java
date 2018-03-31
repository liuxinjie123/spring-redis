package com.dream.spring.redis.service.user;

import com.dream.spring.redis.dto.UserDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserDTO getById(int id) {
        UserDTO user = new UserDTO();
        user.setName("liuxinjie");
        System.out.println(" ----------- ");
        return user;
    }

    @Override
    @CachePut(value = "user", key = "#user.id")
    public boolean addUser(UserDTO user) {
        return true;
    }

    @CacheEvict(value = "user", key = "#user.id") // 移除指定key的数据
    public UserDTO delete(UserDTO user) {
        return user;
    }

    @CacheEvict(value = "user", allEntries = true) // 移除所有数据
    public void deleteAll() {
    }

}
