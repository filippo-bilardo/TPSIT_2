/**
 * Esempio 03: Contatore Thread-Safe
 * 
 * Questo esempio implementa un contatore thread-safe utilizzando diversi meccanismi di sincronizzazione:
 * 1. Nessuna sincronizzazione (non thread-safe)
 * 2. Metodi sincronizzati
 * 3. Blocco sincronizzato su un oggetto lock dedicato
 * 
 * L'esempio dimostra come la mancanza di sincronizzazione porti a race condition,
 * mentre i meccanismi di sincronizzazione garantiscono la correttezza del risultato.
 */
public class ES03_ContatoreThreadSafe {
    public static void main(String[] args) {
        System.out.println("Dimostrazione di contatori thread-safe");
        System.out.println("======================================\n");
        
        // Numero di thread e incrementi per thread
        final int NUM_THREADS = 5;
        final int INCREMENTI_PER_THREAD = 1000;
        
        // Test con contatore non sicuro
        ContatoreNonSicuro contNonSicuro = new ContatoreNonSicuro();
        eseguiTest("Contatore NON sicuro", contNonSicuro, NUM_THREADS, INCREMENTI_PER_THREAD);
        
        // Test con contatore sincronizzato
        ContatoreSincronizzato contSincronizzato = new ContatoreSincronizzato();
        eseguiTest("Contatore con metodi sincronizzati", contSincronizzato, NUM_THREADS, INCREMENTI_PER_THREAD);
        
        // Test con contatore con lock
        ContatoreLock contLock = new ContatoreLock();
        eseguiTest("Contatore con lock dedicato", contLock, NUM_THREADS, INCREMENTI_PER_THREAD);
    }
    
    /**
     * Esegue un test sul contatore specificato utilizzando più thread
     */
    private static void eseguiTest(String descrizione, Contatore contatore, int numThreads, int incrementiPerThread) {
        System.out.println("Test: " + descrizione);
        
        // Array di thread
        Thread[] threads = new Thread[numThreads];
        
        // Creiamo e avviamo i thread
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementiPerThread; j++) {
                    contatore.incrementa();
                }
            });
            threads[i].start();
        }
        
        // Attendiamo che tutti i thread completino l'esecuzione
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Valore atteso: numThreads * incrementiPerThread
        int valoreAtteso = numThreads * incrementiPerThread;
        int valoreEffettivo = contatore.getValore();
        
        System.out.println("Valore atteso: " + valoreAtteso);
        System.out.println("Valore effettivo: " + valoreEffettivo);
        
        if (valoreEffettivo == valoreAtteso) {
            System.out.println("Risultato corretto!\n");
        } else {
            System.out.println("Risultato NON corretto (race condition)!\n");
        }
    }
}

/**
 * Interfaccia comune per tutti i contatori
 */
interface Contatore {
    void incrementa();
    int getValore();
}

/**
 * Implementazione di un contatore non thread-safe
 */
class ContatoreNonSicuro implements Contatore {
    private int valore = 0;
    
    @Override
    public void incrementa() {
        valore++; // Operazione non atomica
    }
    
    @Override
    public int getValore() {
        return valore;
    }
}

/**
 * Implementazione di un contatore thread-safe con metodi sincronizzati
 */
class ContatoreSincronizzato extends ContatoreNonSicuro {
    private int valore = 0;
    
    @Override
    public synchronized void incrementa() {
        valore++;
    }
    
    @Override
    public synchronized int getValore() {
        return valore;
    }
}

/**
 * Implementazione di un contatore thread-safe con lock dedicato
 */
class ContatoreLock extends ContatoreNonSicuro {
    private int valore = 0;
    private final Object lock = new Object(); // Oggetto lock dedicato
    
    @Override
    public void incrementa() {
        synchronized(lock) {
            valore++;
        }
    }
    
    @Override
    public int getValore() {
        synchronized(lock) {
            return valore;
        }
    }
}