package com.incarcloud.cuckoo.service;

import com.incarcloud.cuckoo.dto.MqttArgs;

public class ApowSimArgs {
    public String host;
    public int port;
    public String topic;

    public ApowSimArgs() {
    }

    public ApowSimArgs(MqttArgs args){
        this.host = args.getHost();
        this.port = args.getPort();
        this.topic = args.getTopic();
    }
}
