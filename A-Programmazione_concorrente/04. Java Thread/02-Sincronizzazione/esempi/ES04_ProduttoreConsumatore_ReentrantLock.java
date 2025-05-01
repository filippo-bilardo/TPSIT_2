/**
 * Esempio: Problema Produttore-Consumatore con ReentrantLock e Condition
 * 
 * Questa implementazione utilizza ReentrantLock e Condition invece di
 * synchronized, wait() e notify() per gestire la sincronizzazione.
 * 
 * Vantaggi di ReentrantLock rispetto a synchronized:
 * - Possibilità di interrompere l'attesa (lockInterruptibly)
 * - Possibilità di tentare l'acquisizione del lock con timeout
 * - Supporto per condizioni multiple (Condition)
 * - Fairness configurabile (ordine FIFO per i thread in attesa)
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ES04_ProduttoreConsumatore_ReentrantLock {
    public static void main(String[] args) {
        System.out.println("Dimostrazione del problema Produttore-Consumatore con ReentrantLock");
        System.out.println("===================================================================\n");
        
        // Parametri configurabili
        int capacitaBuffer = 3;
        int numProduttori = 2;
        int numConsumatori = 2;
        int numProduzioniPerProduttore = 4;
        
        // Creiamo un buffer condiviso con capacità specificata
        BufferLock buffer = new BufferLock(capacitaBuffer);
        
        // Array per tenere traccia dei thread
        Thread[] produttori = new Thread[numProduttori];
        Thread[] consumatori = new Thread[numConsumatori];
        
        // Calcolo del numero totale di elementi da produrre e consumare
        int totaleElementi = numProduttori * numProduzioniPerProduttore;
        
        System.out.println("Configurazione:");
        System.out.println("- Capacità buffer: " + capacitaBuffer);
        System.out.println("- Numero produttori: " + numProduttori);
        System.out.println("- Numero consumatori: " + numConsumatori);
        System.out.println("- Produzioni per produttore: " + numProduzioniPerProduttore);
        System.out.println("- Totale elementi: " + totaleElementi + "\n");
        
        // Creiamo e avviamo i thread produttori
        for (int i = 0; i < numProduttori; i++) {
            produttori[i] = new Thread(new ProduttoreLock(buffer, numProduzioniPerProduttore));
            produttori[i].setName("Produttore-" + (i+1));
        }
        
        // Creiamo e avviamo i thread consumatori
        for (int i = 0; i < numConsumatori; i++) {
            // Distribuiamo equamente gli elementi da consumare
            int elementiDaConsumarePerThread = totaleElementi / numConsumatori;
            // Aggiungiamo gli elementi rimanenti ai primi thread
            if (i < totaleElementi % numConsumatori) {
                elementiDaConsumarePerThread++;
            }
            
            consumatori[i] = new Thread(new ConsumatoreLock(buffer, elementiDaConsumarePerThread));
            consumatori[i].setName("Consumatore-" + (i+1));
        }
        
        // Registriamo il tempo di inizio
        long tempoInizio = System.currentTimeMillis();
        
        // Avviamo tutti i thread
        System.out.println("Avvio dei thread...");
        for (Thread p : produttori) p.start();
        for (Thread c : consumatori) c.start();
        
        // Attendiamo che tutti i thread completino l'esecuzione
        try {
            for (Thread p : produttori) p.join();
            for (Thread c : consumatori) c.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Calcoliamo il tempo totale di esecuzione
        long tempoFine = System.currentTimeMillis();
        long tempoTotale = tempoFine - tempoInizio;
        
        // Stampiamo le statistiche finali
        System.out.println("\nStatistiche finali:");
        System.out.println("- Elementi prodotti: " + buffer.getElementiProdotti());
        System.out.println("- Elementi consumati: " + buffer.getElementiConsumati());
        System.out.println("- Tempo totale di esecuzione: " + tempoTotale + " ms");
        
        System.out.println("\nProduzione e consumo completati!");
    }
}

/**
 * Classe che rappresenta il buffer condiviso tra produttori e consumatori
 * utilizzando ReentrantLock e Condition per la sincronizzazione
 */
class BufferLock {
    private final Queue<Integer> dati;      // Coda per memorizzare i dati
    private final int capacita;            // Capacità massima del buffer
    
