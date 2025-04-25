package com.gyjs.mqtt.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyjs.mqtt.admin.Utils.JWTUtil;
import com.gyjs.mqtt.admin.bean.ListQuery;
import com.gyjs.mqtt.admin.bean.Result;
import com.gyjs.mqtt.admin.repository.DataCurrentService;
import com.gyjs.mqtt.admin.repository.DataHistoryService;
import com.gyjs.mqtt.admin.table.DataCurrent;
import com.gyjs.mqtt.admin.table.DataHistory;
import com.gyjs.mqtt.publish.MqttMsgPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@CrossOrigin
public class ArticleController {
    @Autowired
    private DataCurrentService dataCurrentService;
    @Autowired
    private DataHistoryService dataHistoryService;
    @Autowired
    private MqttMsgPublisher publisher;
    @Autowired
    private ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @GetMapping("/list")
    public Result listInfo(ListQuery listQuery) {

        List<DataCurrent> all = dataCurrentService.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (listQuery.getClientId() != null && !listQuery.getClientId().isEmpty()) {
                predicates.add(cb.like(root.get("clientId"), "%" + listQuery.getClientId() + "%"));
            }
            if (listQuery.getNickname() != null && !listQuery.getNickname().isEmpty()) {
                predicates.add(cb.like(root.get("nickname"), "%" + listQuery.getNickname() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, getSortOrder(listQuery.getSort()));


        return Result.success().data("total", all.size()).data("items", all);

    }

    @GetMapping("/history")
    public Result history(ListQuery listQuery) {
        if (listQuery.getPage() < 1) {
            return Result.error().msg("页码小于第一页");
        }

        if (listQuery.getClientId() == null || listQuery.getClientId().isEmpty()) {
            return Result.error().msg("Client_id 是null");
        }

        try {
            int total = dataHistoryService.countDataHistoriesByClientId(listQuery.getClientId());
            Page<DataHistory> all = dataHistoryService.findAllByClientId(listQuery.getClientId(), PageRequest.of(listQuery.getPage() - 1, listQuery.getLimit(), getSortOrder(listQuery.getSort())));
            //List<DataHistory> list = all.toList();

            logger.info("fetch history client_id : {} size : {} data: {}", listQuery.getClientId(), all.getTotalElements(), all.getContent());
            return Result.success().data("total", all.getTotalElements()).data("items", all.getContent());
        } catch (Exception e) {
            logger.error("error when fetch history : {}", listQuery, e);
            return Result.error().msg("查询出错");
        }
    }


    @PostMapping("/create")
    public Result create(@RequestBody DataCurrent dataCurrent) {
        dataCurrent.setCreateTime(System.currentTimeMillis());
        dataCurrent.setUpdateTime(System.currentTimeMillis());

        try {
            dataCurrentService.save(dataCurrent);
        } catch (Exception e) {
            Result error = Result.error();
            error.setMessage("device_id 已经存在或者其他错误");
            return error;
        }
        return Result.success();
    }

    @PostMapping("/update")
    public Result update(@RequestBody DataCurrent dataCurrent) {


        DataCurrent data = dataCurrentService.findById(dataCurrent.getId()).orElse(null);
        if (data != null) {
            data.setUpdateTime(System.currentTimeMillis());
            data.setRemark(dataCurrent.getRemark());
            data.setNickname(dataCurrent.getNickname());
            dataCurrentService.save(data);

        } else
            return Result.error().msg("未找到数据");


        return Result.success();
    }

    @PostMapping("/publish/rate")
    public Result publishRate(@RequestBody DataCurrent dataCurrent) {
        if (dataCurrent.getImei() != null && !dataCurrent.getImei().isEmpty()) {
            publishV2("set_rate", dataCurrent.getRate(), dataCurrent.getImei());
            return Result.success();
        }
        publish(dataCurrent.getClientId() + "_rate", dataCurrent.getRate().toString());
        return Result.success();
    }

    @PostMapping("/publish/step")
    public Result publishStep(@RequestBody DataCurrent dataCurrent) {
        if (dataCurrent.getImei() != null && !dataCurrent.getImei().isEmpty()) {
            publishV2("set_step", dataCurrent.getStep(), dataCurrent.getImei());
            return Result.success();
        }
        publish(dataCurrent.getClientId() + "_step", dataCurrent.getStep().toString());

        return Result.success();
    }


    @PostMapping("/publish/qos")
    public Result publishQos(@RequestBody DataCurrent dataCurrent) {
        if (dataCurrent.getImei() != null && !dataCurrent.getImei().isEmpty()) {
            publishV2("set_qos", dataCurrent.getQos(), dataCurrent.getImei());
            return Result.success();
        }
        publish(dataCurrent.getClientId() + "_qos", dataCurrent.getQos().toString());

        return Result.success();
    }

    @PostMapping("/publish/qos_all")
    @Deprecated
    public Result publishQosAll(@RequestBody DataCurrent dataCurrent) {
        //这个不再支持 不再更新
//        List<DataCurrent> all = dataCurrentService.findAll();
//
//        all.parallelStream().forEach(dataC -> {
//            publish(dataC.getClientId() + "_qos", dataCurrent.getQos().toString());
//        });

        return Result.success();
    }

    @GetMapping("/info")
    public Result info(String token) {


        Map<String, Object> userInfo = JWTUtil.getInfo(token);
        return Result.success().data("name", userInfo.get("username")).data("avatar", userInfo.get("avatar"));

    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Long id) {
        if (id == null)
            return Result.error().msg("id 数据错误");
        dataCurrentService.deleteById(id);
        logger.info("delete id: {}", id);
        return Result.success();
    }

    // 根据 sort 参数生成 Sort 对象
    private Sort getSortOrder(String sort) {
        if (sort != null) {
            if (sort.equals("+id")) {
                return Sort.by(Sort.Direction.ASC, "id");
            } else if (sort.equals("-id")) {
                return Sort.by(Sort.Direction.DESC, "id");
            }
        }
        // 默认排序：正序
        return Sort.by(Sort.Direction.ASC, "id");
    }

    private void publish(String topic, Object payload) {
        publisher.publishMsgWithQos2(topic, payload);
        logger.info("推送主题-->{},推送消息-->{}", topic, payload);
    }

    private void publishV2(String cmd, Object value, String imei) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("cmd", cmd);
        if (value != null) {
            payload.put("value", value);
        }


        String topic = "/v2/sub/" + imei;
        String jsonPayload = null;
        try {
            jsonPayload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        publisher.publishMsgWithQos2(topic, jsonPayload);
        logger.info("推送主题-->{},推送消息-->{}", topic, jsonPayload);
    }
}
