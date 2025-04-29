# Metodi di Comunicazione tra Thread

## Introduzione

Nella programmazione multi-thread, la comunicazione tra thread è essenziale per coordinare le attività e condividere dati in modo sicuro. Java fornisce un meccanismo integrato per la comunicazione tra thread attraverso i metodi `wait()`, `notify()` e `notifyAll()` della classe `Object`.

## Metodi `wait()`, `notify()` e `notifyAll()`

Questi metodi sono parte della classe `Object` e quindi disponibili per tutti gli oggetti Java. Sono progettati per essere utilizzati all'interno di blocchi sincronizzati.

### Il metodo `wait()`

```java
public final void wait() throws InterruptedException
public final void wait(long timeout) throws InterruptedException
public final void wait(long timeout, int nanos) throws InterruptedException
```

Quando un thread chiama il metodo `wait()` su un oggetto:

1. Rilascia il lock sull'oggetto
2. Entra nello stato di attesa (WAITING o TIMED_WAITING)
3. Rimane in attesa finché un altro thread non chiama `notify()` o `notifyAll()` sullo stesso oggetto, o fino allo scadere del timeout se specificato

### Il metodo `notify()`

```java
public final void notify()
```

Quando un thread chiama il metodo `notify()` su un oggetto:

1. Sveglia un singolo thread in attesa sull'oggetto (se ce ne sono)
2. La scelta di quale thread svegliare è arbitraria e dipende dall'implementazione della JVM
3. Il thread risvegliato non ottiene immediatamente il lock, ma compete per acquisirlo

### Il metodo `notifyAll()`

```java
public final void notifyAll()
```

Quando un thread chiama il metodo `notifyAll()` su un oggetto:

1. Sveglia tutti i thread in attesa sull'oggetto
2. I thread risvegliati competono per acquisire il lock sull'oggetto

## Meccanismo di Attesa e Notifica

Il meccanismo di attesa e notifica in Java segue un pattern specifico:

```java
synchronized(oggetto) {
    while(!condizione) {
        oggetto.wait();
    }
    // Esegui operazioni quando la condizione è vera
}
```

```java
synchronized(oggetto) {
    // Modifica lo stato che influenza la condizione
    oggetto.notify(); // o oggetto.notifyAll();
}
```

Punti importanti da considerare:

1. I metodi `wait()`, `notify()` e `notifyAll()` devono essere chiamati all'interno di un blocco sincronizzato sull'oggetto su cui vengono invocati, altrimenti viene lanciata un'eccezione `IllegalMonitorStateException`.

2. È consigliabile utilizzare un ciclo `while` invece di un `if` per verificare la condizione di attesa, per proteggersi da risvegli spuri (quando un thread viene risvegliato senza che sia stata chiamata `notify()`).

3. Il metodo `notifyAll()` è generalmente più sicuro di `notify()` perché garantisce che tutti i thread in attesa abbiano l'opportunità di verificare la loro condizione.

## Condizioni di Attesa

Le condizioni di attesa sono predicati booleani che determinano se un thread deve attendere o può procedere. Esempi comuni includono:

- Attendere che una risorsa diventi disponibile
- Attendere che un buffer si riempia o si svuoti
- Attendere che un certo stato venga raggiunto

Esempio di un buffer condiviso:

```java
public class Buffer {
    private List<Object> data = new ArrayList<>();
    private int maxSize = 10;
    
    public synchronized void put(Object item) throws InterruptedException {
        while(data.size() == maxSize) {
            // Buffer pieno, attendere
            wait();
        }
        data.add(item);
        // Notificare che il buffer non è più vuoto
        notifyAll();
    }
    
    public synchronized Object get() throws InterruptedException {
        while(data.isEmpty()) {
            // Buffer vuoto, attendere
            wait();
        }
        Object item = data.remove(0);
        // Notificare che il buffer non è più pieno
        notifyAll();
        return item;
    }
}
```

## Gestione delle Interruzioni

I metodi `wait()` lanciano l'eccezione `InterruptedException` se il thread viene interrotto mentre è in attesa. È importante gestire questa eccezione correttamente:

```java
try {
    synchronized(oggetto) {
        while(!condizione) {
            oggetto.wait();
        }
        // Operazioni da eseguire
    }
} catch(InterruptedException e) {
    // Gestione dell'interruzione
    Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
}
```

La pratica comune è ripristinare lo stato di interruzione chiamando `Thread.currentThread().interrupt()` per permettere ai metodi chiamanti di rilevare l'interruzione.

## Problemi Comuni

### Deadlock

Il deadlock può verificarsi quando due o più thread attendono reciprocamente che l'altro rilasci un lock. Per evitare deadlock quando si utilizzano `wait()` e `notify()`:

- Evitare di acquisire più lock in ordine diverso
- Utilizzare timeout nei metodi `wait()`
- Implementare meccanismi di rilevamento e recupero da deadlock

### Livelock

Il livelock si verifica quando i thread rispondono continuamente alle azioni degli altri senza fare progressi. Può accadere quando i thread vengono risvegliati ma trovano che la loro condizione non è ancora soddisfatta e tornano immediatamente in attesa.

### Lost Notification

Se un thread chiama `notify()` quando nessun thread è in attesa, la notifica va persa. Se successivamente un thread chiama `wait()`, potrebbe rimanere bloccato indefinitamente. Per evitare questo problema, verificare sempre la condizione prima di chiamare `wait()`.

## Conclusione

I metodi `wait()`, `notify()` e `notifyAll()` forniscono un meccanismo potente per la comunicazione tra thread in Java. Utilizzati correttamente all'interno di blocchi sincronizzati e con appropriate condizioni di attesa, permettono di implementare pattern di comunicazione complessi come produttore-consumatore, lettori-scrittori e altri.

Nei prossimi capitoli, esploreremo pattern specifici di comunicazione tra thread e tecniche avanzate per lo scambio sicuro di dati tra thread.