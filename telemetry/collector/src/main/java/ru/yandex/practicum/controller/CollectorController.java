package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public void createSensorEvent(@Valid @RequestBody SensorEvent event) {
        log.info("Пришел POST запрос /events/sensors с телом: {}", event);
        collectorService.loadSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void createHubEvent(@Valid @RequestBody HubEvent event) {
        log.info("Пришел POST запрос /events/hubs с телом: {}", event);
        collectorService.loadHubEvent(event);
    }
}
