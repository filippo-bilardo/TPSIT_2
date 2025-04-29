# Compare-and-Swap (CAS)

L'algoritmo Compare-and-Swap (CAS) è una primitiva hardware fondamentale per l'implementazione di operazioni atomiche e strutture dati lock-free. In questo capitolo, esploreremo il funzionamento di CAS, la sua implementazione in Java e i problemi associati.

## Funzionamento dell'Algoritmo CAS

L'algoritmo CAS opera su tre valori:

1. **Posizione di memoria** (M): L'indirizzo di memoria che contiene il valore da modificare.
2. **Valore atteso** (A): Il valore che ci si aspetta di trovare nella posizione di memoria.
3. **Nuovo valore** (N): Il valore con cui si vuole sostituire il valore atteso.

L'operazione CAS funziona in questo modo:

1. Verifica se il valore corrente nella posizione di memoria M è uguale al valore atteso A.
2. Se sono uguali, sostituisce il valore in M con il nuovo valore N e restituisce `true`.
3. Se sono diversi, non fa nulla e restituisce `false`.

L'aspetto cruciale è che questa sequenza di operazioni viene eseguita come un'unica operazione atomica a livello hardware, garantendo che nessun altro thread possa interferire durante l'esecuzione.

Ecco una rappresentazione pseudocode dell'algoritmo CAS:

```
funzione CAS(M, A, N):
    se M == A:
        M = N
        restituisci true
    altrimenti:
        restituisci false
```

## Implementazione in Java

In Java, l'algoritmo CAS è implementato nelle classi del package `java.util.concurrent.atomic` attraverso il metodo `compareAndSet(expectedValue, newValue)`. Internamente, questo metodo utilizza metodi nativi che si interfacciano con le istruzioni CAS hardware specifiche della piattaforma.

Ecco un esempio di utilizzo di CAS con `AtomicInteger`:

```java
import java.util.concurrent.atomic.AtomicInteger;

public class CASExample {
    public static void main(String[] args) {
        AtomicInteger atomicInt = new AtomicInteger(5);
        
        // Tentativo di aggiornamento con CAS
        boolean success = atomicInt.compareAndSet(5, 10);
        System.out.println("Primo tentativo: " + (success ? "successo" : "fallimento"));
        System.out.println("Valore dopo il primo tentativo: " + atomicInt.get());
        
        // Altro tentativo con valore atteso errato
        boolean failure = atomicInt.compareAndSet(5, 15);
        System.out.println("Secondo tentativo: " + (failure ? "successo" : "fallimento"));
        System.out.println("Valore dopo il secondo tentativo: " + atomicInt.get());
    }
}
```

Output:
```
Primo tentativo: successo
Valore dopo il primo tentativo: 10
Secondo tentativo: fallimento
Valore dopo il secondo tentativo: 10
```

### Implementazione di un Incremento Atomico con CAS

Per comprendere meglio come CAS viene utilizzato per implementare operazioni atomiche più complesse, vediamo come potrebbe essere implementato un metodo `incrementAndGet()` utilizzando CAS:

```java
public int incrementAndGet(AtomicInteger value) {
    int current;
    int next;
    do {
        current = value.get();
        next = current + 1;
    } while (!value.compareAndSet(current, next));
    return next;
}
```

Questo pattern è noto come "ciclo di retry CAS" e funziona così:

1. Legge il valore corrente.
2. Calcola il nuovo valore desiderato.
3. Tenta di aggiornare il valore utilizzando CAS.
4. Se il CAS fallisce (perché un altro thread ha modificato il valore nel frattempo), ripete il processo.

Questo approccio garantisce che l'operazione venga completata correttamente anche in presenza di concorrenza, senza utilizzare lock espliciti.

## Problema dell'ABA

Il problema dell'ABA è una sottile complicazione che può verificarsi negli algoritmi basati su CAS. Si verifica quando un valore in una posizione di memoria cambia da A a B e poi torna ad A, facendo sì che un'operazione CAS abbia successo quando invece dovrebbe fallire.

Consideriamo questo scenario:

1. Thread 1 legge il valore A dalla posizione di memoria M.
2. Thread 1 viene sospeso.
3. Thread 2 cambia il valore in M da A a B.
4. Thread 2 esegue altre operazioni.
5. Thread 2 cambia il valore in M da B di nuovo ad A.
6. Thread 1 riprende l'esecuzione e esegue CAS(M, A, C), che ha successo perché il valore in M è ancora A.

Il problema è che, dal punto di vista del Thread 1, sembra che nulla sia cambiato, ma in realtà il Thread 2 ha modificato il valore e potenzialmente ha alterato lo stato del sistema in modi che il Thread 1 non è consapevole.

