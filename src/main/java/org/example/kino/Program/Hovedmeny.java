package org.example.kino.Program;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Scanner;

@Component
public class Hovedmeny implements CommandLineRunner {

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
                    // Kall funksjon for planlegger
                    System.out.println("Du valgte planlegger.");
                    // planleggerService.meny();
                    break;
                case "2":
                    System.out.println("Du valgte kinobetjent.");
                    break;
                case "3":
                    System.out.println("Du valgte kunde.");
                    break;
                case "4":
                    System.out.println("Du valgte systemadministrasjon.");
                    break;
                case "0":
                    System.out.println("Avslutter...");
                    return;
                default:
                    System.out.println("Ugyldig valg. Prøv igjen.");
            }
        }
    }
}
