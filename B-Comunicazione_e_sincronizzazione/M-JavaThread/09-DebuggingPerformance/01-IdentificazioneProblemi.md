# Identificazione di Problemi di Concorrenza

La programmazione concorrente introduce una serie di sfide uniche che possono portare a comportamenti imprevisti e difficili da diagnosticare. In questa sezione, esploreremo i tipi più comuni di problemi di concorrenza, come riconoscerli e quali strumenti utilizzare per diagnosticarli.

## Tipi comuni di bug nella programmazione concorrente

### 1. Race Condition

Una race condition si verifica quando il comportamento di un programma dipende dalla sequenza o dalla tempistica di eventi non controllabili, come l'ordine di esecuzione dei thread.

```java
public class RaceConditionExample {
    private static int counter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter++; // Operazione non atomica
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter++; // Operazione non atomica
            }
        });
        
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        
        System.out.println("Valore finale: " + counter); // Probabilmente non sarà 2000
    }
}
```

### 2. Deadlock

Un deadlock si verifica quando due o più thread si bloccano a vicenda, ciascuno in attesa che l'altro rilasci una risorsa.

### 3. Livelock

Un livelock è simile a un deadlock, ma i thread coinvolti cambiano continuamente il loro stato in risposta all'altro thread, senza fare progressi effettivi.

### 4. Starvation

La starvation si verifica quando un thread non può accedere alle risorse necessarie per procedere, spesso perché altri thread monopolizzano tali risorse.

### 5. Problemi di visibilità

I problemi di visibilità si verificano quando le modifiche apportate a una variabile da un thread non sono visibili ad altri thread.

## Sintomi e cause

### Sintomi comuni

1. **Risultati inconsistenti**: Lo stesso programma produce risultati diversi in esecuzioni successive.
2. **Prestazioni degradate**: L'applicazione diventa improvvisamente lenta o non risponde.
3. **Blocchi imprevisti**: L'applicazione si blocca senza errori evidenti.
4. **Errori intermittenti**: Errori che si verificano solo occasionalmente e sono difficili da riprodurre.

### Cause principali

1. **Sincronizzazione inadeguata**: Mancanza di meccanismi di sincronizzazione appropriati.
2. **Granularità eccessiva del lock**: Bloccare troppe risorse contemporaneamente.
3. **Ordine di acquisizione dei lock inconsistente**: Acquisire lock in ordini diversi in parti diverse del codice.
4. **Uso improprio di variabili condivise**: Accesso non sincronizzato a variabili condivise.

## Strumenti di diagnostica in Java

Java offre diversi strumenti per diagnosticare problemi di concorrenza:

### 1. jstack

`jstack` è uno strumento a riga di comando che genera un dump dei thread di una JVM in esecuzione, utile per identificare deadlock e stati dei thread.

```bash
jstack <pid>
```

### 2. VisualVM

VisualVM è uno strumento visuale che fornisce informazioni dettagliate sui thread, inclusi grafici di utilizzo e analisi dei deadlock.

### 3. Java Flight Recorder (JFR)

JFR raccoglie dati diagnostici e di profilazione con un impatto minimo sulle prestazioni dell'applicazione.

### 4. ThreadMXBean

L'API `java.lang.management.ThreadMXBean` consente di rilevare deadlock e ottenere informazioni sui thread programmaticamente.

```java
ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
long[] deadlockedThreads = threadMXBean.findDeadlockedThreads();
if (deadlockedThreads != null) {
    ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(deadlockedThreads);
    for (ThreadInfo threadInfo : threadInfos) {
        System.out.println(threadInfo.getThreadName() + " bloccato su " + threadInfo.getLockName());
    }
}
```

## Logging thread-safe

Un logging efficace è fondamentale per diagnosticare problemi di concorrenza. Ecco alcune best practice:

### 1. Utilizzare framework di logging thread-safe

Framework come Log4j, Logback e java.util.logging sono progettati per essere thread-safe.

```java
private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

public void metodoMultiThread() {
    logger.debug("Thread {} sta eseguendo l'operazione", Thread.currentThread().getName());
    // Operazioni thread-safe
}
```

### 2. Includere informazioni sul thread nei log

```java
logger.debug("[Thread: {}] Stato corrente: {}", Thread.currentThread().getName(), statoCorrente);
```

### 3. Utilizzare MDC (Mapped Diagnostic Context)

MDC permette di associare informazioni contestuali ai log, utile per tracciare l'esecuzione di thread specifici.

```java
MDC.put("threadId", Thread.currentThread().getId());
MDC.put("operazione", "elaborazioneDati");
try {
    logger.info("Inizio elaborazione");
    // Operazioni
    logger.info("Fine elaborazione");
} finally {
    MDC.clear(); // Importante pulire l'MDC alla fine
}
```

### 4. Evitare operazioni di logging bloccanti

Configurare il logger per utilizzare code asincrone per evitare che le operazioni di logging rallentino l'applicazione.

## Strategie preventive

1. **Code review focalizzate sulla concorrenza**: Revisioni del codice specificamente mirate a identificare potenziali problemi di concorrenza.
2. **Test di stress**: Eseguire test con carichi elevati per far emergere problemi di concorrenza.
3. **Analisi statica del codice**: Utilizzare strumenti come FindBugs, PMD o SonarQube con regole specifiche per la concorrenza.
4. **Documentazione chiara**: Documentare le assunzioni sulla concorrenza e i requisiti di sincronizzazione.

## Navigazione

- [Indice del Corso](../README.md)
- [Indice del Modulo](./README.md)
- Successivo: [Deadlock e Starvation](./02-DeadlockStarvation.md)