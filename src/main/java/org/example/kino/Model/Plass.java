package org.example.kino.Model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tblplass")
@IdClass(Plass.PlassId.class)
public class Plass {

    @Id
    @Column(name = "p_radnr")
    private int radNr;

    @Id
    @Column(name = "p_setenr")
    private int seteNr;

    @Id
    @Column(name = "p_kinosalnr")
    private int kinosalNr;

    // Constructors
    public Plass() {}

    public Plass(int radNr, int seteNr, int kinosalNr) {
        this.radNr = radNr;
        this.seteNr = seteNr;
        this.kinosalNr = kinosalNr;
    }

    // Getters and setters
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
    public static class PlassId implements Serializable {
        private int radNr;
        private int seteNr;
        private int kinosalNr;

        public PlassId() {}

        public PlassId(int radNr, int seteNr, int kinosalNr) {
            this.radNr = radNr;
            this.seteNr = seteNr;
            this.kinosalNr = kinosalNr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PlassId that)) return false;
            return radNr == that.radNr &&
                    seteNr == that.seteNr &&
                    kinosalNr == that.kinosalNr;
        }

        @Override
        public int hashCode() {
            return Objects.hash(radNr, seteNr, kinosalNr);
        }
    }
}
