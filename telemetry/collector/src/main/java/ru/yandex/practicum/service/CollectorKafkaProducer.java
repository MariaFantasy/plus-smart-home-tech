package ru.yandex.practicum.service;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.stereotype.Service;

@Service
public class CollectorKafkaProducer {
    public static final String BOOTSTRAP_SERVERS_CONFIG = "localhost:9092";
    public static final String KEY_SERIALIZER_CLASS_CONFIG = "";
    public static final String VALUE_SERIALIZER_CLASS_CONFIG = "";

    // PRODUCER

    public CollectorKafkaProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS_CONFIG);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER_CLASS_CONFIG);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS_CONFIG);
        // PRODUCER producer = new KafkaProducer<>(config);
    }

    public void send() {
        //
    }

    public close() {
        // PRODUCER producer.close()
    }
}
