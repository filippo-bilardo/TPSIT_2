# Pattern Produttore-Consumatore

## Introduzione

Il pattern Produttore-Consumatore è uno dei modelli fondamentali nella programmazione concorrente. Questo pattern risolve il problema di coordinare thread che producono dati (produttori) con thread che elaborano tali dati (consumatori), utilizzando un buffer condiviso come intermediario.

## Descrizione del Pattern

Il pattern Produttore-Consumatore è composto da tre componenti principali:

1. **Produttori**: Thread che generano dati e li inseriscono nel buffer condiviso
2. **Consumatori**: Thread che prelevano dati dal buffer condiviso e li elaborano
3. **Buffer Condiviso**: Struttura dati che funge da intermediario tra produttori e consumatori

![Diagramma Produttore-Consumatore](./immagini/produttore-consumatore.png)

## Implementazione del Buffer Condiviso

Il buffer condiviso è l'elemento centrale del pattern. Può essere implementato in vari modi, ma deve garantire l'accesso thread-safe e gestire le condizioni di buffer pieno e buffer vuoto.

### Buffer Illimitato

Un buffer illimitato non ha restrizioni sulla sua capacità. I produttori possono sempre aggiungere elementi, mentre i consumatori devono attendere solo se il buffer è vuoto.

```java
public class BufferIllimitato<T> {
    private final Queue<T> buffer = new LinkedList<>();
    
    public synchronized void put(T item) {
        buffer.add(item);
        notify(); // Notifica i consumatori in attesa
    }
    
    public synchronized T get() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Attende che il buffer non sia più vuoto
        }
        return buffer.poll();
    }
}
```

### Buffer Limitato

Un buffer limitato ha una capacità massima. I produttori devono attendere quando il buffer è pieno, mentre i consumatori devono attendere quando è vuoto.

```java
public class BufferLimitato<T> {
    private final Queue<T> buffer = new LinkedList<>();
    private final int capacita;
    
    public BufferLimitato(int capacita) {
        this.capacita = capacita;
    }
    
    public synchronized void put(T item) throws InterruptedException {
        while (buffer.size() == capacita) {
            wait(); // Attende che il buffer non sia più pieno
        }
        buffer.add(item);
        notifyAll(); // Notifica tutti i thread in attesa
    }
    
    public synchronized T get() throws InterruptedException {
        while (buffer.isEmpty()) {
            wait(); // Attende che il buffer non sia più vuoto
        }
        T item = buffer.poll();
        notifyAll(); // Notifica tutti i thread in attesa
        return item;
    }
}
```

## Sincronizzazione Produttore-Consumatore

La sincronizzazione tra produttori e consumatori è cruciale per garantire il corretto funzionamento del pattern. I punti chiave sono:

1. **Accesso Esclusivo**: Solo un thread alla volta può accedere al buffer
2. **Condizioni di Attesa**: I produttori attendono quando il buffer è pieno, i consumatori quando è vuoto
3. **Notifiche**: I produttori notificano i consumatori quando aggiungono elementi, i consumatori notificano i produttori quando rimuovono elementi

### Utilizzo di `wait()` e `notifyAll()`

I metodi `wait()` e `notifyAll()` sono fondamentali per implementare la sincronizzazione:

- `wait()`: Fa attendere un thread fino a quando una condizione non è soddisfatta
- `notifyAll()`: Sveglia tutti i thread in attesa per verificare se le loro condizioni sono soddisfatte

È importante utilizzare `notifyAll()` invece di `notify()` per evitare problemi di deadlock, specialmente quando ci sono sia produttori che consumatori in attesa.

## Gestione del Buffer Pieno/Vuoto

La gestione delle condizioni di buffer pieno e buffer vuoto è un aspetto critico del pattern:

### Buffer Vuoto

Quando il buffer è vuoto:
1. I consumatori devono attendere
2. I produttori possono inserire elementi e notificare i consumatori

### Buffer Pieno

Quando il buffer è pieno:
1. I produttori devono attendere
2. I consumatori possono prelevare elementi e notificare i produttori

## Esempio Completo

Ecco un esempio completo di implementazione del pattern Produttore-Consumatore:

