/**
 * Esempio che dimostra i meccanismi di sincronizzazione tra thread in Java
 * usando blocchi synchronized, metodi synchronized e oggetti Lock
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ES05_SincronizzazioneThread {
    private int contatoreSenzaSincronizzazione = 0;
    private int contatoreConSincronizzazione = 0;
    private int contatoreConMetodoSincronizzato = 0;
    private int contatoreConLock = 0;
    
    // Un lock esplicito per l'ultimo contatore
    private final Lock lock = new ReentrantLock();
    
    public static void main(String[] args) throws InterruptedException {
        05_SincronizzazioneThread esempio = new 05_SincronizzazioneThread();
        esempio.dimostraSincronizzazione();
    }
    
    public void dimostraSincronizzazione() throws InterruptedException {
        // Creiamo 5 thread per incrementare ciascun contatore
        Thread[] threadsNonSincronizzati = new Thread[5];
        Thread[] threadsSincronizzati = new Thread[5];
        Thread[] threadsMetodoSincronizzato = new Thread[5];
        Thread[] threadsConLock = new Thread[5];
        
        // Inizializziamo e avviamo i thread non sincronizzati
        for (int i = 0; i < 5; i++) {
            threadsNonSincronizzati[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    contatoreSenzaSincronizzazione++; // Incremento non sicuro
                }
            });
            threadsNonSincronizzati[i].start();
        }
        
        // Inizializziamo e avviamo i thread con blocco sincronizzato
        for (int i = 0; i < 5; i++) {
            threadsSincronizzati[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    synchronized(this) {
                        contatoreConSincronizzazione++; // Incremento sicuro
                    }
                }
            });
            threadsSincronizzati[i].start();
        }
        
        // Inizializziamo e avviamo i thread che usano un metodo sincronizzato
        for (int i = 0; i < 5; i++) {
            threadsMetodoSincronizzato[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementaConMetodoSincronizzato();
                }
            });
            threadsMetodoSincronizzato[i].start();
        }
        
        // Inizializziamo e avviamo i thread che usano un lock esplicito
        for (int i = 0; i < 5; i++) {
            threadsConLock[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    incrementaConLock();
                }
            });
            threadsConLock[i].start();
        }
        
        // Attendiamo che tutti i thread completino
        for (Thread t : threadsNonSincronizzati) t.join();
        for (Thread t : threadsSincronizzati) t.join();
        for (Thread t : threadsMetodoSincronizzato) t.join();
        for (Thread t : threadsConLock) t.join();
        
        // Visualizziamo i risultati
        System.out.println("Valore atteso per ciascun contatore: 5000");
        System.out.println("Contatore senza sincronizzazione: " + contatoreSenzaSincronizzazione);
        System.out.println("Contatore con blocco sincronizzato: " + contatoreConSincronizzazione);
        System.out.println("Contatore con metodo sincronizzato: " + contatoreConMetodoSincronizzato);
        System.out.println("Contatore con lock esplicito: " + contatoreConLock);
    }
    
    // Metodo sincronizzato
    private synchronized void incrementaConMetodoSincronizzato() {
        contatoreConMetodoSincronizzato++;
    }
    
    // Metodo che usa un lock esplicito
    private void incrementaConLock() {
        lock.lock();
        try {
            contatoreConLock++;
        } finally {
            lock.unlock(); // Importante: rilasciare sempre il lock nel blocco finally
        }
    }
}
