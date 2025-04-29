# Altri Pattern Concorrenti

Oltre ai pattern principali già discussi (Thread-per-Message, Worker Thread e Read-Write Lock), esistono numerosi altri pattern concorrenti che possono essere utili in diversi scenari di programmazione multi-thread. In questo capitolo, esploreremo alcuni di questi pattern aggiuntivi.

## Future

### Descrizione
Il pattern Future rappresenta il risultato di un'operazione asincrona che potrebbe non essere ancora disponibile. È un segnaposto per un valore che sarà disponibile in futuro.

### Implementazione in Java
Java fornisce l'interfaccia `Future<V>` nel package `java.util.concurrent` con i seguenti metodi principali:

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
```

### Esempio di utilizzo

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> future = executor.submit(() -> {
    // Operazione lunga
    Thread.sleep(2000);
    return 42;
});

// Esegui altre operazioni mentre il calcolo è in corso
System.out.println("Elaborazione in corso...");

// Ottieni il risultato (bloccante)
try {
    Integer result = future.get();
    System.out.println("Risultato: " + result);
} catch (Exception e) {
    e.printStackTrace();
} finally {
    executor.shutdown();
}
```

### Vantaggi
- Permette di eseguire operazioni in modo asincrono
- Fornisce meccanismi per controllare lo stato dell'operazione
- Consente di recuperare il risultato quando è disponibile

## Promise

### Descrizione
Il pattern Promise è simile al Future, ma con una differenza fondamentale: mentre un Future è di sola lettura, un Promise può essere sia letto che scritto. In altre parole, un Promise può essere completato esplicitamente.

### Implementazione in Java
In Java, la classe `CompletableFuture<T>` implementa questo pattern:

```java
CompletableFuture<String> promise = new CompletableFuture<>();

// In un altro thread o contesto
promise.complete("Risultato");

// Per gestire errori
// promise.completeExceptionally(new RuntimeException("Errore"));

// Per ottenere il risultato
String result = promise.get();
```

### Vantaggi
- Maggiore controllo sul completamento delle operazioni asincrone
- Supporto per la composizione di operazioni asincrone
- Gestione più flessibile degli errori

## Barrier

### Descrizione
Il pattern Barrier sincronizza un gruppo di thread in un punto specifico, permettendo loro di procedere solo quando tutti hanno raggiunto quel punto.

### Implementazione in Java
Java fornisce la classe `CyclicBarrier` per implementare questo pattern:

