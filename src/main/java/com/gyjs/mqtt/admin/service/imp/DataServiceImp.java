package com.gyjs.mqtt.admin.service.imp;

import com.gyjs.mqtt.admin.repository.DataCurrentService;
import com.gyjs.mqtt.admin.repository.DataHistoryService;
import com.gyjs.mqtt.admin.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class DataServiceImp implements DataService {
    @Autowired
    private DataCurrentService dataCurrentService;
    @Autowired
    private DataHistoryService dataHistoryService;

    @Override
    public boolean saveData(Message<?> message) {
        return false;
    }


}
