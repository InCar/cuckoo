package com.incarcloud.cuckoo.cape.mq.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class Blinken618 implements AutoCloseable{
    private final static Logger s_logger = LoggerFactory.getLogger(Blinken618.class);

    private final KafkaProducer<String, byte[]> kafkaProducer;
    private final KafkaConsumer<String, byte[]> kafkaConsumer;

    private Thread m_threadRecv = null;
    private Consumer<byte[]> onRecv = null;

    private final AtomicBoolean m_atomicCanExit = new AtomicBoolean(false);

    public Blinken618(String host, int port, String groupId){
        Properties propsSender = new Properties();
        propsSender.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
        propsSender.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        propsSender.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());
        propsSender.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        kafkaProducer = new KafkaProducer<>(propsSender);

        Properties propsRecver = new Properties();
        propsRecver.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host + ":" + port);
        propsRecver.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        propsRecver.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        propsRecver.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        propsRecver.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaConsumer = new KafkaConsumer<>(propsRecver);
    }

    public void sendAsync(String topic, byte[] message){
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, message);
        kafkaProducer.send(record);
    }

    public void recvAsync(String topic, Consumer<byte[]> onRecv){
        this.onRecv = onRecv;
        kafkaConsumer.subscribe(Arrays.asList(topic));
        m_atomicCanExit.set(false);
        m_threadRecv = new Thread(this::doRecv);
        m_threadRecv.start();
    }

    public void stopRecv(){
        try {
            m_atomicCanExit.set(true);
            m_threadRecv.join();
        }
        catch (InterruptedException e) {
            s_logger.error("InterruptedException", e);
        }
    }

    private void doRecv(){
        while(!m_atomicCanExit.get()){
            kafkaConsumer.poll(Duration.ofMillis(500)).forEach(record -> {
                this.onRecv.accept(record.value());
            });
        }
    }

    @Override
    public void close() throws Exception {
        kafkaProducer.close();
        kafkaConsumer.close();
    }
}
