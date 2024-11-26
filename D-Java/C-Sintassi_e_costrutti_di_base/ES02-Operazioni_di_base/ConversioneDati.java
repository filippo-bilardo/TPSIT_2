import java.util.Scanner;

public class ConversioneDati {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Acquisizione del numero di bit
        System.out.print("Inserisci il numero di bit: ");
        long numeroBit = scanner.nextLong();

        // Calcolo delle conversioni
        double numeroByte = numeroBit / 8.0;
        double numeroKilobyte = numeroByte / 1024.0;
        double numeroMegabyte = numeroKilobyte / 1024.0;
        double numeroGigabyte = numeroMegabyte / 1024.0;

        // Stampa dei risultati
        System.out.printf("%,d bit corrispondono a:\n", numeroBit);
        System.out.printf("%,.2f byte\n", numeroByte);
        System.out.printf("%,.2f KB\n", numeroKilobyte);
        System.out.printf("%,.2f MB\n", numeroMegabyte);
        System.out.printf("%,.4f GB\n", numeroGigabyte);

        // Chiudi lo scanner
        scanner.close();
    }
}
