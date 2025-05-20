package org.example.kino.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tblbillett")
@Getter
@Setter
public class Billett {

    @Id
    @Column(name = "b_billettkode")
    private String billettkode;

    @Column(name ="b_visningnr")
    private Integer visningNr;

    @Column(name = "b_erbetalt")
    private boolean erBetalt;
    

    public Billett() {
    }
}
