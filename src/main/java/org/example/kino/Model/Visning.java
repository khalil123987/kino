package org.example.kino.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tblVisning")
public class Visning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "v_visningnr")
    private int id;

    @Column(name = "v_flmnr", nullable = false)
    private int filmNr;

    @Column(name = "v_kinosalnr", nullable = false)
    private int kinosalNr;

    @Column(name = "v_starttid", nullable = false)
    private LocalDateTime tidspunkt;

    @Column(name = "v_pris", nullable = false)
    private double pris;

    public Visning() {}

    public Visning(int filmNr, int kinosalNr, LocalDateTime tidspunkt, double pris) {
        this.filmNr = filmNr;
        this.kinosalNr = kinosalNr;
        this.tidspunkt = tidspunkt;
        this.pris = pris;
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

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }
}
