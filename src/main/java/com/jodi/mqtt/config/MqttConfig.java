package com.jodi.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * @ClassName: MqttConfig
 * @Description: MQTT配置类
 * @Author: jodi
 * @Date: 2021/6/29 15:20
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttConfig {

    @Value("${mqtt.host}")
    private String host;

    @Value("${mqtt.userName}")
    private String userName;

    @Value("${mqtt.pwd}")
    private String pwd;

    /**
     * 创建Mqtt客户端
     * @return
     */
    @Bean
    public MqttPahoClientFactory factory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(pwd.toCharArray());
        options.setServerURIs(new String[]{host});
        factory.setConnectionOptions(options);
        return factory;
    }
}
