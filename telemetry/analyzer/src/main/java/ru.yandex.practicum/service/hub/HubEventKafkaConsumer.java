package ru.yandex.practicum.service.hub;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.KafkaProperties;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

@Slf4j
@Service
public class HubEventKafkaConsumer {
    private final KafkaConsumer<String, HubEventAvro> consumer;
    private static final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public HubEventKafkaConsumer(KafkaProperties kafkaProperties) {
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getKeyDeserializerClass());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getHub().getValueDeserializerClass());
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getHub().getConsumerClient());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getHub().getConsumerGroup());
        consumer = new KafkaConsumer<>(config);
    }

    public void subscribe(String topic) {
        List<String> topics = List.of(topic);
        consumer.subscribe(topics);
    }

    public void read(Consumer<HubEventAvro> handleRecord) {
        ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(1000));
        int count = 0;
        for (ConsumerRecord<String, HubEventAvro> record : records) {
            handleRecord.accept(record.value());
            manageOffsets(record, count, consumer);
            count++;
        }
        consumer.commitAsync();
    }

    private static void manageOffsets(ConsumerRecord<String, HubEventAvro> record, int count, KafkaConsumer<String, HubEventAvro> consumer) {
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
