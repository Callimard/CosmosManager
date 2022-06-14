package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.Project;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task.Task;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task.TaskStatusModel;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Milestone")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idMilestone", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "Description")
    private String description;

    @Column(name = "creationDate", nullable = false)
    private Instant creationDate;

    @Column(name = "beginDate")
    private LocalDate beginDate;

    @Column(name = "dueDate")
    private LocalDate dueDate;

    @Column(name = "estimatedTime")
    private Integer estimatedTime;

    @Column(name = "achieved", nullable = false)
    private Integer achieved;

    @Column(name = "closed", nullable = false)
    private Integer closed;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "taskStatusModel", nullable = false)
    private TaskStatusModel taskStatusModel;

    @OneToMany(mappedBy = "milestone")
    @ToString.Exclude
    private Set<Task> tasks = new LinkedHashSet<>();
}