package org.bandrsoftwares.cosmosmanager.backend.data.user;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.general.Address;
import org.bandrsoftwares.cosmosmanager.backend.data.scope.Role;

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
@Table(name = "CosmosManagerUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUser", nullable = false)
    private Integer id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false, length = 30)
    private String password;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "photoPath")
    private String photoPath;

    @Column(name = "creationDate", nullable = false)
    private Instant creationDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserRoles",
            joinColumns = @JoinColumn(name = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "idRole"))
    @ToString.Exclude
    private Set<Role> userRoles = new LinkedHashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserAddresses",
            joinColumns = @JoinColumn(name = "idUser"),
            inverseJoinColumns = @JoinColumn(name = "idAddress"))
    @ToString.Exclude
    private Set<Address> addresses = new LinkedHashSet<>();
}