/**
 * esempi di utilizzo delle stringhe in java:
 * -- Concatenazione: Per unire due o più stringhe, si può utilizzare l'operatore +. 
 *    Ad esempio: String result = "Hello " + "World";. 
 * -- Accesso ai caratteri: Puoi accedere ai singoli caratteri di una stringa utilizzando
 *    il metodo charAt(int index). Ad esempio: char c = "Hello".charAt(1); restituisce 'e'.
 * -- Lunghezza della stringa: Il metodo length() restituisce la lunghezza di una 
 *    stringa, ovvero il numero di caratteri che la compongono.
 * -- Sottostringhe: Il metodo substring(int start, int end) restituisce una nuova 
 *    stringa che è una sottostringa della stringa originale. I caratteri vengono copiati 
 *    a partire dall'indice start fino a end-1. 
 * -- Ricerca di sottostringhe: Il metodo indexOf(String str) restituisce l'indice 
 *    della prima occorrenza della sottostringa str nella stringa, o -1 se 
 *    non viene trovata. 
 * -- Sostituzione di sottostringhe: Il metodo replace(CharSequence target, 
 *    CharSequence replacement) restituisce una nuova stringa in cui tutte le occorrenze 
 *    di target sono state sostituite da replacement.
 * 
 */
public class esempio3 {
    public static void main(String[] args) {
        // Concatenazione
        String str1 = "Hello";
        String str2 = "World";
        String str3 = str1 + " " + str2; // Concatenazione di stringhe
        System.out.println(str3); // Output: Hello World
        
        // Accesso ai caratteri
        char c = str3.charAt(1);
        System.out.println(c); // Output: e
        
        // Lunghezza della stringa
        int len = str3.length();
        System.out.println(len); // Output: 11
        
        // Sottostringhe
        String subStr = str3.substring(6); // Sottostringhe
        System.out.println(subStr); // Output: World
        
        // Ricerca di sottostringhe
        int index = str3.indexOf("World");
        System.out.println(index); // Output: 6
        
        // Sostituzione di sottostringhe
        String replaced = str3.replace("World", "Java");
        System.out.println(replaced); // Output: Hello Java
    }
}
    