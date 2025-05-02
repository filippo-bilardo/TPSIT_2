# Interruzione dei Thread

## Introduzione

In Java, l'interruzione è un meccanismo cooperativo che permette di segnalare a un thread che dovrebbe terminare la sua esecuzione. A differenza di altri linguaggi di programmazione, Java non fornisce un modo per terminare forzatamente un thread dall'esterno. Invece, offre un sistema di segnalazione che consente a un thread di decidere come e quando rispondere a una richiesta di interruzione. Questo approccio è più sicuro e previene problemi come il rilascio improprio di risorse o la corruzione dello stato dell'applicazione.

## Il Meccanismo di Interruzione

### Il Metodo interrupt()

Il metodo `interrupt()` è il principale strumento per segnalare a un thread che dovrebbe interrompere la sua esecuzione. Quando viene chiamato su un'istanza di `Thread`, questo metodo imposta il flag di interruzione del thread.

```java
Thread thread = new Thread(() -> {
    // Codice del thread
});
thread.start();

// In un altro thread o nel thread principale
thread.interrupt(); // Richiede l'interruzione del thread
```

### Verifica dello Stato di Interruzione

Un thread può verificare se è stato interrotto in due modi:

1. **Chiamando `Thread.interrupted()`**: Questo metodo statico verifica e resetta il flag di interruzione del thread corrente.

```java
if (Thread.interrupted()) {
    // Il thread è stato interrotto
    // Il flag di interruzione viene resettato a false
}
```

2. **Chiamando `isInterrupted()`**: Questo metodo d'istanza verifica il flag di interruzione senza resettarlo.

```java
Thread thread = Thread.currentThread();
if (thread.isInterrupted()) {
    // Il thread è stato interrotto
    // Il flag di interruzione rimane impostato
}
```

### Gestione delle InterruptedException

Molti metodi bloccanti in Java, come `Thread.sleep()`, `Object.wait()`, o operazioni di I/O bloccanti, possono lanciare un'`InterruptedException` quando il thread viene interrotto mentre è in attesa. Quando ciò accade, il flag di interruzione viene automaticamente resettato.

```java
try {
    Thread.sleep(5000); // Metodo bloccante
} catch (InterruptedException e) {
    // Il thread è stato interrotto durante il sleep
    // Il flag di interruzione è già stato resettato
    
    // È buona pratica ripristinare il flag di interruzione
    Thread.currentThread().interrupt();
    
    // Gestione dell'interruzione (pulizia, terminazione, ecc.)
    return; // Spesso è opportuno terminare l'esecuzione
}
```

## Pattern di Interruzione

### 1. Interruzione di un Loop

Uno dei pattern più comuni è l'interruzione di un ciclo in esecuzione:

```java
public class InterruptibleLoop implements Runnable {
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Esegue un'operazione
                System.out.println("Esecuzione in corso...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ripristina il flag di interruzione
                Thread.currentThread().interrupt();
                break; // Esce dal ciclo
            }
        }
        System.out.println("Thread terminato correttamente");
    }
    
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptibleLoop());
        thread.start();
        
        // Attende 5 secondi e poi interrompe il thread
        Thread.sleep(5000);
        thread.interrupt();
    }
}
```

### 2. Interruzione con Pulizia delle Risorse

Quando un thread utilizza risorse che devono essere rilasciate, è importante gestire l'interruzione in modo da eseguire la pulizia necessaria:

```java
public class ResourceCleanupExample implements Runnable {
    private final Resource resource;
    
    public ResourceCleanupExample(Resource resource) {
        this.resource = resource;
    }
    
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Utilizza la risorsa
                resource.use();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            // Gestisce l'interruzione
            Thread.currentThread().interrupt();
        } finally {
            // Assicura il rilascio della risorsa
            resource.close();
            System.out.println("Risorsa rilasciata correttamente");
        }
    }
    
    // Classe di esempio per una risorsa
    static class Resource {
        public void use() {
            System.out.println("Utilizzo della risorsa...");
        }
        
        public void close() {
            System.out.println("Chiusura della risorsa...");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        Resource resource = new Resource();
        Thread thread = new Thread(new ResourceCleanupExample(resource));
        thread.start();
        
        // Attende 2 secondi e poi interrompe il thread
        Thread.sleep(2000);
        thread.interrupt();
    }
}
```

### 3. Propagazione dell'Interruzione

In alcuni casi, è opportuno propagare l'interruzione invece di gestirla direttamente:

```java
public void performTask() throws InterruptedException {
    while (true) {
        // Verifica l'interruzione prima di operazioni costose
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException("Task interrotto");
        }
        
        // Esegue operazioni costose
        heavyComputation();
        
        // Operazione bloccante che può lanciare InterruptedException
        Thread.sleep(100);
    }
}

// Chiamata al metodo
try {
    performTask();
} catch (InterruptedException e) {
    // Gestisce l'interruzione a un livello superiore
    System.out.println("Task interrotto: " + e.getMessage());
}
```

