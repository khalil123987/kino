package org.example.kino.Program;

import org.example.kino.Model.Billett;
import org.example.kino.Model.Film;
import org.example.kino.Model.Login;
import org.example.kino.Model.Visning;
import org.example.kino.Repository.BillettRepository;
import org.example.kino.Repository.FilmRepository;
import org.example.kino.Repository.SystemAdminRepository;
import org.example.kino.Repository.VisningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Planlegger-klassen gir planleggeren muligheten til å utføre oppgaver
 * som registrering av filmer, visningshåndtering og generering av statistikk.
 */
@Service
public class Planlegger {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private VisningRepository visningRepository;

    @Autowired
    private BillettRepository billettRepository;

    @Autowired
    private SystemAdminRepository systemAdminRepository;

    private final Scanner scanner = new Scanner(System.in);

    public void meny() {
        while (true) {
            System.out.println("\n--- PLANLEGGER MENY ---");
            System.out.println("1: Registrer film");
            System.out.println("2: Opprett visning");
            System.out.println("3: Oppdater visning");
            System.out.println("4: Hent antall solgte billetter");
            System.out.println("5: Hent antall slettede bestillinger");
            System.out.println("6: Slett visning");
            System.out.println("7: Generer statistikk");
            System.out.println("0: Tilbake til hovedmeny");
            System.out.print("Velg et alternativ: ");

            String valg = scanner.nextLine();

            try {
                switch (valg) {
                    case "1":
                        registrerFilmMeny();
                        break;
                    case "2":
                        opprettVisningMeny();
                        break;
                    case "3":
                        oppdaterVisningMeny();
                        break;
                    case "4":
                        System.out.println("Antall solgte billetter: " + hentAntallSolgteBilletter());
                        break;
                    case "5":
                        System.out.println("Antall slettede bestillinger: " + hentAntallSlettedeBestillinger());
                        break;
                    case "6":
                        slettVisningMeny();
                        break;
                    case "7":
                        genererStatistikk();
                        break;
                    case "0":
                        System.out.println("Tilbake til hovedmeny...");
                        return;
                    default:
                        System.out.println("Ugyldig valg, prøv igjen.");
                }
            } catch (Exception e) {
                System.out.println("Feil: " + e.getMessage());
            }
        }
    }


    private void registrerFilmMeny() {
        System.out.print("Skriv inn filmnavn: ");
        String filmnavn = scanner.nextLine();
        Film film = registrerFilm(filmnavn);
        System.out.println("Film registrert med ID: " + film.getFilmnr());
    }
    public boolean login(String brukernavn, Integer pinkode) {
        Optional<Login> bruker = systemAdminRepository.findByBrukernavnAndPinkode(brukernavn, pinkode);

        return bruker.isPresent() &&
                Boolean.TRUE.equals(bruker.get().getErPlanlegger());
    }

    private void opprettVisningMeny() {
        System.out.print("Skriv inn filmnummer: ");
        int filmNr = Integer.parseInt(scanner.nextLine());
        System.out.print("Skriv inn kinosalnummer: ");
        int kinosalNr = Integer.parseInt(scanner.nextLine());
        System.out.print("Skriv inn dato (format: yyyy-MM-dd): ");
        LocalDate dato = LocalDate.parse(scanner.nextLine());
        System.out.print("Skriv inn starttid (format: HH:mm): ");
        LocalTime starttid = LocalTime.parse(scanner.nextLine());
        System.out.print("Skriv inn pris: ");
        double pris = Double.parseDouble(scanner.nextLine());

        Visning visning = opprettVisning(filmNr, kinosalNr, dato, starttid, pris);
        System.out.println("Visning opprettet med ID: " + visning.getId());
    }

