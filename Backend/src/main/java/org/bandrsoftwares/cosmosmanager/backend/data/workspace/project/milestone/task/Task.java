package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.general.File;
import org.bandrsoftwares.cosmosmanager.backend.data.user.User;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.Milestone;

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
@Table(name = "Task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTask", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description")
    private String description;

    @Column(name = "priority", nullable = false)
    private Integer priority;

    @Column(name = "creationDate", nullable = false)
    private Instant creationDate;

    @Column(name = "beginDate")
    private LocalDate beginDate;

    @Column(name = "dueDate")
    private LocalDate dueDate;

    @Column(name = "estimatedTime")
    private Integer estimatedTime;

    @Column(name = "closed", nullable = false)
    private Integer closed;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "taskStatus", nullable = false)
    private TaskStatus taskStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "taskStatusModel", nullable = false)
    private TaskStatusModel taskStatusModel;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "milestone", nullable = false)
    private Milestone milestone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TaskAssignedUsers",
            joinColumns = @JoinColumn(name = "idTask"),
            inverseJoinColumns = @JoinColumn(name = "idUser"))
    @ToString.Exclude
    private Set<User> assignedUsers = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "TaskAssociatedFiles",
            joinColumns = @JoinColumn(name = "idTask"),
            inverseJoinColumns = @JoinColumn(name = "idFile"))
    @ToString.Exclude
    private Set<File> associatedFiles = new LinkedHashSet<>();
}