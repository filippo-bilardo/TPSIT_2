import java.util.Scanner;

public class CalcoloMediaAritmetica {
    
    public static void main(String[] args) {
        // Creazione di un oggetto Scanner per leggere l'input dell'utente
        Scanner scanner = new Scanner(System.in);

        // Chiedi all'utente di inserire tre numeri reali
        System.out.print("Inserisci il primo numero: ");
        double numero1 = scanner.nextDouble();

        System.out.print("Inserisci il secondo numero: ");
        double numero2 = scanner.nextDouble();

        System.out.print("Inserisci il terzo numero: ");
        double numero3 = scanner.nextDouble();

        // Calcola la media aritmetica
        double media = (numero1 + numero2 + numero3) / 3;

        // Stampa la media usando printf per mostrare solo due decimali
        System.out.printf("La media aritmetica è: %.2f\n", media);

        // Chiudi lo scanner
        scanner.close();
    }
}
