# Race Condition e Problemi di Visibilità

In questa sezione, esploreremo due problemi fondamentali nella programmazione concorrente: le race condition e i problemi di visibilità. Comprenderemo come identificarli, le loro cause e le tecniche per risolverli.

## Identificazione delle race condition

Una race condition si verifica quando il comportamento di un programma dipende dalla sequenza o dalla tempistica di eventi non controllabili, come l'ordine di esecuzione dei thread.

### Caratteristiche delle race condition

1. **Risultati non deterministici**: Lo stesso codice produce risultati diversi in esecuzioni successive.
2. **Dipendenza dalla tempistica**: Il comportamento cambia in base alla velocità relativa dei thread.
3. **Difficoltà di riproduzione**: I problemi possono manifestarsi raramente e in modo imprevedibile.

### Esempio classico di race condition

```java
public class RaceConditionExample {
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter++; // Operazione non atomica
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                counter++; // Operazione non atomica
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Valore finale: " + counter); // Probabilmente non sarà 20000
    }
}
```

In questo esempio, l'operazione `counter++` non è atomica ma composta da tre operazioni distinte:
1. Leggere il valore attuale di counter
2. Incrementare il valore
3. Scrivere il nuovo valore in counter

Se due thread eseguono queste operazioni contemporaneamente, possono verificarsi interferenze che portano a risultati errati.

### Strumenti per identificare race condition

#### 1. Thread Sanitizer

Thread Sanitizer è uno strumento di analisi dinamica che può rilevare race condition durante l'esecuzione.

#### 2. FindBugs e SpotBugs

Questi strumenti di analisi statica possono identificare potenziali race condition nel codice.

#### 3. Java PathFinder

Uno strumento di model checking che può esplorare sistematicamente diverse interleaving di thread per trovare race condition.

## Problemi di memory model

Il Java Memory Model (JMM) definisce come i thread interagiscono attraverso la memoria. Comprendere il JMM è essenziale per scrivere codice concorrente corretto.

### Visibilità delle variabili

In Java, le modifiche apportate a una variabile da un thread potrebbero non essere immediatamente visibili ad altri thread a causa di ottimizzazioni del compilatore, caching della CPU e riordinamento delle istruzioni.

```java
public class VisibilityProblem {
    private static boolean flag = false;
    private static int data = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread writer = new Thread(() -> {
            data = 42; // Scrittura di data
            flag = true; // Segnalazione che data è pronto
        });
        
        Thread reader = new Thread(() -> {
            while (!flag) { // Attesa che data sia pronto
                Thread.yield(); // Cede il controllo ad altri thread
            }
            System.out.println(data); // Potrebbe non vedere 42!
        });
        
        reader.start();
        writer.start();
        reader.join();
        writer.join();
    }
}
```

In questo esempio, il thread reader potrebbe vedere `flag` come true ma ancora vedere il vecchio valore di `data` (0 invece di 42) a causa di problemi di visibilità.

### Riordinamento delle istruzioni

Il compilatore e la JVM possono riordinare le istruzioni per ottimizzare le prestazioni, purché il comportamento del programma a singolo thread rimanga invariato. Tuttavia, questo può causare problemi in programmi multi-thread.

## Keyword volatile

La keyword `volatile` in Java garantisce che le letture e le scritture di una variabile siano sempre effettuate dalla/alla memoria principale, evitando problemi di visibilità.

### Quando usare volatile

1. **Flag di controllo**: Per variabili booleane usate come flag di controllo tra thread.
2. **Pubblicazione di oggetti immutabili**: Per garantire la visibilità di riferimenti a oggetti immutabili.
3. **Double-checked locking**: Come parte del pattern double-checked locking.

### Esempio di uso corretto di volatile

