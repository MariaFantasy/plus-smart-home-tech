package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Slf4j
@Service
public class AggregatorKafkaProducer {
    private final Producer<String, SpecificRecordBase> producer;

    public AggregatorKafkaProducer(KafkaProperties kafkaProperties) {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getKeySerializerClass());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getValueSerializerClass());
        config.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getAggregatorProducerClient());
        producer = new KafkaProducer<>(config);
    }

    public void send(SpecificRecordBase event, String topic) {
        SensorsSnapshotAvro snapshotAvro = (SensorsSnapshotAvro) event;
        ProducerRecord<String, SpecificRecordBase> example = new ProducerRecord<>(topic, null, snapshotAvro.getTimestamp().toEpochMilli(), snapshotAvro.getHubId(), snapshotAvro);
        log.info("ProducerRecord: {}", example);
        producer.send(new ProducerRecord<>(topic, null, snapshotAvro.getTimestamp().toEpochMilli(), snapshotAvro.getHubId(), snapshotAvro));
        log.info("В кафку {} отправлено событие с телом {}", topic, event);
    }

    public void flush() {
        producer.flush();
    }

    public void close() {
        producer.close();
    }
}
