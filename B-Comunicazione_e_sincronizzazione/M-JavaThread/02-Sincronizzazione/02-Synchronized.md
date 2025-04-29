# Keyword synchronized

## Introduzione

In Java, la keyword `synchronized` è uno dei meccanismi fondamentali per gestire la concorrenza e prevenire race condition. Questo meccanismo si basa sul concetto di "monitor" o "intrinsic lock" associato a ogni oggetto Java. In questo capitolo, esploreremo in dettaglio come funziona la sincronizzazione in Java e come utilizzarla correttamente.

## Concetto di Monitor in Java

In Java, ogni oggetto ha un "monitor" associato, che può essere visto come un meccanismo di controllo dell'accesso. Un thread può acquisire il monitor di un oggetto, impedendo ad altri thread di acquisirlo contemporaneamente. Questo meccanismo è alla base della sincronizzazione in Java.

Quando un thread acquisisce il monitor di un oggetto:
1. Nessun altro thread può acquisire lo stesso monitor finché non viene rilasciato
2. Il thread ha accesso esclusivo a tutte le sezioni di codice sincronizzate sullo stesso oggetto

## Metodi Sincronizzati

Un metodo può essere dichiarato come `synchronized`, il che significa che un thread deve acquisire il monitor dell'oggetto (o della classe, per metodi statici) prima di poter eseguire il metodo.

### Sintassi

```java
public synchronized void metodoSincronizzato() {
    // Codice thread-safe
}
```

Per i metodi statici, la sincronizzazione avviene sull'oggetto Class:

```java
public static synchronized void metodoStatico() {
    // Codice thread-safe
}
```

### Esempio di Metodo Sincronizzato

```java
public class ContoBancario {
    private double saldo;
    
    public ContoBancario(double saldoIniziale) {
        this.saldo = saldoIniziale;
    }
    
    public synchronized void deposita(double importo) {
        if (importo > 0) {
            double nuovoSaldo = saldo + importo;
            // Simula un'operazione che richiede tempo
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            saldo = nuovoSaldo;
        }
    }
    
    public synchronized boolean preleva(double importo) {
        if (importo > 0 && saldo >= importo) {
            double nuovoSaldo = saldo - importo;
            // Simula un'operazione che richiede tempo
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            saldo = nuovoSaldo;
            return true;
        }
        return false;
    }
    
    public synchronized double getSaldo() {
        return saldo;
    }
}
```

In questo esempio, i metodi `deposita()`, `preleva()` e `getSaldo()` sono sincronizzati, garantendo che solo un thread alla volta possa modificare o leggere il saldo del conto.

## Blocchi Sincronizzati

Oltre ai metodi sincronizzati, Java permette di sincronizzare solo blocchi specifici di codice. Questo approccio è più flessibile e può migliorare le prestazioni limitando la sincronizzazione solo alle parti di codice che effettivamente necessitano di protezione.

### Sintassi

```java
synchronized (oggetto) {
    // Codice thread-safe
}
```

Dove `oggetto` è l'oggetto il cui monitor verrà acquisito.

### Esempio di Blocco Sincronizzato

```java
public class ContoBancarioOttimizzato {
    private double saldo;
    private final Object lockSaldo = new Object(); // Oggetto dedicato per la sincronizzazione
    
    public ContoBancarioOttimizzato(double saldoIniziale) {
        this.saldo = saldoIniziale;
    }
    
    public void deposita(double importo) {
        if (importo <= 0) return;
        
        // Codice non sincronizzato (validazione, logging, ecc.)
        System.out.println("Tentativo di deposito: " + importo);
        
        synchronized (lockSaldo) {
            // Solo questa parte è sincronizzata
            double nuovoSaldo = saldo + importo;
            try { Thread.sleep(100); } catch (InterruptedException e) { }
            saldo = nuovoSaldo;
        }
        
        // Altro codice non sincronizzato
        System.out.println("Deposito completato");
    }
    
    public boolean preleva(double importo) {
        if (importo <= 0) return false;
        
        boolean prelievoRiuscito = false;
        
        synchronized (lockSaldo) {
            if (saldo >= importo) {
                double nuovoSaldo = saldo - importo;
                try { Thread.sleep(100); } catch (InterruptedException e) { }
                saldo = nuovoSaldo;
                prelievoRiuscito = true;
            }
        }
        
        return prelievoRiuscito;
    }
    
    public double getSaldo() {
        synchronized (lockSaldo) {
            return saldo;
        }
    }
}
```

