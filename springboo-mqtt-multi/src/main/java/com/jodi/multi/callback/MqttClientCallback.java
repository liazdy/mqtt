package com.jodi.multi.callback;

import com.alibaba.fastjson.JSONObject;
import com.jodi.multi.config.MqttClientConnect;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

/**
 * MQTT回调函数
 */
@Slf4j
public class MqttClientCallback implements MqttCallback {

    /**
     * 系统的mqtt客户端id
     */
    private String mqttClientId;

    public MqttClientCallback(String mqttClientId) {
        this.mqttClientId = mqttClientId;
    }

    @Autowired
    private MqttClientConnect mqttClientConnect;

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
        JSONObject object = JSONObject.parseObject(message.toString());
        String direct = (String) object.get("direct");
        if ("0".equals(direct)) {
            // 从哪个clientId订阅的消息，就往哪个client响应消息
            MqttClientConnect mqttClientConnect = mqttClients.get(mqttClientId);
            mqttClientConnect.pub("/jodi/test", "{\"code\":\"200\"}");
        }
    }
}