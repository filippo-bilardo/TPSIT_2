[Indice](./README.md)

# CompletableFuture

## Introduzione

La classe `CompletableFuture<T>` introdotta in Java 8 rappresenta un'evoluzione significativa dell'interfaccia `Future<T>` e fornisce un potente framework per la programmazione asincrona. Mentre l'interfaccia `Future` offre un modo per recuperare il risultato di un'operazione asincrona, presenta alcune limitazioni: non fornisce notifiche di completamento, non permette di concatenare operazioni e non offre gestione degli errori integrata.

`CompletableFuture` risolve queste limitazioni e introduce un approccio funzionale alla programmazione asincrona, permettendo di comporre operazioni in modo fluido e reattivo.

## Caratteristiche Principali

### Creazione di CompletableFuture

Esistono diversi modi per creare un `CompletableFuture`:

```java
// Creazione di un CompletableFuture vuoto
CompletableFuture<String> future = new CompletableFuture<>();

// Completamento manuale
future.complete("Risultato");

// Creazione di un CompletableFuture già completato
CompletableFuture<String> completedFuture = CompletableFuture.completedFuture("Già completato");

// Esecuzione asincrona di un'operazione
CompletableFuture<String> asyncFuture = CompletableFuture.supplyAsync(() -> {
    // Operazione che restituisce un risultato
    return "Risultato dell'operazione asincrona";
});

// Esecuzione asincrona di un'operazione senza risultato
CompletableFuture<Void> asyncAction = CompletableFuture.runAsync(() -> {
    // Operazione che non restituisce risultati
    System.out.println("Esecuzione di un'azione asincrona");
});
```

### Composizione di Operazioni Asincrone

Una delle caratteristiche più potenti di `CompletableFuture` è la capacità di concatenare operazioni asincrone:

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // Prima operazione asincrona
    return "Hello";
}).thenApply(result -> {
    // Trasformazione del risultato
    return result + " World";
}).thenApply(String::toUpperCase);

// Il risultato finale sarà "HELLO WORLD"
```

I metodi principali per la composizione sono:

- `thenApply`: trasforma il risultato di un'operazione asincrona
- `thenAccept`: consuma il risultato senza restituire un valore
- `thenRun`: esegue un'azione dopo il completamento, ignorando il risultato
- `thenCompose`: concatena due operazioni asincrone dove la seconda dipende dal risultato della prima
- `thenCombine`: combina i risultati di due operazioni asincrone indipendenti

### Esecuzione Asincrona e Controllo dell'Executor

I metodi di `CompletableFuture` hanno varianti che permettono di specificare l'`Executor` da utilizzare per l'esecuzione asincrona:

```java
Executor executor = Executors.newFixedThreadPool(5);

CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "Operazione eseguita in un pool personalizzato";
}, executor);

// Varianti asincrone dei metodi di composizione
future.thenApplyAsync(result -> result.toUpperCase(), executor);
future.thenAcceptAsync(result -> System.out.println(result), executor);
```

### Gestione degli Errori

`CompletableFuture` offre meccanismi integrati per la gestione degli errori:

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    if (Math.random() < 0.5) {
        throw new RuntimeException("Errore simulato");
    }
    return "Operazione completata con successo";
}).exceptionally(ex -> {
    // Gestione dell'eccezione
    return "Errore: " + ex.getMessage();
});

// Gestione più avanzata degli errori
CompletableFuture<String> futureWithRecover = CompletableFuture.supplyAsync(() -> {
    // Operazione che potrebbe fallire
    throw new RuntimeException("Errore durante l'operazione");
}).handle((result, ex) -> {
    if (ex != null) {
        return "Si è verificato un errore: " + ex.getMessage();
    } else {
        return "Risultato: " + result;
    }
});
```

### Combinazione di Risultati Multipli

`CompletableFuture` permette di combinare i risultati di più operazioni asincrone:

```java
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "World");

// Combinazione di due future
CompletableFuture<String> combined = future1.thenCombine(future2, (result1, result2) -> 
    result1 + " " + result2);

// Esecuzione di un'azione quando entrambi i future sono completati
future1.thenAcceptBoth(future2, (result1, result2) -> 
    System.out.println(result1 + " " + result2));

// Attesa del completamento di tutti i future
CompletableFuture<Void> allOf = CompletableFuture.allOf(future1, future2);

// Attesa del completamento di almeno uno dei future
CompletableFuture<Object> anyOf = CompletableFuture.anyOf(future1, future2);
```

## Casi d'Uso Comuni

### Parallelizzazione di Operazioni Indipendenti

```java
List<CompletableFuture<String>> futures = urls.stream()
    .map(url -> CompletableFuture.supplyAsync(() -> downloadContent(url)))
    .collect(Collectors.toList());

CompletableFuture<Void> allFutures = CompletableFuture.allOf(
    futures.toArray(new CompletableFuture[0]));

CompletableFuture<List<String>> results = allFutures.thenApply(v -> 
    futures.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList()));
```

### Pipeline di Elaborazione Asincrona

```java
CompletableFuture.supplyAsync(() -> fetchData())
    .thenApply(data -> processData(data))
    .thenApply(processedData -> formatResult(processedData))
    .thenAccept(result -> saveResult(result))
    .exceptionally(ex -> {
        logError(ex);
        return null;
    });
```

### Timeout e Cancellazione

```java
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // Operazione lunga
    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
    return "Risultato";
});

// Impostazione di un timeout
future.completeOnTimeout("Timeout", 2, TimeUnit.SECONDS);

// Oppure generazione di un'eccezione in caso di timeout
future.orTimeout(2, TimeUnit.SECONDS);
```

## Confronto con Future e Promise in Altri Linguaggi

Il concetto di `CompletableFuture` in Java è simile ai Promise in JavaScript o ai Task in C#. Tutti questi meccanismi forniscono un'astrazione per operazioni asincrone che potrebbero completarsi in futuro.

Rispetto all'interfaccia `Future` tradizionale, `CompletableFuture` offre:

- Composizione fluida di operazioni asincrone
- Notifiche di completamento
- Gestione integrata degli errori
- Supporto per operazioni di timeout
- Combinazione di risultati multipli

## Best Practices

1. **Gestione appropriata degli errori**: Utilizzare sempre `exceptionally` o `handle` per gestire le eccezioni
2. **Evitare il blocco**: Non chiamare `get()` o `join()` a meno che non sia assolutamente necessario
3. **Specificare l'Executor**: Utilizzare un Executor appropriato per le operazioni asincrone
4. **Chiudere gli Executor**: Assicurarsi di chiudere gli Executor personalizzati quando non sono più necessari
5. **Evitare operazioni bloccanti**: Non eseguire operazioni bloccanti all'interno delle lambda asincrone

## Conclusione

`CompletableFuture` rappresenta un potente strumento per la programmazione asincrona in Java, offrendo un'API fluida e funzionale per la composizione di operazioni. Utilizzando `CompletableFuture`, è possibile scrivere codice asincrono più leggibile, manutenibile e robusto, migliorando le prestazioni delle applicazioni attraverso l'esecuzione parallela di operazioni indipendenti.

Nel prossimo capitolo, esploreremo esempi pratici di utilizzo del framework Executor e dei thread pool, inclusi esempi di programmazione asincrona con `CompletableFuture`.

---

[Indice](./README.md) | [Precedente: Callable e Future](./03-CallableFuture.md) | [Successivo: Esempi Pratici](./esempi/CompletableFutureExample.java)