// testet i postman for å sjekka om alt funker både i IDE av Zaurbek

/**
 * Controller er utløseren for funksjonene og spørringer for at når
 * brukeren velger funksjon så gir det et resultat
 * controller sørger for at henting, lagring, endring, putting fungerer.
 */

package org.example.kino.Controller;

import org.example.kino.Model.Billett;
import org.example.kino.Service.BillettService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/billetter")
public class BillettController {

    @Autowired
    private BillettService billettService;

    @GetMapping
    public List<Billett> getAlleBilletter() {
        return billettService.hentAlleBilletter();
    }

    @GetMapping("/{billettkode}")
    public ResponseEntity<Billett> hentBillett(@PathVariable String billettkode) {
        Optional<Billett> billett = billettService.hentBillett(billettkode);
        return billett.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Billett lagreBillett(@RequestBody Billett billett) {
        return billettService.lagreBillett(billett);
    }

    @PutMapping("/{billettkode}")
    public ResponseEntity<Billett> oppdaterBillett(@PathVariable String billettkode, @RequestBody Billett oppdatertBillett) {
        Optional<Billett> oppdatert = billettService.oppdaterBillett(billettkode, oppdatertBillett);
        return oppdatert.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{billettkode}")
    public ResponseEntity<Void> slettBillett(@PathVariable String billettkode) {
        boolean slettet = billettService.slettBillett(billettkode);
        return slettet ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
