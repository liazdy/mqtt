package com.gyjs.mqtt.subscribe;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyjs.mqtt.admin.repository.DataCurrentService;
import com.gyjs.mqtt.admin.repository.DataHistoryService;
import com.gyjs.mqtt.admin.table.DataCurrent;
import com.gyjs.mqtt.admin.table.DataHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @ClassName: MqttMsgSubscribe
 * @Description: mqtt订阅处理
 * @Author: jodi
 * @Date: 2021/6/29 15:47
 * @Version: 1.0
 */
@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttMsgSubscribe implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(MqttMsgSubscribe.class);

    @Autowired
    private DataHistoryService dataHistoryService;
    @Autowired
    private DataCurrentService dataCurrentService;
    @Autowired
    private ObjectMapper objectMapper;

    public void merge(Object source, Object target) {
        Field[] fields = source.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // 确保可以访问私有字段
            try {
                Object value = field.get(source);

                // 检查字段是否为 String 类型且不为空
                if (value != null && !(value instanceof String && ((String) value).isEmpty())) {
                    Field targetField = target.getClass().getDeclaredField(field.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("merge error : source {} \n target{} ", source, target, e);
            }
        }
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        long currentTime = System.currentTimeMillis();

        String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));

        String id = String.valueOf(message.getHeaders().get(MqttHeaders.ID));
        int qos = Integer.valueOf(String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_QOS)), 10);
        String[] payload = String.valueOf(message.getPayload()).split("###");
        String mcuPayload = payload[0];
        logger.info("Mqtt服务器ID-->{},订阅主题-->{},收到消息-->{}, qos -->{}", id, topic, message.getPayload(), qos);
        if (topic.equalsIgnoreCase("time")) return;
        if (!topic.split("_")[topic.split("_").length - 1].equals("info"))
            return;


        try {
            DataCurrent dataMqtt = objectMapper.readValue(mcuPayload, DataCurrent.class);
            String clientId = dataMqtt.getClientId();
            if ("".equalsIgnoreCase(clientId) || clientId.isEmpty()) {
                logger.info("no clientId found . return");
                return;
            }

            DataCurrent dataCurrent = dataCurrentService.findByClientId(clientId);
            if (dataCurrent == null) {
                dataCurrent = dataMqtt;
                dataCurrent.setCreateTime(currentTime);
            } else {
                merge(dataMqtt, dataCurrent);
            }

            String imei = "";
            String csq = "";
            if (topic.startsWith("/v2")) {
                if (payload.length > 1) {
                    imei = payload[1];
                    dataCurrent.setImei(imei);
                }
                if (payload.length > 2) {
                    csq = payload[2];
                    dataCurrent.setCsq(csq);
                }
            }
            dataCurrent.setUpdateTime(currentTime);
            dataCurrent.setQos((short) qos);
            dataCurrentService.save(dataCurrent);

            DataHistory dataHistory = objectMapper.readValue(mcuPayload, DataHistory.class);
            if (topic.startsWith("/v2")) {
                if (payload.length > 2) {
                    csq = payload[2];
                    dataHistory.setCsq(csq);
                }
            }
            dataHistory.setCreateTime(currentTime);
            dataHistory.setQos((short) qos);
            dataHistoryService.save(dataHistory);

        } catch (JsonProcessingException e) {
            logger.error("json read error : {}", mcuPayload);
            //throw new RuntimeException(e);
        }
        logger.info("done-->{},订阅主题-->{},收到消息-->{}", id, topic, message.getPayload());
    }
}
