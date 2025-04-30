package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Esempio di utilizzo degli Atomic Field Updater per eseguire operazioni atomiche
 * su campi volatili di oggetti esistenti senza modificare la loro struttura.
 */
public class AtomicFieldUpdaterExample {
    
    /**
     * Classe che rappresenta un contatore con un campo volatile.
     */
    public static class Counter {
        // Il campo deve essere volatile e non final
        public volatile int count = 0;
        
        // Per AtomicReferenceFieldUpdater, il campo deve essere volatile e non final
        public volatile String message = "";
    }
    
    /**
     * Classe che rappresenta un contatore con campi privati.
     * Nota: gli Atomic Field Updater non possono accedere a campi privati
     * di altre classi, a meno che non siano nella stessa classe.
     */
    public static class PrivateCounter {
        private volatile int count = 0;
        
        // Creazione di un updater all'interno della stessa classe
        private static final AtomicIntegerFieldUpdater<PrivateCounter> UPDATER =
                AtomicIntegerFieldUpdater.newUpdater(PrivateCounter.class, "count");
        
        public int getCount() {
            return count;
        }
        
        public void increment() {
            UPDATER.incrementAndGet(this);
        }
    }
    
    // Creazione di updater per i campi della classe Counter
    private static final AtomicIntegerFieldUpdater<Counter> COUNT_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(Counter.class, "count");
    
    private static final AtomicReferenceFieldUpdater<Counter, String> MESSAGE_UPDATER =
            AtomicReferenceFieldUpdater.newUpdater(Counter.class, String.class, "message");
    
    public static void main(String[] args) throws InterruptedException {
        // Creazione di un contatore
        Counter counter = new Counter();
        
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        System.out.println("Dimostrazione di AtomicIntegerFieldUpdater:");
        
        // Incremento del contatore da parte di più thread
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    // Incremento atomico del campo count
                    COUNT_UPDATER.incrementAndGet(counter);
                }
            });
        }
        
        // Attesa del completamento dei thread
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        
        System.out.println("Valore finale del contatore: " + counter.count);
        System.out.println("Valore atteso: 5000");
        
        // Dimostrazione di AtomicReferenceFieldUpdater
        System.out.println("\nDimostrazione di AtomicReferenceFieldUpdater:");
        
        // Aggiornamento atomico del campo message
        MESSAGE_UPDATER.set(counter, "Messaggio iniziale");
        System.out.println("Messaggio iniziale: " + counter.message);
        
        // CompareAndSet con AtomicReferenceFieldUpdater
        boolean success = MESSAGE_UPDATER.compareAndSet(
                counter, "Messaggio iniziale", "Messaggio aggiornato");
        
        System.out.println("CAS (Messaggio iniziale -> Messaggio aggiornato): " + 
                (success ? "successo" : "fallimento"));
        System.out.println("Messaggio dopo CAS: " + counter.message);
        
        // CompareAndSet fallito
        success = MESSAGE_UPDATER.compareAndSet(
                counter, "Messaggio errato", "Altro messaggio");
        
        System.out.println("CAS (Messaggio errato -> Altro messaggio): " + 
                (success ? "successo" : "fallimento"));
        System.out.println("Messaggio finale: " + counter.message);
        
        // Dimostrazione di PrivateCounter con updater interno
        System.out.println("\nDimostrazione di PrivateCounter con updater interno:");
        
        PrivateCounter privateCounter = new PrivateCounter();
        
        // Creazione di un nuovo pool di thread
        executor = Executors.newFixedThreadPool(5);
        
        // Incremento del contatore privato da parte di più thread
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    privateCounter.increment();
                }
            });
        }
        
        // Attesa del completamento dei thread
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        
        System.out.println("Valore finale del contatore privato: " + privateCounter.getCount());
        System.out.println("Valore atteso: 5000");
        
        // Nota sulle limitazioni
        System.out.println("\nLimitazioni degli Atomic Field Updater:");
        System.out.println("1. I campi devono essere dichiarati volatile");
        System.out.println("2. I campi non possono essere final");
        System.out.println("3. I campi devono essere accessibili (pubblici o nella stessa classe)");
        System.out.println("4. Per AtomicReferenceFieldUpdater, è necessario specificare il tipo del campo");
    }
}