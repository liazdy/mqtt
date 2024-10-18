package com.gyjs.mqtt.admin.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result implements Serializable {
    private Integer code;//响应码
    private String message;//提示信息
    private Map<String, Object> data;//封装数据

    public static Result success() {
        Result result = new Result();
        result.setCode(200);
        result.setMessage("成功");
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setCode(400);
        return result;
    }

    public Result data(String key, Object data) {
        Map<String, Object> data1 = this.getData();
        if (data1 == null) {
            data1 = new HashMap<>();
        }
        data1.put(key, data);
        this.setData(data1);
        return this;

    }

    public Result msg(String msg) {
        this.setMessage(msg);
        return this;

    }


}
