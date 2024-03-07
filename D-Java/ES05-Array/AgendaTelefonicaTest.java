/**
 * Test della classe AgendaTelefonica
 * 
 * Scrivere un programma che permetta di gestire una agenda telefonica. L’agenda
 * può gestire un numero limitato di utenze. Tale numero deve essere scelto
 * all’atto della creazione di un oggetto della classe AgendaTelefonica.
 * 
 * Ogni utenza dell’agenda ha un nome, un cognome e un numero di telefono.
 * Il programma deve permettere di:
 * 1. Inserire una nuova utenza nell’agenda
 * 2. Ricerca di una nuova utenza nell’agenda
 * 3. Visualizzazione di tutte le utenze dell’agenda
 * 4. Uscita
 *
 * @version 02/12/2023 
 */
import java.util.Scanner;

public class AgendaTelefonicaTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci la dimensione dell'agenda: ");
        int dimensioneAgenda = scanner.nextInt();

        AgendaTelefonica agenda = new AgendaTelefonica(dimensioneAgenda);

        int scelta;

        do {
            System.out.println("\nMenu:");
            System.out.println("1. Inserimento di una nuova utenza nell’agenda");
            System.out.println("2. Ricerca di una nuova utenza nell’agenda");
            System.out.println("3. Visualizzazione di tutte le utenze dell’agenda");
            System.out.println("4. Uscita");

            System.out.print("Scelta: ");
            scelta = scanner.nextInt();

            switch (scelta) {
                case 1:
                    inserimentoUtenza(agenda, scanner);
                    break;
                case 2:
                    ricercaUtenza(agenda, scanner);
                    break;
                case 3:
                    visualizzazioneUtenze(agenda);
                    break;
                case 4:
                    System.out.println("Programma terminato.");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
            }

        } while (scelta != 4);

        scanner.close();
    }

    private static void inserimentoUtenza(AgendaTelefonica agenda, Scanner scanner) {
        scanner.nextLine(); // Consuma il carattere newline rimasto
        System.out.print("Inserisci il nome: ");
        String nome = scanner.nextLine();

        System.out.print("Inserisci il cognome: ");
        String cognome = scanner.nextLine();

        System.out.print("Inserisci il numero di telefono: ");
        String numeroTelefono = scanner.nextLine();

        agenda.aggiungiContatto(nome, cognome, numeroTelefono);
    }

    private static void ricercaUtenza(AgendaTelefonica agenda, Scanner scanner) {
        scanner.nextLine(); // Consuma il carattere newline rimasto
        System.out.print("Inserisci il nome o il cognome da cercare: ");
        String ricerca = scanner.nextLine();

        agenda.ricercaContatto(ricerca);
    }

    private static void visualizzazioneUtenze(AgendaTelefonica agenda) {
        agenda.visualizzaContatti();
    }
}
