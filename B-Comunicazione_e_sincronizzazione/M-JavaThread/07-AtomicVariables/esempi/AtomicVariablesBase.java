/**
 * Esempio 01: Utilizzo Base delle Variabili Atomiche
 * 
 * Questo esempio dimostra l'utilizzo delle principali classi atomiche di Java:
 * - AtomicInteger
 * - AtomicLong
 * - AtomicBoolean
 * - AtomicReference
 * - AtomicIntegerArray
 */
package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
import java.util.function.IntUnaryOperator;

public class AtomicVariablesBase {
    // Numero di thread per i test
    private static final int NUM_THREADS = 5;
    // Numero di operazioni per thread
    private static final int OPERATIONS_PER_THREAD = 1000;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Dimostrazione dell'utilizzo delle Variabili Atomiche");
        System.out.println("=================================================");
        
        // Esempio 1: AtomicInteger
        System.out.println("\n1. AtomicInteger:");
        demoAtomicInteger();
        
        // Esempio 2: AtomicBoolean
        System.out.println("\n2. AtomicBoolean:");
        demoAtomicBoolean();
        
        // Esempio 3: AtomicReference
        System.out.println("\n3. AtomicReference:");
        demoAtomicReference();
        
        // Esempio 4: AtomicIntegerArray
        System.out.println("\n4. AtomicIntegerArray:");
        demoAtomicIntegerArray();
        
        // Esempio 5: Operazioni atomiche avanzate
        System.out.println("\n5. Operazioni atomiche avanzate (updateAndGet, accumulateAndGet):");
        demoAdvancedOperations();
        