In questo esempio, utilizziamo un oggetto dedicato `lockSaldo` per la sincronizzazione e sincronizziamo solo i blocchi di codice che accedono effettivamente alla variabile `saldo`.

## Sincronizzazione su Oggetti vs Classi

In Java, è possibile sincronizzare sia su oggetti che su classi:

### Sincronizzazione su Oggetti

```java
public void metodo() {
    synchronized (this) {
        // Sincronizzato sull'istanza corrente
    }
}

// Equivalente a:
public synchronized void metodo() {
    // Sincronizzato sull'istanza corrente
}
```

### Sincronizzazione su Classi

```java
public void metodo() {
    synchronized (MiaClasse.class) {
        // Sincronizzato sulla classe
    }
}

// Equivalente a:
public static synchronized void metodoStatico() {
    // Sincronizzato sulla classe
}
```

È importante notare che la sincronizzazione su un oggetto e la sincronizzazione sulla classe sono indipendenti: un thread può acquisire il lock di un'istanza mentre un altro thread acquisisce il lock della classe.

## Granularità del Lock

La granularità del lock si riferisce alla quantità di dati protetti da un singolo lock. Possiamo distinguere tra:

### Lock a Grana Grossa (Coarse-grained)

Un singolo lock protegge una grande quantità di dati o operazioni. Ad esempio, sincronizzare un intero metodo.

**Vantaggi**:
- Semplice da implementare
- Meno rischio di deadlock

**Svantaggi**:
- Minore concorrenza
- Potenziali colli di bottiglia

### Lock a Grana Fine (Fine-grained)

Utilizzo di lock multipli, ciascuno che protegge una piccola quantità di dati. Ad esempio, sincronizzare solo specifiche operazioni critiche.

**Vantaggi**:
- Maggiore concorrenza
- Migliori prestazioni in scenari ad alto carico

**Svantaggi**:
- Più complesso da implementare correttamente
- Maggiore rischio di deadlock

### Esempio di Lock a Grana Fine

```java
public class Inventario {
    private final Map<String, Integer> prodotti = new HashMap<>();
    private final Object lockLettura = new Object();
    private final Object lockScrittura = new Object();
    
    public void aggiungiProdotto(String nome, int quantita) {
        synchronized (lockScrittura) {
            prodotti.put(nome, prodotti.getOrDefault(nome, 0) + quantita);
        }
    }
    
    public boolean rimuoviProdotto(String nome, int quantita) {
        synchronized (lockScrittura) {
            int disponibile = prodotti.getOrDefault(nome, 0);
            if (disponibile >= quantita) {
                prodotti.put(nome, disponibile - quantita);
                return true;
            }
            return false;
        }
    }
    
    public int getQuantita(String nome) {
        synchronized (lockLettura) {
            return prodotti.getOrDefault(nome, 0);
        }
    }
    
    public List<String> getProdottiDisponibili() {
        synchronized (lockLettura) {
            return prodotti.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        }
    }
}
```

In questo esempio, utilizziamo due lock diversi: uno per le operazioni di lettura e uno per le operazioni di scrittura. Questo permette a più thread di leggere contemporaneamente, aumentando la concorrenza.

## Considerazioni sulle Prestazioni

La sincronizzazione ha un costo in termini di prestazioni. Ecco alcune considerazioni importanti:

1. **Minimizzare la sezione critica**: Sincronizzare solo il codice che effettivamente necessita di protezione.

