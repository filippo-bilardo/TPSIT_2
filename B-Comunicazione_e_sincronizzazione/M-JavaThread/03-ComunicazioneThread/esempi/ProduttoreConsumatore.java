/**
 * Implementazione del classico pattern produttore-consumatore.
 * Questo esempio mostra come più thread produttori e consumatori
 * possono interagire in modo sicuro attraverso un buffer condiviso.
 */
public class ProduttoreConsumatore {
    
    /**
     * Metodo main che avvia l'esempio del pattern produttore-consumatore.
     */
    public static void main(String[] args) {
        // Crea un buffer condiviso con capacità 5
        Buffer<String> buffer = new Buffer<>(5);
        
        // Crea e avvia 3 produttori
        for (int i = 0; i < 3; i++) {
            new Thread(new Produttore(buffer, "Produttore-" + i)).start();
        }
        
        // Crea e avvia 2 consumatori
        for (int i = 0; i < 2; i++) {
            new Thread(new Consumatore(buffer, "Consumatore-" + i)).start();
        }
    }
    
    /**
     * Classe che rappresenta il buffer condiviso tra produttori e consumatori.
     */
    static class Buffer<T> {
        private final T[] elementi;
        private int count = 0;
        private int putIndex = 0;
        private int getIndex = 0;
        
        /**
         * Costruttore che inizializza il buffer con la capacità specificata.
         */
        @SuppressWarnings("unchecked")
        public Buffer(int capacita) {
            this.elementi = (T[]) new Object[capacita];
        }
        
        /**
         * Inserisce un elemento nel buffer.
         * Se il buffer è pieno, il thread chiamante viene messo in attesa.
         */
        public synchronized void put(T elemento) throws InterruptedException {
            // Attende finché il buffer non è pieno
            while (count == elementi.length) {
                System.out.println(Thread.currentThread().getName() + ": buffer pieno, in attesa...");
                wait();
            }
            
            // Inserisce l'elemento nel buffer
            elementi[putIndex] = elemento;
            
            // Aggiorna l'indice di inserimento in modo circolare
            putIndex = (putIndex + 1) % elementi.length;
            
            // Incrementa il contatore degli elementi
            count++;
            
            System.out.println(Thread.currentThread().getName() + ": inserito " + elemento + ", elementi nel buffer: " + count);
            
            // Notifica i thread in attesa (consumatori)
            notifyAll();
        }
        
        /**
         * Preleva un elemento dal buffer.
         * Se il buffer è vuoto, il thread chiamante viene messo in attesa.
         */
        public synchronized T get() throws InterruptedException {
            // Attende finché il buffer non è vuoto
            while (count == 0) {
                System.out.println(Thread.currentThread().getName() + ": buffer vuoto, in attesa...");
                wait();
            }
            
            // Preleva l'elemento dal buffer
            T elemento = elementi[getIndex];
            
            // Rimuove il riferimento per aiutare il garbage collector
            elementi[getIndex] = null;
            
            // Aggiorna l'indice di prelievo in modo circolare
            getIndex = (getIndex + 1) % elementi.length;
            
            // Decrementa il contatore degli elementi
            count--;
            
            System.out.println(Thread.currentThread().getName() + ": prelevato " + elemento + ", elementi nel buffer: " + count);
            
            // Notifica i thread in attesa (produttori)
            notifyAll();
            
            return elemento;
        }
    }
    
    /**
     * Classe che rappresenta un produttore che genera dati e li inserisce nel buffer.
     */
    static class Produttore implements Runnable {
        private final Buffer<String> buffer;
        private final String nome;
        
        public Produttore(Buffer<String> buffer, String nome) {
            this.buffer = buffer;
            this.nome = nome;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 1; i <= 5; i++) {
                    // Crea un messaggio
                    String messaggio = nome + "-Messaggio-" + i;
                    
                    // Inserisce il messaggio nel buffer
                    buffer.put(messaggio);
                    
                    // Simula il tempo di produzione
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nome + " interrotto.");
            }
        }
    }
    
    /**
     * Classe che rappresenta un consumatore che preleva dati dal buffer e li elabora.
     */
    static class Consumatore implements Runnable {
        private final Buffer<String> buffer;
        private final String nome;
        
        public Consumatore(Buffer<String> buffer, String nome) {
            this.buffer = buffer;
            this.nome = nome;
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    // Preleva un messaggio dal buffer
                    String messaggio = buffer.get();
                    
                    // Elabora il messaggio
                    System.out.println(nome + ": elaborazione di " + messaggio);
                    
                    // Simula il tempo di elaborazione
                    Thread.sleep((long) (Math.random() * 2000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println(nome + " interrotto.");
            }
        }
    }
}