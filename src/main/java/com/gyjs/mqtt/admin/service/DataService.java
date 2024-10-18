package com.gyjs.mqtt.admin.service;

import org.springframework.messaging.Message;

public interface DataService {

    boolean saveData(Message<?> message);


}
