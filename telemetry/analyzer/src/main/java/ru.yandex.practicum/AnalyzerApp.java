package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.service.hub.HubEventProcessor;
import ru.yandex.practicum.service.snapshot.SnapshotProcessor;

@Slf4j
@SpringBootApplication
@EnableConfigurationProperties(KafkaProperties.class)
public class AnalyzerApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApp.class, args);

        Environment env = context.getEnvironment();
        log.info("GRPC: {}", env.getProperty("grpc.client.hub-router.address"));

        final HubRouterControllerGrpc.HubRouterControllerBlockingStub grpcClient = context.getBean(HubRouterControllerGrpc.HubRouterControllerBlockingStub.class);
        final HubEventProcessor hubEventProcessor = context.getBean(HubEventProcessor.class);
        SnapshotProcessor snapshotProcessor = context.getBean(SnapshotProcessor.class);

        // запускаем в отдельном потоке обработчик событий
        // от пользовательских хабов
        Thread hubEventsThread = new Thread(hubEventProcessor);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();

        // В текущем потоке начинаем обработку
        // снимков состояния датчиков
        snapshotProcessor.start();
    }
}
