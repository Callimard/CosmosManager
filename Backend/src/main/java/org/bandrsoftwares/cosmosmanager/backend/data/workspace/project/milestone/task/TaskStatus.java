package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task;

import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "TaskStatus")
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idTaskStatus", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "color", nullable = false)
    private Integer color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "previous")
    private TaskStatus previous;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "next")
    private TaskStatus next;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "taskStatusModel", nullable = false)
    private TaskStatusModel taskStatusModel;
}