import java.util.Scanner;

public class AcquisizioneInput {

    public static void main(String[] args) {
        // Creazione di un oggetto Scanner per leggere l'input
        Scanner scanner = new Scanner(System.in);

        // Acquisizione di una stringa
        System.out.print("Inserisci il tuo nome: ");
        String nome = scanner.nextLine();
        // Acquisizione di un numero intero
        System.out.print("Inserisci la tua età: ");
        int età = scanner.nextInt();
        // Acquisizione di un numero decimale
        System.out.print("Inserisci la tua altezza (in metri): ");
        double altezza = scanner.nextDouble();
        // Stampa dei dati inseriti
        System.out.println("Nome: " + nome + ", Età: " + età + ", Altezza: " + altezza + " metri");
        
        // Chiudi lo scanner per evitare leak di risorse
        scanner.close();
    }
}
