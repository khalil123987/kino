package org.example.kino.Repository;

import org.example.kino.Model.Billett;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillettRepository extends JpaRepository<Billett, String> {
}