```java
public class BarrierExample {
    public static void main(String[] args) {
        final int NUM_THREADS = 3;
        CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
            // Azione da eseguire quando tutti i thread raggiungono la barriera
            System.out.println("Tutti i thread hanno raggiunto la barriera!");
        });
        
        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadNum = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadNum + " sta eseguendo la fase 1");
                    Thread.sleep((long) (Math.random() * 1000));
                    
                    System.out.println("Thread " + threadNum + " ha raggiunto la barriera");
                    barrier.await(); // Attende che tutti i thread raggiungano questo punto
                    
                    System.out.println("Thread " + threadNum + " sta eseguendo la fase 2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

### Vantaggi
- Sincronizzazione di più thread in punti specifici
- Utile per algoritmi che richiedono fasi sincronizzate
- Riutilizzabile per più cicli di sincronizzazione

## Semaphore

### Descrizione
Il pattern Semaphore controlla l'accesso a risorse condivise limitando il numero di thread che possono accedervi contemporaneamente.

### Implementazione in Java
Java fornisce la classe `Semaphore` nel package `java.util.concurrent`:

```java
public class SemaphoreExample {
    public static void main(String[] args) {
        // Semaforo che permette a massimo 3 thread di accedere alla risorsa
        Semaphore semaphore = new Semaphore(3);
        
        for (int i = 0; i < 10; i++) {
            final int threadNum = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadNum + " sta cercando di acquisire il permesso");
                    semaphore.acquire();
                    
                    System.out.println("Thread " + threadNum + " ha acquisito il permesso");
                    Thread.sleep(1000); // Simula l'utilizzo della risorsa
                    
                    System.out.println("Thread " + threadNum + " rilascia il permesso");
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
```

### Vantaggi
- Limita l'accesso concorrente a risorse condivise
- Previene sovraccarichi di sistema
- Utile per implementare pool di risorse

## Double-Checked Locking

### Descrizione
Il pattern Double-Checked Locking è utilizzato per ridurre il costo della sincronizzazione nell'inizializzazione lazy di un'istanza singleton.

### Implementazione in Java

```java
public class Singleton {
    private static volatile Singleton instance;
    
    private Singleton() {}
    
    public static Singleton getInstance() {
        if (instance == null) { // Primo controllo (non sincronizzato)
            synchronized (Singleton.class) {
                if (instance == null) { // Secondo controllo (sincronizzato)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### Vantaggi
- Riduce l'overhead della sincronizzazione
- Garantisce l'inizializzazione thread-safe
- Migliora le prestazioni in scenari con molte letture

## Thread-Local Storage

### Descrizione
Il pattern Thread-Local Storage permette di associare dati specifici a ciascun thread, evitando la condivisione e quindi i problemi di concorrenza.

### Implementazione in Java
Java fornisce la classe `ThreadLocal` per implementare questo pattern:

```java
public class ThreadLocalExample {
    // Variabile ThreadLocal che mantiene un valore diverso per ogni thread
    private static ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(() -> 0);
    
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            final int threadNum = i;
            new Thread(() -> {
                // Ogni thread ha la propria copia della variabile
                threadLocalValue.set(threadNum);
                
                System.out.println("Thread " + threadNum + ": valore iniziale = " + threadLocalValue.get());
                
                // Incrementa il valore
                threadLocalValue.set(threadLocalValue.get() + 100);
                
                System.out.println("Thread " + threadNum + ": valore finale = " + threadLocalValue.get());
            }).start();
        }
    }
}
```

### Vantaggi
- Elimina la necessità di sincronizzazione per variabili thread-specifiche
- Migliora le prestazioni riducendo la contesa
- Semplifica il codice in scenari con dati specifici per thread

## Active Object

### Descrizione
Il pattern Active Object disaccoppia l'esecuzione del metodo dalla sua invocazione per gli oggetti che risiedono in un proprio thread di controllo. Questo pattern è utile per implementare oggetti che operano in modo asincrono.

### Implementazione in Java

```java
// Interfaccia per le richieste
interface Command {
    void execute();
}

// Active Object
class ActiveObject {
    private final BlockingQueue<Command> queue;
    private final Thread thread;
    private volatile boolean isStopped = false;
    
    public ActiveObject() {
        queue = new LinkedBlockingQueue<>();
        thread = new Thread(() -> {
            while (!isStopped || !queue.isEmpty()) {
                try {
                    Command command = queue.take();
                    command.execute();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        thread.start();
    }
    
    public void execute(Command command) {
        if (!isStopped) {
            queue.offer(command);
        }
    }
    
    public void shutdown() {
        isStopped = true;
        thread.interrupt();
    }
}

// Esempio di utilizzo
class ActiveObjectExample {
    public static void main(String[] args) throws InterruptedException {
        ActiveObject activeObject = new ActiveObject();
        
        // Invia comandi all'active object
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            activeObject.execute(() -> {
                System.out.println("Esecuzione del task " + taskId);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Attendi che tutti i comandi vengano eseguiti
        Thread.sleep(1000);
        
        // Arresta l'active object
        activeObject.shutdown();
    }
}
```

### Vantaggi
- Disaccoppia l'invocazione del metodo dalla sua esecuzione
- Semplifica la programmazione concorrente nascondendo i dettagli di sincronizzazione
- Migliora la modularità e la manutenibilità del codice

## Conclusione

I pattern concorrenti presentati in questo capitolo forniscono soluzioni robuste e testate per affrontare diverse sfide nella programmazione multi-thread. La scelta del pattern appropriato dipende dalle specifiche esigenze dell'applicazione, dal carico di lavoro previsto e dai requisiti di prestazioni.

È importante notare che questi pattern non sono mutuamente esclusivi e spesso possono essere combinati per creare soluzioni più complesse e potenti. Ad esempio, è comune utilizzare Future insieme a Worker Thread, o combinare Thread-Local Storage con altri pattern per migliorare le prestazioni.

La comprensione di questi pattern e la capacità di applicarli correttamente sono competenze fondamentali per sviluppare applicazioni concorrenti efficienti, scalabili e prive di errori.