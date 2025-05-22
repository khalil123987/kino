// Testet og implementert av Sahil
package org.example.kino.Model;

import jakarta.persistence.*;

@Entity // Angir at denne klassen er en JPA-entitet
@Table(name = "tblfilm") // Mapper klassen til tabellen "tblfilm" i databasen
public class Film {

    @Id // Primærnøkkel
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto generert verdi
    @Column(name = "f_filmnr") // Knytter feltet til kolonnen f_filmnr
    private int filmnr;

    @Column(name = "f_filmnavn") // Knytter feltet til kolonnen f_filmnavn
    private String filmnavn;

    public Film() {}

    public Film(int filmnr, String filmnavn) { // Konstruktør for enkel opprettelse av objekt
        this.filmnr = filmnr;
        this.filmnavn = filmnavn;
    }

    public int getFilmnr() { // Returnerer filmens ID
        return filmnr;
    }

    public void setFilmnr(int filmnr) { // Setter filmens ID
        this.filmnr = filmnr;
    }

    public String getFilmnavn() { // Returnerer filmens navn
        return filmnavn;
    }

    public void setFilmnavn(String filmnavn) { // Setter filmens navn
        this.filmnavn = filmnavn;
    }

    @Override
    public String toString() { // Returnerer filmens navn som streng
        return filmnavn;
    }
}
