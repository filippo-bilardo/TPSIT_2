/**
 * esempi di utilizzo delle stringhe in java:
 * -- Divisione di stringhe: Il metodo split(String regex) divide la stringa in un 
 *    array di sottostringhe utilizzando l'espressione regolare regex come delimitatore.
 * -- Rimozione di spazi bianchi: I metodi trim(), stripLeading() e stripTrailing()
 *    rimuovono rispettivamente gli spazi bianchi all'inizio, alla fine o sia 
 *    all'inizio che alla fine di una stringa.
 * -- Conversione di case: I metodi toLowerCase() e toUpperCase() restituiscono una 
 *    nuova stringa con tutti i caratteri in minuscolo o maiuscolo, rispettivamente.
 * -- Conversione di tipi: Il metodo valueOf() converte un valore di tipo primitivo
 *    (come int, double, boolean) in una stringa.
 * -- Formattazione di stringhe: La classe String fornisce il metodo format() per 
 *    formattare le stringhe in modo simile a printf() in C.
 * -- Stringhe letterali: Le stringhe letterali, delimitate da doppi apici "", 
 *    vengono memorizzate nel "pool delle stringhe" per risparmiare memoria.
 * -- StringBuilder e StringBuffer: Per operazioni che comportano molte manipolazioni
 *    di stringhe, è più efficiente utilizzare le classi StringBuilder o StringBuffer
 *    invece di concatenare stringhe immutabili.
 */
public class esempio4 {
    public static void main(String[] args) {
        // Divisione di stringhe
        String str = "Ciao, Mondo, Java";
        String[] tokens = str.split(", ");
        for (String token : tokens) {
            System.out.println(token);
        }
        
        // Rimozione di spazi bianchi
        String s = "  Hello World  ";
        System.out.println(s.trim()); // Output: "Hello World"
        System.out.println(s.strip()); // Output: "Hello World"
        System.out.println(s.stripLeading()); // Output: "Hello World  "
        System.out.println(s.stripTrailing()); // Output: "  Hello World"
        
        // Conversione di case
        String str1 = "Hello";
        System.out.println(str1.toLowerCase()); // Output: hello
        System.out.println(str1.toUpperCase()); // Output: HELLO
        
        // Conversione di tipi
        int value = 123;
        String str2 = String.valueOf(value);
        System.out.println(str2); // Output: 123
        
        // Formattazione di stringhe
        String formatted = String.format("Valore: %d", value);
        System.out.println(formatted); // Output: Valore: 123
        
        // Stringhe letterali
        String s1 = "Hello";
        String s2 = "Hello";
        System.out.println(s1 == s2); // Output: true
        
        // StringBuilder e StringBuffer
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");
        sb.append(" ");
        sb.append("World");
        System.out.println(sb.toString()); // Output: Hello World
    }
}
    