/**
 * Esempio 02: Utilizzo del keyword synchronized
 * 
 * Questo esempio dimostra come utilizzare il keyword 'synchronized' per risolvere
 * i problemi di concorrenza mostrati nell'esempio precedente. Vengono illustrati
 * tre approcci di sincronizzazione:
 * 1. Metodi sincronizzati
 * 2. Blocchi sincronizzati su this
 * 3. Blocchi sincronizzati su un oggetto lock dedicato
 */
public class ES02_Synchronized {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dell'uso di synchronized");
        System.out.println("=====================================");
        
        // Numero di thread e incrementi per thread
        final int NUM_THREADS = 5;
        final int INCREMENTI_PER_THREAD = 1000;
        
        // Test con contatore non sincronizzato
        ContatoreNonSincronizzato contNonSync = new ContatoreNonSincronizzato();
        eseguiTest("Contatore NON sincronizzato", contNonSync, NUM_THREADS, INCREMENTI_PER_THREAD);
        
        // Test con contatore con metodo sincronizzato
        ContatoreSincronizzatoMetodo contSyncMetodo = new ContatoreSincronizzatoMetodo();
        eseguiTest("Contatore con metodo sincronizzato", contSyncMetodo, NUM_THREADS, INCREMENTI_PER_THREAD);
        
        // Test con contatore con blocco sincronizzato su this
        ContatoreSincronizzatoBloccoThis contSyncThis = new ContatoreSincronizzatoBloccoThis();
        eseguiTest("Contatore con blocco sincronizzato su this", contSyncThis, NUM_THREADS, INCREMENTI_PER_THREAD);
        
        // Test con contatore con blocco sincronizzato su oggetto lock
        ContatoreSincronizzatoBloccoLock contSyncLock = new ContatoreSincronizzatoBloccoLock();
        eseguiTest("Contatore con blocco sincronizzato su oggetto lock", contSyncLock, NUM_THREADS, INCREMENTI_PER_THREAD);
        
        System.out.println("\nConclusioni:");
        System.out.println("1. Senza sincronizzazione, si verificano race condition");
        System.out.println("2. Tutti e tre gli approcci di sincronizzazione risolvono il problema");
        System.out.println("3. La sincronizzazione garantisce l'atomicità delle operazioni");
        System.out.println("4. La sincronizzazione può introdurre overhead di prestazioni");
        System.out.println("5. È importante sincronizzare solo il codice necessario (granularità fine)");
    }
    
    // Interfaccia comune per tutti i contatori
    interface Contatore {
        void incrementa();
        int getValore();
    }
    
    // Contatore senza sincronizzazione
    static class ContatoreNonSincronizzato implements Contatore {
        private int valore = 0;
        
        @Override
        public void incrementa() {
            valore++;
        }
        
        @Override
        public int getValore() {
            return valore;
        }
    }
    
    // Contatore con metodo sincronizzato
    static class ContatoreSincronizzatoMetodo implements Contatore {
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
    
    // Contatore con blocco sincronizzato su this
    static class ContatoreSincronizzatoBloccoThis implements Contatore {
        private int valore = 0;
        
        @Override
        public void incrementa() {
            synchronized(this) {
                valore++;
            }
        }
        
        @Override
        public int getValore() {
            synchronized(this) {
                return valore;
            }
        }
    }
    
    // Contatore con blocco sincronizzato su oggetto lock dedicato
    static class ContatoreSincronizzatoBloccoLock implements Contatore {
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
    
    // Metodo per eseguire il test con un tipo di contatore
    private static void eseguiTest(String descrizione, Contatore contatore, int numThreads, int incrementiPerThread) {
        System.out.println("\nTest: " + descrizione);
        
        // Array di thread
        Thread[] threads = new Thread[numThreads];
        
        // Tempo di inizio
        long inizio = System.currentTimeMillis();
        
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
        
        // Tempo di fine
        long fine = System.currentTimeMillis();
        
        // Valore atteso: numThreads * incrementiPerThread
        int valoreAtteso = numThreads * incrementiPerThread;
        int valoreEffettivo = contatore.getValore();
        
        System.out.println("Valore atteso: " + valoreAtteso);
        System.out.println("Valore effettivo: " + valoreEffettivo);
        System.out.println("Tempo di esecuzione: " + (fine - inizio) + " ms");
        
        if (valoreEffettivo < valoreAtteso) {
            System.out.println("ERRORE: Si è verificata una race condition!");
        } else {
            System.out.println("SUCCESSO: Nessuna race condition rilevata.");
        }
    }
}