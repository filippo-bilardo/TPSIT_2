/**
 * Esempio 01: Concorrenza vs Parallelismo
 * 
 * Questo esempio illustra la differenza tra concorrenza e parallelismo:
 * - Concorrenza: gestione di più attività che progrediscono nel tempo 
 *   (anche su un singolo core)
 * - Parallelismo: esecuzione simultanea di più attività (richiede hardware 
 *   multi-core)
 */
public class ES01c_ConcorrenzaVsParallelismo {
    public static void main(String[] args) {
        System.out.println("Dimostrazione di Concorrenza vs Parallelismo");
        System.out.println("==========================================");
        
        // Otteniamo il numero di processori disponibili
        int numProcessori = Runtime.getRuntime().availableProcessors();
        System.out.println("Numero di processori disponibili: " + numProcessori);
        
        // Esempio di concorrenza (task che si alternano)
        System.out.println("\nEsempio di CONCORRENZA (anche su CPU single-core):");
        Thread taskA = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task A: iterazione " + i);
                try {
                    Thread.sleep(100); // Simuliamo un'operazione
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        Thread taskB = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Task B: iterazione " + i);
                try {
                    Thread.sleep(100); // Simuliamo un'operazione
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Avviamo i thread concorrenti
        taskA.start();
        taskB.start();
        
        // Attendiamo il completamento
        try {
            taskA.join();
            taskB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Esempio di potenziale parallelismo (se ci sono abbastanza core)
        System.out.println("\nEsempio di potenziale PARALLELISMO (richiede CPU multi-core):");
        System.out.println("Avvio di " + numProcessori + " worker in parallelo...");
        
        Thread[] workers = new Thread[numProcessori];
        for (int i = 0; i < numProcessori; i++) {
            final int workerId = i;
            workers[i] = new Thread(() -> {
                System.out.println("Worker " + workerId + ": iniziato");
                // Simuliamo un calcolo intensivo
                long somma = 0;
                for (long j = 0; j < 100_000_000; j++) {
                    somma += j;
                }
                System.out.println("Worker " + workerId + ": completato. Somma = " + somma);
            });
            workers[i].start();
        }
        
        // Attendiamo il completamento di tutti i worker
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("\nNota: Su un sistema multi-core, i worker possono essere eseguiti");
        System.out.println("effettivamente in parallelo, mentre su un sistema single-core");
        System.out.println("verrebbero eseguiti in modo concorrente (alternandosi).");
    }
}