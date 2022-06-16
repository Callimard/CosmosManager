package org.bandrsoftwares.cosmosmanager.backend.data.user;

import lombok.*;

import javax.persistence.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "EmailToVerify")
public class EmailToVerify {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idEmailToVerify", nullable = false)
    private Integer id;

    @Column(name = "nonce", nullable = false)
    private Long nonce;

    @Column(name = "email", nullable = false)
    private String email;
}