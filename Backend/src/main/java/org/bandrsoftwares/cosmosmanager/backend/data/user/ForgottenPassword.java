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
@Table(name = "ForgottenPassword")
public class ForgottenPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idForgottenPassword", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cosmosManagerUser", nullable = false)
    private User user;

    @Column(name = "nonce", nullable = false)
    private Long nonce;
}