package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "conditions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = { "id" })
public class Condition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ScenarioConditionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation")
    private ScenarioConditionOperation operation;

    @Column(name = "value")
    private Integer value;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;

    @ManyToMany(mappedBy = "conditions")
    private List<Scenario> scenarios;
}
