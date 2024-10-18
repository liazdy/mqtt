package com.gyjs.mqtt.admin.bean;

import com.gyjs.mqtt.admin.table.DataCurrent;
import lombok.Data;

import java.util.List;

@Data
/*
*  "id": 1,
    "timestamp": 1171462803908,
    "author": "Frank",
    "reviewer": "Jose",
    "title": "Rqhwuhdtt Lvzja Ewzetbcts Tsdzcyoqw Fuihxuu Oopp Knrzvhr Hmoc",
    "content_short": "mock data",
    "content": "<p>I am testing data, I am testing data.</p><p><img src=\"https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943\"></p>",
    "forecast": 77.17,
    "importance": 2,
    "type": "JP",
    "status": "draft",
    "display_time": "1981-08-19 11:15:18",
    "comment_disabled": true,
    "pageviews": 2059,
    "image_uri": "https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3",
    "platforms": [
        "a-platform"
    ]
* */
public class Item extends DataCurrent {
    private String author;
    private String reviewer;
    private String title;
    private String content_short;
    private String content;
    private Float forecast;
    private Integer importance;
    private String type;
    private String status;
    private String display_time;
    private boolean comment_disabled;
    private Long pageviews;
    private String image_uri;
    private List<String> platforms;

    /**
     * CREATE TABLE `data_current` (
     *   `id` int NOT NULL AUTO_INCREMENT,
     *   `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备id 唯一',
     *   `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备别名',
     *   `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注/说明',
     *   `timestamp` bigint DEFAULT NULL COMMENT '上传时的设备时间',
     *   `step` int DEFAULT NULL COMMENT '上传的时间间隔 单位：秒',
     *   `rate` smallint DEFAULT '100' COMMENT '工作功率',
     *   `create_time` bigint DEFAULT NULL COMMENT '创建时间/出厂时间',
     *   `update_time` bigint DEFAULT NULL COMMENT '最后更新/人工修改时间',
     *   `status` smallint NOT NULL DEFAULT '1',
     *   PRIMARY KEY (`id`),
     *   UNIQUE KEY `device_id` (`client_id`),
     *   KEY `time` (`update_time`)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
     */


}
