package com.gyjs.mqtt.publish;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MqttPushlishMsg
 * @Description: 向MQTT服务器推送消息
 * @Author: jodi
 * @Date: 2021/6/29 15:27
 * @Version: 1.0
 */
@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
@MessagingGateway(defaultRequestChannel = "publishChannelQos2")
public interface MqttMsgPublisher {

    /**
     * 推送消息
     *
     * @param topic   主题
     * @param payload 消息内容
     */
    void publishMsgWithQos2(@Header(MqttHeaders.TOPIC) String topic, Object payload);
}
