package org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.delivery;

import lombok.*;
import org.bandrsoftwares.cosmosmanager.backend.data.general.File;
import org.bandrsoftwares.cosmosmanager.backend.data.workspace.project.Project;

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
@Table(name = "Delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDelivery", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "deliverySpecification")
    private String deliverySpecification;

    @Column(name = "creationDate", nullable = false)
    private Instant creationDate;

    @Column(name = "dueDate")
    private LocalDate dueDate;

    @Column(name = "completed", nullable = false)
    private Integer completed;

    @Column(name = "closed", nullable = false)
    private Integer closed;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "project", nullable = false)
    private Project project;

    @ManyToMany
    @JoinTable(name = "DeliveryAssociatedFiles",
            joinColumns = @JoinColumn(name = "idDelivery"),
            inverseJoinColumns = @JoinColumn(name = "idFile"))
    @ToString.Exclude
    private Set<File> associatedFiles = new LinkedHashSet<>();
}