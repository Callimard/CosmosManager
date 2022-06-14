package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TaskStatusModel")
public class TaskStatusModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTaskStatusModel", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TaskStatusModelTaskStatusList",
            joinColumns = @JoinColumn(name = "idTaskStatusModel"),
            inverseJoinColumns = @JoinColumn(name = "idTaskStatus"))
    @ToString.Exclude
    private Set<TaskStatus> taskStatusList = new LinkedHashSet<>();
}