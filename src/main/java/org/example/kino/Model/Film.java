package org.example.kino.Model;


public class Film {
    private int filmnr;
    private String filmnavn;

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
