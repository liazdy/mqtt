package com.gyjs.mqtt.admin.bean;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String username;
    private String password;
    private int role;
    private String avatar;


}
