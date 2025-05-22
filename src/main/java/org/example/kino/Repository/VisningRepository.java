package org.example.kino.Repository;

import org.example.kino.Model.Visning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisningRepository extends JpaRepository<Visning, Integer> {
    List<Visning> findByFilmNr(int filmNr);
    // Ekstra spørringer kan defineres her ved behov
}

