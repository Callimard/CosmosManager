package org.bandrsoftwares.cosmosmanager.backend.data.workspace;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.scope.Scope;
import org.bandrsoftwares.cosmosmanager.backend.data.user.User;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.Project;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.milestone.task.TaskStatusModel;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.team.Team;

import javax.persistence.*;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Workspace")
@DiscriminatorValue("W")
public class Workspace extends Scope {
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description")
    private String description;

    @Column(name = "photoPath")
    private String photoPath;

    @Column(name = "creationDate", nullable = false)
    private Instant creationDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "userOwner", nullable = false)
    private User userOwner;

    @ManyToMany
    @JoinTable(name = "WorkspaceUsers",
            joinColumns = @JoinColumn(name = "idWorkspace"),
            inverseJoinColumns = @JoinColumn(name = "idUser"))
    @ToString.Exclude
    private Set<User> users = new LinkedHashSet<>();

    @OneToMany(mappedBy = "workspace")
    @ToString.Exclude
    private Set<Team> teams = new LinkedHashSet<>();

    @OneToMany(mappedBy = "workspace")
    @ToString.Exclude
    private Set<Project> projects = new LinkedHashSet<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "defaultTaskStatusModel", nullable = false)
    private TaskStatusModel defaultTaskStatusModel;

    @ManyToMany
    @JoinTable(name = "WorkspaceCustomTaskStatus",
            joinColumns = @JoinColumn(name = "idWorkspace"),
            inverseJoinColumns = @JoinColumn(name = "idTaskStatusModel"))
    @ToString.Exclude
    private Set<TaskStatusModel> customTaskStatusModels = new LinkedHashSet<>();
}