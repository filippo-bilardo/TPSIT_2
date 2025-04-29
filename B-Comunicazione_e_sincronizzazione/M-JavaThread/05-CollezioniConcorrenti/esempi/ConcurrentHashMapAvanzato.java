/**
 * Esempio 02: Utilizzo Avanzato di ConcurrentHashMap
 * 
 * Questo esempio dimostra le funzionalità avanzate di ConcurrentHashMap:
 * - Operazioni atomiche (compute, merge, putIfAbsent)
 * - Operazioni in bulk (forEach, search, reduce)
 * - Contatori e accumulatori integrati
 * - Gestione della concorrenza con alta contesa
 */
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class ConcurrentHashMapAvanzato {
    // Numero di thread per i test
    private static final int NUM_THREADS = 8;
    // Numero di operazioni per thread
    private static final int OPERATIONS_PER_THREAD = 1000;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione delle funzionalità avanzate di ConcurrentHashMap");
        System.out.println("===========================================================");
        
        // Esempio 1: Operazioni atomiche
        System.out.println("\n1. Operazioni Atomiche:");
        testOperazioniAtomiche();
        
        // Esempio 2: Operazioni in bulk
        System.out.println("\n2. Operazioni in Bulk:");
        testOperazioniBulk();
        
        // Esempio 3: Contatori e accumulatori
        System.out.println("\n3. Contatori e Accumulatori:");
        testContatoriAccumulatori();
        
        // Esempio 4: Gestione alta contesa
        System.out.println("\n4. Gestione Alta Contesa:");
        testAltaContesa();
        
        System.out.println("\nVantaggi delle operazioni atomiche di ConcurrentHashMap:");
        System.out.println("1. Eliminazione delle race condition nelle operazioni read-modify-write");
        System.out.println("2. Migliori prestazioni rispetto all'uso di blocchi synchronized esterni");
        System.out.println("3. Maggiore espressività e leggibilità del codice");
        System.out.println("4. Supporto per operazioni funzionali in stile Java 8+");
    }
    
    /**
     * Test delle operazioni atomiche di ConcurrentHashMap
     */
    private static void testOperazioniAtomiche() throws InterruptedException {
        final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        System.out.println("Test delle operazioni atomiche:");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        
        // Inizializza alcuni valori
        map.put("contatore1", 0);
        map.put("contatore2", 0);
        map.put("contatore3", 0);
        
        // Avvia più thread che eseguono operazioni atomiche
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        // compute: aggiorna un valore in modo atomico
                        map.compute("contatore1", (k, v) -> (v == null) ? 1 : v + 1);
                        
                        // putIfAbsent: inserisce solo se la chiave non esiste
                        map.putIfAbsent("nuovaChiave" + j, j);
                        
                        // merge: combina il valore esistente con uno nuovo
                        map.merge("contatore2", 1, Integer::sum);
                        
                        // computeIfPresent: aggiorna solo se la chiave esiste
                        map.computeIfPresent("contatore3", (k, v) -> v + 1);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        System.out.println("- Valore contatore1 (compute): " + map.get("contatore1"));
        System.out.println("- Valore contatore2 (merge): " + map.get("contatore2"));
        System.out.println("- Valore contatore3 (computeIfPresent): " + map.get("contatore3"));
        System.out.println("- Numero di chiavi nuovaChiave*: " + 
                map.keySet().stream().filter(k -> k.startsWith("nuovaChiave")).count());
    }
    
    /**
     * Test delle operazioni in bulk di ConcurrentHashMap
     */
    private static void testOperazioniBulk() {
        final ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        
        // Popola la mappa con dati di test
        for (int i = 0; i < 1000; i++) {
            map.put("chiave" + i, i);
        }
        
        System.out.println("Test delle operazioni in bulk:");
        
        // forEach: esegue un'azione per ogni elemento
        System.out.println("- forEach: Conteggio elementi con valore > 900");
        final AtomicInteger contatore = new AtomicInteger(0);
        map.forEach(1, (k, v) -> {
            if (v > 900) {
                contatore.incrementAndGet();
            }
        });
        System.out.println("  Elementi con valore > 900: " + contatore.get());
        
        // search: cerca un elemento che soddisfa una condizione
        System.out.println("- search: Ricerca primo elemento con valore divisibile per 123");
        String risultatoRicerca = map.search(1, (k, v) -> v % 123 == 0 ? k : null);
        System.out.println("  Primo elemento trovato: " + risultatoRicerca + 
                " (valore: " + (risultatoRicerca != null ? map.get(risultatoRicerca) : "nessuno") + ")");
        
        // reduce: combina tutti gli elementi
        System.out.println("- reduce: Calcolo della somma di tutti i valori");
        Integer somma = map.reduce(1, 
                (k, v) -> v, // trasformazione
                Integer::sum); // riduzione
        System.out.println("  Somma di tutti i valori: " + somma);
    }
    
    /**
     * Test dei contatori e accumulatori integrati in ConcurrentHashMap
     */
    private static void testContatoriAccumulatori() throws InterruptedException {
        final ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        
        System.out.println("Test dei contatori e accumulatori:");
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        CountDownLatch latch = new CountDownLatch(NUM_THREADS);
        
        // Avvia più thread che incrementano contatori
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.execute(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        // Incrementa o crea contatori per diverse categorie
                        String categoria = "categoria" + (j % 5);
                        map.compute(categoria, (k, v) -> (v == null) ? 1L : v + 1L);
                        
                        // Accumula valori per thread
                        String threadKey = "thread" + threadId;
                        map.compute(threadKey, (k, v) -> (v == null) ? j : v + j);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        executor.shutdown();
        
        // Stampa statistiche per categoria
        System.out.println("- Conteggi per categoria:");
        for (int i = 0; i < 5; i++) {
            System.out.println("  categoria" + i + ": " + map.get("categoria" + i));
        }
        
        // Calcola e stampa il totale
        long totale = map.reduceValues(1, Long::sum);
        System.out.println("- Totale di tutti i contatori: " + totale);
    }
    
    /**
     * Test della gestione di alta contesa in ConcurrentHashMap
     */
    private static void testAltaContesa() throws InterruptedException {
        // Creiamo una mappa con un fattore di concorrenza elevato
        final ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>(16, 0.75f, 32);
        
        System.out.println("Test della gestione di alta contesa:");
        System.out.println("- Utilizzo di un numero limitato di chiavi con molti thread");
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS * 2); // Più thread
        CountDownLatch latch = new CountDownLatch(NUM_THREADS * 2);
        
        long startTime = System.currentTimeMillis();
        
        // Avvia molti thread che aggiornano le stesse chiavi (alta contesa)
        for (int i = 0; i < NUM_THREADS * 2; i++) {
            executor.execute(() -> {
                try {
                    for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                        // Solo 10 chiavi diverse (alta contesa)
                        String chiave = "chiaveContesa" + (j % 10);
                        map.compute(chiave, (k, v) -> (v == null) ? 1L : v + 1L);
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        latch.await();
        long endTime = System.currentTimeMillis();
        executor.shutdown();
        
        // Verifica i risultati
        System.out.println("- Tempo di esecuzione: " + (endTime - startTime) + " ms");
        System.out.println("- Valori finali delle chiavi contese:");
        for (int i = 0; i < 10; i++) {
            System.out.println("  chiaveContesa" + i + ": " + map.get("chiaveContesa" + i));
        }
        
        // Verifica che il totale sia corretto
        long totaleAtteso = (long) NUM_THREADS * 2 * OPERATIONS_PER_THREAD;
        long totaleEffettivo = map.reduceValues(1, Long::sum);
        System.out.println("- Totale operazioni attese: " + totaleAtteso);
        System.out.println("- Totale operazioni effettive: " + totaleEffettivo);
    }
}