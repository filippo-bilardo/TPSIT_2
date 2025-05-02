# Join dei Thread

## Introduzione

In Java, il metodo `join()` è uno strumento fondamentale per la sincronizzazione tra thread. Questo metodo permette a un thread di attendere il completamento dell'esecuzione di un altro thread prima di proseguire. Questo meccanismo è particolarmente utile quando un thread ha bisogno dei risultati prodotti da un altro thread o quando è necessario garantire che determinate operazioni vengano eseguite in sequenza, pur mantenendo i vantaggi della programmazione concorrente.

## Il Metodo join()

### Sintassi Base

Il metodo `join()` viene chiamato sull'istanza del thread di cui si vuole attendere il completamento:

```java
Thread thread = new Thread(() -> {
    // Codice del thread
});
thread.start();

try {
    thread.join(); // Il thread corrente attende che 'thread' termini
} catch (InterruptedException e) {
    // Gestione dell'interruzione
    Thread.currentThread().interrupt();
}

// Questo codice verrà eseguito solo dopo che 'thread' è terminato
```

### Varianti con Timeout

Il metodo `join()` ha due varianti che permettono di specificare un timeout, dopo il quale il thread chiamante riprenderà l'esecuzione anche se il thread target non è ancora terminato:

```java
// Attende al massimo 1000 millisecondi (1 secondo)
try {
    thread.join(1000);
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}

// Attende al massimo 1 secondo e 500 millisecondi
try {
    thread.join(1000, 500000); // millisecondi, nanosecondi
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

### Verifica del Completamento

Dopo aver chiamato `join()` con un timeout, potrebbe essere necessario verificare se il thread è effettivamente terminato:

```java
try {
    thread.join(1000); // Attende al massimo 1 secondo
    
    if (thread.isAlive()) {
        // Il thread è ancora in esecuzione dopo il timeout
        System.out.println("Il thread non ha terminato entro il timeout");
    } else {
        // Il thread è terminato
        System.out.println("Il thread ha terminato correttamente");
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

## Pattern di Utilizzo

### 1. Attesa di Task Paralleli

Uno dei pattern più comuni è l'esecuzione di più task in parallelo e poi l'attesa che tutti completino prima di procedere:

```java
public class ParallelTasksExample {
    public static void main(String[] args) {
        // Creazione di più thread per eseguire task in parallelo
        Thread task1 = new Thread(() -> {
            System.out.println("Task 1 in esecuzione...");
            try {
                Thread.sleep(2000); // Simula un'operazione che richiede 2 secondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 1 completato");
        });
        
        Thread task2 = new Thread(() -> {
            System.out.println("Task 2 in esecuzione...");
            try {
                Thread.sleep(3000); // Simula un'operazione che richiede 3 secondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 2 completato");
        });
        
        Thread task3 = new Thread(() -> {
            System.out.println("Task 3 in esecuzione...");
            try {
                Thread.sleep(1500); // Simula un'operazione che richiede 1.5 secondi
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Task 3 completato");
        });
        
        // Avvio di tutti i thread
        task1.start();
        task2.start();
        task3.start();
        
        // Attesa del completamento di tutti i thread
        try {
            System.out.println("In attesa del completamento di tutti i task...");
            task1.join();
            task2.join();
            task3.join();
        } catch (InterruptedException e) {
            System.out.println("Interruzione durante l'attesa");
            Thread.currentThread().interrupt();
            return;
        }
        
        System.out.println("Tutti i task sono stati completati!");
        System.out.println("Proseguimento con l'elaborazione principale...");
    }
}
```

### 2. Implementazione di un Timeout Globale

Quando si lavora con più thread, potrebbe essere necessario implementare un timeout globale per tutti:

```java
public class GlobalTimeoutExample {
    public static void main(String[] args) {
        // Creazione di più thread
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            final int threadId = i;
            Thread thread = new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + " iniziato");
                    // Simuliamo operazioni di durata variabile
                    Thread.sleep(1000 + threadId * 1000);
                    System.out.println("Thread " + threadId + " completato");
                } catch (InterruptedException e) {
                    System.out.println("Thread " + threadId + " interrotto");
                }
            });
            threads.add(thread);
            thread.start();
        }
        
        // Timeout globale di 3 secondi
        long globalTimeout = 3000;
        long startTime = System.currentTimeMillis();
        
        // Attendiamo ogni thread con un timeout adattivo
        for (Thread thread : threads) {
            try {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTimeout = Math.max(0, globalTimeout - elapsedTime);
                
                if (remainingTimeout > 0) {
                    thread.join(remainingTimeout);
                }
                
                if (thread.isAlive()) {
                    // Il thread non ha terminato entro il timeout
                    System.out.println("Timeout scaduto per un thread, interrompiamo");
                    thread.interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        System.out.println("Completamento dell'operazione (con o senza timeout)");
    }
}
```

### 3. Raccolta di Risultati da Thread

Un pattern comune è l'utilizzo di `join()` per attendere i risultati prodotti da thread paralleli:

```java
public class ResultCollectionExample {
    static class Worker implements Runnable {
        private final int id;
        private final List<Integer> results;
        
        public Worker(int id, List<Integer> results) {
            this.id = id;
            this.results = results;
        }
        
        @Override
        public void run() {
            try {
                System.out.println("Worker " + id + ": calcolo in corso...");
                Thread.sleep(1000 + (int)(Math.random() * 2000)); // Tempo variabile
                
                // Simula un calcolo e aggiunge il risultato alla lista condivisa
                int result = id * 10;
                synchronized (results) {
                    results.add(result);
                }
                
                System.out.println("Worker " + id + ": calcolo completato con risultato " + result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public static void main(String[] args) {
        // Lista condivisa per i risultati (utilizziamo una lista sincronizzata)
        List<Integer> results = Collections.synchronizedList(new ArrayList<>());
        
        // Creazione dei worker thread
        List<Thread> workers = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Thread worker = new Thread(new Worker(i, results));
            workers.add(worker);
            worker.start();
        }
        
        // Attendiamo il completamento di tutti i worker
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Interruzione durante l'attesa dei worker");
                break;
            }
        }
        
        // Elaborazione dei risultati
        System.out.println("\nTutti i worker hanno completato. Risultati:");
        int sum = 0;
        for (Integer result : results) {
            System.out.println("- Risultato: " + result);
            sum += result;
        }
        
        System.out.println("Somma totale dei risultati: " + sum);
    }
}
```

## Esempi Avanzati

### Esempio 1: Implementazione di un Task Executor con Timeout

```java
public class TaskExecutorWithTimeout {
    public static <T> Optional<T> executeWithTimeout(Callable<T> task, long timeoutMillis) {
        final AtomicReference<T> result = new AtomicReference<>();
        final AtomicReference<Exception> exception = new AtomicReference<>();
        
        Thread worker = new Thread(() -> {
            try {
                result.set(task.call());
            } catch (Exception e) {
                exception.set(e);
            }
        });
        
        worker.start();
        
        try {
            worker.join(timeoutMillis);
            
            if (worker.isAlive()) {
                // Il task non è stato completato entro il timeout
                worker.interrupt();
                return Optional.empty();
            }
            
            if (exception.get() != null) {
                // Il task ha lanciato un'eccezione
                throw new RuntimeException("Task fallito", exception.get());
            }
            
            // Il task è stato completato con successo
            return Optional.ofNullable(result.get());
            
        } catch (InterruptedException e) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            return Optional.empty();
        }
    }
    
    public static void main(String[] args) {
        // Esempio di utilizzo: task che completa normalmente
        Optional<String> result1 = executeWithTimeout(() -> {
            Thread.sleep(500); // Operazione che richiede 500ms
            return "Operazione completata con successo";
        }, 1000); // Timeout di 1 secondo
        
        System.out.println("Risultato 1: " + (result1.isPresent() ? result1.get() : "Timeout"));
        
        // Esempio di utilizzo: task che va in timeout
        Optional<String> result2 = executeWithTimeout(() -> {
            Thread.sleep(2000); // Operazione che richiede 2 secondi
            return "Questa stringa non verrà mai restituita";
        }, 1000); // Timeout di 1 secondo
        
        System.out.println("Risultato 2: " + (result2.isPresent() ? result2.get() : "Timeout"));
    }
}
```

### Esempio 2: Implementazione di un CountDownLatch Semplificato

```java
public class SimpleCountDownLatch {
    private int count;
    
    public SimpleCountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("Il conteggio non può essere negativo");
        this.count = count;
    }
    
    public synchronized void countDown() {
        if (count > 0) {
            count--;
            if (count == 0) {
                notifyAll(); // Sveglia tutti i thread in attesa
            }
        }
    }
    
    public synchronized void await() throws InterruptedException {
        while (count > 0) {
            wait(); // Attende finché count non diventa 0
        }
    }
    
    public synchronized boolean await(long timeout) throws InterruptedException {
        if (count == 0) return true;
        
        long startTime = System.currentTimeMillis();
        long remainingTime = timeout;
        
        while (count > 0 && remainingTime > 0) {
            wait(remainingTime);
            remainingTime = timeout - (System.currentTimeMillis() - startTime);
        }
        
        return count == 0;
    }
    
    public static void main(String[] args) {
        final int THREAD_COUNT = 5;
        SimpleCountDownLatch latch = new SimpleCountDownLatch(THREAD_COUNT);
        
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    System.out.println("Thread " + threadId + ": iniziato");
                    Thread.sleep(1000 + threadId * 500); // Tempo variabile
                    System.out.println("Thread " + threadId + ": completato");
                    latch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        
        System.out.println("In attesa che tutti i thread completino...");
        try {
            boolean completed = latch.await(4000); // Timeout di 4 secondi
            if (completed) {
                System.out.println("Tutti i thread hanno completato entro il timeout");
            } else {
                System.out.println("Timeout scaduto, alcuni thread potrebbero essere ancora in esecuzione");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Attesa interrotta");
        }
    }
}
```

## Best Practices

### 1. Gestire Sempre l'InterruptedException

Il metodo `join()` può lanciare un'`InterruptedException` se il thread che sta attendendo viene interrotto. È importante gestire questa eccezione correttamente:

```java
try {
    thread.join();
} catch (InterruptedException e) {
    // Ripristina il flag di interruzione
    Thread.currentThread().interrupt();
    // Gestisci l'interruzione in modo appropriato
    return; // Spesso è opportuno terminare l'operazione corrente
}
```

### 2. Utilizzare Timeout Appropriati

Quando si utilizza `join()` con un timeout, è importante scegliere valori appropriati per evitare attese troppo lunghe o troppo brevi:

```java
// Timeout troppo breve potrebbe non dare al thread abbastanza tempo
// Timeout troppo lungo potrebbe bloccare l'applicazione inutilmente
try {
    // Scegli un timeout ragionevole basato sul contesto dell'applicazione
    thread.join(5000); // 5 secondi
    
    if (thread.isAlive()) {
        // Gestisci il caso in cui il thread non ha terminato entro il timeout
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

### 3. Verificare lo Stato del Thread dopo join() con Timeout

Dopo aver chiamato `join()` con un timeout, è importante verificare se il thread è effettivamente terminato utilizzando `isAlive()`:

```java
try {
    thread.join(timeout);
    
    if (thread.isAlive()) {
        // Il thread è ancora in esecuzione
        // Decidi se interromperlo, attendere ulteriormente o procedere comunque
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

### 4. Evitare Deadlock

Fai attenzione quando utilizzi `join()` in scenari complessi con più thread, poiché potrebbe portare a deadlock se i thread attendono reciprocamente:

```java
// Scenario potenzialmente pericoloso
Thread threadA = new Thread(() -> {
    // ... codice ...
    try {
        threadB.join(); // threadA attende threadB
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

Thread threadB = new Thread(() -> {
    // ... codice ...
    try {
        threadA.join(); // threadB attende threadA
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

// Questo porterà a un deadlock!
threadA.start();
threadB.start();
```

### 5. Combinare join() con isAlive() per Polling

In alcuni casi, potrebbe essere utile combinare `join()` con brevi timeout e `isAlive()` per implementare un meccanismo di polling:

```java
while (thread.isAlive()) {
    try {
        // Attendi per brevi intervalli e verifica periodicamente una condizione
        thread.join(100); // 100ms
        
        // Esegui operazioni periodiche durante l'attesa
        updateProgressBar();
        checkForCancellation();
        
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
    }
}
```

## Conclusione

Il metodo `join()` è uno strumento potente per la sincronizzazione tra thread in Java. Permette di coordinare l'esecuzione di thread paralleli, attendendo il completamento di specifici thread prima di procedere con altre operazioni. Questo meccanismo è fondamentale per implementare pattern di concorrenza come fork-join, barriere di sincronizzazione e raccolta di risultati da operazioni parallele.

Utilizzando correttamente `join()`, insieme alle sue varianti con timeout e in combinazione con altri meccanismi di sincronizzazione, è possibile sviluppare applicazioni multi-thread robuste ed efficienti, che sfruttano al meglio i vantaggi della programmazione concorrente mantenendo il controllo sul flusso di esecuzione.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Interruzione dei Thread](./03c-InterruzioneThread.md)
- [➡️ Sincronizzazione](../02-Sincronizzazione/README.md)