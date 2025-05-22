//Testet og godkjent av Khalil, videreutviklet av alle gruppemedlemmer

package org.example.kino.Program;

import org.example.kino.Model.*;
import org.example.kino.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Kunde-klasse som håndterer kundeinteraksjon for kinosystemet.
 * Implementerer funksjonalitet for billettbestilling og bestillingsadministrasjon.
 *
 * Klassen håndterer kompleks forretningslogikk inkludert:
 * - 30-minutters regel for bestillinger og kanselleringer
 * - Multiple plassvalg med sanntidsoppdatering
 * - Database-transaksjoner for billetter og plassbilletter
 *
 *
 */
@Component
public class Kunde {

    // Dependency injection av alle nødvendige tjenester
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

    /**
     * Hovedmeny for kunder med navigasjonsløkke.
     * Bruker switch-case for menyvalg og input-validering.
     */
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

    /**
     * Komplett billettbestillingsprosess med trinnvis brukernavigasjon.
     *
     * Prosessflyt:
     * 1. Henter og filtrerer visninger basert på 30-min regel
     * 2. Grupperer visninger per film for bedre organisering
     * 3. Lar bruker velge film og spesifikk visning
     * 4. Kaller plassvelger for setevalg
     * 5. Genererer billettkode og lagrer i database
     * 6. Viser bekreftelse med alle detaljer
     *
     * Forretningsregel: Kun visninger som starter om minst 30 minutter vises.
     *
     * Note: Datagruppering og filtreringslogikk utviklet med AI-assistanse (ChatGPT/Claude)
     * for å optimalisere ytelse og brukeropplevelse. Vi forstår logikken og har tilpasset den
     * til våre spesifikke behov.
     */
    private void bestillBillett() {
        // Hent alle visninger og initialiser datastrukturer
        List<Visning> alleVisninger = visningService.finnAlleVisninger();
        System.out.println("Hentet " + alleVisninger.size() + " visninger fra databasen");
        Map<Integer, List<Visning>> visningerPerFilm = new HashMap<>();

        // Implementer 30-minutters forretningsregel
        LocalDateTime grense = LocalDateTime.now().plusMinutes(30);

        // Gruppér gyldige visninger etter film (Map-basert gruppering)
        for (Visning visning : alleVisninger) {
            if (visning.getTidspunkt().isAfter(grense)) {
                visningerPerFilm.computeIfAbsent(visning.getFilmNr(), k -> new ArrayList<>()).add(visning);
            }
        }

        // Validering: Sjekk om det finnes tilgjengelige visninger
        if (visningerPerFilm.isEmpty()) {
            System.out.println("Ingen kommende visninger funnet.");
            System.out.println("\nTrykk Enter for å gå tilbake...");
            scanner.nextLine();
            return;
        }

        // Presentér tilgjengelige filmer med nummerert liste
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

        // Brukerinteraksjon: Filmvalg med input-validering
        System.out.print("\nVelg filmnummer (0 for å avbryte): ");
        String valg = scanner.nextLine();
        try {
            int valgtNr = Integer.parseInt(valg);
            if (valgtNr == 0) return;
            if (valgtNr < 1 || valgtNr > filmerMedVisninger.size()) {
                System.out.println("Ugyldig valg.");
                return;
            }

            Film valgtFilm = filmerMedVisninger.get(valgtNr - 1);

            // Vis detaljerte visningsalternativer for valgt film
            System.out.println("\n--- VISNINGER FOR " + valgtFilm.getFilmnavn() + " ---");
            List<Visning> filmensVisninger = visningerPerFilm.get(valgtFilm.getFilmnr());
            teller = 1;

            for (Visning visning : filmensVisninger) {
                Optional<Kinosal> kinosalOpt = kinosalService.finnKinosalMedId(visning.getKinosalNr());
                String kinosalNavn = kinosalOpt.isPresent() ? kinosalOpt.get().getKinosalNavn() : "Ukjent sal";

                // datoformatering med exception-håndtering
                try {
                    System.out.printf("%d. Sal: %s, Tid: %s, Pris: %.2f kr\n",
                            teller, kinosalNavn,
                            visning.getTidspunkt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                            visning.getPris());
                } catch (Exception e) {
                    System.out.printf("%d. Sal: %s, Tid: [FORMATERING FEILET], Pris: %.2f kr\n",
                            teller, kinosalNavn, visning.getPris());
                }
                teller++;
            }

            // Visningsvalg med validering
            System.out.print("\nVelg visningsnummer (0 for å avbryte): ");
            valg = scanner.nextLine();
            valgtNr = Integer.parseInt(valg);
            if (valgtNr == 0) return;
            if (valgtNr < 1 || valgtNr > filmensVisninger.size()) {
                System.out.println("Ugyldig valg.");
                return;
            }

            Visning valgtVisning = filmensVisninger.get(valgtNr - 1);

            // Avansert plassvalg - delegerer til spesialisert metode
            List<Plass> valgtePlasser = velgLedigePlasser(valgtVisning);
            if (valgtePlasser.isEmpty()) {
                System.out.println("Ingen plasser valgt. Avbryter bestilling.");
                System.out.println("\nTrykk Enter for å gå tilbake...");
                scanner.nextLine();
                return;
            }

            // Database-transaksjoner: Opprett billett og tilhørende plassbilletter
            String billettkode = genererBillettkode();

            // Hovedbillett (parent record)
            Billett billett = new Billett();
            billett.setBillettkode(billettkode);
            billett.setVisningNr(valgtVisning.getId());
            billett.setErBetalt(false);
            billettService.lagreBillett(billett);

            // Plassbilletter (child records) - én per valgt sete
            for (Plass plass : valgtePlasser) {
                Plassbillett plassbillett = new Plassbillett(
                        billettkode, plass.getRadNr(), plass.getSeteNr(), plass.getKinosalNr());
                plassBillettService.lagrePlassBillett(plassbillett);
            }

            // Beregninger og bekreftelsesinformasjon
            double totalPris = valgtePlasser.size() * valgtVisning.getPris();

            // Komplett bestillingsbekreftelse
            System.out.println("\n--- BESTILLINGSBEKREFTELSE ---");
            System.out.println("Film: " + valgtFilm.getFilmnavn());
            try {
                System.out.println("Visning: " + valgtVisning.getTidspunkt()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            } catch (Exception e) {
                System.out.println("Visning: [Dato/tid-formatering feilet]");
            }

            System.out.println("\nValgte plasser:");
            valgtePlasser.forEach(plass ->
                    System.out.println("Rad " + plass.getRadNr() + ", Sete " + plass.getSeteNr()));

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

    /**
     * Avansert interaktiv plassvelger med sanntids status-oppdatering.
     *
     *   Funksjoner:
     * - Identifikasjon av ledige vs opptatte plasser per visning
     * - Visuell presentasjon i tabellformat
     * - Multiple plassvalg med add/remove funksjonalitet
     * - Sanntids prisberegning og status-visning
     * - Konfirmasjon før fullføring
     *
     * @param visning Visningen det skal velges plasser til
     * @return Liste over valgte plasser, tom liste hvis prosessen avbrytes
     *
     * Note: Avansert brukergrensesnitt-logikk og algoritmer for plassadministrasjon
     * utviklet med AI-assistanse (ChatGPT og Claude). Vi har fullstendig forståelse
     * av implementasjonen og har tilpasset den våre krav til brukeropplevelse.
     * Spesielt plasskonflikt-håndtering og visuell presentasjon ble optimalisert med AI-hjelp.
     */
    private List<Plass> velgLedigePlasser(Visning visning) {
        List<Plass> valgtePlasser = new ArrayList<>();

        // Data-innhenting: Alle plasser i aktuell kinosal
        List<Plass> allePlasserISal = plassService.hentPlasserIKinosal(visning.getKinosalNr());

        // Algoritme for å identifisere opptatte plasser for denne spesifikke visningen
        List<Plassbillett> allePlassbilletter = plassBillettService.finnAllePlassBilletter();
        Set<String> opptattePlasser = new HashSet<>();

        // Effektiv lookup ved å bygge Set med sammensatte nøkler
        for (Plassbillett pb : allePlassbilletter) {
            Optional<Billett> billettOpt = billettService.hentBillett(pb.getBillettkode());
            if (billettOpt.isPresent() && billettOpt.get().getVisningNr() == visning.getId()) {
                String plassNøkkel = pb.getRadNr() + "," + pb.getSeteNr();
                opptattePlasser.add(plassNøkkel);
            }
        }

        // Filtrer og identifiser ledige plasser
        List<Plass> ledigePlasser = allePlasserISal.stream()
                .filter(plass -> !opptattePlasser.contains(plass.getRadNr() + "," + plass.getSeteNr()))
                .sorted(Comparator.comparing(Plass::getRadNr).thenComparing(Plass::getSeteNr))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Hovedinteraksjonsløkke for plassvalg
        boolean ferdig = false;
        while (!ferdig) {
            // Visuell presentasjon av ledige plasser
            System.out.println("\n--- LEDIGE PLASSER ---");
            if (ledigePlasser.isEmpty()) {
                System.out.println("Ingen ledige plasser for denne visningen.");
                return valgtePlasser;
            }

            // Tabellformatert visning (5 plasser per linje for lesbarhet)
            int antallPerRad = 5;
            for (int i = 0; i < ledigePlasser.size(); i++) {
                Plass plass = ledigePlasser.get(i);
                boolean erValgt = valgtePlasser.contains(plass);
                System.out.printf("%3d. Rad %-2d, Sete %-2d %s",
                        i + 1, plass.getRadNr(), plass.getSeteNr(),
                        erValgt ? "[VALGT]" : "");

                // Linjeformatering
                if ((i + 1) % antallPerRad == 0 || i == ledigePlasser.size() - 1) {
                    System.out.println();
                } else {
                    System.out.print(" | ");
                }
            }

            // Status-dashboard og handlingsalternativer
            System.out.println("\nValgte plasser: " + valgtePlasser.size());
            System.out.println("Totalpris: " + (valgtePlasser.size() * visning.getPris()) + " kr");
            System.out.println("\n1: Velg en plass");
            System.out.println("2: Fjern valgt plass");
            System.out.println("3: Fullfør bestilling");
            System.out.println("0: Avbryt bestilling");
            System.out.print("Velg et alternativ: ");

            String valg = scanner.nextLine();
            switch (valg) {
                case "1": // Plassvalg med duplikatsjekk
                    System.out.print("Oppgi plassnummer: ");
                    try {
                        int plassIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (plassIndex < 0 || plassIndex >= ledigePlasser.size()) {
                            System.out.println("Ugyldig plassnummer.");
                        } else {
                            Plass valgtPlass = ledigePlasser.get(plassIndex);
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

                case "2": // Plassfjerning med listevisning
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
                                // Avbryt fjerning - ingen handling
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

                case "3": // Bestillingsvalidering
                    if (valgtePlasser.isEmpty()) {
                        System.out.println("Du må velge minst én plass for å fullføre bestillingen.");
                    } else {
                        ferdig = true;
                    }
                    break;

                case "0": // Avbryt prosess
                    return new ArrayList<>(); // Tom liste signaliserer avbrutt bestilling

                default:
                    System.out.println("Ugyldig valg. Prøv igjen.");
            }
        }

        return valgtePlasser;
    }

    /**
     * Bestillingsadministrasjon: Visning og sletting av eksisterende bestillinger.
     *
     * Implementerer forretningsregler:
     * - Billettkode-validering mot database
     * - Komplett informasjonsvisning (film, tid, plasser, status)
     * - 30-minutters regel for kanselleringsrettigheter
     * - Kaskadesletting (plassbilletter først, deretter hovedbillett)
     *
     * Note: 30-minutters regel og kaskadeslettingslogikk implementert med
     * AI-assistanse for å forstå best practice for database constraints og
     * forretningsregler. Vi har full forståelse av implementasjonen.
     */
    private void sjekkEllerSlettBestilling() {
        System.out.println("\n--- SJEKK ELLER SLETT BESTILLING ---");
        System.out.print("Oppgi billettkode: ");
        String billettkode = scanner.nextLine().trim();

        // Database-oppslag: Finn billett basert på kode
        Optional<Billett> billettOpt = billettService.hentBillett(billettkode);
        if (!billettOpt.isPresent()) {
            System.out.println("Fant ingen bestilling med denne koden.");
            System.out.println("\nTrykk Enter for å gå tilbake...");
            scanner.nextLine();
            return;
        }

        Billett billett = billettOpt.get();

        // Sammenstill komplett bestillingsinformasjon fra relaterte tabeller
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

        // Saml alle plasser tilknyttet denne billetten
        List<Plassbillett> billettensPlasser = plassBillettService.finnAllePlassBilletter().stream()
                .filter(pb -> pb.getBillettkode().equals(billettkode))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        // Komplett informasjonsvisning
        System.out.println("\n--- BESTILLINGSINFORMASJON ---");
        System.out.println("Film: " + filmNavn);
        System.out.println("Kinosal: " + kinosalNavn);
        try {
            System.out.println("Tidspunkt: " + visning.getTidspunkt()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        } catch (Exception e) {
            System.out.println("Tidspunkt: [Dato/tid-formatering feilet]");
        }
        System.out.println("Status: " + (billett.isErbetalt() ? "Betalt" : "Ikke betalt"));

        System.out.println("\nPlasser:");
        billettensPlasser.forEach(pb ->
                System.out.println("Rad " + pb.getRadNr() + ", Sete " + pb.getSeteNr()));

        // Forretningsregel: 30-minutters kanselleringsfrist
        LocalDateTime grense = LocalDateTime.now().plusMinutes(30);
        boolean kanSlettes = visning.getTidspunkt().isAfter(grense);

        if (kanSlettes) {
            System.out.println("\nVil du slette denne bestillingen? (J/N): ");
            String valg = scanner.nextLine().trim().toUpperCase();

            if (valg.equals("J")) {
                // Kaskadesletting: Plassbilletter først (foreign key constraints)
                for (Plassbillett pb : billettensPlasser) {
                    try {
                        Plassbillett.SammensattPlassBillettId id = new Plassbillett.SammensattPlassBillettId(
                                billettkode, pb.getRadNr(), pb.getSeteNr(), pb.getKinosalNr());
                        plassBillettService.slettPlassBillett(id);
                    } catch (Exception e) {
                        System.out.println("Feil ved sletting av plassbillett: " + e.getMessage());
                    }
                }

                // Slett hovedbillett
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

    /**
     * Generer unik alphanumerisk billettkode med kollisjonssjekk.
     *
     * Algoritme:
     * - 6 tegn bestående av A-Z og 0-9
     * - Loop med database-validering til unik kode finnes
     * - StringBuilder for effektiv string-konstruksjon
     *
     * @return Garantert unik 6-tegns billettkode
     *
     * Note: Kollisjonshåndtering og optimaliseringsalgoritme inspirert av AI-forslag
     * for å balansere ytelse og unikhet. Vi forstår sannsynlighetsberegningene
     * og har valgt passende kodelengde basert på forventet volum.
     */
    private String genererBillettkode() {
        String tegn = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder billettkode = new StringBuilder(6); // Pre-allokert kapasitet
        Random random = new Random();
        boolean unik = false;

        // Genereringsløkke med unikhetsgaranti
        while (!unik) {
            billettkode.setLength(0); // Reset uten ny allokering
            for (int i = 0; i < 6; i++) {
                billettkode.append(tegn.charAt(random.nextInt(tegn.length())));
            }

            // Database-validering for unikhet
            unik = !billettService.hentBillett(billettkode.toString()).isPresent();
        }

        return billettkode.toString();
    }
}