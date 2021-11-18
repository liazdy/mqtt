package com.jodi.multi.service;

import com.jodi.multi.entity.MqttInfo;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

public interface MqttInfoService {

    List<MqttInfo> findAll();

    int insert(MqttInfo mqttInfo) throws MqttException;

    int remove(String id) throws MqttException;

    MqttInfo findById(String id);
}
