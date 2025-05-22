// Testet og implementert av Sahil
package org.example.kino.Repository;

import org.example.kino.Model.Plass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlassRepository extends JpaRepository<Plass, Plass.PlassId> {
    List<Plass> findByKinosalNr(int kinosalNr);
}
