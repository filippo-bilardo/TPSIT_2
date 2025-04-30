# Callable e Future

In questo capitolo esploreremo le interfacce `Callable` e `Future` del framework Executor, che permettono di eseguire task che restituiscono un risultato e di gestire operazioni asincrone in modo efficiente.

## Interfaccia Callable

L'interfaccia `Callable<V>` rappresenta un task che restituisce un risultato e può lanciare un'eccezione. È simile all'interfaccia `Runnable`, ma con due differenze fondamentali:

1. Il metodo `call()` può restituire un valore (di tipo parametrizzato `V`)
2. Il metodo `call()` può lanciare eccezioni checked

```java
public interface Callable<V> {
    V call() throws Exception;
}
```

### Confronto con Runnable

| Caratteristica | Runnable | Callable |
|----------------|----------|----------|
| Metodo principale | `void run()` | `V call() throws Exception` |
| Valore di ritorno | No | Sì (tipo parametrizzato `V`) |
| Eccezioni checked | No | Sì |
| Introdotto in | Java 1.0 | Java 5 |

### Esempio di Callable

```java
Callable<Integer> task = () -> {
    // Simulazione di un'operazione che richiede tempo
    Thread.sleep(2000);
    return 42; // Restituisce un risultato
};
```

## Interfaccia Future

L'interfaccia `Future<V>` rappresenta il risultato di un'operazione asincrona. Quando si sottomette un `Callable` a un `ExecutorService`, viene restituito un `Future` che può essere utilizzato per:

- Verificare se l'operazione è completata
- Attendere il completamento dell'operazione
- Recuperare il risultato dell'operazione
- Annullare l'operazione

```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
```

### Metodi principali

- **`cancel(boolean mayInterruptIfRunning)`**: Tenta di annullare l'esecuzione del task. Se `mayInterruptIfRunning` è `true`, il thread che esegue il task verrà interrotto.

- **`isCancelled()`**: Restituisce `true` se il task è stato annullato prima del suo completamento normale.

- **`isDone()`**: Restituisce `true` se il task è completato (normalmente, con eccezione o annullato).

- **`get()`**: Attende, se necessario, il completamento del task e restituisce il risultato. Questo metodo è bloccante.

- **`get(long timeout, TimeUnit unit)`**: Attende, se necessario, il completamento del task per un tempo massimo specificato e restituisce il risultato se disponibile.

## Ottenere risultati da attività asincrone

Per eseguire un `Callable` e ottenere un `Future`, si utilizza un `ExecutorService`:

```java
ExecutorService executor = Executors.newSingleThreadExecutor();

// Sottomissione di un Callable
Future<Integer> future = executor.submit(() -> {
    Thread.sleep(2000);
    return 42;
});

// Esecuzione di altre operazioni mentre il task è in corso
System.out.println("Elaborazione in corso...");

// Recupero del risultato (bloccante)
try {
    Integer result = future.get();
    System.out.println("Risultato: " + result);
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    executor.shutdown();
}
```

### Utilizzo di timeout

Per evitare blocchi indefiniti, è possibile specificare un timeout quando si recupera il risultato:

```java
try {
    Integer result = future.get(5, TimeUnit.SECONDS);
    System.out.println("Risultato: " + result);
} catch (TimeoutException e) {
    System.out.println("L'operazione ha impiegato troppo tempo!");
    future.cancel(true); // Annulla l'operazione
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}
```

## Esecuzione di multiple attività

È possibile eseguire più task contemporaneamente e gestire i loro risultati:

### Esecuzione di una collezione di task

```java
List<Callable<Integer>> tasks = Arrays.asList(
    () -> {
        Thread.sleep(2000);
        return 1;
    },
    () -> {
        Thread.sleep(1000);
        return 2;
    },
    () -> {
        Thread.sleep(3000);
        return 3;
    }
);

ExecutorService executor = Executors.newFixedThreadPool(3);

try {
    // Esecuzione di tutti i task e raccolta dei Future
    List<Future<Integer>> futures = executor.invokeAll(tasks);
    
    // Elaborazione dei risultati
    for (Future<Integer> future : futures) {
        System.out.println("Risultato: " + future.get());
    }
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    executor.shutdown();
}
```

### Ottenere il primo risultato disponibile

```java
try {
    // Esecuzione di tutti i task e restituzione del primo risultato disponibile
    Integer result = executor.invokeAny(tasks);
    System.out.println("Primo risultato disponibile: " + result);
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
} finally {
    executor.shutdown();
}
```

## Gestione delle eccezioni

Quando un `Callable` lancia un'eccezione, questa viene incapsulata in un'`ExecutionException` quando si chiama `future.get()`:

```java
Future<Integer> future = executor.submit(() -> {
    if (Math.random() < 0.5) {
        throw new IllegalStateException("Errore simulato");
    }
    return 42;
});

try {
    Integer result = future.get();
    System.out.println("Risultato: " + result);
} catch (ExecutionException e) {
    System.out.println("Il task ha lanciato un'eccezione: " + e.getCause().getMessage());
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.out.println("L'operazione è stata interrotta");
}
```

## Implementazione di FutureTask

Java fornisce anche una classe concreta `FutureTask<V>` che implementa sia `Future<V>` che `Runnable`, permettendo di eseguire un `Callable` come un `Runnable`:

```java
Callable<Integer> callable = () -> {
    Thread.sleep(2000);
    return 42;
};

FutureTask<Integer> futureTask = new FutureTask<>(callable);

// Esecuzione in un nuovo thread
new Thread(futureTask).start();

// Oppure sottomissione a un ExecutorService
// executor.execute(futureTask);

try {
    Integer result = futureTask.get();
    System.out.println("Risultato: " + result);
} catch (InterruptedException | ExecutionException e) {
    e.printStackTrace();
}
```

## Best Practices

1. **Gestione appropriata delle eccezioni**: Catturare sempre `InterruptedException` e `ExecutionException` quando si chiama `future.get()`.

2. **Utilizzo di timeout**: Evitare blocchi indefiniti utilizzando la versione di `get()` con timeout.

3. **Cancellazione dei task**: Annullare i task che non sono più necessari per liberare risorse.

4. **Shutdown degli executor**: Chiudere sempre gli executor quando non sono più necessari.

5. **Evitare operazioni bloccanti nei Callable**: Se possibile, evitare operazioni che bloccano indefinitamente all'interno dei `Callable`.

## Conclusione

Le interfacce `Callable` e `Future` forniscono un potente meccanismo per eseguire operazioni asincrone che restituiscono un risultato. Combinandole con il framework Executor, è possibile implementare pattern di concorrenza avanzati e gestire in modo efficiente l'esecuzione di task paralleli.