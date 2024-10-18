public class esempio2 {
    public static void main(String[] args) {
        //Dichiarazione e inizializzazione separata:
        String str; // Dichiarazione
        str = "Valore1"; // Inizializzazione
        //Dichiarazione e inizializzazione nella stessa riga:
        String str2 = "Valore2";	//"Valore" è un letterale di stringa
        //Inizializzazione con una stringa vuota:
        String str3 = "";
        //Inizializzazione con un valore null:
        String str4 = null;
        //Inizializzazione con una nuova istanza di String:
        String str5 = new String("Valore5");
        //Inizializzazione con caratteri:
        char[] charArray = {'H', 'e', 'l', 'l', 'o'};
        String str6 = new String(charArray);
        //Inizializzazione con un riferimento a un'altra stringa:
        String str7 = str;

        //Stampa il contenuto delle variabili
        System.out.println("str: " + str + "\n" +
                "str2: " + str2 + "\n" +
                "str3: " + str3 + "\n" +
                "str4: " + str4 + "\n" +
                "str5: " + str5 + "\n" +
                "str6: " + str6 + "\n" +
                "str7: " + str7);
    }
}
