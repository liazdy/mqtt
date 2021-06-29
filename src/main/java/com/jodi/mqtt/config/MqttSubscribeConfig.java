package com.jodi.mqtt.config;

import com.jodi.mqtt.subscribe.MqttMsgSubscribe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @ClassName: MqttSubscribeConfig
 * @Description: mqtt订阅消息配置
 * @Author: jodi
 * @Date: 2021/6/29 15:51
 * @Version: 1.0
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttSubscribeConfig {

    @Value("${mqtt.topic}")
    private String topic;

    private final MqttMsgSubscribe subscriber;

    public MqttSubscribeConfig(MqttMsgSubscribe mqttMsgSubscribe) {
        this.subscriber = mqttMsgSubscribe;
    }

    /**
     * mqtt消息订阅消息通道
     *
     * @return
     */
    @Bean
    public MessageChannel mqttSubscribeChannel() {
        return new DirectChannel();
    }

    /**
     * 指定订阅的主题,理解为消息的生产者
     * @param mqttSubscribeChannel 订阅消息通道
     * @param factory mqtt客户端
     * @return
     */
    @Bean
    public MessageProducer channelInbound(MessageChannel mqttSubscribeChannel, MqttPahoClientFactory factory) {
        String clientId = "mqtt-subscribe-" + System.currentTimeMillis();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, factory, topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttSubscribeChannel);
        return adapter;
    }

    /**
     * 订阅消息处理类
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttSubscribeChannel")
    public MessageHandler mqttMessageHandler(){
        return subscriber;
    }
}
