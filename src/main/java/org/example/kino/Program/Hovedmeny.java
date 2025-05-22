package org.example.kino.Program;

import org.example.kino.Repository.BillettRepository;
import org.example.kino.Repository.SystemAdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Hovedmeny implements CommandLineRunner {

    private final Planlegger planlegger;
    private final Kunde kunde;
    private final KinobetjentProgram kinobetjentProgram;
    private final SystemAdministrasjon admin;

    public Hovedmeny(
            Planlegger planlegger,
            Kunde kunde,
            KinobetjentProgram kinobetjentProgram,
            BillettRepository billettRepository,
            SystemAdminRepository adminRepo
    ) {
        this.planlegger = planlegger;
        this.kunde = kunde;
        this.kinobetjentProgram = kinobetjentProgram;
        this.admin = new SystemAdministrasjon(billettRepository, adminRepo);
    }

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- HOVEDMENY ---");
            System.out.println("1: Logg inn som planlegger");
            System.out.println("2: Logg inn som kinobetjent");
            System.out.println("3: Logg inn som kunde");
            System.out.println("4: Systemadministrasjon");
            System.out.println("0: Avslutt");

            System.out.print("Velg et alternativ: ");
            String valg = scanner.nextLine();

            switch (valg) {
                case "1":
                    System.out.println("Du valgte planlegger.");

                    System.out.print("Brukernavn: ");
                    String brukernavn = scanner.nextLine();

                    System.out.print("Pinkode: ");
                    int pinkode = Integer.parseInt(scanner.nextLine());

                    boolean innlogget = planlegger.login(brukernavn, pinkode);

                    if (innlogget) {
                        System.out.println("Innlogging vellykket.");
                        planlegger.meny(); 
                    } else {
                        System.out.println("Feil brukernavn eller pinkode.");
                    }
                    break;

                case "2":
                    System.out.println("Du valgte kinobetjent.");
                    kinobetjentProgram.visKinobetjentMeny();
                    break;
                case "3":
                    System.out.println("Du valgte kunde.");
                    kunde.visKundemeny();
                    break;
                case "4":
                    System.out.println("Du valgte systemadministrasjon.");
                    admin.visMeny(); // <-- Send kontrollen videre til SystemAdmin
                    break;
                case "0":
                    System.out.println("Avslutter...");
                    return;
                default:
                    System.out.println("Ugyldig valg. Prøv igjen kompis.");
            }
        }
    }
}
