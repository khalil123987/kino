package org.example.kino.Service;

import org.example.kino.Model.Film;
import org.example.kino.Repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    @Autowired
    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> hentAllFilms() {
        return filmRepository.findAll();
    }

    public Film hentFilm(int id) {
        return filmRepository.findById(id);
    }

    public Film lagNyFilm(Film film) {
        return filmRepository.save(film);
    }

    public boolean oppdaterFilm(Film film) {
        filmRepository.update(film);
    }

    public boolean deleteFilm(int id) {
        return filmRepository.delete(id);
    }
}

