package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.service.AggregationStarter;

@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class AggregatorApp {
    public static void main(String[] args) {
        // Запуск Spring Boot приложения при помощи вспомогательного класса SpringApplication
        // метод run возвращает назад настроенный контекст, который мы можем использовать для
        // получения настроенных бинов
        ConfigurableApplicationContext context = SpringApplication.run(AggregatorApp.class, args);

        // Получаем бин AggregationStarter из контекста и запускаем основную логику сервиса
        AggregationStarter aggregator = context.getBean(AggregationStarter.class);
        aggregator.start();
    }
}
