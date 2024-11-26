/**
 * Scrivere un programma che effettua le seguenti operazioni: 
 *  Fa inserire all’utente una stringa s; 
 *  Visualizza all’utente il numero di caratteri di s; 
 *  Chiede all’utente di inserire due numeri interi non negativi, a e b, compresi tra 0 
 * e la lunghezza di s meno 1, e tali che a < b (il programma assume che l’utente 
 * inserisca correttamente i dati, cioè non deve eseguire controlli di correttezza); 
 *  Visualizza all’utente la sottostringa di s compresa tra il carattere di posizione 
 * a (incluso) e quello di posizione b (escluso). 
 * 
 */
import java.util.Scanner;

public class Stinghe1 {
    
    public static void main(String[] args) {
        // Inserisci una stringa
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci una stringa: ");
        String s = scanner.nextLine();

        // Visualizza il numero di caratteri di s
        System.out.println("Il numero di caratteri di s è: " + s.length());

        // Chiede all'utente di inserire due numeri interi non negativi, a e b, compresi tra 0
        // e la lunghezza di s meno 1, e tali che a < b (il programma assume che l’utente
        // inserisca correttamente i dati, cioè non deve eseguire controlli di correttezza);
        System.out.print("Inserisci il primo numero intero non negativo (a): ");
        int a = scanner.nextInt();

        System.out.print("Inserisci il secondo numero intero non negativo (b>a): ");
        int b = scanner.nextInt();

        // Visualizza la sottostringa di s compresa tra il carattere di posizione
        // a (incluso) e quello di posizione b (escluso).
        System.out.println("La sottostringa di s compresa tra il carattere di posizione a (incluso) e quello di posizione b (escluso) è: " + s.substring(a, b));

        // Chiude lo scanner
        scanner.close();
    }
}
