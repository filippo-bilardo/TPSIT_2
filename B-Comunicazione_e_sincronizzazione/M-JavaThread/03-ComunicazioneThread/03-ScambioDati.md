# Scambio di Dati tra Thread

## Introduzione

Lo scambio di dati tra thread è un aspetto fondamentale della programmazione concorrente. Quando più thread condividono informazioni, è essenziale garantire che questo scambio avvenga in modo sicuro e coerente per evitare race condition, deadlock e altri problemi di concorrenza.

## Tecniche Sicure per lo Scambio di Dati

Esistono diverse tecniche per scambiare dati tra thread in modo sicuro:

### 1. Utilizzo di Oggetti Sincronizzati

La sincronizzazione è il metodo più basilare per garantire lo scambio sicuro di dati:

```java
public class DatiCondivisi {
    private int valore;
    
    public synchronized void setValore(int nuovoValore) {
        this.valore = nuovoValore;
    }
    
    public synchronized int getValore() {
        return valore;
    }
}
```

Questo approccio garantisce che solo un thread alla volta possa accedere ai metodi `setValore()` e `getValore()`, prevenendo race condition.

### 2. Utilizzo di Collezioni Thread-Safe

Java fornisce collezioni thread-safe nel package `java.util.concurrent`:

```java
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EsempioCollezioniThreadSafe {
    // Map thread-safe
    private ConcurrentHashMap<String, Integer> mappa = new ConcurrentHashMap<>();
    
    // Lista thread-safe
    private CopyOnWriteArrayList<String> lista = new CopyOnWriteArrayList<>();
    
    public void aggiungiElemento(String chiave, Integer valore) {
        mappa.put(chiave, valore);
        lista.add(chiave);
    }
}
```

Queste collezioni sono progettate per essere utilizzate in ambienti multi-thread senza richiedere sincronizzazione esplicita.

### 3. Utilizzo di Code Bloccanti

Le code bloccanti sono ideali per lo scambio di dati tra produttori e consumatori:

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScambioConCoda {
    private BlockingQueue<String> coda = new LinkedBlockingQueue<>(10);
    
    public void produci(String messaggio) throws InterruptedException {
        coda.put(messaggio); // Blocca se la coda è piena
    }
    
    public String consuma() throws InterruptedException {
        return coda.take(); // Blocca se la coda è vuota
    }
}
```

I metodi `put()` e `take()` bloccano il thread chiamante quando la coda è rispettivamente piena o vuota.

## Immutabilità e Thread Safety

Gli oggetti immutabili sono intrinsecamente thread-safe poiché il loro stato non può essere modificato dopo la creazione:

```java
public final class Messaggio {
    private final String testo;
    private final long timestamp;
    
    public Messaggio(String testo) {
        this.testo = testo;
        this.timestamp = System.currentTimeMillis();
    }
    
    public String getTesto() {
        return testo;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
}
```

Caratteristiche degli oggetti immutabili:
1. Tutti i campi sono `final`
2. La classe è dichiarata `final` per impedire sottoclassi che potrebbero violare l'immutabilità
3. Non ci sono metodi che modificano lo stato interno
4. Tutti i campi mutabili (se presenti) sono difensivamente copiati

## Passaggio di Messaggi

Il passaggio di messaggi è un paradigma in cui i thread comunicano inviando messaggi anziché condividere memoria:

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Attore {
    private final String nome;
    private final BlockingQueue<Messaggio> mailbox = new LinkedBlockingQueue<>();
    
    public Attore(String nome) {
        this.nome = nome;
        // Avvia un thread per processare i messaggi
        new Thread(this::processaMessaggi).start();
    }
    
    public void invia(Messaggio messaggio) {
        mailbox.offer(messaggio);
    }
    
    private void processaMessaggi() {
        try {
            while (true) {
                Messaggio messaggio = mailbox.take();
                System.out.println(nome + " ha ricevuto: " + messaggio.getTesto());
                // Elabora il messaggio
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

Vantaggi del passaggio di messaggi:
1. Riduce la complessità della sincronizzazione
2. Migliora la modularità e la manutenibilità
3. Facilita la distribuzione su sistemi distribuiti

## Callback e Listener

I callback e i listener permettono di implementare comunicazioni asincrone tra thread:

```java
public interface TaskListener {
    void onTaskCompleted(String risultato);
    void onTaskFailed(Exception errore);
}

public class TaskAsincrono {
    private final TaskListener listener;
    
    public TaskAsincrono(TaskListener listener) {
        this.listener = listener;
    }
    
    public void esegui() {
        new Thread(() -> {
            try {
                // Simula un'operazione lunga
                Thread.sleep(2000);
                String risultato = "Operazione completata";
                // Notifica il completamento
                listener.onTaskCompleted(risultato);
            } catch (Exception e) {
                // Notifica l'errore
                listener.onTaskFailed(e);
            }
        }).start();
    }
}
```

Utilizzo:

```java
public class Main {
    public static void main(String[] args) {
        TaskAsincrono task = new TaskAsincrono(new TaskListener() {
            @Override
            public void onTaskCompleted(String risultato) {
                System.out.println("Task completato: " + risultato);
            }
            
            @Override
            public void onTaskFailed(Exception errore) {
                System.out.println("Task fallito: " + errore.getMessage());
            }
        });
        
        task.esegui();
        System.out.println("Il main thread continua l'esecuzione...");
    }
}
```

## Best Practices per lo Scambio di Dati

1. **Preferire l'immutabilità**: Quando possibile, utilizzare oggetti immutabili per lo scambio di dati
2. **Minimizzare lo stato condiviso**: Ridurre al minimo la quantità di dati condivisi tra thread
3. **Utilizzare strutture dati thread-safe**: Sfruttare le collezioni concorrenti di Java
4. **Documentare la thread-safety**: Indicare chiaramente quali classi e metodi sono thread-safe
5. **Evitare l'escape di riferimenti**: Non esporre riferimenti a oggetti mutabili interni
6. **Utilizzare copie difensive**: Creare copie degli oggetti mutabili quando necessario

## Conclusione

Lo scambio sicuro di dati tra thread è fondamentale per creare applicazioni concorrenti robuste. Java offre diverse tecniche e strumenti per facilitare questo scambio, dalle primitive di sincronizzazione di base alle strutture dati concorrenti avanzate. La scelta della tecnica più appropriata dipende dalle specifiche esigenze dell'applicazione, considerando fattori come prestazioni, scalabilità e semplicità di implementazione.

Nei prossimi capitoli, esploreremo strumenti più avanzati per la gestione della concorrenza, come i Thread Pool e gli Executor, che forniscono meccanismi di alto livello per l'esecuzione di task concorrenti.