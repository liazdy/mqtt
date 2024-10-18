package com.gyjs.mqtt.admin.table;

import lombok.Data;

@Data
public class DataMqtt {

    private String clientId;

    private String nickname;

    private String remark;

    private Integer step;

    private Short rate;

    private Float a1;

    private Float a2;

    private Float cp;

    private Float cs;

    private Float ca;

    private Float td1;

    private Float td2;

    private Short qos;

    private Long timestamp;

    private Long updateTime;

    private Long createTime;
}