    // Lock e condizioni per la sincronizzazione
    private final ReentrantLock lock = new ReentrantLock(true); // true = fair lock (FIFO)
    private final Condition nonPieno = lock.newCondition();    // Condizione: buffer non pieno
    private final Condition nonVuoto = lock.newCondition();    // Condizione: buffer non vuoto
    
    // Contatori per le statistiche
    private int elementiProdotti = 0;
    private int elementiConsumati = 0;
    
    public BufferLock(int capacita) {
        this.capacita = capacita;
        this.dati = new LinkedList<>();
    }
    
    /**
     * Metodo utilizzato dai produttori per inserire un dato nel buffer
     */
    public void produci(int valore) throws InterruptedException {
        lock.lock(); // Acquisisce il lock
        try {
            // Attendi se il buffer è pieno
            while (dati.size() == capacita) {
                System.out.println(Thread.currentThread().getName() + ": buffer pieno, in attesa... [" + getStatoBuffer() + "]");
                nonPieno.await(); // Attende sulla condizione nonPieno
            }
            
            // Inserisci il dato nel buffer
            dati.add(valore);
            elementiProdotti++;
            
            System.out.println(Thread.currentThread().getName() + ": prodotto valore " + valore + " [" + getStatoBuffer() + "]");
            
            // Segnala ai consumatori che il buffer non è più vuoto
            nonVuoto.signalAll();
        } finally {
            lock.unlock(); // Rilascia il lock in ogni caso
        }
    }
    
    /**
     * Metodo utilizzato dai consumatori per prelevare un dato dal buffer
     */
    public int consuma() throws InterruptedException {
        lock.lock(); // Acquisisce il lock
        try {
            // Attendi se il buffer è vuoto
            while (dati.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + ": buffer vuoto, in attesa... [" + getStatoBuffer() + "]");
                nonVuoto.await(); // Attende sulla condizione nonVuoto
            }
            
            // Preleva il dato dal buffer
            int valore = dati.poll();
            elementiConsumati++;
            
            System.out.println(Thread.currentThread().getName() + ": consumato valore " + valore + " [" + getStatoBuffer() + "]");
            
            // Segnala ai produttori che il buffer non è più pieno
            nonPieno.signalAll();
            
            return valore;
        } finally {
            lock.unlock(); // Rilascia il lock in ogni caso
        }
    }
    
    /**
     * Restituisce una rappresentazione testuale dello stato attuale del buffer
     */
    private String getStatoBuffer() {
        return dati.size() + "/" + capacita;
    }
    
    // Metodi getter per le statistiche
    public int getElementiProdotti() { return elementiProdotti; }
    public int getElementiConsumati() { return elementiConsumati; }
}

/**
 * Classe che rappresenta un produttore che utilizza il buffer con lock
 */
class ProduttoreLock implements Runnable {
    private final BufferLock buffer;
    private final int numProduzioni;
    private final Random random = new Random();
    
    public ProduttoreLock(BufferLock buffer, int numProduzioni) {
        this.buffer = buffer;
        this.numProduzioni = numProduzioni;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= numProduzioni; i++) {
                // Genera un valore casuale da produrre
                int valore = random.nextInt(100);
                
                // Produci il valore
                buffer.produci(valore);
                
                // Simula un tempo di elaborazione variabile
                Thread.sleep(random.nextInt(500) + 100);
            }
            System.out.println(Thread.currentThread().getName() + ": ha completato tutte le produzioni.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + ": interrotto");
        }
    }
}

/**
 * Classe che rappresenta un consumatore che utilizza il buffer con lock
 */
class ConsumatoreLock implements Runnable {
    private final BufferLock buffer;
    private final int numConsumi;
    private final Random random = new Random();
    
    public ConsumatoreLock(BufferLock buffer, int numConsumi) {
        this.buffer = buffer;
        this.numConsumi = numConsumi;
    }
    
    @Override
    public void run() {
        try {
            int somma = 0;
            for (int i = 1; i <= numConsumi; i++) {
                // Consuma un valore
                int valore = buffer.consuma();
                somma += valore;
                
                // Simula un tempo di elaborazione variabile
                Thread.sleep(random.nextInt(800) + 200);
            }
            System.out.println(Thread.currentThread().getName() + ": ha completato tutti i consumi. Somma totale: " + somma);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println(Thread.currentThread().getName() + ": interrotto");
        }
    }
}