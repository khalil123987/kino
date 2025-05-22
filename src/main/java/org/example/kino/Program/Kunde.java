package org.example.kino.Program;

import org.example.kino.Model.*;
import org.example.kino.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class Kunde {
    @Autowired
    private VisningService visningService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private KinosalService kinosalService;

    @Autowired
    private BillettService billettService;

    @Autowired
    private PlassBillettService plassBillettService;

    @Autowired
    private PlassService plassService;

    private Scanner scanner = new Scanner(System.in);


    public void visKundemeny() {
        boolean fortsett = true;
        while (fortsett) {
            System.out.println("\n--- KUNDE MENY ---");
            System.out.println("1: Se tilgjengelige filmer og bestill billett");
            System.out.println("2: Sjekk eller slett bestilling");
            System.out.println("0: Tilbake til hovedmeny");
            System.out.print("Velg et alternativ: ");
            String valg = scanner.nextLine();
            switch (valg) {
                case "1":
                    bestillBillett();
                    break;
                case "2":
                    sjekkEllerSlettBestilling();
                    break;
                case "0":
                    fortsett = false;
                    break;
                default:
                    System.out.println("Ugyldig valg. Prøv igjen.");
            }
        }
    }

    // Metode 1: Se tilgjengelige filmer og bestille billett
    private void bestillBillett() {
        // Steg 1: Vis kommende filmer og visninger
        List<Visning> alleVisninger = visningService.finnAlleVisninger();
        System.out.println("Hentet " + alleVisninger.size() + " visninger fra databasen");
        Map<Integer, List<Visning>> visningerPerFilm = new HashMap<>();

        // Grenseverdi - kun visninger minst 30 minutter fram i tid
        LocalDateTime grense = LocalDateTime.now().plusMinutes(30);

        // Grupper visninger etter filmnr - MED 30-MIN SJEKK
        for (Visning visning : alleVisninger) {
            if (visning.getTidspunkt().isAfter(grense)) {
                if (!visningerPerFilm.containsKey(visning.getFilmNr())) {
                    visningerPerFilm.put(visning.getFilmNr(), new ArrayList<>());
                }
                visningerPerFilm.get(visning.getFilmNr()).add(visning);
            }
        }

        // Hvis ingen kommende visninger
        if (visningerPerFilm.isEmpty()) {
            System.out.println("Ingen kommende visninger funnet.");
            System.out.println("\nTrykk Enter for å gå tilbake...");
            scanner.nextLine();
            return;
        }

        // Vis tilgjengelige filmer
        System.out.println("\n--- TILGJENGELIGE FILMER ---");
        List<Film> filmerMedVisninger = new ArrayList<>();
        int teller = 1;
        for (Integer filmNr : visningerPerFilm.keySet()) {
            Optional<Film> filmOpt = filmService.hentFilmVedId(filmNr);
            if (filmOpt.isPresent()) {
                Film film = filmOpt.get();
                filmerMedVisninger.add(film);
                System.out.println(teller + ". " + film.getFilmnavn());
                teller++;
            }
        }

        // La brukeren velge film
        System.out.print("\nVelg filmnummer (0 for å avbryte): ");
        String valg = scanner.nextLine();
        try {
            int valgtNr = Integer.parseInt(valg);
            if (valgtNr == 0) {
                return;
            }
            if (valgtNr < 1 || valgtNr > filmerMedVisninger.size()) {
                System.out.println("Ugyldig valg.");
                return;
            }

            Film valgtFilm = filmerMedVisninger.get(valgtNr - 1);

            // Vis visninger for valgt film
            System.out.println("\n--- VISNINGER FOR " + valgtFilm.getFilmnavn() + " ---");
            List<Visning> filmensVisninger = visningerPerFilm.get(valgtFilm.getFilmnr());
            teller = 1;
            for (Visning visning : filmensVisninger) {
                Optional<Kinosal> kinosalOpt = kinosalService.finnKinosalMedId(visning.getKinosalNr());
                String kinosalNavn = kinosalOpt.isPresent() ? kinosalOpt.get().getKinosalNavn() : "Ukjent sal";
                try {
                    System.out.printf("%d. Sal: %s, Tid: %s, Pris: %.2f kr\n",
                            teller,
                            kinosalNavn,
                            visning.getTidspunkt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                            visning.getPris());
                } catch (Exception e) {
                    System.out.printf("%d. Sal: %s, Tid: [FEIL VED FORMATERING], Pris: %.2f kr\n",
                            teller,
                            kinosalNavn,
                            visning.getPris());
                }
                teller++;
            }

            // La brukeren velge visning
            System.out.print("\nVelg visningsnummer (0 for å avbryte): ");
            valg = scanner.nextLine();
            valgtNr = Integer.parseInt(valg);
            if (valgtNr == 0) {
                return;
            }
            if (valgtNr < 1 || valgtNr > filmensVisninger.size()) {
                System.out.println("Ugyldig valg.");
                return;
            }

            Visning valgtVisning = filmensVisninger.get(valgtNr - 1);

            // Vis ledige plasser og la brukeren velge
            List<Plass> valgtePlasser = velgLedigePlasser(valgtVisning);
            if (valgtePlasser.isEmpty()) {
                System.out.println("Ingen plasser valgt. Avbryter bestilling.");
                System.out.println("\nTrykk Enter for å gå tilbake...");
                scanner.nextLine();
                return;
            }

            // Generer billettkode og lagre bestillingen
            String billettkode = genererBillettkode();

            // Opprett billett
            Billett billett = new Billett();
            billett.setBillettkode(billettkode);
            billett.setVisningNr(valgtVisning.getId());
            billett.setErBetalt(false);
            billettService.lagreBillett(billett);

            // Opprett plassbilletter for alle valgte seter
            for (Plass plass : valgtePlasser) {
                Plassbillett plassbillett = new Plassbillett(
                        billettkode,
                        plass.getRadNr(),
                        plass.getSeteNr(),
                        plass.getKinosalNr()
                );
                plassBillettService.lagrePlassBillett(plassbillett);
            }

            double totalPris = valgtePlasser.size() * valgtVisning.getPris();

            // Vis bestillingsbekreftelse
            System.out.println("\n--- BESTILLINGSBEKREFTELSE ---");
            System.out.println("Film: " + valgtFilm.getFilmnavn());
            try {
                System.out.println("Visning: " + valgtVisning.getTidspunkt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            } catch (Exception e) {
                System.out.println("Visning: [Dato/tid-formatering feilet]");
            }

            System.out.println("\nValgte plasser:");
            for (Plass plass : valgtePlasser) {
                System.out.println("Rad " + plass.getRadNr() + ", Sete " + plass.getSeteNr());
            }

            System.out.println("\nAntall billetter: " + valgtePlasser.size());
            System.out.println("Totalpris: " + totalPris + " kr");
            System.out.println("\nDin billettkode: " + billettkode);
            System.out.println("\nVIKTIG: Billettene må hentes og betales senest 30 minutter før forestillingen starter.");
        } catch (NumberFormatException e) {
            System.out.println("Ugyldig input. Skriv inn et tall.");
        }

        System.out.println("\nTrykk Enter for å gå tilbake...");
        scanner.nextLine();
    }

    // Forenklet metode for å vise og velge ledige plasser
    private List<Plass> velgLedigePlasser(Visning visning) {
        List<Plass> valgtePlasser = new ArrayList<>();

        // Hent alle plasser i kinosalen
        List<Plass> allePlasserISal = plassService.hentPlasserIKinosal(visning.getKinosalNr());

        // Finn opptatte plasser for denne visningen
        List<Plassbillett> allePlassbilletter = plassBillettService.finnAllePlassBilletter();
        Set<String> opptattePlasser = new HashSet<>();

        for (Plassbillett pb : allePlassbilletter) {
            Optional<Billett> billettOpt = billettService.hentBillett(pb.getBillettkode());
            if (billettOpt.isPresent() && billettOpt.get().getVisningNr() == visning.getId()) {
                String plassNøkkel = pb.getRadNr() + "," + pb.getSeteNr();
                opptattePlasser.add(plassNøkkel);
            }
        }

        // Lag liste over ledige plasser
        List<Plass> ledigePlasser = new ArrayList<>();
        for (Plass plass : allePlasserISal) {
            String plassNøkkel = plass.getRadNr() + "," + plass.getSeteNr();
            if (!opptattePlasser.contains(plassNøkkel)) {
                ledigePlasser.add(plass);
            }
        }

        // Sorter ledige plasser for bedre lesbarhet (rad, deretter sete)
        ledigePlasser.sort(Comparator.comparing(Plass::getRadNr).thenComparing(Plass::getSeteNr));

        boolean ferdig = false;
        while (!ferdig) {
            // Vis ledige plasser
            System.out.println("\n--- LEDIGE PLASSER ---");
            if (ledigePlasser.isEmpty()) {
                System.out.println("Ingen ledige plasser for denne visningen.");
                return valgtePlasser;
            }

            int antallPerRad = 5; // Vis 5 plasser per rad i utskriften
            for (int i = 0; i < ledigePlasser.size(); i++) {
                Plass plass = ledigePlasser.get(i);
                // Sjekk om plassen allerede er valgt
                boolean erValgt = valgtePlasser.contains(plass);
                System.out.printf("%3d. Rad %-2d, Sete %-2d %s",
                        i + 1,
                        plass.getRadNr(),
                        plass.getSeteNr(),
                        erValgt ? "[VALGT]" : "");

                // Linjeskift etter hver 5. plass
                if ((i + 1) % antallPerRad == 0 || i == ledigePlasser.size() - 1) {
                    System.out.println();
                } else {
                    System.out.print(" | ");
                }
            }

            // Vis status og meny
            System.out.println("\nValgte plasser: " + valgtePlasser.size());
            System.out.println("Totalpris: " + (valgtePlasser.size() * visning.getPris()) + " kr");
            System.out.println("\n1: Velg en plass");
            System.out.println("2: Fjern valgt plass");
            System.out.println("3: Fullfør bestilling");
            System.out.println("0: Avbryt bestilling");
            System.out.print("Velg et alternativ: ");

            String valg = scanner.nextLine();
            switch (valg) {
                case "1":
                    System.out.print("Oppgi plassnummer: ");
                    try {
                        int plassIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (plassIndex < 0 || plassIndex >= ledigePlasser.size()) {
                            System.out.println("Ugyldig plassnummer.");
                        } else {
                            Plass valgtPlass = ledigePlasser.get(plassIndex);
                            // Sjekk om plassen allerede er valgt
                            if (valgtePlasser.contains(valgtPlass)) {
                                System.out.println("Denne plassen er allerede valgt.");
                            } else {
                                valgtePlasser.add(valgtPlass);
                                System.out.println("Plass lagt til.");
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Ugyldig nummer.");
                    }
                    break;
                case "2":
                    if (valgtePlasser.isEmpty()) {
                        System.out.println("Ingen plasser er valgt.");
                    } else {
                        System.out.println("\nValgte plasser:");
                        for (int i = 0; i < valgtePlasser.size(); i++) {
                            Plass plass = valgtePlasser.get(i);
                            System.out.printf("%d. Rad %d, Sete %d\n",
                                    i + 1, plass.getRadNr(), plass.getSeteNr());
                        }
                        System.out.print("Oppgi nummer på plass å fjerne (0 for å avbryte): ");
                        try {
                            int index = Integer.parseInt(scanner.nextLine()) - 1;
                            if (index == -1) {
                                // Avbryt fjerning
                            } else if (index < 0 || index >= valgtePlasser.size()) {
                                System.out.println("Ugyldig nummer.");
                            } else {
                                Plass fjernetPlass = valgtePlasser.remove(index);
                                System.out.println("Plass fjernet: Rad " + fjernetPlass.getRadNr() +
                                        ", Sete " + fjernetPlass.getSeteNr());
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Ugyldig nummer.");
                        }
                    }
                    break;
                case "3":
                    if (valgtePlasser.isEmpty()) {
                        System.out.println("Du må velge minst én plass for å fullføre bestillingen.");
                    } else {
                        ferdig = true;
                    }
                    break;
                case "0":
                    return new ArrayList<>(); // Tom liste indikerer avbrutt bestilling
                default:
                    System.out.println("Ugyldig valg. Prøv igjen.");
            }
        }

        return valgtePlasser;
    }

    // Metode 2: Sjekk eller slett bestilling
    private void sjekkEllerSlettBestilling() {
        System.out.println("\n--- SJEKK ELLER SLETT BESTILLING ---");
        System.out.print("Oppgi billettkode: ");
        String billettkode = scanner.nextLine().trim();

        Optional<Billett> billettOpt = billettService.hentBillett(billettkode);
        if (!billettOpt.isPresent()) {
            System.out.println("Fant ingen bestilling med denne koden.");
            System.out.println("\nTrykk Enter for å gå tilbake...");
            scanner.nextLine();
            return;
        }

        Billett billett = billettOpt.get();
        Optional<Visning> visningOpt = visningService.finnVisningMedId(billett.getVisningNr());
        if (!visningOpt.isPresent()) {
            System.out.println("Feil: Fant ikke visningsinformasjon for billetten.");
            System.out.println("\nTrykk Enter for å gå tilbake...");
            scanner.nextLine();
            return;
        }

        Visning visning = visningOpt.get();
        Optional<Film> filmOpt = filmService.hentFilmVedId(visning.getFilmNr());
        String filmNavn = filmOpt.isPresent() ? filmOpt.get().getFilmnavn() : "Ukjent film";

        Optional<Kinosal> kinosalOpt = kinosalService.finnKinosalMedId(visning.getKinosalNr());
        String kinosalNavn = kinosalOpt.isPresent() ? kinosalOpt.get().getKinosalNavn() : "Ukjent sal";

        // Finn plassene for denne billetten
        List<Plassbillett> billettensPlasser = new ArrayList<>();
        List<Plassbillett> allePlassbilletter = plassBillettService.finnAllePlassBilletter();
        for (Plassbillett pb : allePlassbilletter) {
            if (pb.getBillettkode().equals(billettkode)) {
                billettensPlasser.add(pb);
            }
        }

        // Vis bestillingsinformasjon
        System.out.println("\n--- BESTILLINGSINFORMASJON ---");
        System.out.println("Film: " + filmNavn);
        System.out.println("Kinosal: " + kinosalNavn);
        try {
            System.out.println("Tidspunkt: " + visning.getTidspunkt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        } catch (Exception e) {
            System.out.println("Tidspunkt: [Dato/tid-formatering feilet]");
        }
        System.out.println("Status: " + (billett.isErbetalt() ? "Betalt" : "Ikke betalt"));

        // Vis plassene
        System.out.println("\nPlasser:");
        for (Plassbillett pb : billettensPlasser) {
            System.out.println("Rad " + pb.getRadNr() + ", Sete " + pb.getSeteNr());
        }

        // Sjekk om visningen er minst 30 minutter fram i tid
        LocalDateTime grense = LocalDateTime.now().plusMinutes(30);
        boolean kanSlettes = visning.getTidspunkt().isAfter(grense);

        if (kanSlettes) {
            System.out.println("\nVil du slette denne bestillingen? (J/N): ");
            String valg = scanner.nextLine().trim().toUpperCase();

            if (valg.equals("J")) {
                // Slett plassbilletter først
                for (Plassbillett pb : billettensPlasser) {
                    try {
                        Plassbillett.SammensattPlassBillettId id = new Plassbillett.SammensattPlassBillettId(
                                billettkode, pb.getRadNr(), pb.getSeteNr(), pb.getKinosalNr());
                        plassBillettService.slettPlassBillett(id);
                    } catch (Exception e) {
                        System.out.println("Feil ved sletting av plassbillett: " + e.getMessage());
                    }
                }

                // Slett billett
                billettService.slettBillett(billettkode);
                System.out.println("Bestillingen er slettet.");
            } else {
                System.out.println("Bestillingen er ikke slettet.");
            }
        } else {
            System.out.println("\nBestillingen kan ikke slettes fordi det er mindre enn 30 minutter til visningen starter.");
        }

        System.out.println("\nTrykk Enter for å gå tilbake...");
        scanner.nextLine();
    }

    // Hjelpemetode for å generere billettkode
    private String genererBillettkode() {
        String tegn = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder billettkode = new StringBuilder();
        Random random = new Random();
        boolean unik = false;

        while (!unik) {
            billettkode.setLength(0);
            for (int i = 0; i < 6; i++) {
                billettkode.append(tegn.charAt(random.nextInt(tegn.length())));
            }

            // Sjekk om billettkoden allerede eksisterer
            unik = !billettService.hentBillett(billettkode.toString()).isPresent();
        }

        return billettkode.toString();
    }
}