/**
 * Esempi di invocazione dei metodi statici della classe String
 * 
 * @author Filippo Bilardo
 * @date 07/03/2024
 */
public class es01_metodi_statici {

    public static void main(String[] args) {
        // Creazione di un'istanza della classe
        String valore = String.valueOf("12");
        System.out.println(valore);

        //Striga formattata 
        String s = String.format("Valore: %d", 123);
        System.out.println(s);

        //Esempio di utilizzo del metodo join
        String[] array = {"Ciao", "Mondo", "!"};
        String messaggio = String.join(" ", array);
        System.out.println(messaggio);
        String messaggio2 = String.join("-", array);
        System.out.println(messaggio2);
        System.out.printf("Messaggio: %s\n", messaggio);

        //Esempio di utilizzo del metodo split
        String testo = "Ciao Mondo !";
        String[] parole = testo.split(" ");
        for (String parola : parole) {
            System.out.println(parola);
        }
        String testo2 = "Ciao+Mondo+tu+come+stai";
        String[] parole2 = testo.split("\\+");
        for (String parola : parole2) {
            System.out.println(parola);
        }
        for(int i = 0; i < parole2.length; i++) {
            System.out.println(parole2[i]);
        }
    }

    //
    
}
