package org.example.kino.Model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tblPlassbillett")
@IdClass(Plassbillett.SammensattPlassBillettId.class)
public class Plassbillett {

    @Id
    @Column(name = "pb_billettkode", nullable = false, length = 6)
    private String billettkode;

    @Id
    @Column(name = "pb_radnr")
    private int radNr;

    @Id
    @Column(name = "pb_setenr")
    private int seteNr;

    @Id
    @Column(name = "pb_kinosalnr")
    private int kinosalNr;

    // Constructors
    public Plassbillett() {}

    public Plassbillett(String billettkode, int radNr, int seteNr, int kinosalNr) {
        this.billettkode = billettkode;
        this.radNr = radNr;
        this.seteNr = seteNr;
        this.kinosalNr = kinosalNr;
    }

    // Getters and setters

    public String getBillettkode() {
        return billettkode;
    }

    public void setBillettkode(String billettkode) {
        this.billettkode = billettkode;
    }

    public int getRadNr() {
        return radNr;
    }

    public void setRadNr(int radNr) {
        this.radNr = radNr;
    }

    public int getSeteNr() {
        return seteNr;
    }

    public void setSeteNr(int seteNr) {
        this.seteNr = seteNr;
    }

    public int getKinosalNr() {
        return kinosalNr;
    }

    public void setKinosalNr(int kinosalNr) {
        this.kinosalNr = kinosalNr;
    }

    // ID Class
    public static class SammensattPlassBillettId implements Serializable {
        private String billettkode;
        private int radNr;
        private int seteNr;
        private int kinosalNr;

        public SammensattPlassBillettId() {}

        public SammensattPlassBillettId(String billettkode, int radNr, int seteNr, int kinosalNr) {
            this.billettkode = billettkode;
            this.radNr = radNr;
            this.seteNr = seteNr;
            this.kinosalNr = kinosalNr;
        }

        // equals and hashCode (viktig for sammensatte nøkler)
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SammensattPlassBillettId that)) return false;
            return radNr == that.radNr &&
                    seteNr == that.seteNr &&
                    kinosalNr == that.kinosalNr &&
                    billettkode.equals(that.billettkode);
        }

        @Override
        public int hashCode() {
            return billettkode.hashCode() + radNr + seteNr + kinosalNr;
        }
    }
}