2. **Evitare operazioni bloccanti in sezioni sincronizzate**: Operazioni come I/O o chiamate di rete all'interno di blocchi sincronizzati possono ridurre significativamente la concorrenza.

3. **Considerare alternative**: In alcuni casi, classi come `AtomicInteger` o `ConcurrentHashMap` possono offrire migliori prestazioni rispetto alla sincronizzazione esplicita.

4. **Bilanciare granularità e complessità**: Lock a grana fine offrono maggiore concorrenza ma aumentano la complessità e il rischio di deadlock.

## Limitazioni della Keyword synchronized

Nonostante la sua utilità, `synchronized` ha alcune limitazioni:

1. **Non è possibile interrompere un thread in attesa di un lock**: Un thread che attende di acquisire un monitor non può essere interrotto.

2. **Non supporta timeout**: Non è possibile specificare per quanto tempo un thread dovrebbe attendere prima di rinunciare ad acquisire un lock.

3. **Non supporta acquisizione condizionale**: Non è possibile tentare di acquisire un lock senza bloccarsi.

4. **Non supporta lock multipli atomicamente**: Per acquisire più lock, è necessario nidificare i blocchi sincronizzati, aumentando il rischio di deadlock.

Per superare queste limitazioni, Java offre il framework `java.util.concurrent.locks` che vedremo in moduli successivi.

## Best Practices

1. **Mantenere le sezioni sincronizzate brevi**: Minimizzare il tempo in cui un thread detiene un lock.

2. **Evitare di chiamare metodi esterni in blocchi sincronizzati**: Questi metodi potrebbero essere bloccanti o causare deadlock.

3. **Essere consistenti con gli oggetti di sincronizzazione**: Se più metodi proteggono la stessa risorsa, dovrebbero sincronizzarsi sullo stesso oggetto.

4. **Documentare la strategia di sincronizzazione**: Rendere chiaro quali risorse sono protette da quali lock.

5. **Preferire oggetti privati e finali per la sincronizzazione**: Questo previene interferenze esterne con il meccanismo di sincronizzazione.

```java
private final Object lock = new Object(); // Buona pratica
```

6. **Evitare di sincronizzare su oggetti immutabili o String literals**: A causa dell'interning delle stringhe, sincronizzarsi su stringhe letterali può causare lock condivisi non intenzionali.

```java
// Da evitare
synchronized ("lock") { ... }

// Anche da evitare
synchronized (Boolean.TRUE) { ... }
```

## Esempi Pratici

### Esempio 1: Singleton Thread-Safe

```java
public class Singleton {
    private static Singleton istanza;
    
    private Singleton() {}
    
    public static synchronized Singleton getIstanza() {
        if (istanza == null) {
            istanza = new Singleton();
        }
        return istanza;
    }
}
```

### Esempio 2: Double-Checked Locking

```java
public class SingletonOttimizzato {
    private static volatile SingletonOttimizzato istanza;
    
    private SingletonOttimizzato() {}
    
    public static SingletonOttimizzato getIstanza() {
        if (istanza == null) { // Prima verifica (non sincronizzata)
            synchronized (SingletonOttimizzato.class) {
                if (istanza == null) { // Seconda verifica (sincronizzata)
                    istanza = new SingletonOttimizzato();
                }
            }
        }
        return istanza;
    }
}
```

In questo esempio, utilizziamo il pattern "Double-Checked Locking" per ridurre l'overhead della sincronizzazione. La keyword `volatile` è necessaria per garantire la visibilità corretta dell'istanza tra i thread.

## Conclusione

La keyword `synchronized` è uno strumento potente per gestire la concorrenza in Java. Comprendere come funziona e come utilizzarla correttamente è essenziale per scrivere applicazioni multi-thread robuste. Nel prossimo capitolo, esploreremo ulteriormente i lock e gli oggetti di sincronizzazione in Java.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Problemi di Concorrenza](./01-ProblemiConcorrenza.md)
- [➡️ Lock e Oggetti di Sincronizzazione](./03-LockOggetti.md)