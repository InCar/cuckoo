package com.incarcloud.cuckoo.service;

import com.incarcloud.cuckoo.dto.KafkaArgs;
import com.incarcloud.cuckoo.dto.MqttArgs;

public class Im2tSimArgs {
    public String host;
    public int port;
    public String topic;
    public String groupId;

    public Im2tSimArgs() {
    }

    public Im2tSimArgs(KafkaArgs args){
        this.host = args.getHost();
        this.port = args.getPort();
        this.topic = args.getTopic();
        this.groupId = args.getGroupId();
    }
}
