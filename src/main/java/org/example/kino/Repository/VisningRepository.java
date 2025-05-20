package org.example.kino.Repository;

import org.example.kino.Model.Visning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisningRepository extends JpaRepository<Visning, Integer> {
    // Ekstra spørringer kan defineres her ved behov
}

