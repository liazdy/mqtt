package com.gyjs.mqtt.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyjs.mqtt.admin.Utils.JWTUtil;
import com.gyjs.mqtt.admin.bean.Result;
import com.gyjs.mqtt.admin.bean.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @PostMapping("/login")
    public Result login(@RequestBody User user) {

        //User user = new User();
        //user.setUsername(username);
        if (user == null) {
            return Result.error().msg("请填写用户名密码");
        }

        if ("admin".equalsIgnoreCase(user.getUsername()) && "shi666ge".equals(user.getPassword())) {
            user.setId(1);
            user.setRole(1);
            user.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");


            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> userMap = mapper.convertValue(user, Map.class);
            String token = JWTUtil.createTokenWithUserInfo(userMap);

            return Result.success().data("token", token);
        }
        return Result.error().msg("用户名密码错误");

    }

    @PostMapping("/logout")
    public Result logout(String token) {


        return Result.success();

    }


    @GetMapping("/info")
    public Result info(String token) {


        Map<String, Object> userInfo = JWTUtil.getInfo(token);
        return Result.success().data("name", userInfo.get("username")).data("avatar", userInfo.get("avatar"));

    }


}
