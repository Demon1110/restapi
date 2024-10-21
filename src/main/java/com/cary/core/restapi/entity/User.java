package com.cary.core.restapi.entity;

import lombok.Data;

@Data
public class User {
    private String key;
    private String name;
    private String email;
    private String phone;
}
