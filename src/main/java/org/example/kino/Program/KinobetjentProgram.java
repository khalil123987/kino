// Implementert av Sahil og godkjent av hele gruppa

// Dette er kinobetjentprogrammet koden skal kunne gjøre følgene:

// Logge inn med brukernavn og pinkode (Fikk ikke tid til å implementere dette se mer på dokumentasjon )

// Kinobetjent skal kunne gjøre et direkte salg av altså han skal kunne selge billet og
// alt skal bli lagret i databasen med et randomisert billettkode

// Kinobetjent skal kunne selge billett til en eksisterende bestilling der kunden har billettkode
// Når betjenten oppgir billettkoden skal den bli solgt

// Kinobetjent skal kunne slette alle ubetalte bestillinger 30min før starttid og det
// skal videre loggføres i sletting.dat fil


// Package definerer hvilken pakke klassen tilhører
package org.example.kino.Program;

// Importer egne modeller og tjenester
import org.example.kino.Model.*;
import org.example.kino.Service.*;

// Spring-komponenter
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Java standardbiblioteker
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Her blir komponentene som brukes av Spring
@Component
public class KinobetjentProgram {

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

    // Dette er menysystemet for kinobetjent der han kan velge hvilke operasjon trenger å gjøre
    private final Scanner scanner = new Scanner(System.in);
    public void visKinobetjentMeny() {
        boolean fortsett = true;
        while (fortsett) {
            System.out.println("\n--- KINOBETJENT MENY ---");
            System.out.println("1: Direkte salg av billetter");
            System.out.println("2: Registrer billettkode (sett billett som betalt)");
            System.out.println("3: Slett ubetalte billetter for visninger innen 30 minutter");
            System.out.println("0: Logg ut / Avslutt");
            System.out.print("Velg et alternativ: ");
            String valg = scanner.nextLine();

            // Valget som styrer
            switch (valg) {
                case "1" -> direkteSalg();
                case "2" -> registrerBillettkode();
                case "3" -> slettUbetalteVisninger();
                case "0" -> {
                    fortsett = false;
                    System.out.println("Logget ut. Ha en fin dag!");
                }
                default -> System.out.println("Ugyldig valg. Prøv igjen.");
            }
        }
    }
    // Her blir alle visninger som ikke er betalt slettet hvis det er innen 30 min
    // Brukte ganske lang tid på dette så jeg måtte bruke KI
    public void slettUbetalteVisninger() {
        LocalDateTime nå = LocalDateTime.now();
        LocalDateTime grense = nå.plusMinutes(30);// Grense for hvilke visninger som skal sjekkes
        List<Visning> visninger = visningService.finnAlleVisninger();

        // Her åpner programmet en loggfil der slettede visninger blir loggført inni i en fil som heter slettinger.dat ved hjelp av KI
        // Siden det er append slipper bruker å lage en seperat slettinger fil siden den lager det automatisk
        try (PrintWriter logg = new PrintWriter(new FileWriter("slettinger.dat", true))) {
            int totaltSlettet = 0;
            for (Visning visning : visninger) {
                LocalDateTime visningsTidspunkt = visning.getTidspunkt();
                // Kun visninger som starter innen 30 min slik oppgaven spurte om
                if (!visningsTidspunkt.isAfter(grense)) {
                    List<Billett> ubetalteBilletter = billettService.finnAlleUbetalteBilletterForVisning(visning.getId());
                    for (Billett billett : ubetalteBilletter) {
                        // Billetten og plassbilletten blir sletta
                        plassBillettService.slettPlassBilletterForBillettkode(billett.getBillettkode());
                        billettService.slettBillett(billett.getBillettkode());
                        String tid = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
                        logg.println(tid + " - Slettet ubetalt billett: " + billett.getBillettkode() + ", Visning: " + visning.getId());
                        totaltSlettet++;
                    }
                }
            }
            System.out.println("Slettet totalt " + totaltSlettet + " ubetalte billetter for visninger innen 30 minutter.");
        } catch (IOException e) {
            System.out.println("Feil ved skriving til loggfil: " + e.getMessage());
        }
    }

