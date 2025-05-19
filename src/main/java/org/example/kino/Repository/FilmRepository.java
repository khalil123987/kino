package org.example.kino.Repository;

import org.example.kino.Model.Film;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends CrudRepository<Film, Integer> {

    List<Film> findByTitle(String title);
    List<Film> findByYear(int year);
    List<Film> findByYearAndTitle(int year, String title);
    List<Film> findByTitleAndYear(String title, int year);

    boolean update(Film film);
}
