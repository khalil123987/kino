package org.example.kino.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "tblkinosal")
public class Kinosal {

    @Id
    @Column(name = "k_kinosalnr")
    private Integer kinosalNr;

    @Column(name = "k_kinonavn", length = 20)
    private String kinoNavn;

    @Column(name = "k_kinosalnavn", length = 20)
    private String kinosalNavn;

    // Relasjoner til andre tabeller
    // En kinosal kan ha mange plasser, relasjon til tblplasser
    @OneToMany(mappedBy = "kinosal")
    private List<Plass> plasser;

    // En kinosal kan ha mange visninger, relasjon til visning
    @OneToMany(mappedBy = "kinosal")
    private List<Visning> visninger;
}