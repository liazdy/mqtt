package com.jodi.multi.callback;

import com.alibaba.fastjson.JSONObject;
import com.jodi.multi.config.MqttClientConnect;
import com.jodi.multi.entity.MqttInfo;
import com.jodi.multi.service.MqttInfoService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MQTT回调函数
 */
@Slf4j
@Component
public class MqttClientCallback implements MqttCallback, MqttCallbackExtended {

    /**
     * 系统的mqtt客户端id
     */
    private String mqttClientId;

    @Autowired
    private MqttClientConnect mqttClientConnect;

    private static MqttClientCallback callback;

    @Autowired
    private MqttInfoService mqttInfoService;

    @PostConstruct
    public void init() {
        callback = this;
    }

    public MqttClientCallback(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    public MqttClientCallback() {

    }

    /**
     * MQTT 断开连接会执行此方法
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.error("断开了MQTT连接 ：{}", throwable.getMessage());
        log.error(throwable.getMessage(), throwable);
    }

    /**
     * publish发布成功后会执行到这里
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("发布消息成功");
    }

    /**
     * subscribe订阅后得到的消息会执行到这里
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("收到来自 " + mqttClientId + "====" + topic + " 的消息：{}", new String(message.getPayload()));
        if (message.isRetained()) {
            log.info("收到保留消息：{}，消息质量：{}，消息主题：{}", message.getPayload(), message.getQos(), topic);
        }
        ConcurrentHashMap<String, MqttClientConnect> mqttClients = MqttClientConnect.mqttClients;
        String payload = message.toString();
        // 方法一：解析JSON异常捕获防止断开连接
        JSONObject object = null;
        try {
            object = JSONObject.parseObject(payload);
        } catch (Exception e) {
//            e.printStackTrace();
            // 解析JSON消息异常打印日志并返回
            log.error("解析JSON异常:" + e.getMessage() + ",message内容：" + payload);
            return;
        }
        // 返回消息
        String direct = (String) object.get("direct");
        if ("0".equals(direct)) {
            // 从哪个clientId订阅的消息，就往哪个client响应消息
            MqttClientConnect mqttClientConnect = mqttClients.get(mqttClientId);
            mqttClientConnect.pub("/jodi/test", "{\"code\":\"200\"}");
        }
    }

    /**
     * 连接成功(首次连接、断开连接都会执行该方法)
     *
     * @param b
     * @param s
     */
    @Override
    public void connectComplete(boolean b, String s) {
        ConcurrentHashMap<String, MqttClientConnect> mqttClients = MqttClientConnect.mqttClients;
        MqttClientConnect mqttConnect = mqttClients.get(mqttClientId);
        if (ObjectUtils.isEmpty(mqttConnect)) {
            // 首次连接
            log.info("连接成功，clientId-->" + mqttClientId);
            return;
        }
        // 方法二：异常断开后重连，需要重新订阅主题(因为异常断开重连后不会订阅原来主题)
        MqttInfo mqtt = callback.mqttInfoService.findById(mqttClientId);
        String topic = mqtt.getTopic();
        try {
            mqttConnect.sub(topic, 0);
        } catch (MqttException e) {
            e.printStackTrace();
            log.error("重新连接后订阅异常");
        }
        log.info("重新连接后，订阅成功，topic-->" + topic);
    }
}