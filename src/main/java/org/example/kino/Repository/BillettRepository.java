package org.example.kino.Repository;

import org.example.kino.Model.Billett;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillettRepository extends JpaRepository<Billett, Integer> {

    List<Billett> findByErBetalt(boolean erBetalt);


}