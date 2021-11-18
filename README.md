# springboot-mqtt-demo

#### 介绍
SpringBoot整合MQTT，实现推送以及订阅消息

##### 2021-09-26
增加连接多个MQTT服务操作(springboot-mqtt-multi)

##### 2021-11-18
1.增加对解析JSON消息(非JSON消息内容)异常捕获,防止断开连接
2.断开后重连,重新订阅原来主题