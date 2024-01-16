/**
 * Programma che stampa a video gli argomenti passati da riga di comando.
 */

public class PassaggioParametri {
  
    public static void main(String[] args) {
        // Corpo del metodo main

        System.out.println(null);
        // Stampa a video degli argomenti passati da riga di comando
        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "] = " + args[i]);
        }
        // Restituzione di un valore di uscita
        System.exit(5);
    }
}
