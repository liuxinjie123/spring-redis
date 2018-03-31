package com.dream.spring.redis.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserDTO implements Serializable {
    private String id;
    private String username;
    private String password;
    private String name;
    private int age;
    private String phone;
    private String email;
    private String desc;

    public UserDTO(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
