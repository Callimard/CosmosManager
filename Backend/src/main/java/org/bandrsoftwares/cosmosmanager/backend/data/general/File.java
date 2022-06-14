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
@Table(name = "File")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFile", nullable = false)
    private Integer id;

    @Column(name = "filePath", nullable = false)
    private String filePath;
}