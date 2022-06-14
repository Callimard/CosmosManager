package org.bandrsoftwares.cosmosmanager.backend.data.scope;

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
@Table(name = "Role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRole", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description")
    private String description;

    @Column(name = "administratorRole", nullable = false)
    private Integer administratorRole;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "scope", nullable = false)
    private Scope scope;

    @ManyToMany
    @JoinTable(name = "RolePrivileges",
            joinColumns = @JoinColumn(name = "idRole"),
            inverseJoinColumns = @JoinColumn(name = "idPrivilege"))
    @ToString.Exclude
    private Set<Privilege> privileges = new LinkedHashSet<>();
}