package org.example.kino.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "tblfilm")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "f_filmnr")
    private int filmnr;

    @Column(name = "f_filmnavn")
    private String filmnavn;

    public Film() {}

    public Film(int filmnr, String filmnavn) {
        this.filmnr = filmnr;
        this.filmnavn = filmnavn;
    }

    public int getFilmnr() {
        return filmnr;
    }

    public void setFilmnr(int filmnr) {
        this.filmnr = filmnr;
    }

    public String getFilmnavn() {
        return filmnavn;
    }

    public void setFilmnavn(String filmnavn) {
        this.filmnavn = filmnavn;
    }

    @Override
    public String toString() {
        return filmnavn;
    }
}