```java
public class ProduttoreConsumatore {
    public static void main(String[] args) {
        BufferLimitato<Integer> buffer = new BufferLimitato<>(5);
        
        // Crea e avvia i produttori
        for (int i = 0; i < 3; i++) {
            new Thread(new Produttore(buffer, i)).start();
        }
        
        // Crea e avvia i consumatori
        for (int i = 0; i < 2; i++) {
            new Thread(new Consumatore(buffer, i)).start();
        }
    }
    
    static class Produttore implements Runnable {
        private final BufferLimitato<Integer> buffer;
        private final int id;
        
        public Produttore(BufferLimitato<Integer> buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = (id * 100) + i;
                    buffer.put(item);
                    System.out.println("Produttore " + id + " ha prodotto: " + item);
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    static class Consumatore implements Runnable {
        private final BufferLimitato<Integer> buffer;
        private final int id;
        
        public Consumatore(BufferLimitato<Integer> buffer, int id) {
            this.buffer = buffer;
            this.id = id;
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    int item = buffer.get();
                    System.out.println("Consumatore " + id + " ha consumato: " + item);
                    Thread.sleep((long) (Math.random() * 2000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

## Varianti del Pattern

Esistono diverse varianti del pattern Produttore-Consumatore:

### Multiple Produttori e Consumatori

La versione più comune prevede più thread produttori e più thread consumatori che condividono lo stesso buffer. Questa configurazione richiede una sincronizzazione attenta per evitare race condition.

### Buffer Circolare

Un buffer circolare (o ring buffer) è una implementazione efficiente per buffer di dimensione fissa, dove gli indici di inserimento e prelievo ruotano all'interno dell'array.

```java
public class BufferCircolare<T> {
    private final T[] buffer;
    private int putIndex = 0;
    private int getIndex = 0;
    private int count = 0;
    
    @SuppressWarnings("unchecked")
    public BufferCircolare(int capacita) {
        buffer = (T[]) new Object[capacita];
    }
    
    public synchronized void put(T item) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }
        buffer[putIndex] = item;
        putIndex = (putIndex + 1) % buffer.length;
        count++;
        notifyAll();
    }
    
    public synchronized T get() throws InterruptedException {
        while (count == 0) {
            wait();
        }
        T item = buffer[getIndex];
        getIndex = (getIndex + 1) % buffer.length;
        count--;
        notifyAll();
        return item;
    }
}
```

### Priorità

È possibile implementare un buffer con priorità, dove gli elementi hanno diversi livelli di importanza e vengono consumati in base alla loro priorità anziché in ordine FIFO.

## Considerazioni Avanzate

### Prestazioni

Le prestazioni del pattern Produttore-Consumatore dipendono da vari fattori:

- **Dimensione del Buffer**: Un buffer troppo piccolo può causare frequenti attese, mentre un buffer troppo grande può consumare memoria inutilmente
- **Numero di Produttori e Consumatori**: Trovare il giusto equilibrio tra produttori e consumatori è importante per massimizzare il throughput
- **Granularità della Sincronizzazione**: Una sincronizzazione troppo fine può causare overhead, mentre una troppo grossolana può limitare il parallelismo

### Implementazioni Alternative

Java offre classi nella libreria `java.util.concurrent` che implementano il pattern Produttore-Consumatore in modo efficiente:

- `BlockingQueue`: Interfaccia che rappresenta una coda thread-safe con operazioni bloccanti
- `ArrayBlockingQueue`: Implementazione basata su array con capacità fissa
- `LinkedBlockingQueue`: Implementazione basata su linked list con capacità opzionalmente limitata

Esempio con `BlockingQueue`:

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;

public class ProduttoreConsumatoreConcurrent {
    public static void main(String[] args) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        
        // Crea e avvia i produttori
        for (int i = 0; i < 3; i++) {
            new Thread(new Produttore(queue, i)).start();
        }
        
        // Crea e avvia i consumatori
        for (int i = 0; i < 2; i++) {
            new Thread(new Consumatore(queue, i)).start();
        }
    }
    
    static class Produttore implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final int id;
        
        public Produttore(BlockingQueue<Integer> queue, int id) {
            this.queue = queue;
            this.id = id;
        }
        
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int item = (id * 100) + i;
                    queue.put(item); // Blocca se la coda è piena
                    System.out.println("Produttore " + id + " ha prodotto: " + item);
                    Thread.sleep((long) (Math.random() * 1000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    static class Consumatore implements Runnable {
        private final BlockingQueue<Integer> queue;
        private final int id;
        
        public Consumatore(BlockingQueue<Integer> queue, int id) {
            this.queue = queue;
            this.id = id;
        }
        
        @Override
        public void run() {
            try {
                while (true) {
                    int item = queue.take(); // Blocca se la coda è vuota
                    System.out.println("Consumatore " + id + " ha consumato: " + item);
                    Thread.sleep((long) (Math.random() * 2000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
```

## Conclusione

Il pattern Produttore-Consumatore è un modello potente per gestire la comunicazione tra thread in modo efficiente e thread-safe. Comprendere questo pattern è fondamentale per implementare sistemi concorrenti robusti e scalabili.

Nel prossimo capitolo, esploreremo tecniche avanzate per lo scambio sicuro di dati tra thread, concentrandoci su immutabilità, passaggio di messaggi e altri meccanismi di comunicazione.