package org.example.kino.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tblVisning")
public class Visning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "v_id")
    private int id;

    @Column(name = "v_flmnr", nullable = false)
    private int filmNr;

    @Column(name = "v_kinosalnr", nullable = false)
    private int kinosalNr;

    @Column(name = "v_tid", nullable = false)
    private LocalDateTime tidspunkt;

    public Visning() {}

    public Visning(int filmNr, int kinosalNr, LocalDateTime tidspunkt) {
        this.filmNr = filmNr;
        this.kinosalNr = kinosalNr;
        this.tidspunkt = tidspunkt;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public int getFilmNr() {
        return filmNr;
    }

    public void setFilmNr(int filmNr) {
        this.filmNr = filmNr;
    }

    public int getKinosalNr() {
        return kinosalNr;
    }

    public void setKinosalNr(int kinosalNr) {
        this.kinosalNr = kinosalNr;
    }

    public LocalDateTime getTidspunkt() {
        return tidspunkt;
    }

    public void setTidspunkt(LocalDateTime tidspunkt) {
        this.tidspunkt = tidspunkt;
    }
}
