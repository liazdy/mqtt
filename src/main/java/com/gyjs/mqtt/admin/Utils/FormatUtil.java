package com.gyjs.mqtt.admin.Utils;

import com.gyjs.mqtt.admin.bean.Item;

import java.util.ArrayList;

public class FormatUtil {
    /**
     * *  "id": 1,
     * "timestamp": 1171462803908,
     * "author": "Frank",
     * "reviewer": "Jose",
     * "title": "Rqhwuhdtt Lvzja Ewzetbcts Tsdzcyoqw Fuihxuu Oopp Knrzvhr Hmoc",
     * "content_short": "mock data",
     * "content": "<p>I am testing data, I am testing data.</p><p><img src=\"https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943\"></p>",
     * "forecast": 77.17,
     * "importance": 2,
     * "type": "JP",
     * "status": "draft",
     * "display_time": "1981-08-19 11:15:18",
     * "comment_disabled": true,
     * "pageviews": 2059,
     * "image_uri": "https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3",
     * "platforms": [
     * "a-platform"
     * ]
     *
     * @param id
     * @return
     */
    public static Item formatData(Long id) {
        if (id == null) {
            return null;
        }
        Item item = new Item();
        item.setId(id);
        item.setAuthor("Frank");
        item.setContent("<p>I am testing data, I am testing data.</p><p><img src=\\\"https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943\\\"></p>");
        item.setForecast(77.17F);
        item.setDisplay_time("1981-08-19 11:15:18");
        item.setContent_short("mock data");
        item.setImportance(2);
        item.setPageviews(2059L);
        item.setPlatforms(new ArrayList<>());
        item.setStatus("draft");
        item.setType("JP");
        item.setReviewer("reviewer");
        item.setTitle("title");
        //item.setTimestamp(System.currentTimeMillis());
        item.setImage_uri("https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3");
        item.setComment_disabled(true);


        return item;


    }

    public static Item formatData(Long id, Item item) {
        if (id == null) {
            return null;
        }
        item.setId(id);
        item.setAuthor("Frank");
        item.setContent("<p>I am testing data, I am testing data.</p><p><img src=\\\"https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943\\\"></p>");
        item.setForecast(77.17F);
        item.setDisplay_time("1981-08-19 11:15:18");
        item.setContent_short("mock data");
        item.setImportance(2);
        item.setPageviews(2059L);
        item.setPlatforms(new ArrayList<>());
        item.setStatus("draft");
        item.setType("JP");
        item.setReviewer("reviewer");
        item.setTitle("title");
        //item.setTimestamp(System.currentTimeMillis());
        item.setImage_uri("https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3");
        item.setComment_disabled(true);


        return item;


    }
}
