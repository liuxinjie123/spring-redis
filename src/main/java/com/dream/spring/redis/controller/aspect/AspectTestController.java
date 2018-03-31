package com.dream.spring.redis.controller.aspect;

import com.dream.spring.redis.aop.Action;
import com.dream.spring.redis.dto.Constants;
import com.dream.spring.redis.dto.ResultDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aspect")
public class AspectTestController {

    @GetMapping("/test/001")
    public ResultDTO test001() throws InterruptedException {
        Thread.sleep(1000);
        return new ResultDTO(Constants.RETURN_SUCCESS, Constants.MSG_SUCCESS, "test001");
    }

    @GetMapping("/test/002")
    @Action(name = "test002")
    public ResultDTO test002() throws InterruptedException {
        Thread.sleep(1000);
        return new ResultDTO(Constants.RETURN_SUCCESS, Constants.MSG_SUCCESS, "test001");
    }
}
