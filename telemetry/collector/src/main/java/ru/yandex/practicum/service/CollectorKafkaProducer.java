package ru.yandex.practicum.service;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class CollectorKafkaProducer {
    private static final String BOOTSTRAP_SERVERS_CONFIG = "localhost:9092";
    private static final String KEY_SERIALIZER_CLASS_CONFIG = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String VALUE_SERIALIZER_CLASS_CONFIG = "ru.yandex.practicum.mapper.GeneralAvroSerializer";
    private final Producer<Void, SpecificRecordBase> producer;

    public CollectorKafkaProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER_CLASS_CONFIG);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS_CONFIG);
        producer = new KafkaProducer<>(config);
    }

    public void send(SpecificRecordBase event, String topic) {
        ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(topic, event);
        producer.send(record);
    }

    public void close() {
        producer.close();
    }
}
