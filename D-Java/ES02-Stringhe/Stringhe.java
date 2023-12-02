

public class Stringhe {
    public static void main(String[] args) {
        // Creazione di un oggetto String
        String primaStringa = new String("Ciao, Mondo!");
        String secondaStringa = "Hello, World!";


        // Creazione di un'altra variabile di riferimento che punta allo stesso oggetto
        String terzaStringa = primaStringa;

        // Concatenazione tra dati tipo primitivo e stringa
        primaStringa = primaStringa + " " + 100 + " Volte Benvenuto!";

        // Stampa il contenuto di entrambe le variabili
        System.out.println("Contenuto di primaStringa: " + primaStringa);
        System.out.println("Contenuto di secondaStringa: " + secondaStringa);
        System.out.println("Contenuto di terzaStringa : " + terzaStringa );

        // Metodi della classe String --------------------------------------------
        // Lunghezza della stringa
        System.out.println("Lunghezza di primaStringa: " + primaStringa.length());

        // Confronto tra stringhe
        System.out.println("primaStringa è uguale a secondaStringa? " + primaStringa.equals(secondaStringa));

        // Conversione in maiuscolo
        System.out.println("primaStringa in maiuscolo: " + primaStringa.toUpperCase());

        // Il metodo charAt() restituisce il carattere in posizione i
        System.out.println("primaStringa.charAt(5): " + primaStringa.charAt(5));

        // Ricerca di una sottostringa
        System.out.println("primaStringa.indexOf(\"Mondo\"): " + primaStringa.indexOf("Mondo"));

        // Verifica dell'inizio o della fine di una stringa
        System.out.println("primaStringa.startsWith(\"Ciao\"): " + primaStringa.startsWith("Ciao"));
        System.out.println("primaStringa.endsWith(\"!\"): " + primaStringa.endsWith("!"));

        // Estrazione di una sottostringa
        System.out.println("primaStringa.substring(5): " + primaStringa.substring(5));

        
    }

}