    // Her er delen av kinobetjent sitt program der han registrer salg ved å oppgi billettkode
    private void registrerBillettkode() {
        System.out.print("Skriv inn billettkode: ");
        String kode = scanner.nextLine().trim().toUpperCase();
        Optional<Billett> billettOpt = billettService.hentBillett(kode);

        if (billettOpt.isEmpty()) {
            System.out.println("Billettkode ikke funnet.");
            return;
        }

        Billett billett = billettOpt.get();
        if (billett.getErbetalt()) {
            System.out.println("Denne billetten er allerede betalt.");
            return;
        }

        // Her blir billetten lagret som solgt i databasen etter at billettkoden er oppgitt
        billett.setErbetalt(true);
        billettService.lagreBillett(billett);
        System.out.println("Billett med kode " + kode + " er nå registrert som betalt.");
    }

    // Her generer det tidspunkt for visningen og det er koblet sammen med service for å forsikre god dataflyt
    private LocalDateTime hentVisningsTidspunkt(Visning visning) {
        int visningsId = visning.getId();
        LocalDateTime nå = LocalDateTime.now();
        return nå.plusDays(1).withHour(10 + (visningsId % 12)).withMinute((visningsId * 5) % 60);
    }

    // Her blir direkte salg av billetter gjennomført
    private void direkteSalg() {
        try {
            LocalDateTime nå = LocalDateTime.now();
            LocalDateTime grense = nå.plusMinutes(30);
            System.out.println("Nåværende tidspunkt: " + nå.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            System.out.println("Viser kun visninger som starter minst 30 minutter fra nå.");
            List<Visning> alleVisninger = visningService.finnAlleVisninger();
            Map<Integer, List<Visning>> visningerPerFilm = new HashMap<>();

            // Her blir visninger gruppert per film hvis de er etter 30min grensa
            for (Visning visning : alleVisninger) {
                LocalDateTime visningsTidspunkt = hentVisningsTidspunkt(visning);
                if (visningsTidspunkt.isAfter(grense)) {
                    visningerPerFilm.computeIfAbsent(visning.getFilmNr(), k -> new ArrayList<>()).add(visning);
                }
            }

            if (visningerPerFilm.isEmpty()) {
                System.out.println("Ingen tilgjengelige visninger funnet.");
                return;
            }

            // Henter filmer med tilgjengelige visninger
            List<Film> filmerMedVisninger = new ArrayList<>();
            int teller = 1;
            System.out.println("\n--- TILGJENGELIGE FILMER ---");
            for (Integer filmNr : visningerPerFilm.keySet()) {
                Optional<Film> filmOpt = filmService.hentFilmVedId(filmNr);
                if (filmOpt.isPresent()) {
                    Film film = filmOpt.get();
                    filmerMedVisninger.add(film);
                    System.out.println(teller + ". " + film.getFilmnavn());
                    teller++;
                }
            }

            // Dette gir brukeren filmvalg ved direkte salg av billett
            System.out.print("\nVelg filmnummer (0 for å avbryte): ");
            int valgtFilmNr = Integer.parseInt(scanner.nextLine());
            if (valgtFilmNr == 0) return;
            if (valgtFilmNr < 1 || valgtFilmNr > filmerMedVisninger.size()) {
                System.out.println("Ugyldig valg.");
                return;
            }

            Film valgtFilm = filmerMedVisninger.get(valgtFilmNr - 1);
            List<Visning> filmensVisninger = visningerPerFilm.get(valgtFilm.getFilmnr());
            teller = 1;
            // Her vises tilgjengelige visninger for valgt film med hjelp av KI
            System.out.println("\n--- VISNINGER FOR " + valgtFilm.getFilmnavn() + " ---");
            for (Visning visning : filmensVisninger) {
                Optional<Kinosal> kinosalOpt = kinosalService.finnKinosalMedId(visning.getKinosalNr());
                String kinosalNavn = kinosalOpt.map(Kinosal::getKinosalNavn).orElse("Ukjent sal");
                LocalDateTime visningsTidspunkt = hentVisningsTidspunkt(visning);
                System.out.printf("%d. Sal: %s, Tid: %s, Pris: %.2f kr\n",
                        teller,
                        kinosalNavn,
                        visningsTidspunkt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                        visning.getPris());
                teller++;
            }

            // visningsvalg
            System.out.print("\nVelg visningsnummer (0 for å avbryte): ");
            int valgtVisningNr = Integer.parseInt(scanner.nextLine());
            if (valgtVisningNr == 0) return;
            if (valgtVisningNr < 1 || valgtVisningNr > filmensVisninger.size()) {
                System.out.println("Ugyldig valg.");
                return;
            }

            Visning valgtVisning = filmensVisninger.get(valgtVisningNr - 1);
            // Brukeren får velge plass
            List<Plass> valgtePlasser = velgLedigePlasser(valgtVisning);
            if (valgtePlasser.isEmpty()) {
                System.out.println("Ingen plasser valgt. Avbryter salg.");
                return;
            }

            // Her oppretter og lagrer programmet billetten
            String billettkode = genererBillettkode();
            Billett billett = new Billett();
            billett.setBillettkode(billettkode);
            billett.setVisningsnr(valgtVisning.getId());
            billett.setErbetalt(true);
            billettService.lagreBillett(billett);

            // Her blir valgte plasser lagret
            for (Plass plass : valgtePlasser) {
                Plassbillett pb = new Plassbillett(
                        billettkode,
                        plass.getRadNr(),
                        plass.getSeteNr(),
                        plass.getKinosalNr());
                plassBillettService.lagrePlassBillett(pb);
            }

            // Dette er kjøpskvitering over visning sammen med tid set osv
            double totalPris = valgtePlasser.size() * valgtVisning.getPris();
            System.out.println("\n--- KJØPSKVITTERING ---");
            System.out.println("Film: " + valgtFilm.getFilmnavn());
            System.out.println("Visning: " + hentVisningsTidspunkt(valgtVisning).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            System.out.println("\nValgte plasser:");
            for (Plass plass : valgtePlasser) {
                System.out.println("Rad " + plass.getRadNr() + ", Sete " + plass.getSeteNr());
            }
            System.out.println("\nAntall billetter: " + valgtePlasser.size());
            System.out.println("Total pris: " + totalPris + " kr");
            System.out.println("Billettkode: " + billettkode);
            System.out.println("Status: Betalt (direkte kjøp)");
            System.out.println("\nTakk for kjøpet! Velkommen til kino.");
        } catch (NumberFormatException e) {
            System.out.println("Ugyldig input. Vennligst skriv inn et tall.");
        }
        System.out.println("\nTrykk Enter for å gå tilbake...");
        scanner.nextLine();
    }
    // Metode for å velge ledige plasser for en visning her fikk jeg hjelp av KI
    private List<Plass> velgLedigePlasser(Visning visning) {
        List<Plass> valgtePlasser = new ArrayList<>();
        List<Plass> allePlasserISal = plassService.hentPlasserIKinosal(visning.getKinosalNr());
        List<Plassbillett> allePlassbilletter = plassBillettService.finnAllePlassBilletter();

        Set<String> opptattePlasser = new HashSet<>();
        // Her blir optatte plasser filtrert ut slik at man ikke velger et sete som allerede er tatt
        for (Plassbillett pb : allePlassbilletter) {
            Optional<Billett> billettOpt = billettService.hentBillett(pb.getBillettkode());
            if (billettOpt.isPresent() && billettOpt.get().getVisningsnr() == visning.getId()) {
                opptattePlasser.add(pb.getRadNr() + "-" + pb.getSeteNr());
            }
        }

        System.out.println("\nLedige plasser (Rad-Sete):");
        // Viser ledige plasser på en strukturert måte
        for (Plass plass : allePlasserISal) {
            String key = plass.getRadNr() + "-" + plass.getSeteNr();
            if (!opptattePlasser.contains(key)) {
                System.out.print("[" + key + "] ");
            }
        }

        System.out.println();
        System.out.print("Skriv inn ønsket plass som Rad-Sete (f.eks. 3-5), eller 0 for å avbryte valg: ");
        String input = scanner.nextLine();
        if ("0".equals(input)) return Collections.emptyList();

        String[] deler = input.split("-");
        if (deler.length != 2) {
            System.out.println("Ugyldig format. Må være Rad-Sete, f.eks. 3-5.");
            return Collections.emptyList();
        }

        try {
            int rad = Integer.parseInt(deler[0].trim());
            int sete = Integer.parseInt(deler[1].trim());
            String key = rad + "-" + sete;
            if (!opptattePlasser.contains(key)) {
                Optional<Plass> plassOpt = allePlasserISal.stream()
                        .filter(p -> p.getRadNr() == rad && p.getSeteNr() == sete)
                        .findFirst();
                plassOpt.ifPresent(valgtePlasser::add);
            } else {
                System.out.println("Plassen er allerede opptatt.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ugyldig rad- eller setenummer.");
        }

        return valgtePlasser;
    }
    // Her generer programmet en tilfeldig billettkode med UUID som ble anbefalt fra selve oppgaven
    private String genererBillettkode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

