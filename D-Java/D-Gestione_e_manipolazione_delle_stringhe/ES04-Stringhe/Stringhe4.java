
/**
 * Esercizio sulle stringhe
 * @author Filippo Bilardo
 * @date 07/03/2024
 */
public class Stringhe4 {
    public static void main(String[] args) {
        // Creazione di un oggetto String
        String oggeto_str = "Hello, World! Studenti della 4G";
        System.out.println("oggeto_str: " + oggeto_str);

        // Stampa il contenuto di entrambe le variabili
        System.out.println("Lunghezza: " + oggeto_str.length());

        int len = oggeto_str.length();
        System.out.println("Char in posizione centrale di oggeto_str è " + oggeto_str.charAt(len/2));
    }

}
