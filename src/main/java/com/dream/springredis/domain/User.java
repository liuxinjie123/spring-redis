package com.dream.springredis.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class User implements Serializable {
    private String id;
    private String username;
    private String password;
    private String name;
    private int age;
    private String phone;
    private String email;
    private String desc;

    public User(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
