package ru.t1.asavin.techSupportAutomation.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @UpdateTimestamp
    private LocalDateTime dateStatusAssignedToIncident;

    @ManyToOne
    @JoinColumn(name = "incident_id")
    private Incident incident;

    public Status(String name) {
        this.name = name;
    }

    public Status(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