```java
public class VolatileExample {
    private static volatile boolean flag = false;
    private static volatile int data = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread writer = new Thread(() -> {
            data = 42;
            flag = true; // Garantisce che data=42 sia visibile quando flag=true è visibile
        });
        
        Thread reader = new Thread(() -> {
            while (!flag) {
                Thread.yield();
            }
            System.out.println(data); // Ora vedrà sicuramente 42
        });
        
        reader.start();
        writer.start();
        reader.join();
        writer.join();
    }
}
```

### Limitazioni di volatile

1. **Non garantisce atomicità**: `volatile` garantisce solo la visibilità, non l'atomicità delle operazioni.
2. **Non adatto per operazioni composte**: Operazioni come `i++` non sono atomiche anche se `i` è volatile.

## Tecniche di correzione

### 1. Sincronizzazione

L'uso di blocchi `synchronized` garantisce sia la visibilità che l'atomicità.

```java
public class SynchronizedCounter {
    private int counter = 0;
    
    public synchronized void increment() {
        counter++;
    }
    
    public synchronized int getValue() {
        return counter;
    }
    
    public static void main(String[] args) throws InterruptedException {
        SynchronizedCounter sc = new SynchronizedCounter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                sc.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                sc.increment();
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Valore finale: " + sc.getValue()); // Sarà sempre 20000
    }
}
```

### 2. Classi atomiche

Le classi nel pacchetto `java.util.concurrent.atomic` forniscono operazioni atomiche senza necessità di sincronizzazione esplicita.

```java
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {
    private AtomicInteger counter = new AtomicInteger(0);
    
    public void increment() {
        counter.incrementAndGet();
    }
    
    public int getValue() {
        return counter.get();
    }
    
    public static void main(String[] args) throws InterruptedException {
        AtomicCounter ac = new AtomicCounter();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                ac.increment();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                ac.increment();
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Valore finale: " + ac.getValue()); // Sarà sempre 20000
    }
}
```

### 3. Lock espliciti

I lock nel pacchetto `java.util.concurrent.locks` offrono un controllo più fine rispetto ai blocchi `synchronized`.

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockCounter {
    private int counter = 0;
    private final Lock lock = new ReentrantLock();
    
    public void increment() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock(); // Importante rilasciare il lock anche in caso di eccezione
        }
    }
    
    public int getValue() {
        lock.lock();
        try {
            return counter;
        } finally {
            lock.unlock();
        }
    }
}
```

### 4. Immutabilità

Gli oggetti immutabili sono intrinsecamente thread-safe e non richiedono sincronizzazione.

```java
public final class ImmutablePoint {
    private final int x;
    private final int y;
    
    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public ImmutablePoint translate(int dx, int dy) {
        return new ImmutablePoint(x + dx, y + dy); // Crea un nuovo oggetto
    }
}
```

### 5. ThreadLocal

`ThreadLocal` permette di creare variabili locali al thread, eliminando la condivisione e quindi le race condition.

```java
public class ThreadLocalExample {
    private static ThreadLocal<Integer> threadLocalValue = ThreadLocal.withInitial(() -> 0);
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            threadLocalValue.set(1);
            System.out.println("Thread 1: " + threadLocalValue.get());
        });
        
        Thread t2 = new Thread(() -> {
            threadLocalValue.set(2);
            System.out.println("Thread 2: " + threadLocalValue.get());
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Main thread: " + threadLocalValue.get()); // Valore indipendente
    }
}
```

## Best Practices

1. **Minimizzare lo stato condiviso**: Meno stato condiviso significa meno opportunità per race condition.
2. **Preferire l'immutabilità**: Gli oggetti immutabili sono intrinsecamente thread-safe.
3. **Usare strutture dati thread-safe**: Utilizzare le implementazioni thread-safe in `java.util.concurrent`.
4. **Documentare le assunzioni sulla concorrenza**: Rendere esplicite le assunzioni sulla thread-safety.
5. **Testare con carichi concorrenti**: Utilizzare test di stress per far emergere problemi di concorrenza.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Deadlock e Starvation](./02-DeadlockStarvation.md)
- Successivo: [Strumenti di Debugging](./04-StrumentiDebugging.md)