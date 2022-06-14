package org.bandrsoftwares.cosmosmanager.backend.data.workspace.team;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.scope.Scope;
import org.bandrsoftwares.cosmosmanager.backend.data.user.User;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.Workspace;

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
@Table(name = "Team")
@DiscriminatorValue("T")
public class Team extends Scope {
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
    @JoinColumn(name = "manager", nullable = false)
    private User manager;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "workspace", nullable = false)
    private Workspace workspace;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TeamMembers",
            joinColumns = @JoinColumn(name = "idTeam"),
            inverseJoinColumns = @JoinColumn(name = "idUser"))
    @ToString.Exclude
    private Set<User> members = new LinkedHashSet<>();
}