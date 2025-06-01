package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.hub.HubEvent;
import ru.yandex.practicum.dto.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class CollectorController {
    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public void createSensorEvent(@Valid @RequestBody SensorEvent event) {
        collectorService.loadSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void createHubEvent(@Valid @RequestBody HubEvent event) {
        collectorService.loadHubEvent(event);
    }
}
