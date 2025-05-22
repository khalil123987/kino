package org.example.kino.Program;

import org.example.kino.Repository.BillettRepository;
import org.example.kino.Repository.SystemAdminRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/** Implementert først av Sarmad men testet og finpusset av alle
 * En viktig ting å merke seg med denne klassen er at det er mange ulike login variabler
 * de referer til ulike scenarioer og ulike situasjoner man bruker de i

*/

@Service
public class SystemAdministrasjon {

    private final BillettRepository billettRepo;
    private final SystemAdminRepository adminRepo;

    public SystemAdministrasjon(BillettRepository billettRepo, SystemAdminRepository adminRepo) {
        this.billettRepo = billettRepo;
        this.adminRepo = adminRepo;
    }

               /** Her er admin rettet mot en brukerkonto*/

    public void opprettBruker(String brukernavn, String rolle, String pinkode) {
        if (adminRepo.brukernavnEksisterer(brukernavn)) {
            System.out.println("Brukernavn eksisterer allerede.");
            return;
        }

        boolean erPlanlegger = rolle.equalsIgnoreCase("planlegger");

        try {
            int pinkodeTall = Integer.parseInt(pinkode);
            adminRepo.opprettBruker(brukernavn, pinkodeTall, erPlanlegger);
            System.out.println("Bruker opprettet: " + brukernavn + " med rolle: " + rolle);
        } catch (NumberFormatException e) {
            System.out.println("Ugyldig pinkode. Må være tall.");
        }
    }

    public void slettBruker(String brukernavn) {
        if (!adminRepo.brukernavnEksisterer(brukernavn)) {
            System.out.println("Bruker ikke funnet.");
            return;
        }

        adminRepo.slettBruker(brukernavn);
        System.out.println("Bruker slettet: " + brukernavn);
    }
/**Referere til repository for å hente data*/

    public void endrePinkode(String brukernavn, String nyKode) {
        if (!adminRepo.brukernavnEksisterer(brukernavn)) {
            System.out.println("Bruker ikke funnet.");
            return;
        }

        try {
            int nyPin = Integer.parseInt(nyKode);
            adminRepo.endrePin(brukernavn, nyPin);
            System.out.println("PIN-kode endret for: " + brukernavn);
        } catch (NumberFormatException e) {
            System.out.println("Ugyldig ny pinkode. Må være tall.");
        }
    }

    /**
     * Adminsitrasjonsdel også her
     * Her brukte vi den ene weblinken med kode sammen med KI generert hjelp til å lage denne
     * Dette da fordi vi ikke fant noen ellers kilder som kunne støtte det vi kan
     * Lite erfaring med det hadde vi å
     */

    public void backupDatabase() {
        try {
            String jarDir;
            CodeSource codeSource = SystemAdministrasjon.class.getProtectionDomain().getCodeSource();
            if (codeSource != null) {
                File jarFile = new File(codeSource.getLocation().toURI().getPath());
                jarDir = jarFile.getParentFile().getPath();
            } else {
                jarDir = System.getProperty("user.dir");
            }

            File backupFolder = new File(jarDir + File.separator + "backup");
            if (!backupFolder.exists()) {
                backupFolder.mkdir();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String savePath = jarDir + File.separator + "backup" + File.separator + "kino_" + timestamp + ".dump";

            String pgDumpPath = "\"C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe\"";
            String executeCmd = pgDumpPath + " -U postgres -Fc -f \"" + savePath + "\" kino";

            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", executeCmd);
            pb.environment().put("PGPASSWORD", "1234");

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.err.println("pg_dump error: " + line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println(" Backup fullført: " + savePath);
            } else {
                System.out.println(" Backup feilet. Exit-kode: " + exitCode);
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            System.out.println(" Feil ved backup: " + e.getMessage());
        }
    }

    /**
     * Denne voiden skal rydde filer som er gamle --> dvs visninger som var for 24 timer siden
     * og også billetter som er ubetalt og har vært i 24 timer+
     */
    public void ryddMidlertidigeData() {
        try {
            int timer = 24;

            // Først slett ALLE billetter for gamle visninger
            int slettetBilletter = billettRepo.slettAlleBilletterForVisningerEldreEnnXTimer(timer);
            System.out.println("Slettet " + slettetBilletter + " billetter eldre enn " + timer + " timer.");

            // Deretter slett gamle visninger
            int slettetVisninger = adminRepo.slettVisningerEldreEnnXTimer(timer);
            System.out.println("Slettet " + slettetVisninger + " visninger eldre enn 24 timer.");

        } catch (Exception e) {
            System.out.println("Feil under sletting av midlertidige data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**Navigasjon for systemadmin*/

    public void visMeny() {
        Scanner scanner = new Scanner(System.in);
        boolean adminKjører = true;

        System.out.println("SystemAdmin: velkommen til administrasjonsmodulen.");

        while (adminKjører) {
            System.out.println("\n=== SystemAdmin Meny ===");
            System.out.println("1. Opprett bruker");
            System.out.println("2. Slett bruker");
            System.out.println("3. Endre PIN-kode");
            System.out.println("4. Backup database");
            System.out.println("5. Rydd midlertidige data");
            System.out.println("6. Vis antall aktive ubetalte bestillinger");
            System.out.println("7. Vis antall aktive *betalte* bestillinger");
            System.out.println("0. Tilbake til hovedmeny");
            System.out.print("Velg et alternativ: ");
            String valg = scanner.nextLine();

            switch (valg) {
                case "1":
                    System.out.print("Brukernavn: ");
                    String brukernavnOpprett = scanner.nextLine();
                    System.out.print("Rolle (planlegger eller annet): ");
                    String rolle = scanner.nextLine();
                    System.out.print("Pinkode (tall): ");
                    String pinkode = scanner.nextLine();
                    opprettBruker(brukernavnOpprett, rolle, pinkode);
                    break;
                case "2":
                    System.out.print("Brukernavn som skal slettes: ");
                    String brukernavnSlett = scanner.nextLine();
                    slettBruker(brukernavnSlett);
                    break;
                case "3":
                    System.out.print("Brukernavn for endring av PIN: ");
                    String brukernavnEndre = scanner.nextLine();
                    System.out.print("Ny pinkode (tall): ");
                    String nyPin = scanner.nextLine();
                    endrePinkode(brukernavnEndre, nyPin);
                    break;
                case "4":
                    backupDatabase();
                    break;
                case "5":
                    ryddMidlertidigeData();
                    break;
                case "6":
                    visAntallAktiveBestillinger();
                    break;
                case "7":
                    visAntallAktiveBetalteBestillinger();
                    break;
                case "0":
                    adminKjører = false;
                    System.out.println("Tilbake til hovedmeny.");
                    break;
                default:
                    System.out.println("Ugyldig valg. Prøv igjen.");
            }
        }
    }


    public void visAntallAktiveBestillinger() {
        long antall = billettRepo.countByErbetaltFalse();
        System.out.println("Aktive ubetalte bestillinger: " + antall);
    }

    public void visAntallAktiveBetalteBestillinger() {
        long antall = billettRepo.countByErbetaltTrue();
        System.out.println("Aktive betalte bestillinger: " + antall);
    }
}
