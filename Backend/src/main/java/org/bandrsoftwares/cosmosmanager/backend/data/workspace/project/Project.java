package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.scope.Scope;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.Workspace;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.delivery.Delivery;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.Milestone;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.team.Team;

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
@Table(name = "Project")
@DiscriminatorValue("P")
public class Project extends Scope {
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description")
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
    @JoinColumn(name = "workspace", nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "project")
    @ToString.Exclude
    private Set<Milestone> milestones = new LinkedHashSet<>();

    @OneToMany(mappedBy = "project")
    @ToString.Exclude
    private Set<Delivery> deliveries = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(name = "ProjectAssignedTeam",
            joinColumns = @JoinColumn(name = "idProject"),
            inverseJoinColumns = @JoinColumn(name = "idTeam"))
    @ToString.Exclude
    private Set<Team> assignedTeams = new LinkedHashSet<>();
}