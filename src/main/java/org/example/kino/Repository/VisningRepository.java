package org.example.kino.Repository;

import org.example.kino.Model.Visning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Enkel repo med ikke mye som trengtes akkurat her
 */

public interface VisningRepository extends JpaRepository<Visning, Integer> {
    List<Visning> findByFilmNr(int filmNr);

}

