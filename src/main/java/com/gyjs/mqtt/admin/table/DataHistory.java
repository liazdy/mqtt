package com.gyjs.mqtt.admin.table;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "data_history", indexes = {
        @Index(name = "client_id", columnList = "client_id"),
        @Index(name = "time", columnList = "update_time")
})
public class DataHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "step")
    private Integer step;

    @Column(name = "rate")
    private Short rate;

    @Column(name = "a1")
    private Float a1;

    @Column(name = "a2")
    private Float a2;

    @Column(name = "cp")
    private Float cp;

    @Column(name = "cs")
    private Float cs;

    @Column(name = "ca")
    private Float ca;

    @Column(name = "td1")
    private Float td1;

    @Column(name = "td2")
    private Float td2;

    @Column(name = "td3")
    private Float td3;

    @Column(name = "td4")
    private Float td4;

    @Column(name = "qos")
    private Short qos;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "update_time")
    private Long updateTime;

    @Column(name = "create_time")
    private Long createTime;

}
