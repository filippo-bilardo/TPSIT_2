/**
 * Esempio avanzato: Problema Produttore-Consumatore con buffer a capacità multipla
 * 
 * Questa versione avanzata implementa il classico problema del produttore-consumatore
 * con un buffer a capacità multipla e supporto per più produttori e consumatori.
 * Utilizza i metodi wait() e notify() per la sincronizzazione.
 * 
 * Caratteristiche:
 * - Buffer con capacità configurabile
 * - Supporto per più produttori e consumatori
 * - Statistiche di esecuzione
 * - Visualizzazione dello stato del buffer
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ES04_ProduttoreConsumatore_Avanzato {
    public static void main(String[] args) {
        System.out.println("Dimostrazione avanzata del problema Produttore-Consumatore");
        System.out.println("======================================================\n");
        
        // Parametri configurabili
        int capacitaBuffer = 5;
        int numProduttori = 3;
        int numConsumatori = 2;
        int numProduzioniPerProduttore = 5;
        
        // Creiamo un buffer condiviso con capacità specificata
        BufferAvanzato buffer = new BufferAvanzato(capacitaBuffer);
        
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
            produttori[i] = new Thread(new ProduttoreAvanzato(buffer, numProduzioniPerProduttore));
            produttori[i].setName("Produttore-" + (i+1));
        }
        
        // Creiamo e avviamo i thread consumatori
        for (int i = 0; i < numConsumatori; i++) {
            consumatori[i] = new Thread(new ConsumatoreAvanzato(buffer, totaleElementi / numConsumatori + (i < totaleElementi % numConsumatori ? 1 : 0)));
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
        System.out.println("- Attese produttori: " + buffer.getAtteseProduttori());
        System.out.println("- Attese consumatori: " + buffer.getAtteseConsumatori());
        
        System.out.println("\nProduzione e consumo completati!");
    }
}

/**
 * Classe che rappresenta il buffer avanzato condiviso tra produttori e consumatori
 */
class BufferAvanzato {
    private final Queue<Integer> dati;      // Coda per memorizzare i dati
    private final int capacita;            // Capacità massima del buffer
    
    // Contatori per le statistiche
    private int elementiProdotti = 0;
    private int elementiConsumati = 0;
    private int atteseProduttori = 0;
    private int atteseConsumatori = 0;
    
    public BufferAvanzato(int capacita) {
        this.capacita = capacita;
        this.dati = new LinkedList<>();
    }
    
    /**
     * Metodo utilizzato dai produttori per inserire un dato nel buffer
     */
    public synchronized void produci(int valore) {
        // Attendi se il buffer è pieno
        while (dati.size() == capacita) {
            try {
                atteseProduttori++;
                System.out.println(Thread.currentThread().getName() + ": buffer pieno, in attesa... [" + getStatoBuffer() + "]");
                wait(); // Mette il thread in attesa finché il buffer non ha spazio
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(Thread.currentThread().getName() + ": interrotto durante l'attesa");
                return;
            }
        }
        
        // Inserisci il dato nel buffer
        dati.add(valore);
        elementiProdotti++;
        
        System.out.println(Thread.currentThread().getName() + ": prodotto valore " + valore + " [" + getStatoBuffer() + "]");
        
        // Notifica tutti i thread in attesa che il buffer è cambiato
        notifyAll();
    }
    
    /**
     * Metodo utilizzato dai consumatori per prelevare un dato dal buffer
     */
    public synchronized int consuma() {
        // Attendi se il buffer è vuoto
        while (dati.isEmpty()) {
            try {
                atteseConsumatori++;
                System.out.println(Thread.currentThread().getName() + ": buffer vuoto, in attesa... [" + getStatoBuffer() + "]");
                wait(); // Mette il thread in attesa finché non viene prodotto un dato
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println(Thread.currentThread().getName() + ": interrotto durante l'attesa");
                return -1;
            }
        }
        
        // Preleva il dato dal buffer
        int valore = dati.poll();
        elementiConsumati++;
        
        System.out.println(Thread.currentThread().getName() + ": consumato valore " + valore + " [" + getStatoBuffer() + "]");
        
        // Notifica tutti i thread in attesa che il buffer è cambiato
        notifyAll();
        
        return valore;
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
    public int getAtteseProduttori() { return atteseProduttori; }
    public int getAtteseConsumatori() { return atteseConsumatori; }
}

/**
 * Classe che rappresenta un produttore avanzato
 */
class ProduttoreAvanzato implements Runnable {
    private final BufferAvanzato buffer;
    private final int numProduzioni;
    private final Random random = new Random();
    
    public ProduttoreAvanzato(BufferAvanzato buffer, int numProduzioni) {
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
            System.err.println(Thread.currentThread().getName() + ": interrotto durante il sonno");
        }
    }
}

/**
 * Classe che rappresenta un consumatore avanzato
 */
class ConsumatoreAvanzato implements Runnable {
    private final BufferAvanzato buffer;
    private final int numConsumi;
    private final Random random = new Random();
    
    public ConsumatoreAvanzato(BufferAvanzato buffer, int numConsumi) {
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
            System.err.println(Thread.currentThread().getName() + ": interrotto durante il sonno");
        }
    }
}