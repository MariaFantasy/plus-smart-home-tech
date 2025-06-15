package ru.yandex.practicum.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

@Slf4j
@Service
public class AggregatorKafkaConsumer {
    private final KafkaConsumer<String, SensorEventAvro> consumer;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public AggregatorKafkaConsumer(KafkaProperties kafkaProperties) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializerClass());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getValueDeserializerClass());
        consumer = new KafkaConsumer<>(config);
        List<String> topics = List.of(kafkaProperties.getSensorKafkaTopic());
        consumer.subscribe(topics);
    }

    public void read(Consumer<SensorEventAvro> handleRecord) {
        ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(1000));
        int count = 0;
        for (ConsumerRecord<String, SensorEventAvro> record : records) {
            handleRecord.accept(record.value());
            manageOffsets(record, count, consumer);
            count++;
        }
        consumer.commitAsync();
    }

    private static void manageOffsets(ConsumerRecord<String, SensorEventAvro> record, int count, KafkaConsumer<String, SensorEventAvro> consumer) {
        // обновляем текущий оффсет для топика-партиции
        currentOffsets.put(
                new TopicPartition(record.topic(), record.partition()),
                new OffsetAndMetadata(record.offset() + 1)
        );

        if(count % 10 == 0) {
            consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                if(exception != null) {
                    log.warn("Ошибка во время фиксации оффсетов: {}", offsets, exception);
                }
            });
        }
    }

    public void commit() {
        consumer.commitAsync();
    }

    public void close() {
        consumer.close();
    }
}
