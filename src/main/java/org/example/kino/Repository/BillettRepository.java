//testet av zaurbek og godkjent at funker

/**
 * Her sørger repository at henting og lagring av entitet / verdier funker
 * i programmet.
 */
package org.example.kino.Repository;

import org.example.kino.Model.Billett;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BillettRepository extends JpaRepository<Billett, String> {

    long countByErbetaltFalse();
    long countByErbetaltTrue();
    long countByErbetalt(boolean erBetalt);
    long countByVisningsnr(int visningsnr);

    List<Billett> findByVisningsnr(int visningsnr);
    List<Billett> findByVisningsnrAndErbetaltFalse(int visningsnr);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM kino.tblbillett
        WHERE b_visningsnr IN (
            SELECT v.v_visningnr
            FROM kino.tblvisning v
            WHERE (v.v_dato + v.v_starttid) <= CURRENT_TIMESTAMP - INTERVAL '1 hour' * ?1
        )
    """, nativeQuery = true)
    int slettAlleBilletterForVisningerEldreEnnXTimer(int timer);
}
