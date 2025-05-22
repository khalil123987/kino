package org.example.kino.Service;

import org.example.kino.Model.Plassbillett;
import org.example.kino.Repository.PlassBillettRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlassBillettService {

    @Autowired
    private PlassBillettRepository plassBillettRepository;

    public List<Plassbillett> finnAllePlassBilletter() {
        return plassBillettRepository.findAll();
    }

    public Optional<Plassbillett> finnMedId(Plassbillett.SammensattPlassBillettId id) {
        return plassBillettRepository.findById(id);
    }

    public Plassbillett lagrePlassBillett(Plassbillett plassBillett) {
        return plassBillettRepository.save(plassBillett);
    }
    public List<Plassbillett> finnPlassbilletterForBillettkode(String billettkode) {
        return plassBillettRepository.findByBillettkode(billettkode);
    }

    public void slettPlassBilletterForBillettkode(String billettkode) {
        List<Plassbillett> plassbilletter = finnPlassbilletterForBillettkode(billettkode);
        plassBillettRepository.deleteAll(plassbilletter);
    }

    public void slettPlassBillett(Plassbillett.SammensattPlassBillettId id) {
        plassBillettRepository.deleteById(id);
    }
}
