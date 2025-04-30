/**
 * Esempio 02: Pattern Produttore-Consumatore
 * 
 * Questo esempio implementa il classico pattern Produttore-Consumatore utilizzando
 * una coda condivisa. I produttori inseriscono elementi nella coda e i consumatori
 * li prelevano. La sincronizzazione garantisce che le operazioni avvengano in modo
 * thread-safe.
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProduttoreConsumatore {
    public static void main(String[] args) {
        System.out.println("Dimostrazione del pattern Produttore-Consumatore");
        System.out.println("==============================================");
        
        // Creiamo un buffer condiviso con capacità limitata
        BufferCondiviso<Integer> buffer = new BufferCondiviso<>(5);
        
        // Creiamo due produttori
        Thread produttore1 = new Thread(new Produttore(buffer, "Produttore-1", 8));
        Thread produttore2 = new Thread(new Produttore(buffer, "Produttore-2", 7));
        
        // Creiamo due consumatori
        Thread consumatore1 = new Thread(new Consumatore(buffer, "Consumatore-1", 10));
        Thread consumatore2 = new Thread(new Consumatore(buffer, "Consumatore-2", 5));
        
        // Avviamo i thread
        System.out.println("Avvio dei thread produttori e consumatori...");
        produttore1.start();
        produttore2.start();
        consumatore1.start();
        consumatore2.start();
        
        // Attendiamo che tutti i thread completino
        try {
            produttore1.join();
            produttore2.join();
            consumatore1.join();
            consumatore2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nPattern Produttore-Consumatore completato.");
    }
    
    /**
     * Classe che rappresenta un buffer condiviso con capacità limitata.
     * Implementa la sincronizzazione per garantire operazioni thread-safe.
     */
    static class BufferCondiviso<T> {
        private final Queue<T> buffer = new LinkedList<>();
        private final int capacita;
        
        public BufferCondiviso(int capacita) {
            this.capacita = capacita;
        }
        
        /**
         * Inserisce un elemento nel buffer.
         * Se il buffer è pieno, attende che si liberi spazio.
         */
        public synchronized void inserisci(T elemento, String produttore) throws InterruptedException {
            // Attende che ci sia spazio nel buffer
            while (buffer.size() == capacita) {
                System.out.println(produttore + ": Buffer pieno, attendo...");
                wait();
            }
            
            // Inserisce l'elemento nel buffer
            buffer.add(elemento);
            System.out.println(produttore + ": Ho inserito l'elemento " + elemento + 
                               " - Stato buffer: " + buffer.size() + "/" + capacita);
            
            // Notifica i thread in attesa che il buffer è cambiato
            notifyAll();
        }
        
        /**
         * Preleva un elemento dal buffer.
         * Se il buffer è vuoto, attende che arrivi un elemento.
         */
        public synchronized T preleva(String consumatore) throws InterruptedException {
            // Attende che ci sia almeno un elemento nel buffer
            while (buffer.isEmpty()) {
                System.out.println(consumatore + ": Buffer vuoto, attendo...");
                wait();
            }
            
            // Preleva l'elemento dal buffer
            T elemento = buffer.remove();
            System.out.println(consumatore + ": Ho prelevato l'elemento " + elemento + 
                               " - Stato buffer: " + buffer.size() + "/" + capacita);
            
            // Notifica i thread in attesa che il buffer è cambiato
            notifyAll();
            
            return elemento;
        }
    }
    
    /**
     * Classe che rappresenta un produttore che inserisce elementi nel buffer.
     */
    static class Produttore implements Runnable {
        private final BufferCondiviso<Integer> buffer;
        private final String nome;
        private final int numElementi;
        private final Random random = new Random();
        
        public Produttore(BufferCondiviso<Integer> buffer, String nome, int numElementi) {
            this.buffer = buffer;
            this.nome = nome;
            this.numElementi = numElementi;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 1; i <= numElementi; i++) {
                    // Genera un elemento casuale
                    int elemento = random.nextInt(100);
                    
                    // Inserisce l'elemento nel buffer
                    buffer.inserisci(elemento, nome);
                    
                    // Simula un tempo variabile di produzione
                    Thread.sleep(random.nextInt(500) + 200);
                }
                System.out.println(nome + ": Ho completato la produzione di " + numElementi + " elementi.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nome + ": Sono stato interrotto.");
            }
        }
    }
    
    /**
     * Classe che rappresenta un consumatore che preleva elementi dal buffer.
     */
    static class Consumatore implements Runnable {
        private final BufferCondiviso<Integer> buffer;
        private final String nome;
        private final int numElementi;
        private final Random random = new Random();
        
        public Consumatore(BufferCondiviso<Integer> buffer, String nome, int numElementi) {
            this.buffer = buffer;
            this.nome = nome;
            this.numElementi = numElementi;
        }
        
        @Override
        public void run() {
            try {
                int somma = 0;
                for (int i = 1; i <= numElementi; i++) {
                    // Preleva un elemento dal buffer
                    int elemento = buffer.preleva(nome);
                    somma += elemento;
                    
                    // Simula un tempo variabile di consumo
                    Thread.sleep(random.nextInt(800) + 100);
                }
                System.out.println(nome + ": Ho completato il consumo di " + numElementi + 
                                   " elementi. Somma totale: " + somma);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nome + ": Sono stato interrotto.");
            }
        }
    }
}