package org.example.kino.Repository;

import org.example.kino.Model.Plassbillett;
import org.example.kino.Model.Plassbillett.SammensattPlassBillettId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlassBillettRepository extends JpaRepository<Plassbillett, SammensattPlassBillettId> {
    // Ekstra spørringer kan defineres her ved behov
}

