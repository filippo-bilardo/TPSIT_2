/**
 * Esempio 01: Utilizzo Base delle Variabili Atomiche
 * 
 * Questo esempio dimostra l'utilizzo delle principali classi atomiche di Java:
 * - AtomicInteger, AtomicLong, AtomicBoolean
 * - AtomicReference
 * - Operazioni atomiche (get, set, getAndSet, compareAndSet)
 * - Operazioni aritmetiche atomiche (incrementAndGet, decrementAndGet, addAndGet)
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;

public class AtomicVariablesBase {
    // Numero di thread per i test
    private static final int NUM_THREADS = 5;
    // Numero di operazioni per thread
    private static final int OPERATIONS_PER_THREAD = 1000;
    
    // Contatori per confronto
    private static int contatoreSemplice = 0;
    private static final AtomicInteger contatoreAtomico = new AtomicInteger(0);
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dell'utilizzo delle Variabili Atomiche");
        System.out.println("===================================================");
        
        // Esempio 1: Confronto tra contatore semplice e atomico
        System.out.println("\n1. Confronto tra contatore semplice e atomico:");
        testContatori();
        
        // Esempio 2: Operazioni atomiche di base
        System.out.println("\n2. Operazioni atomiche di base:");
        testOperazioniBase();
        
        // Esempio 3: Operazioni aritmetiche atomiche
        System.out.println("\n3. Operazioni aritmetiche atomiche:");
        testOperazioniAritmetiche();
        
        // Esempio 4: AtomicReference
        System.out.println("\n4. AtomicReference:");
        testAtomicReference();
        
        System.out.println("\nVantaggi dell'utilizzo delle Variabili Atomiche:");
        System.out.println("1. Operazioni thread-safe senza bisogno di sincronizzazione esplicita");
        System.out.println("2. Migliori prestazioni rispetto ai blocchi synchronized");
        System.out.println("3. Eliminazione del rischio di deadlock");
        System.out.println("4. Supporto per operazioni atomiche complesse (CAS)");
    }
    
    /**
     * Test di confronto tra contatore semplice e contatore atomico
     */
    private static void testContatori() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        System.out.println("Incremento concorrente di contatori:");
        
        // Reset dei contatori
        contatoreSemplice = 0;
        contatoreAtomico.set(0);
        
        // Avvia più thread che incrementano entrambi i contatori
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    // Incremento non atomico (non thread-safe)
                    contatoreSemplice++;
                    
                    // Incremento atomico (thread-safe)
                    contatoreAtomico.incrementAndGet();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        int totaleAtteso = NUM_THREADS * OPERATIONS_PER_THREAD;
        System.out.println("- Valore atteso: " + totaleAtteso);
        System.out.println("- Contatore semplice (non thread-safe): " + contatoreSemplice);
        System.out.println("- Contatore atomico (thread-safe): " + contatoreAtomico.get());
        
        // Verifica se il contatore semplice ha perso aggiornamenti
        if (contatoreSemplice < totaleAtteso) {
            System.out.println("  → Il contatore semplice ha perso " + 
                    (totaleAtteso - contatoreSemplice) + " incrementi a causa di race condition");
        }
    }
    
    /**
     * Test delle operazioni atomiche di base
     */
    private static void testOperazioniBase() {
        System.out.println("Dimostrazione delle operazioni atomiche di base:");
        
        AtomicInteger atomic = new AtomicInteger(100);
        System.out.println("- Valore iniziale: " + atomic.get());
        
        // Operazione set
        atomic.set(200);
        System.out.println("- Dopo set(200): " + atomic.get());
        
        // Operazione getAndSet
        int vecchioValore = atomic.getAndSet(300);
        System.out.println("- getAndSet(300) ha restituito: " + vecchioValore);
        System.out.println("- Nuovo valore: " + atomic.get());
        
        // Operazione compareAndSet
        boolean successo = atomic.compareAndSet(300, 400); // Dovrebbe avere successo
        System.out.println("- compareAndSet(300, 400): " + (successo ? "successo" : "fallito"));
        System.out.println("- Valore dopo CAS: " + atomic.get());
        
        successo = atomic.compareAndSet(300, 500); // Dovrebbe fallire
        System.out.println("- compareAndSet(300, 500): " + (successo ? "successo" : "fallito"));
        System.out.println("- Valore dopo CAS fallito: " + atomic.get());
        
        // Operazione lazySet (rilassata, utile in alcuni contesti di performance)
        atomic.lazySet(600);
        System.out.println("- Dopo lazySet(600): " + atomic.get());
    }
    
    /**
     * Test delle operazioni aritmetiche atomiche
     */
    private static void testOperazioniAritmetiche() {
        System.out.println("Dimostrazione delle operazioni aritmetiche atomiche:");
        
        AtomicInteger atomic = new AtomicInteger(100);
        System.out.println("- Valore iniziale: " + atomic.get());
        
        // Incremento
        System.out.println("- incrementAndGet(): " + atomic.incrementAndGet());
        System.out.println("- getAndIncrement(): " + atomic.getAndIncrement() + 
                " (valore dopo: " + atomic.get() + ")");
        
        // Decremento
        System.out.println("- decrementAndGet(): " + atomic.decrementAndGet());
        System.out.println("- getAndDecrement(): " + atomic.getAndDecrement() + 
                " (valore dopo: " + atomic.get() + ")");
        
        // Addizione
        System.out.println("- addAndGet(50): " + atomic.addAndGet(50));
        System.out.println("- getAndAdd(25): " + atomic.getAndAdd(25) + 
                " (valore dopo: " + atomic.get() + ")");
        
        // Operazioni con funzioni (Java 8+)
        System.out.println("- updateAndGet(x -> x * 2): " + 
                atomic.updateAndGet(x -> x * 2));
        System.out.println("- getAndUpdate(x -> x / 2): " + 
                atomic.getAndUpdate(x -> x / 2) + 
                " (valore dopo: " + atomic.get() + ")");
        
        // Operazioni con accumulatori
        System.out.println("- accumulateAndGet(5, (x,y) -> x*y): " + 
                atomic.accumulateAndGet(5, (x, y) -> x * y));
        System.out.println("- getAndAccumulate(10, (x,y) -> x+y): " + 
                atomic.getAndAccumulate(10, (x, y) -> x + y) + 
                " (valore dopo: " + atomic.get() + ")");
    }
    
    /**
     * Test di AtomicReference
     */
    private static void testAtomicReference() throws InterruptedException {
        System.out.println("Dimostrazione di AtomicReference:");
        
        // Creiamo un riferimento atomico a un oggetto Persona
        final AtomicReference<Persona> riferimentoAtomico = 
                new AtomicReference<>(new Persona("Mario", 30));
        
        System.out.println("- Valore iniziale: " + riferimentoAtomico.get());
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Avvia più thread che tentano di aggiornare l'oggetto atomicamente
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    // Aggiornamento atomico con CAS
                    while (true) {
                        Persona personaCorrente = riferimentoAtomico.get();
                        Persona nuovaPersona = new Persona(
                                personaCorrente.getNome(), 
                                personaCorrente.getEta() + 1);
                        
                        if (riferimentoAtomico.compareAndSet(personaCorrente, nuovaPersona)) {
                            // Aggiornamento riuscito
                            break;
                        }
                        // Altrimenti riprova (qualcun altro ha modificato il riferimento)
                    }
                }
                System.out.println("Thread " + threadId + " ha completato gli aggiornamenti");
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        
        System.out.println("- Valore finale: " + riferimentoAtomico.get());
        System.out.println("- Età finale attesa: " + (30 + NUM_THREADS * 100));
    }
    
    /**
     * Classe di esempio per AtomicReference
     */
    private static class Persona {
        private final String nome;
        private final int eta;
        
        public Persona(String nome, int eta) {
            this.nome = nome;
            this.eta = eta;
        }
        
        public String getNome() {
            return nome;
        }
        
        public int getEta() {
            return eta;
        }
        
        @Override
        public String toString() {
            return nome + " (" + eta + " anni)";
        }
    }
}