Questo problema è particolarmente rilevante nelle strutture dati basate su puntatori, come le liste concatenate lock-free, dove un nodo potrebbe essere rimosso e poi un nuovo nodo con lo stesso indirizzo di memoria potrebbe essere inserito.

## Soluzioni al Problema dell'ABA

Esistono diverse strategie per mitigare il problema dell'ABA:

### 1. Utilizzo di Contatori di Versione (Stamping)

Una soluzione comune è associare un contatore di versione al valore. Ogni volta che il valore viene modificato, il contatore viene incrementato. In questo modo, anche se il valore torna ad essere A, il contatore sarà diverso, facendo fallire l'operazione CAS.

Java fornisce la classe `AtomicStampedReference` per implementare questa strategia:

```java
import java.util.concurrent.atomic.AtomicStampedReference;

public class StampedReferenceExample {
    public static void main(String[] args) {
        // Inizializzazione con valore "A" e stamp 0
        AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);
        
        // Ottiene il valore e lo stamp correnti
        int[] stampHolder = new int[1];
        String value = ref.get(stampHolder);
        int stamp = stampHolder[0];
        
        System.out.println("Valore iniziale: " + value + ", Stamp: " + stamp);
        
        // Simula il problema ABA in un altro thread
        // (in un'applicazione reale, questo sarebbe in un thread separato)
        ref.compareAndSet("A", "B", stamp, stamp + 1); // A -> B
        ref.compareAndSet("B", "A", stamp + 1, stamp + 2); // B -> A
        
        System.out.println("Dopo ABA: " + ref.getReference() + ", Stamp: " + ref.getStamp());
        
        // Tentativo di CAS che fallirà a causa dello stamp diverso
        boolean success = ref.compareAndSet("A", "C", stamp, stamp + 1);
        System.out.println("CAS riuscito? " + success);
        System.out.println("Valore finale: " + ref.getReference() + ", Stamp: " + ref.getStamp());
    }
}
```

Output:
```
Valore iniziale: A, Stamp: 0
Dopo ABA: A, Stamp: 2
CAS riuscito? false
Valore finale: A, Stamp: 2
```

### 2. Utilizzo di Marcatori (Marking)

Un'altra soluzione è utilizzare un bit di marcatura per indicare se un oggetto è stato logicamente rimosso. Java fornisce la classe `AtomicMarkableReference` per questa strategia:

```java
import java.util.concurrent.atomic.AtomicMarkableReference;

public class MarkableReferenceExample {
    public static void main(String[] args) {
        // Inizializzazione con valore "A" e mark false
        AtomicMarkableReference<String> ref = new AtomicMarkableReference<>("A", false);
        
        // Ottiene il valore e il mark correnti
        boolean[] markHolder = new boolean[1];
        String value = ref.get(markHolder);
        boolean mark = markHolder[0];
        
        System.out.println("Valore iniziale: " + value + ", Mark: " + mark);
        
        // Marca l'oggetto come logicamente rimosso
        ref.compareAndSet("A", "A", false, true);
        
        System.out.println("Dopo marcatura: " + ref.getReference() + ", Mark: " + ref.isMarked());
        
        // Tentativo di CAS che fallirà a causa del mark diverso
        boolean success = ref.compareAndSet("A", "B", false, false);
        System.out.println("CAS riuscito? " + success);
        System.out.println("Valore finale: " + ref.getReference() + ", Mark: " + ref.isMarked());
    }
}
```

### 3. Garbage Collection e Memory Management

Alcune soluzioni al problema dell'ABA si basano su tecniche di gestione della memoria che garantiscono che un oggetto non possa essere riutilizzato finché nessun thread sta ancora facendo riferimento ad esso. Queste tecniche includono:

- **Hazard Pointers**: Un meccanismo che permette ai thread di dichiarare quali puntatori stanno attualmente utilizzando.
- **Epoch-Based Reclamation**: Un approccio che ritarda il riutilizzo della memoria fino a quando tutti i thread hanno completato le operazioni in corso.
- **Reference Counting**: Tenere traccia del numero di riferimenti a un oggetto e liberarlo solo quando il contatore raggiunge zero.

Queste tecniche sono più complesse da implementare ma possono essere più efficienti in scenari ad alta concorrenza.

## Conclusioni

L'algoritmo Compare-and-Swap è un elemento fondamentale per l'implementazione di strutture dati e algoritmi concorrenti senza lock. Comprendere il suo funzionamento e le sue limitazioni, come il problema dell'ABA, è essenziale per sviluppare applicazioni concorrenti corrette ed efficienti.

Nel prossimo capitolo, esploreremo le classi Atomic avanzate di Java, che forniscono funzionalità aggiuntive per scenari di concorrenza più complessi.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Precedente: [Classi Atomic di Base](./02-ClassiAtomicBase.md)
- Successivo: [Classi Atomic Avanzate](./04-ClassiAtomicAvanzate.md)