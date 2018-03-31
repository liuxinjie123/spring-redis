package com.dream.spring.redis.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDAO implements Serializable {
    private String id;
    private String username;
    private String password;
    private String name;
    private int age;
    private String phone;
    private String email;
    private String desc;
}
