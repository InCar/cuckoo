package com.incarcloud.cuckoo.cape.mq.mqtt;

import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PahoV5 implements MqttCallback, AutoCloseable {
    private final static Logger s_logger = LoggerFactory.getLogger(PahoV5.class);
    private final MqttAsyncClient mqttAsyncClient;

    private Consumer<String> onRecv = null;

    public PahoV5(String host, int port, String clientId){
        try {
            this.mqttAsyncClient = new MqttAsyncClient("tcp://" + host + ":" + port, clientId);
            this.mqttAsyncClient.setCallback(this);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        this.mqttAsyncClient.close();
    }

    public void connect(String username){
        try {
            MqttConnectionOptions options = new MqttConnectionOptions();
            options.setAutomaticReconnect(false);
            options.setCleanStart(false);
            options.setUserName(username);

            this.mqttAsyncClient.connect(options).waitForCompletion(1000*5); // 5s
        }
        catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect(){
        try {
            this.mqttAsyncClient.disconnect().waitForCompletion(1000*5); // 5s
        }
        catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public IMqttToken sendAsync(String topic, String message) {
        try {
            int qos = 0; // 0: 至多一次,有可能丢失; 1: 至少一次,有可能重复; 2: 有且只有一次
            boolean retained = false;
            return this.mqttAsyncClient.publish(topic, message.getBytes(), qos, retained);
        }
        catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public IMqttToken recvAsync(String topic, Consumer<String> onRecv) {
        try {
            this.onRecv = onRecv;
            return this.mqttAsyncClient.subscribe(topic, 0);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {
        s_logger.info("MQTT disconnected");
    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {
        s_logger.error("MQTT error occurred", exception);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        if(this.onRecv != null) {
            this.onRecv.accept(msg);
        }
        else {
            s_logger.info("MQTT message arrived, topic: {}, message: {}", topic, message);
        }
    }

    @Override
    public void deliveryComplete(IMqttToken token) {
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        s_logger.info("MQTT {} connected", serverURI);
    }

    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {
        s_logger.info("MQTT auth packet arrived, reasonCode: {}", reasonCode);
    }
}
