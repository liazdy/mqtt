package com.gyjs.mqtt.admin.Utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.auth0.jwt.interfaces.Verification;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    private static final String KEY = "makeGYJSGreat";

    //通过jwt创建token令牌
    public static String createTokenWithUserInfo(Map<String, Object> map) {
        Map<String, Object> head = new HashMap<>();
        head.put("alg", "HS256");
        head.put("typ", "JWT");

        Date date = new Date();//发布日期
        Calendar instance = Calendar.getInstance();//获取当前时间
        instance.set(Calendar.SECOND, 60 * 60 * 24 * 3);//在当前时间的基础上添加7200秒
        Date time = instance.getTime();

        //设置头
        //设置发布日期
        //设置过期时间
        //设置个人信息
        //签名

        return JWT.create()
                .withHeader(head) //设置头
                .withIssuedAt(date) //设置发布日期
                .withExpiresAt(time) //设置过期时间
                .withClaim("userinfo", map) //设置个人信息
                .sign(Algorithm.HMAC256(KEY));
    }

    //校验token
    public static boolean verify(String token) {
        Verification require = JWT.require(Algorithm.HMAC256(KEY));
        try {
            com.auth0.jwt.JWTVerifier build = require.build();
            build.verify(token);
            return true;
        } catch (Exception e) {
            System.out.println("token错误");
            return false;
        }
    }

    //根据token获取自定义的信息
    public static Map<String, Object> getInfo(String token) {
        JWTVerifier build = JWT.require(Algorithm.HMAC256(JWTUtil.KEY)).build();
        Claim claim = build.verify(token).getClaim("userinfo");
        return claim.asMap();
    }
}