        System.out.println("\nVantaggi dell'utilizzo delle Variabili Atomiche:");
        System.out.println("1. Operazioni thread-safe senza bisogno di sincronizzazione esplicita");
        System.out.println("2. Migliori prestazioni rispetto ai lock");
        System.out.println("3. Supporto per operazioni atomiche complesse (CAS)");
        System.out.println("4. Eliminazione del rischio di deadlock");
    }
    
    /**
     * Dimostrazione di AtomicInteger
     */
    private static void demoAtomicInteger() throws InterruptedException {
        // Contatore atomico
        final AtomicInteger atomicCounter = new AtomicInteger(0);
        // Contatore non atomico per confronto
        final Counter nonAtomicCounter = new Counter();
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Avvia thread che incrementano entrambi i contatori
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    // Incremento atomico
                    atomicCounter.incrementAndGet();
                    
                    // Incremento non atomico (non thread-safe)
                    nonAtomicCounter.increment();
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        int expectedValue = NUM_THREADS * OPERATIONS_PER_THREAD;
        System.out.println("Valore AtomicInteger: " + atomicCounter.get());
        System.out.println("Valore contatore non atomico: " + nonAtomicCounter.getValue());
        System.out.println("Valore atteso: " + expectedValue);
        System.out.println("AtomicInteger corretto: " + (atomicCounter.get() == expectedValue));
        System.out.println("Contatore non atomico corretto: " + (nonAtomicCounter.getValue() == expectedValue));
    }
    
    /**
     * Dimostrazione di AtomicBoolean
     */
    private static void demoAtomicBoolean() throws InterruptedException {
        // Flag atomico per controllo di inizializzazione
        final AtomicBoolean initialized = new AtomicBoolean(false);
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Contatore per tracciare quale thread ha eseguito l'inizializzazione
        final AtomicInteger initializerThread = new AtomicInteger(-1);
        
        // Avvia thread che tentano di inizializzare una risorsa
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            executor.execute(() -> {
                // Tenta di impostare il flag da false a true
                // compareAndSet è atomico: controlla se il valore corrente è false
                // e lo imposta a true solo se la condizione è verificata
                if (initialized.compareAndSet(false, true)) {
                    // Solo un thread riuscirà ad eseguire questo blocco
                    initializerThread.set(threadId);
                    System.out.println("Inizializzazione eseguita dal thread " + threadId);
                    
                    // Simulazione di un'operazione di inizializzazione
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    // Gli altri thread salteranno l'inizializzazione
                    System.out.println("Thread " + threadId + ": inizializzazione già eseguita");
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("Stato finale di initialized: " + initialized.get());
        System.out.println("Thread che ha eseguito l'inizializzazione: " + initializerThread.get());
    }
    
    /**
     * Dimostrazione di AtomicReference
     */
    private static void demoAtomicReference() throws InterruptedException {
        // Riferimento atomico a un oggetto User
        final AtomicReference<User> userRef = new AtomicReference<>(new User("utente1", 25));
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Thread che aggiorna l'età solo se il nome è "utente1"
        executor.execute(() -> {
            User currentUser = userRef.get();
            if ("utente1".equals(currentUser.getName())) {
                User newUser = new User(currentUser.getName(), 30);
                boolean success = userRef.compareAndSet(currentUser, newUser);
                System.out.println("Thread 1: Tentativo di aggiornamento età a 30 - " + 
                                  (success ? "Successo" : "Fallimento"));
            }
        });
        
        // Thread che tenta di cambiare l'utente completamente
        executor.execute(() -> {
            try {
                // Piccolo ritardo per aumentare la probabilità di concorrenza
                Thread.sleep(50);
                
                User currentUser = userRef.get();
                User newUser = new User("utente2", 40);
                boolean success = userRef.compareAndSet(currentUser, newUser);
                System.out.println("Thread 2: Tentativo di cambio utente - " + 
                                  (success ? "Successo" : "Fallimento"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        User finalUser = userRef.get();
        System.out.println("Utente finale: " + finalUser.getName() + ", età: " + finalUser.getAge());
    }
    
    /**
     * Dimostrazione di AtomicIntegerArray
     */
    private static void demoAtomicIntegerArray() throws InterruptedException {
        // Array atomico di interi
        final AtomicIntegerArray atomicArray = new AtomicIntegerArray(10);
        
        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);
        
        // Avvia thread che incrementano elementi casuali dell'array
        for (int i = 0; i < NUM_THREADS; i++) {
            executor.execute(() -> {
                for (int j = 0; j < OPERATIONS_PER_THREAD; j++) {
                    // Seleziona un indice casuale nell'array
                    int index = (int) (Math.random() * atomicArray.length());
                    // Incrementa atomicamente l'elemento all'indice selezionato
                    atomicArray.incrementAndGet(index);
                }
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        // Stampa i valori finali dell'array
        System.out.println("Valori finali dell'array atomico:");
        int sum = 0;
        for (int i = 0; i < atomicArray.length(); i++) {
            int value = atomicArray.get(i);
            sum += value;
            System.out.println("Elemento " + i + ": " + value);
        }
        
        System.out.println("Somma di tutti gli elementi: " + sum);
        System.out.println("Totale operazioni eseguite: " + (NUM_THREADS * OPERATIONS_PER_THREAD));
    }
    
    /**
     * Dimostrazione di operazioni atomiche avanzate
     */
    private static void demoAdvancedOperations() throws InterruptedException {
        // AtomicInteger per operazioni avanzate
        final AtomicInteger atomicValue = new AtomicInteger(0);
        
        // Definizione di un operatore che raddoppia il valore
        IntUnaryOperator doubleIt = x -> x * 2;
        
        // Incrementa e poi raddoppia atomicamente
        atomicValue.incrementAndGet(); // 0 -> 1
        int doubledValue = atomicValue.updateAndGet(doubleIt); // 1 -> 2
        System.out.println("Dopo incrementAndGet e updateAndGet(double): " + doubledValue);
        
        // Accumula atomicamente usando una funzione di somma
        int accumulated = atomicValue.accumulateAndGet(10, Integer::sum); // 2 + 10 = 12
        System.out.println("Dopo accumulateAndGet(10, sum): " + accumulated);
        
        // Esempio di getAndUpdate
        int previousValue = atomicValue.getAndUpdate(x -> x + 5); // Ritorna 12, imposta a 17
        System.out.println("getAndUpdate(+5) ha restituito: " + previousValue + ", nuovo valore: " + atomicValue.get());
        
        // Esempio di getAndAccumulate
        previousValue = atomicValue.getAndAccumulate(3, (x, y) -> x * y); // Ritorna 17, imposta a 51
        System.out.println("getAndAccumulate(3, multiply) ha restituito: " + previousValue + ", nuovo valore: " + atomicValue.get());
    }
    
    /**
     * Classe User per la dimostrazione di AtomicReference
     */
    static class User {
        private final String name;
        private final int age;
        
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
        }
    }
    
    /**
     * Classe Counter non thread-safe per confronto
     */
    static class Counter {
        private int value = 0;
        
        public void increment() {
            value++;
        }
        
        public int getValue() {
            return value;
        }
    }
}