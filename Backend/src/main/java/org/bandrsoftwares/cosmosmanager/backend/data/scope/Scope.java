package org.bandrsoftwares.cosmosmanager.backend.data.scope;

import lombok.*;

import javax.persistence.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Scope")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "scopeType")
public class Scope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idScope", nullable = false)
    private Integer id;
}