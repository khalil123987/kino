package org.example.kino.Model;

import jakarta.persistence.*;
// Testet og godkjent av Zaurbek
/**
 * Dette er en entitet Billett som er utgangspunkt i både repository, service og
 * controller. Dette er det vi bruker i spring boot og disse bruker vi også for prosjektet. Prosjektet bruker
 * jpa i spring boot så vi får kommunisert med databasen.
 */
@Entity
@Table(name = "tblbillett")
public class Billett {

    @Id
    @Column(name = "b_billettkode")
    private String billettkode;

    @Column(name = "b_visningsnr")
    private Integer visningsnr;

    @Column(name = "b_erbetalt")
    private boolean erbetalt;

    public Billett() {}

    public String getBillettkode() {
        return billettkode;
    }

    public void setBillettkode(String billettkode) {
        this.billettkode = billettkode;
    }

    public int getVisningNr() {
        return visningsnr != null ? visningsnr : 0;
    }

    public void setVisningNr(int visningsnr) {
        this.visningsnr = visningsnr;
    }

    public boolean isErbetalt() {
        return erbetalt;
    }

    public void setErbetalt(boolean erbetalt) {
        this.erbetalt = erbetalt;
    }

    public boolean getErbetalt() {
        return false;
    }

    public void setVisningsnr(int visningsnr) {
        this.visningsnr = visningsnr;
    }

    public int getVisningsnr() {
        return visningsnr;
    }

    public void setErBetalt(boolean erBetalt) {
        this.erbetalt = erBetalt;
    }
}
