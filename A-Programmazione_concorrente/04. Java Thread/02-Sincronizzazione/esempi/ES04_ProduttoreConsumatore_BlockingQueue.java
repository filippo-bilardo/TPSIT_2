/**
 * Esempio: Problema Produttore-Consumatore con BlockingQueue
 * 
 * Questa implementazione utilizza BlockingQueue, un'interfaccia della libreria
 * java.util.concurrent che fornisce operazioni bloccanti per inserire e rimuovere elementi.
 * 
 * Vantaggi di BlockingQueue:
 * - Implementazione thread-safe già pronta
 * - Metodi bloccanti (put/take) che gestiscono automaticamente l'attesa
 * - Metodi non bloccanti con timeout (offer/poll)
 * - Diverse implementazioni disponibili (ArrayBlockingQueue, LinkedBlockingQueue, ecc.)
 */
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ES04_ProduttoreConsumatore_BlockingQueue {
    public static void main(String[] args) {
        System.out.println("Dimostrazione del problema Produttore-Consumatore con BlockingQueue");
        System.out.println("===================================================================\n");
        
        // Parametri configurabili
        int capacitaBuffer = 3;
        int numProduttori = 2;
        int numConsumatori = 2;
        int numProduzioniPerProduttore = 5;
        
        // Creiamo un buffer condiviso con BlockingQueue
        // ArrayBlockingQueue è una implementazione basata su array con capacità fissa
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(capacitaBuffer);
        
        // Contatori atomici per le statistiche
        AtomicInteger elementiProdotti = new AtomicInteger(0);
        AtomicInteger elementiConsumati = new AtomicInteger(0);
        
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
            produttori[i] = new Thread(new ProduttoreQueue(queue, numProduzioniPerProduttore, elementiProdotti));
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
            
            consumatori[i] = new Thread(new ConsumatoreQueue(queue, elementiDaConsumarePerThread, elementiConsumati));
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
        System.out.println("- Elementi prodotti: " + elementiProdotti.get());
        System.out.println("- Elementi consumati: " + elementiConsumati.get());
        System.out.println("- Tempo totale di esecuzione: " + tempoTotale + " ms");
        
        System.out.println("\nProduzione e consumo completati!");
    }
}

/**
 * Classe che rappresenta un produttore che utilizza BlockingQueue
 */
class ProduttoreQueue implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int numProduzioni;
    private final AtomicInteger contatoreProdotti;
    private final Random random = new Random();
    
    public ProduttoreQueue(BlockingQueue<Integer> queue, int numProduzioni, AtomicInteger contatoreProdotti) {
        this.queue = queue;
        this.numProduzioni = numProduzioni;
        this.contatoreProdotti = contatoreProdotti;
    }
    
    @Override
    public void run() {
        try {
            for (int i = 1; i <= numProduzioni; i++) {
                // Genera un valore casuale da produrre
                int valore = random.nextInt(100);
                
                // Il metodo put() blocca se la coda è piena
                System.out.println(Thread.currentThread().getName() + ": tentativo di produrre " + valore + 
                                   " [" + queue.size() + "/" + queue.remainingCapacity() + "]");
                queue.put(valore);
                
                // Incrementa il contatore atomico
                contatoreProdotti.incrementAndGet();
                
                System.out.println(Thread.currentThread().getName() + ": prodotto valore " + valore + 
                                   " [" + queue.size() + "/" + (queue.size() + queue.remainingCapacity()) + "]");
                
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
 * Classe che rappresenta un consumatore che utilizza BlockingQueue
 */
class ConsumatoreQueue implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int numConsumi;
    private final AtomicInteger contatoreConsumati;
    private final Random random = new Random();
    
    public ConsumatoreQueue(BlockingQueue<Integer> queue, int numConsumi, AtomicInteger contatoreConsumati) {
        this.queue = queue;
        this.numConsumi = numConsumi;
        this.contatoreConsumati = contatoreConsumati;
    }
    
    @Override
    public void run() {
        try {
            int somma = 0;
            for (int i = 1; i <= numConsumi; i++) {
                // Il metodo take() blocca se la coda è vuota
                System.out.println(Thread.currentThread().getName() + ": tentativo di consumare" + 
                                   " [" + queue.size() + "/" + (queue.size() + queue.remainingCapacity()) + "]");
                int valore = queue.take();
                
                // Incrementa il contatore atomico
                contatoreConsumati.incrementAndGet();
                
                somma += valore;
                System.out.println(Thread.currentThread().getName() + ": consumato valore " + valore + 
                                   " [" + queue.size() + "/" + (queue.size() + queue.remainingCapacity()) + "]");
                
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