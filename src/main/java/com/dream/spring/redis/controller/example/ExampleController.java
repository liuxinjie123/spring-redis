package com.dream.spring.redis.controller.example;

import com.dream.spring.redis.dto.Constants;
import com.dream.spring.redis.dto.ResultDTO;
import com.dream.spring.redis.dto.UserDTO;
import com.dream.spring.redis.service.redis.IRedisService;
import com.dream.spring.redis.service.user.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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