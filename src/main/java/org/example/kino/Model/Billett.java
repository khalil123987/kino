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

    @Column(name = "b_erBetalt")
    private boolean erBetalt;

    @ManyToOne
    @JoinColumn(name = "b_visningnr", referencedColumnName ="v_visningnr")
    private Visning visning;

    public Billett() {

    }






}