    private void oppdaterVisningMeny() {
        System.out.print("Skriv inn visningsnummer som skal oppdateres: ");
        int visningsnr = Integer.parseInt(scanner.nextLine());
        System.out.print("Skriv inn ny dato (format: yyyy-MM-dd): ");
        LocalDate nyDato = LocalDate.parse(scanner.nextLine());
        System.out.print("Skriv inn ny starttid (format: HH:mm): ");
        LocalTime nyStarttid = LocalTime.parse(scanner.nextLine());
        System.out.print("Skriv inn ny pris: ");
        double nyPris = Double.parseDouble(scanner.nextLine());

        LocalDateTime nyttTidspunkt = LocalDateTime.of(nyDato, nyStarttid);
        Visning oppdatertVisning = oppdaterVisning(visningsnr, nyttTidspunkt, nyPris);
        System.out.println("Visning oppdatert. Nytt tidspunkt: " + oppdatertVisning.getTidspunkt()
                + ", ny pris: " + oppdatertVisning.getPris());
    }

    public Film registrerFilm(String filmnavn) {
        Film film = new Film();
        film.setFilmnavn(filmnavn);
        return filmRepository.save(film);
    }

    public Visning opprettVisning(int filmNr, int kinosalNr, LocalDate dato, LocalTime starttid, double pris) {
        Visning visning = new Visning(filmNr, kinosalNr, dato, starttid, pris);
        return visningRepository.save(visning);
    }

    public Visning oppdaterVisning(int visningsnr, LocalDateTime nyTidspunkt, double nyPris) {
        List<Billett> billetter = billettRepository.findByVisningsnr(visningsnr);

        boolean harSolgteBilletter = billetter.stream().anyMatch(Billett::isErbetalt);
        if (harSolgteBilletter) {
            throw new IllegalStateException("Kan ikke oppdatere visning med solgte billetter.");
        }

        Optional<Visning> optionalVisning = visningRepository.findById(visningsnr);
        if (optionalVisning.isPresent()) {
            Visning visning = optionalVisning.get();
            visning.setTidspunkt(nyTidspunkt);
            visning.setPris(nyPris);
            return visningRepository.save(visning);
        } else {
            throw new IllegalArgumentException("Visning med ID " + visningsnr + " finnes ikke.");
        }
    }

    private void slettVisningMeny() {
        System.out.print("Skriv inn visningsnummer som skal slettes: ");
        int visningsnr = Integer.parseInt(scanner.nextLine());

        List<Billett> billetter = billettRepository.findByVisningsnr(visningsnr);
        boolean harSolgteBilletter = billetter.stream().anyMatch(Billett::isErbetalt);

        if (harSolgteBilletter) {
            System.out.println("Kan ikke slette visning med solgte billetter.");
            return;
        }

        Optional<Visning> optionalVisning = visningRepository.findById(visningsnr);
        if (optionalVisning.isPresent()) {
            visningRepository.deleteById(visningsnr);
            System.out.println("Visning slettet.");
        } else {
            System.out.println("Visning med ID " + visningsnr + " finnes ikke.");
        }
    }

    public void genererStatistikk() {
        System.out.println("\n--- STATISTIKK ---");

        Iterable<Film> filmer = filmRepository.findAll();
        for (Film film : filmer) {
            int filmNr = film.getFilmnr();
            List<Visning> visninger = visningRepository.findByFilmNr(filmNr);
            long totaltSolgte = 0;
            double totalInntekt = 0;

            for (Visning visning : visninger) {
                List<Billett> billetter = billettRepository.findByVisningsnr(visning.getId());
                long antallBetalte = billetter.stream().filter(Billett::isErbetalt).count();
                totaltSolgte += antallBetalte;
                totalInntekt += antallBetalte * visning.getPris();
            }

            System.out.println("Film: " + film.getFilmnavn());
            System.out.println(" - Antall visninger: " + visninger.size());
            System.out.println(" - Antall solgte billetter: " + totaltSolgte);
            System.out.println(" - Total inntekt: " + totalInntekt + " kr\n");
        }
    }

    public long hentAntallSolgteBilletter() {
        return billettRepository.countByErbetalt(true);
    }

    public long hentAntallSlettedeBestillinger() {
        return billettRepository.countByErbetalt(false);
    }
}
