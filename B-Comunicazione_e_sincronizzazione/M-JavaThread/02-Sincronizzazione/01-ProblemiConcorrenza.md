# Problemi di Concorrenza

## Introduzione

La programmazione multi-thread offre numerosi vantaggi in termini di prestazioni e reattività delle applicazioni. Tuttavia, quando più thread operano contemporaneamente sugli stessi dati, possono verificarsi vari problemi di concorrenza che compromettono la correttezza del programma. In questo capitolo, esploreremo i principali problemi che si verificano in ambiente multi-thread e perché è necessaria la sincronizzazione.

## Race Condition

Una **race condition** (condizione di gara) si verifica quando il comportamento di un programma dipende dalla sequenza o dalla tempistica di eventi non controllabili, come l'ordine di esecuzione dei thread.

### Esempio di Race Condition

Consideriamo un semplice contatore condiviso tra due thread:

```java
public class ContatoreNonSicuro {
    private int valore = 0;
    
    public void incrementa() {
        valore++; // Operazione non atomica!
    }
    
    public int getValore() {
        return valore;
    }
    
    public static void main(String[] args) throws InterruptedException {
        final ContatoreNonSicuro contatore = new ContatoreNonSicuro();
        
        // Thread 1: incrementa il contatore 1000 volte
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                contatore.incrementa();
            }
        });
        
        // Thread 2: incrementa il contatore 1000 volte
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                contatore.incrementa();
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Valore finale: " + contatore.getValore());
        // Ci aspetteremmo 2000, ma spesso otteniamo un valore inferiore
    }
}
```

Eseguendo questo codice, ci aspetteremmo che il valore finale del contatore sia 2000 (1000 incrementi per ciascuno dei due thread). Tuttavia, spesso otterremo un valore inferiore a causa della race condition.

### Perché Accade?

L'operazione `valore++` sembra atomica, ma in realtà è composta da tre operazioni distinte a livello di bytecode:

1. Leggere il valore corrente della variabile
2. Incrementare il valore
3. Scrivere il nuovo valore nella variabile

Se due thread eseguono queste operazioni in modo interlacciato, possono verificarsi perdite di aggiornamenti. Ad esempio:

1. Thread 1 legge il valore (ad es. 10)
2. Thread 2 legge lo stesso valore (10)
3. Thread 1 incrementa a 11 e scrive
4. Thread 2 incrementa a 11 (anziché 12) e scrive

In questo scenario, uno degli incrementi viene effettivamente perso.

## Inconsistenza dei Dati

L'inconsistenza dei dati si verifica quando i dati in memoria non riflettono lo stato corretto dell'applicazione a causa di operazioni concorrenti non sincronizzate.

### Esempio di Inconsistenza

```java
public class ContoCorrente {
    private double saldo = 1000.0;
    
    public void preleva(double importo) {
        if (saldo >= importo) {
            // Simula un ritardo nell'operazione
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            
            saldo -= importo;
        }
    }
    
    public double getSaldo() {
        return saldo;
    }
    
    public static void main(String[] args) throws InterruptedException {
        ContoCorrente conto = new ContoCorrente();
        
        // Due thread prelevano contemporaneamente
        Thread t1 = new Thread(() -> conto.preleva(800));
        Thread t2 = new Thread(() -> conto.preleva(800));
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Saldo finale: " + conto.getSaldo());
        // Ci aspetteremmo 200 o un errore, ma potremmo ottenere -600
    }
}
```

In questo esempio, entrambi i thread verificano che ci siano fondi sufficienti (1000 >= 800), poi entrambi procedono con il prelievo, portando il saldo a -600, un valore inconsistente rispetto alla logica dell'applicazione.

## Visibilità delle Variabili

Un altro problema comune nella programmazione multi-thread è la **visibilità delle variabili**. In Java, per motivi di ottimizzazione, un thread può memorizzare nella cache locale le variabili che utilizza frequentemente, invece di leggerle ogni volta dalla memoria principale.

