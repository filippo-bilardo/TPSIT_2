package esempi;

import java.util.concurrent.CyclicBarrier;

/**
 * Esempio di utilizzo del pattern Barrier con CyclicBarrier.
 * Questo pattern permette di sincronizzare un gruppo di thread in un punto specifico,
 * consentendo loro di procedere solo quando tutti hanno raggiunto quel punto.
 */
public class BarrierExample {
    private static final int NUM_THREADS = 3;
    private static final int NUM_FASI = 3;
    
    public static void main(String[] args) {
        // Creazione di una barriera che si attiva quando tutti i thread la raggiungono
        // e che esegue un'azione quando tutti i thread sono arrivati
        CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
            System.out.println("\n=== Tutti i thread hanno completato la fase corrente! ===\n");
        });
        
        // Creazione e avvio dei thread worker
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadNum = i;
            new Thread(() -> {
                try {
                    for (int fase = 1; fase <= NUM_FASI; fase++) {
                        // Simula il lavoro della fase corrente
                        System.out.println("Thread " + threadNum + ": esecuzione della fase " + fase);
                        Thread.sleep((long) (Math.random() * 2000)); // Tempo di lavoro casuale
                        
                        System.out.println("Thread " + threadNum + ": ha raggiunto la barriera nella fase " + fase);
                        
                        // Attende che tutti i thread raggiungano la barriera
                        // prima di procedere alla fase successiva
                        barrier.await();
                    }
                    
                    System.out.println("Thread " + threadNum + ": ha completato tutte le fasi");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, "Worker-" + threadNum).start();
        }
    }
}

/**
 * Note sul pattern Barrier:
 * 
 * 1. Utilizzo: Il pattern Barrier è utile quando un gruppo di thread deve sincronizzarsi
 *    in punti specifici prima di procedere, come in algoritmi paralleli con fasi distinte.
 * 
 * 2. CyclicBarrier vs CountDownLatch:
 *    - CyclicBarrier può essere riutilizzato dopo che è stato attivato
 *    - CountDownLatch è monouso e non può essere resettato
 * 
 * 3. Vantaggi:
 *    - Sincronizzazione precisa tra thread
 *    - Supporto per azioni da eseguire quando tutti i thread raggiungono la barriera
 *    - Riutilizzabile per più cicli di sincronizzazione
 * 
 * 4. Svantaggi:
 *    - Può causare deadlock se un thread non raggiunge mai la barriera
 *    - Overhead di sincronizzazione
 *    - Tutti i thread devono attendere il thread più lento
 */