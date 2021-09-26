package com.jodi.multi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jodi.multi.callback.MqttClientCallback;
import com.jodi.multi.config.MqttClientConnect;
import com.jodi.multi.entity.MqttInfo;
import com.jodi.multi.mapper.MqttInfoMapper;
import com.jodi.multi.service.MqttInfoService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: MqttInfoServiceImpl
 * @Description: TODO
 * @Date: 2021/9/26 16:05
 */
@Service
@Slf4j
public class MqttInfoServiceImpl implements MqttInfoService {

    @Autowired
    private MqttInfoMapper mqttInfoMapper;

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<MqttInfo> findAll() {
        QueryWrapper<MqttInfo> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("id");
        return mqttInfoMapper.selectList(wrapper);
    }

    /**
     * 新增
     *
     * @param mqttInfo
     * @return
     */
    @Override
    @Transactional
    public int insert(MqttInfo mqttInfo) {
        try {
            MqttClientConnect mqttClientConnect = new MqttClientConnect();
            mqttClientConnect.setMqttClientId(mqttInfo.getId());
            mqttClientConnect.setMqttClient(mqttInfo.getHost(), mqttInfo.getId(), mqttInfo.getUserName(), mqttInfo.getPwd(), true, new MqttClientCallback(mqttInfo.getId()));
            mqttClientConnect.sub(StringUtils.commaDelimitedListToStringArray(mqttInfo.getTopic()));
            MqttClientConnect.mqttClients.put(mqttInfo.getId(), mqttClientConnect);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增连接失败,host--->" + mqttInfo.getHost());
            return 0;
        }
        mqttInfoMapper.insert(mqttInfo);
        log.error("新增连接成功,host--->" + mqttInfo.getHost());
        return 1;
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int remove(String id) {
        try {
            ConcurrentHashMap<String, MqttClientConnect> mqttClients = MqttClientConnect.mqttClients;
            MqttClientConnect mqttClientConnect = mqttClients.get(id);
            mqttClientConnect.close();
        } catch (MqttException e) {
            e.printStackTrace();
            log.error("断开连接失败,id--->" + id);
            return 0;
        }
        mqttInfoMapper.deleteById(id);
        log.info("断开连接成功,id--->" + id);
        return 1;
    }
}
