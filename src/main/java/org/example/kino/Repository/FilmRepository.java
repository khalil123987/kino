package org.example.kino.Repository;

import org.example.kino.Model.Film;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends CrudRepository<Film, Integer> {
    // Du kan legge til spesialspørringer her senere om du ønsker
}
