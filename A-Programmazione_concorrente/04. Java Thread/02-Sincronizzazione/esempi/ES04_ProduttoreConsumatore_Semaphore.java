/**
 * Esempio: Problema Produttore-Consumatore con Semaphore
 * 
 * Questa implementazione utilizza i semafori (java.util.concurrent.Semaphore)
 * per gestire la sincronizzazione tra produttori e consumatori.
 * 
 * Vantaggi dei semafori:
 * - Controllo preciso sul numero di thread che possono accedere a una risorsa
 * - Possibilità di implementare facilmente buffer con capacità limitata
 * - Supporto per operazioni con timeout
 * - Fairness configurabile
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class ES04_ProduttoreConsumatore_Semaphore {
    public static void main(String[] args) {
        System.out.println("Dimostrazione del problema Produttore-Consumatore con Semaphore");
        System.out.println("================================================================\n");
        
        // Parametri configurabili
        int capacitaBuffer = 4;
        int numProduttori = 3;
        int numConsumatori = 2;
        int numProduzioniPerProduttore = 4;
        
        // Creiamo un buffer condiviso con semafori
        BufferSemaphore buffer = new BufferSemaphore(capacitaBuffer);
        
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
            produttori[i] = new Thread(new ProduttoreSemaphore(buffer, numProduzioniPerProduttore));
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
            
            consumatori[i] = new Thread(new ConsumatoreSemaphore(buffer, elementiDaConsumarePerThread));
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
 * utilizzando semafori per la sincronizzazione
 */
class BufferSemaphore {
    private final Queue<Integer> dati = new LinkedList<>();
    private final int capacita;
    
    // Semafori per la sincronizzazione
    private final Semaphore semaforoPosti;     // Controlla i posti disponibili nel buffer
    private final Semaphore semaforoDati;      // Controlla i dati disponibili nel buffer
    private final Semaphore mutexAccesso;      // Garantisce l'accesso esclusivo al buffer
    
    // Contatori per le statistiche
    private final AtomicInteger elementiProdotti = new AtomicInteger(0);
    private final AtomicInteger elementiConsumati = new AtomicInteger(0);
    
    public BufferSemaphore(int capacita) {
        this.capacita = capacita;
        
        // Inizializza i semafori:
        // - semaforoPosti: inizialmente ci sono 'capacita' posti liberi
        // - semaforoDati: inizialmente non ci sono dati disponibili
        // - mutexAccesso: garantisce che un solo thread alla volta acceda al buffer
        semaforoPosti = new Semaphore(capacita, true); // true = fair (FIFO)
        semaforoDati = new Semaphore(0, true);         // true = fair (FIFO)
        mutexAccesso = new Semaphore(1);               // Semaforo binario (mutex)
    }
    
    /**
     * Metodo utilizzato dai produttori per inserire un dato nel buffer
     */
    public void produci(int valore) throws InterruptedException {
        // Acquisisce un permesso dal semaforo dei posti (attende se il buffer è pieno)
        System.out.println(Thread.currentThread().getName() + ": tentativo di acquisire un posto nel buffer...");
        semaforoPosti.acquire();
        
        try {
            // Acquisisce l'accesso esclusivo al buffer
            mutexAccesso.acquire();
            try {
                // Inserisce il dato nel buffer
                dati.add(valore);
                elementiProdotti.incrementAndGet();
                
                System.out.println(Thread.currentThread().getName() + ": prodotto valore " + valore + 
                                   " [" + dati.size() + "/" + capacita + "]");
            } finally {
                // Rilascia l'accesso esclusivo al buffer
                mutexAccesso.release();
            }
        } catch (InterruptedException e) {
            // In caso di interruzione, rilascia il permesso acquisito
            semaforoPosti.release();
            throw e;
        }
        
        // Rilascia un permesso al semaforo dei dati (segnala che c'è un nuovo dato)
        semaforoDati.release();
    }
    
    /**
     * Metodo utilizzato dai consumatori per prelevare un dato dal buffer
     */
    public int consuma() throws InterruptedException {
        // Acquisisce un permesso dal semaforo dei dati (attende se il buffer è vuoto)
        System.out.println(Thread.currentThread().getName() + ": tentativo di acquisire un dato dal buffer...");
        semaforoDati.acquire();
        
        int valore;
        try {
            // Acquisisce l'accesso esclusivo al buffer
            mutexAccesso.acquire();
            try {
                // Preleva il dato dal buffer
                valore = dati.poll();
                elementiConsumati.incrementAndGet();
                
                System.out.println(Thread.currentThread().getName() + ": consumato valore " + valore + 
                                   " [" + dati.size() + "/" + capacita + "]");
            } finally {
                // Rilascia l'accesso esclusivo al buffer
                mutexAccesso.release();
            }
        } catch (InterruptedException e) {
            // In caso di interruzione, rilascia il permesso acquisito
            semaforoDati.release();
            throw e;
        }
        
        // Rilascia un permesso al semaforo dei posti (segnala che c'è un posto libero)
        semaforoPosti.release();
        
        return valore;
    }
    
    // Metodi getter per le statistiche
    public int getElementiProdotti() { return elementiProdotti.get(); }
    public int getElementiConsumati() { return elementiConsumati.get(); }
}

/**
 * Classe che rappresenta un produttore che utilizza il buffer con semafori
 */
class ProduttoreSemaphore implements Runnable {
    private final BufferSemaphore buffer;
    private final int numProduzioni;
    private final Random random = new Random();
    
    public ProduttoreSemaphore(BufferSemaphore buffer, int numProduzioni) {
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
 * Classe che rappresenta un consumatore che utilizza il buffer con semafori
 */
class ConsumatoreSemaphore implements Runnable {
    private final BufferSemaphore buffer;
    private final int numConsumi;
    private final Random random = new Random();
    
    public ConsumatoreSemaphore(BufferSemaphore buffer, int numConsumi) {
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