Questo può portare a situazioni in cui un thread non vede le modifiche apportate da un altro thread.

### Esempio di Problema di Visibilità

```java
public class ProblemaVisibilita {
    private boolean flag = false;
    
    public void setFlag() {
        flag = true; // Potrebbe rimanere nella cache del thread
    }
    
    public void checkFlag() {
        while (!flag) {
            // Attesa attiva finché flag non diventa true
            // Potrebbe non terminare mai se il thread non vede
            // l'aggiornamento di flag
        }
        System.out.println("Flag rilevato!");
    }
    
    public static void main(String[] args) {
        ProblemaVisibilita esempio = new ProblemaVisibilita();
        
        Thread t1 = new Thread(() -> esempio.checkFlag());
        t1.start();
        
        // Breve pausa
        try { Thread.sleep(100); } catch (InterruptedException e) { }
        
        Thread t2 = new Thread(() -> esempio.setFlag());
        t2.start();
    }
}
```

In questo esempio, il primo thread potrebbe non vedere mai il cambiamento della variabile `flag` effettuato dal secondo thread, rimanendo bloccato in un ciclo infinito.

## Atomicità delle Operazioni

In Java, solo alcune operazioni sono garantite essere atomiche:

- Letture e scritture di variabili di tipo primitivo (eccetto `long` e `double`)
- Letture e scritture di riferimenti a oggetti
- Letture e scritture di variabili dichiarate `volatile`

Tutte le altre operazioni, incluse quelle che sembrano semplici come l'incremento (`i++`), non sono atomiche e possono causare race condition.

## Riordinamento delle Istruzioni

Per ottimizzare le prestazioni, sia il compilatore Java che la JVM possono riordinare le istruzioni, a condizione che il comportamento del programma non cambi dal punto di vista di un singolo thread. Tuttavia, questo riordinamento può causare problemi in un contesto multi-thread.

## Soluzioni ai Problemi di Concorrenza

Java offre diversi meccanismi per risolvere i problemi di concorrenza:

1. **Keyword `synchronized`**: Garantisce che solo un thread alla volta possa eseguire un blocco di codice o un metodo.

2. **Variabili `volatile`**: Garantisce che le letture e le scritture avvengano sempre nella memoria principale, risolvendo i problemi di visibilità.

3. **Classi atomiche**: Il package `java.util.concurrent.atomic` fornisce classi che supportano operazioni atomiche senza bisogno di sincronizzazione esplicita.

4. **Locks espliciti**: Il package `java.util.concurrent.locks` offre implementazioni più flessibili dei meccanismi di lock rispetto alla keyword `synchronized`.

5. **Collezioni concorrenti**: Il package `java.util.concurrent` fornisce implementazioni thread-safe delle principali strutture dati.

## Esempio di Soluzione con synchronized

```java
public class ContatoreSicuro {
    private int valore = 0;
    
    public synchronized void incrementa() {
        valore++;
    }
    
    public synchronized int getValore() {
        return valore;
    }
    
    public static void main(String[] args) throws InterruptedException {
        final ContatoreSicuro contatore = new ContatoreSicuro();
        
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                contatore.incrementa();
            }
        });
        
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                contatore.incrementa();
            }
        });
        
        t1.start();
        t2.start();
        
        t1.join();
        t2.join();
        
        System.out.println("Valore finale: " + contatore.getValore());
        // Ora otterremo sempre 2000
    }
}
```

In questo esempio, l'uso della keyword `synchronized` garantisce che solo un thread alla volta possa eseguire il metodo `incrementa()`, risolvendo la race condition.

## Conclusione

I problemi di concorrenza sono tra le sfide più complesse nella programmazione multi-thread. Comprendere questi problemi è il primo passo per scrivere codice thread-safe. Nei prossimi capitoli, esploreremo in dettaglio i meccanismi di sincronizzazione offerti da Java per affrontare questi problemi.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Sincronizzazione](./README.md)
- [➡️ Keyword synchronized](./02-Synchronized.md)