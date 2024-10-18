package com.gyjs.mqtt.admin.bean;

import lombok.Data;

import java.util.List;

@Data
public class ListData {
    private int total;
    private List<Item> items;
}
