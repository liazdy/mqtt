package com.jodi.multi.listener;

import com.jodi.multi.callback.MqttClientCallback;
import com.jodi.multi.config.MqttClientConnect;
import com.jodi.multi.entity.MqttInfo;
import com.jodi.multi.service.MqttInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目启动 监听主题
 */
@Slf4j
@Component
public class MqttClientListener implements ApplicationListener<ContextRefreshedEvent> {

//    @Value("${spring.profiles.active}")
//    private String active;

    @Autowired
    private MqttInfoService mqttInfoService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            List<MqttInfo> mqttInfos = mqttInfoService.findAll();
            for (MqttInfo mqtt : mqttInfos) {
                MqttClientConnect mqttClientConnect = new MqttClientConnect();
                mqttClientConnect.setMqttClientId(mqtt.getId());
                mqttClientConnect.setMqttClient(mqtt.getHost(), mqtt.getId(), mqtt.getUserName(), mqtt.getPwd(), true, new MqttClientCallback(mqtt.getId()));
                mqttClientConnect.sub(StringUtils.commaDelimitedListToStringArray(mqtt.getTopic()));
                MqttClientConnect.mqttClients.put(mqtt.getId(), mqttClientConnect);
                log.info("{}--连接成功！！订阅主题--{}", mqtt.getId(), mqtt.getTopic());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}