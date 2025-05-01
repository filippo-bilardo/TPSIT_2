/**
 * Esempio 01: Differenza tra Processo e Thread
 * 
 * Questo esempio mostra la differenza fondamentale tra processi e thread:
 * - I processi sono istanze separate di programmi con spazi di memoria isolati
 * - I thread condividono lo stesso spazio di memoria all'interno di un processo
 */
public class ES01b_ProcessoVsThread {
    // Variabile condivisa tra tutti i thread
    private static int contatoreComuneThread = 0;
    
    public static void main(String[] args) {
        System.out.println("Dimostrazione della differenza tra processi e thread");
        System.out.println("==================================================");
        
        // Creiamo due thread che incrementano lo stesso contatore
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                contatoreComuneThread++;
            }
            System.out.println("Thread 1 completato");
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                contatoreComuneThread++;
            }
            System.out.println("Thread 2 completato");
        });
        
        // Avviamo i thread
        thread1.start();
        thread2.start();
        
        // Attendiamo che entrambi i thread completino l'esecuzione
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Mostriamo il valore finale del contatore
        System.out.println("\nValore finale del contatore condiviso: " + contatoreComuneThread);
        System.out.println("\nNota: Se fossero stati processi separati, non avrebbero");
        System.out.println("potuto modificare la stessa variabile senza meccanismi");
        System.out.println("di comunicazione inter-processo (IPC).");
        
        // Nota: Il valore potrebbe non essere esattamente 2000 a causa di race condition
        // Questo è un altro concetto importante nella programmazione multi-thread
        // che verrà affrontato nei moduli sulla sincronizzazione
    }
}