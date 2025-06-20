package ru.yandex.practicum;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka")
@Data
public class KafkaProperties {
    private String bootstrapServers;
    private String keySerializerClass;
    private String valueSerializerClass;
    private String sensorKafkaTopic;
    private String hubKafkaTopic;
}
