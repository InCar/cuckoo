package com.incarcloud.cuckoo.service;

import com.incarcloud.cuckoo.dto.MqttArgs;

public class ApowSimArgs {
    public String host;
    public int port;
    public String topic;
    public float speed; // 速度,每秒发送的数据包数量

    public ApowSimArgs() {
    }

    public ApowSimArgs(MqttArgs args, float speed){
        this.host = args.getHost();
        this.port = args.getPort();
        this.topic = args.getTopic();

        this.speed = speed;
    }
}
