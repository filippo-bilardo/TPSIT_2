/**
 * Esempio 03: Interruzione dei Thread
 * 
 * Questo esempio dimostra come interrompere correttamente i thread in Java.
 * L'interruzione è un meccanismo cooperativo: un thread può segnalare a un altro
 * thread che dovrebbe terminare, ma il thread target deve controllare periodicamente
 * il suo stato di interruzione e rispondere di conseguenza.
 */
public class ES03_InterruzioneThread {
    public static void main(String[] args) {
        System.out.println("Dimostrazione dell'interruzione dei thread");
        System.out.println("=========================================\n");
        
        // Esempio 1: Thread che controlla il flag di interruzione
        Thread threadSensibile = new Thread(() -> {
            System.out.println("Thread sensibile: iniziato");
            try {
                // Ciclo che controlla periodicamente se il thread è stato interrotto
                for (int i = 0; i < 10; i++) {
                    // Controlliamo se il thread è stato interrotto
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Thread sensibile: rilevata interruzione, uscita dal ciclo");
                        break; // Usciamo dal ciclo se il thread è stato interrotto
                    }
                    
                    System.out.println("Thread sensibile: lavorando... " + i);
                    Thread.sleep(200); // Può lanciare InterruptedException
                }
            } catch (InterruptedException e) {
                // Questo blocco viene eseguito se il thread viene interrotto mentre è in sleep
                System.out.println("Thread sensibile: interrotto durante sleep()");
                // Importante: l'eccezione resetta il flag di interruzione, quindi lo reimpostiamo
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread sensibile: terminato");
        }, "ThreadSensibile");
        
        // Esempio 2: Thread che ignora l'interruzione
        Thread threadIgnorante = new Thread(() -> {
            System.out.println("Thread ignorante: iniziato");
            // Questo thread non controlla mai il suo stato di interruzione
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread ignorante: lavorando... " + i);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    System.out.println("Thread ignorante: ho ricevuto un'interruzione ma la ignoro");
                    // Nota: non reimpostiamo il flag di interruzione e continuiamo l'esecuzione
                }
            }
            System.out.println("Thread ignorante: completato normalmente");
        }, "ThreadIgnorante");
        
        // Avviamo i thread
        threadSensibile.start();
        threadIgnorante.start();
        
        // Attendiamo un po' e poi interrompiamo entrambi i thread
        try {
            Thread.sleep(600);
            System.out.println("\nThread principale: interruzione di entrambi i thread");
            threadSensibile.interrupt();
            threadIgnorante.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Attendiamo che entrambi i thread terminino
        try {
            threadSensibile.join();
            threadIgnorante.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nNota: L'interruzione in Java è un meccanismo cooperativo.");
        System.out.println("Un thread deve controllare periodicamente il suo stato di interruzione");
        System.out.println("e decidere come rispondere. Non è possibile forzare la terminazione");
        System.out.println("di un thread dall'esterno in modo sicuro.");
        
        System.out.println("\nBest practices per gestire l'interruzione:");
        System.out.println("1. Controllare regolarmente Thread.currentThread().isInterrupted()");
        System.out.println("2. Uscire dai cicli e terminare il thread quando viene rilevata un'interruzione");
        System.out.println("3. Quando si cattura InterruptedException, reimpostare il flag di interruzione");
        System.out.println("   con Thread.currentThread().interrupt() se si vuole propagare l'interruzione");
    }
}

/*
 * Esempio di output:
 
    Dimostrazione dell'interruzione dei thread
    =========================================

    Thread sensibile: iniziato
    Thread ignorante: iniziato
    Thread sensibile: lavorando... 0
    Thread ignorante: lavorando... 0
    Thread sensibile: lavorando... 1
    Thread ignorante: lavorando... 1
    Thread sensibile: lavorando... 2
    Thread ignorante: lavorando... 2

    Thread principale: interruzione di entrambi i thread
    Thread ignorante: ho ricevuto un'interruzione ma la ignoro
    Thread ignorante: lavorando... 3
    Thread sensibile: interrotto durante sleep()
    Thread sensibile: terminato
    Thread ignorante: lavorando... 4
    Thread ignorante: completato normalmente

    Nota: L'interruzione in Java è un meccanismo cooperativo.
    Un thread deve controllare periodicamente il suo stato di interruzione
    e decidere come rispondere. Non è possibile forzare la terminazione
    di un thread dall'esterno in modo sicuro.

    Best practices per gestire l'interruzione:
    1. Controllare regolarmente Thread.currentThread().isInterrupted()
    2. Uscire dai cicli e terminare il thread quando viene rilevata un'interruzione
    3. Quando si cattura InterruptedException, reimpostare il flag di interruzione
   con Thread.currentThread().interrupt() se si vuole propagare l'interruzione

 */