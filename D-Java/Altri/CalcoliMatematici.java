/**
 * Programma che chiede all'utente di inserire due numeri interi positivi e
 * calcola e visualizza i seguenti risultati:
 * 
 * - La potenza ab 
 * - Il logaritmo in base e di a
 * - Il logaritmo in base 10 di b
 * - La radice quadrata della somma dei quadrati di a e b
 * 
 * Utilizzare il metodo Math.pow() per calcolare la potenza ab, il metodo
 * Math.log() per calcolare il logaritmo in base e di a, il metodo Math.log10()
 * per calcolare il logaritmo in base 10 di b e il metodo Math.sqrt() per
 * calcolare la radice quadrata della somma dei quadrati di a e b.
 * 
 * Per eseguire il calcolo della radice quadrata della somma dei quadrati di a e
 * b, è necessario calcolare prima i quadrati di a e b e poi sommarli.
 * 
 * Per eseguire il calcolo del logaritmo in base e di a, è necessario calcolare
 * prima il logaritmo naturale di a e poi dividerlo per il logaritmo naturale di
 * e.
 * 
 * @version 02/12/2023
 */
import java.util.Scanner;

public class CalcoliMatematici {

    public static void main(String[] args) {
        // Chiede all'utente di inserire due numeri interi positivi
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci il primo numero intero positivo (a): ");
        int a = scanner.nextInt();

        System.out.print("Inserisci il secondo numero intero positivo (b): ");
        int b = scanner.nextInt();

        // Calcola e visualizza i risultati
        calcolaERisultati(a, b);

        // Chiude lo scanner
        scanner.close();
    }

    // Metodo per calcolare e visualizzare i risultati
    private static void calcolaERisultati(int a, int b) {
        // Calcola la potenza ab
        double potenza = Math.pow(a, b);
        System.out.println("Il valore della potenza ab è: " + potenza);

        // Calcola il logaritmo in base e di a
        double logaritmoBaseE = Math.log(a);
        System.out.println("Il valore del logaritmo in base e di a è: " + logaritmoBaseE);

        // Calcola il logaritmo in base 10 di b
        double logaritmoBase10 = Math.log10(b);
        System.out.println("Il valore del logaritmo in base 10 di b è: " + logaritmoBase10);

        // Calcola la radice quadrata della somma dei quadrati di a e b
        double radiceQuadrata = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
        System.out.println("La radice quadrata della somma dei quadrati di a e b è: " + radiceQuadrata);
    }
}
