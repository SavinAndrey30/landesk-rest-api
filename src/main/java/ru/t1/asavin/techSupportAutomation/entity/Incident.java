package ru.t1.asavin.techSupportAutomation.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String screenshotFilename;
    private String errorDescription;
    private LocalDateTime dateCreated;
    private LocalDateTime deadline;
    private LocalDateTime dateClosed;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    private String initiatorFullName;
    private String initiatorUsername;
    private String initiatorPhone;
    private String initiatorLocation;
    private String initiatorEmail;

    @OneToMany(mappedBy="incident", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Status> statusUpdates;

    @ManyToOne
    @JoinColumn(name = "analyst_id")
    private User assignedAnalyst;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public void addStatusUpdates(Status statusToAdd) {
        statusUpdates.add(statusToAdd);
    }
}
