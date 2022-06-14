package org.bandrsoftwares.cosmosmanager.backend.data.general;

import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idAddress", nullable = false)
    private Integer id;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "postalCode", nullable = false, length = 5)
    private String postalCode;

    @Column(name = "city", nullable = false)
    private String city;
}