/**
 * Esempio del pattern Produttore-Consumatore usando wait() e notify()
 */
import java.util.LinkedList;
import java.util.Queue;

public class ES06_ProduttoreConsumatore {
    public static void main(String[] args) {
        // Creiamo una coda condivisa tra produttore e consumatore
        Buffer<Integer> buffer = new Buffer<>(5); // buffer di capacità 5
        
        // Creiamo il thread produttore
        Thread produttore = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    System.out.println("Produttore produce: " + i);
                    buffer.put(i);
                    Thread.sleep(500); // Simula il lavoro di produzione
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Produttore");
        
        // Creiamo il thread consumatore
        Thread consumatore = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    int item = buffer.get();
                    System.out.println("Consumatore consuma: " + item);
                    Thread.sleep(800); // Simula il lavoro di consumo
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumatore");
        
        // Avviamo i thread
        System.out.println("Avvio del pattern produttore-consumatore");
        produttore.start();
        consumatore.start();
        
        // Attendiamo il completamento
        try {
            produttore.join();
            consumatore.join();
        } catch (InterruptedException e) {
            System.out.println("Thread principale interrotto");
        }
        
        System.out.println("Dimostrazione completata");
    }
    
    /**
     * Classe Buffer che rappresenta lo spazio condiviso tra produttore e consumatore
     */
    static class Buffer<T> {
        private final Queue<T> queue = new LinkedList<>();
        private final int capacity;
        
        public Buffer(int capacity) {
            this.capacity = capacity;
        }
        
        /**
         * Metodo usato dal produttore per inserire un elemento
         */
        public synchronized void put(T item) throws InterruptedException {
            // Mentre la coda è piena, attendiamo
            while (queue.size() == capacity) {
                System.out.println("Buffer pieno, produttore in attesa...");
                wait(); // Rilascia il lock e attende notifica
            }
            
            // Aggiungiamo l'elemento alla coda
            queue.add(item);
            System.out.println("Buffer occupato: " + queue.size() + "/" + capacity);
            
            // Notifichiamo il consumatore che c'è un nuovo elemento
            notify();
        }
        
        /**
         * Metodo usato dal consumatore per prelevare un elemento
         */
        public synchronized T get() throws InterruptedException {
            // Mentre la coda è vuota, attendiamo
            while (queue.isEmpty()) {
                System.out.println("Buffer vuoto, consumatore in attesa...");
                wait(); // Rilascia il lock e attende notifica
            }
            
            // Preleviamo l'elemento dalla coda
            T item = queue.poll();
            System.out.println("Buffer occupato: " + queue.size() + "/" + capacity);
            
            // Notifichiamo il produttore che c'è spazio disponibile
            notify();
            
            return item;
        }
    }
}
