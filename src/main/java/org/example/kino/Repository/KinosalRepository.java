// godkjent av Khalil

package org.example.kino.Repository;

import org.example.kino.Model.Kinosal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KinosalRepository extends JpaRepository<Kinosal, Integer> {

    // Finn kinosaler basert på kinonavn
    List<Kinosal> findByKinoNavn(String kinoNavn);

    // Finn kinosal basert på kinonavn og kinosalnavn
    Kinosal findByKinoNavnAndKinosalNavn(String kinoNavn, String kinosalNavn);

    // Finn kinosaler hvor navnet inneholder en bestemt tekst (case-insensitive)
    List<Kinosal> findByKinosalNavnContainingIgnoreCase(String navnDel);

    // Telle antall kinosaler per kino
    Long countByKinoNavn(String kinoNavn);
}
