package com.jodi.mqtt.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @ClassName: MqttPublishConfig
 * @Description: Mqtt推送消息配置类
 * @Author: jodi
 * @Date: 2021/6/29 15:32
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttPublishConfig {

    /**
     * 创建推送消息通道
     *
     * @return
     */
    @Bean(value = "publishChannel")
    public MessageChannel publishChannel() {
        return new DirectChannel();
    }

    /**
     * mqtt推送配置
     * @param factory mqtt客户端
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "publishChannel")
    public MessageHandler mqttOutbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler handler = new MqttPahoMessageHandler("mqtt-publish-" + System.currentTimeMillis(), factory);
        handler.setAsync(true);
        handler.setDefaultQos(2);
        return handler;
    }
}
