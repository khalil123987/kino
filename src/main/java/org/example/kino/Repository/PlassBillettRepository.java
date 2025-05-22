package org.example.kino.Repository;

import org.example.kino.Model.Plassbillett;
import org.example.kino.Model.Plassbillett.SammensattPlassBillettId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlassBillettRepository extends JpaRepository<Plassbillett, SammensattPlassBillettId> {

    List<Plassbillett> findByBillettkode(String billettkode);
}

