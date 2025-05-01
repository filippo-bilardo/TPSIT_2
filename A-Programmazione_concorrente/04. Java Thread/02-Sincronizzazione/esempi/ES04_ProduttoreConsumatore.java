/**
 * Esempio 04: Problema Produttore-Consumatore
 * 
 * Questo esempio implementa il classico problema del produttore-consumatore
 * utilizzando i metodi wait() e notify() per la sincronizzazione.
 * 
 * Il produttore inserisce dati in un buffer condiviso solo quando è vuoto,
 * mentre il consumatore preleva dati solo quando il buffer è pieno.
 * I metodi wait() e notify() garantiscono la corretta sincronizzazione
 * tra i due thread.
 */
public class ES04_ProduttoreConsumatore {
    public static void main(String[] args) {
        System.out.println("Dimostrazione del problema Produttore-Consumatore");
        System.out.println("===============================================\n");
        
        // Creiamo un buffer condiviso
        Buffer buffer = new Buffer();
        
        // Creiamo e avviamo il thread produttore
        Thread produttore = new Thread(new Produttore(buffer));
        produttore.setName("Produttore");
        
        // Creiamo e avviamo il thread consumatore
        Thread consumatore = new Thread(new Consumatore(buffer));
        consumatore.setName("Consumatore");
        
        // Avviamo i thread
        produttore.start();
        consumatore.start();
        
        // Attendiamo che i thread completino l'esecuzione
        try {
            produttore.join();
            consumatore.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nProduzione e consumo completati!");
    }
}

/**
 * Classe che rappresenta il buffer condiviso tra produttore e consumatore
 */
class Buffer {
    private int dato;           // Il dato condiviso
    private boolean disponibile = false;  // Flag che indica se il dato è disponibile
    
    /**
     * Metodo utilizzato dal produttore per inserire un dato nel buffer
     */
    public synchronized void produci(int valore) {
        // Attendi se il buffer è pieno (dato disponibile)
        while (disponibile) {
            try {
                wait(); // Mette il thread in attesa finché il buffer non viene svuotato
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrotto durante l'attesa");
                return;
            }
        }
        
        // Inserisci il dato nel buffer
        dato = valore;
        disponibile = true;
        
        System.out.println(Thread.currentThread().getName() + ": prodotto valore " + valore);
        
        // Notifica il consumatore che c'è un dato disponibile
        notify();
    }
    
    /**
     * Metodo utilizzato dal consumatore per prelevare un dato dal buffer
     */
    public synchronized int consuma() {
        // Attendi se il buffer è vuoto (dato non disponibile)
        while (!disponibile) {
            try {
                wait(); // Mette il thread in attesa finché non viene prodotto un dato
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread interrotto durante l'attesa");
                return -1;
            }
        }
        
        // Preleva il dato dal buffer
        disponibile = false;
        
        System.out.println(Thread.currentThread().getName() + ": consumato valore " + dato);
        
        // Notifica il produttore che il buffer è stato svuotato
        notify();
        
        return dato;
    }
}

/**
 * Classe che rappresenta il produttore
 */
class Produttore implements Runnable {
    private final Buffer buffer;
    private final int NUM_PRODUZIONI = 10;
    
    public Produttore(Buffer buffer) {
        this.buffer = buffer;
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= NUM_PRODUZIONI; i++) {
            // Produci un valore
            buffer.produci(i);
            
            // Simula un tempo di elaborazione
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Produttore interrotto durante il sonno");
                return;
            }
        }
    }
}

/**
 * Classe che rappresenta il consumatore
 */
class Consumatore implements Runnable {
    private final Buffer buffer;
    private final int NUM_CONSUMI = 10;
    
    public Consumatore(Buffer buffer) {
        this.buffer = buffer;
    }
    
    @Override
    public void run() {
        for (int i = 1; i <= NUM_CONSUMI; i++) {
            // Consuma un valore
            int valore = buffer.consuma();
            
            // Simula un tempo di elaborazione
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumatore interrotto durante il sonno");
                return;
            }
        }
    }
}