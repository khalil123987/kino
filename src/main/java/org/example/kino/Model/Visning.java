package org.example.kino.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "tblvisning")
public class Visning {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "v_visningnr")
    private int id;

    @Column(name = "v_filmnr", nullable = false)
    private int filmNr;

    @Column(name = "v_kinosalnr", nullable = false)
    private int kinosalNr;

    @Column(name = "v_dato", nullable = false)
    private LocalDate dato;

    @Column(name = "v_starttid", nullable = false)
    private LocalTime starttid;

    @Column(name = "v_pris", nullable = false)
    private double pris;

    public Visning() {}

    public Visning(int filmNr, int kinosalNr, LocalDate dato, LocalTime starttid, double pris) {
        this.filmNr = filmNr;
        this.kinosalNr = kinosalNr;
        this.dato = dato;
        this.starttid = starttid;
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

    public LocalDate getDato() {
        return dato;
    }

    public void setDato(LocalDate dato) {
        this.dato = dato;
    }

    public LocalTime getStarttid() {
        return starttid;
    }

    public void setStarttid(LocalTime starttid) {
        this.starttid = starttid;
    }

    // Denne metoden kombinerer dato og klokkeslett til et LocalDateTime-objekt
    public LocalDateTime getTidspunkt() {
        if (dato != null && starttid != null) {
            return LocalDateTime.of(dato, starttid);
        }
        return null;
    }

    // Denne metoden setter både dato og klokkeslett fra et LocalDateTime-objekt
    public void setTidspunkt(LocalDateTime tidspunkt) {
        if (tidspunkt != null) {
            this.dato = tidspunkt.toLocalDate();
            this.starttid = tidspunkt.toLocalTime();
        }
    }

    public double getPris() {
        return pris;
    }

    public void setPris(double pris) {
        this.pris = pris;
    }
}