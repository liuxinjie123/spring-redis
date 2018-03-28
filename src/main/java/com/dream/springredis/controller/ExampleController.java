package com.dream.springredis.controller;

import com.dream.springredis.dao.UserDAO;
import com.dream.springredis.dto.Constants;
import com.dream.springredis.dto.ResultDTO;
import com.dream.springredis.dto.UserDTO;
import com.dream.springredis.service.redis.IRedisService;
import com.dream.springredis.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExampleController {
    private Logger logger = LoggerFactory.getLogger(ExampleController.class);
    @Autowired
    private IUserService userService;
    @Autowired
    private IRedisService redisService;

    @GetMapping("/user/{id}")
    public ResultDTO users(@PathVariable("id") int id) {
        UserDTO user = userService.getById(id);
        return new ResultDTO(Constants.RETURN_SUCCESS, Constants.MSG_SUCCESS, user);
    }

    @PostMapping("/redis/set")
    public ResultDTO redisSet(@RequestParam("key") String key,
                              @RequestParam("value") String value) {
        boolean isOk = redisService.set(key, value);
        if (isOk) {
            return new ResultDTO(Constants.RETURN_SUCCESS, Constants.MSG_SUCCESS);
        } else {
            return new ResultDTO(Constants.RETURN_FAILURE, Constants.MSG_FAILURE);
        }
    }

    @GetMapping("/redis/get/{key}")
    public ResultDTO redisGet(@PathVariable("key") String key) {
        String name = redisService.get(key);
        return new ResultDTO(Constants.RETURN_SUCCESS, Constants.MSG_SUCCESS, name);
    }

}