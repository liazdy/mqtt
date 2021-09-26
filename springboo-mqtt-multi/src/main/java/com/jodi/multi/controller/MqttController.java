package com.jodi.multi.controller;

import com.jodi.multi.entity.MqttInfo;
import com.jodi.multi.service.MqttInfoService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName: MqttController
 * @Description: 测试控制层
 * @Date: 2021/9/26 16:30
 */
@RestController
public class MqttController {

    @Autowired
    private MqttInfoService mqttInfoService;

    @PostMapping("/insert")
    public String insert(@RequestBody MqttInfo mqttInfo) throws MqttException {
        return "" + mqttInfoService.insert(mqttInfo);
    }

    @PostMapping("/remove/{id}")
    public String insert(@PathVariable String id) throws MqttException {
        return "" + mqttInfoService.remove(id);
    }
}
