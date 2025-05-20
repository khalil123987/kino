package org.example.kino.Service;

import org.example.kino.Model.Film;
import org.example.kino.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> hentAllFilms() {
        return (List<Film>) filmRepository.findAll();
    }

    public Optional<Film> hentFilm(int id) {
        return filmRepository.findById(id);
    }

    public Film lagNyFilm(Film film) {
        return filmRepository.save(film);
    }

    public boolean oppdaterFilm(Film film) {
        filmRepository.update(film);
        return false;
    }

    public boolean deleteFilm(int id) {
        return filmRepository.delete(id);
    }

    public List<Film> hentAlleFilmer() {
        return List.of();
    }

    public Film hentFilmVedId(int id) {
        return null;
    }

    public boolean slettFilm(int id) {
        return false;
    }
}

