package org.example.kino.Service;

import org.example.kino.Model.Billett;
import org.example.kino.Repository.BillettRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillettService {

    @Autowired
    private BillettRepository billettRepository;

    public List<Billett> hentAlleBilletter() {
        return billettRepository.findAll();
    }

    public Optional<Billett> hentBillett(String billettkode) {
        return billettRepository.findById(billettkode);
    }

    public Billett lagreBillett(Billett billett) {
        return billettRepository.save(billett);
    }

    public Optional<Billett> oppdaterBillett(String billettkode, Billett oppdatertBillett) {
        if (!billettRepository.existsById(billettkode)) {
            return Optional.empty();
        }
        oppdatertBillett.setBillettkode(billettkode);
        return Optional.of(billettRepository.save(oppdatertBillett));
    }

    public boolean slettBillett(String billettkode) {
        if (!billettRepository.existsById(billettkode)) {
            return false;
        }
        billettRepository.deleteById(billettkode);
        return true;
    }
}
