package com.gyjs.mqtt.admin.bean;

import lombok.Data;

@Data
/*
* listQuery: {
        page: 1,
        limit: 20,
        importance: undefined,
        title: undefined,
        type: undefined,
        sort: '+id'
      }
*
* */

public class ListQuery {
    private int page = 1;
    private int limit = 10;
    private String clientId;
    private String nickname;
    private String sort;

}