## Esempi Avanzati

### Esempio 1: Timeout con Interruzione

```java
public class TimeoutExample {
    public static void main(String[] args) {
        Runnable task = () -> {
            try {
                // Simula un'operazione lunga
                System.out.println("Inizio operazione lunga...");
                Thread.sleep(10000); // 10 secondi
                System.out.println("Operazione completata!");
            } catch (InterruptedException e) {
                System.out.println("Operazione interrotta dopo timeout!");
            }
        };
        
        Thread worker = new Thread(task);
        worker.start();
        
        // Imposta un timeout di 3 secondi
        try {
            worker.join(3000); // Attende che il thread termini per max 3 secondi
            if (worker.isAlive()) {
                // Il thread è ancora in esecuzione dopo il timeout
                worker.interrupt();
                System.out.println("Timeout scaduto, thread interrotto");
            }
        } catch (InterruptedException e) {
            // Il thread principale è stato interrotto
            e.printStackTrace();
        }
    }
}
```

### Esempio 2: Interruzione di Più Thread

```java
public class MultiThreadInterruption {
    public static void main(String[] args) {
        // Crea un gruppo di thread
        List<Thread> threads = new ArrayList<>();
        
        // Avvia 5 thread
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        System.out.println("Thread " + threadId + " in esecuzione");
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Thread " + threadId + " interrotto");
                    return;
                }
                System.out.println("Thread " + threadId + " terminato normalmente");
            });
            
            thread.start();
            threads.add(thread);
        }
        
        // Attende 3 secondi
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Interrompe tutti i thread
        System.out.println("Interruzione di tutti i thread...");
        for (Thread thread : threads) {
            thread.interrupt();
        }
        
        // Attende che tutti i thread terminino
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Tutti i thread sono stati terminati");
    }
}
```

### Esempio 3: Implementazione di un Task Cancellabile

```java
public class CancellableTask {
    private volatile boolean cancelled = false;
    
    public void cancel() {
        cancelled = true;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void run() {
        Thread currentThread = Thread.currentThread();
        
        while (!cancelled && !currentThread.isInterrupted()) {
            try {
                // Verifica la cancellazione prima di operazioni costose
                if (cancelled || currentThread.isInterrupted()) {
                    break;
                }
                
                // Esegue un'operazione
                System.out.println("Esecuzione del task...");
                Thread.sleep(500);
                
            } catch (InterruptedException e) {
                // Gestisce l'interruzione
                System.out.println("Task interrotto");
                currentThread.interrupt(); // Ripristina il flag
                break;
            }
        }
        
        System.out.println("Task terminato. Cancellato: " + cancelled + 
                          ", Interrotto: " + currentThread.isInterrupted());
    }
    
    public static void main(String[] args) throws InterruptedException {
        CancellableTask task = new CancellableTask();
        
        Thread thread = new Thread(task::run);
        thread.start();
        
        // Attende 2 secondi e poi cancella il task
        Thread.sleep(2000);
        task.cancel();
        
        // Attende che il thread termini
        thread.join();
        System.out.println("Programma terminato");
    }
}
```

## Best Practices

### 1. Rispondere Prontamente all'Interruzione

I thread dovrebbero verificare regolarmente il loro stato di interruzione e rispondere prontamente. Questo è particolarmente importante per i thread che eseguono operazioni lunghe o cicli infiniti.

### 2. Ripristinare il Flag di Interruzione

Quando si cattura un'`InterruptedException`, è buona pratica ripristinare il flag di interruzione chiamando `Thread.currentThread().interrupt()`. Questo permette ai metodi chiamanti di rilevare che il thread è stato interrotto.

### 3. Rilasciare le Risorse

Assicurarsi che tutte le risorse vengano rilasciate correttamente quando un thread viene interrotto, utilizzando blocchi `finally` o try-with-resources.

### 4. Evitare di Ignorare le InterruptedException

Non ignorare mai le `InterruptedException` senza una buona ragione. Se non puoi gestire l'interruzione nel punto in cui viene catturata, propaga l'eccezione o ripristina il flag di interruzione.

### 5. Utilizzare Variabili Volatili per la Cancellazione

Se hai bisogno di un meccanismo di cancellazione personalizzato oltre all'interruzione, utilizza variabili `volatile` per garantire la visibilità tra i thread.

## Conclusione

L'interruzione dei thread in Java è un meccanismo cooperativo che richiede la partecipazione attiva del thread che si desidera interrompere. Sebbene questo approccio richieda più codice rispetto a una terminazione forzata, offre maggiore sicurezza e controllo, permettendo ai thread di rilasciare correttamente le risorse e mantenere l'integrità dell'applicazione.

Comprendere e utilizzare correttamente il meccanismo di interruzione è fondamentale per sviluppare applicazioni multi-thread robuste e affidabili in Java.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Sospensione dell'Esecuzione con Sleep](./03b-SospensioneConSleep.md)
- [➡️ Sincronizzazione](../02-Sincronizzazione/README.md)