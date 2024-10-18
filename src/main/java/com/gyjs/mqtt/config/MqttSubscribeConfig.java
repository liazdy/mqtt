package com.gyjs.mqtt.config;

import com.gyjs.mqtt.subscribe.MqttMsgSubscribe;
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


    private final MqttMsgSubscribe subscriber;
    //    @Value("${mqtt.topic}")
    @Value("#{'${mqtt.topic}'.split(',')}")
    private String[] topic;

    public MqttSubscribeConfig(MqttMsgSubscribe mqttMsgSubscribe) {
        this.subscriber = mqttMsgSubscribe;
    }

    /**
     * mqtt消息订阅消息通道
     *
     * @return
     */
    @Bean(value = "mqttSubscribeChannel")
    public MessageChannel mqttSubscribeChannel() {
        return new DirectChannel();
    }

    /**
     * 使用客户端从订阅消息通道获取消息，配置qos等信息
     *
     * @param mqttSubscribeChannel  订阅消息通道
     * @param mqttPahoClientFactory mqtt服务器连接信息
     * @return
     */
    @Bean
    public MessageProducer channelInbound(MessageChannel mqttSubscribeChannel, MqttPahoClientFactory mqttPahoClientFactory) {
        String clientId = "mqtt-subscribe-" + System.currentTimeMillis();
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, mqttPahoClientFactory, topic);
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttSubscribeChannel);
        return adapter;
    }

    /**
     * 订阅消息处理类
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttSubscribeChannel")
    public MessageHandler mqttMessageHandler() {
        return subscriber;
    }
}
