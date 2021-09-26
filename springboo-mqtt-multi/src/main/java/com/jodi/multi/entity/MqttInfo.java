package com.jodi.multi.entity;

import lombok.Data;

/**
 * @ClassName: MqttInfo
 * @Description: TODO
 * @Date: 2021/9/24 17:29
 */
@Data
public class MqttInfo {

    private String id;
    private String host;
    private String userName;
    private String pwd;
    private String topic;

}
