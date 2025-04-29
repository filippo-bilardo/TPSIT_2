# Introduzione alle Collezioni Concorrenti

## Problematiche delle Collezioni Standard

Le collezioni standard di Java, come quelle fornite dal package `java.util` (ArrayList, HashMap, HashSet, ecc.), non sono progettate per essere utilizzate in ambienti multi-thread senza sincronizzazione esterna. Quando più thread accedono e modificano contemporaneamente una collezione standard, possono verificarsi diversi problemi:

1. **Race Condition**: Quando il risultato dipende dall'ordine di esecuzione dei thread
2. **Inconsistenza dei Dati**: La collezione può trovarsi in uno stato intermedio non valido
3. **ConcurrentModificationException**: Lanciata quando un thread modifica una collezione mentre un altro la sta iterando
4. **Perdita di Aggiornamenti**: Le modifiche di un thread possono sovrascrivere quelle di un altro

## Collezioni Sincronizzate Tradizionali

Java offre metodi di utilità nella classe `Collections` per ottenere versioni sincronizzate delle collezioni standard:

```java
List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());
Map<String, Integer> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
Set<Integer> synchronizedSet = Collections.synchronizedSet(new HashSet<>());
```

Queste collezioni sincronizzate proteggono i dati utilizzando la sincronizzazione intrinseca (monitor lock) su ogni operazione. Tuttavia, presentano alcuni svantaggi significativi:

1. **Sincronizzazione Coarse-Grained**: Ogni operazione blocca l'intera collezione, limitando la concorrenza
2. **Iterazione Non Sicura**: È necessario sincronizzare manualmente durante l'iterazione
3. **Prestazioni Limitate**: In scenari di alta concorrenza, diventano un collo di bottiglia

Esempio di iterazione sicura su una lista sincronizzata:

```java
List<String> list = Collections.synchronizedList(new ArrayList<>());
// ...
// L'iterazione deve essere protetta manualmente
synchronized (list) {
    Iterator<String> i = list.iterator();
    while (i.hasNext()) {
        foo(i.next());
    }
}
```

## Concorrenza Fine-Grained vs Coarse-Grained

La sincronizzazione può essere classificata in base alla sua granularità:

1. **Coarse-Grained (Grossolana)**: Blocca grandi porzioni di dati o intere strutture
   - Più semplice da implementare
   - Meno overhead di gestione dei lock
   - Limita significativamente la concorrenza

2. **Fine-Grained (Fine)**: Blocca solo piccole porzioni di dati
   - Permette maggiore concorrenza
   - Più complessa da implementare
   - Maggiore overhead per la gestione di molti lock
   - Rischio di deadlock più elevato

Le collezioni concorrenti moderne utilizzano tecniche di sincronizzazione fine-grained per migliorare le prestazioni in scenari di alta concorrenza.

## Lock-Free e Wait-Free Algorithms

Le collezioni concorrenti più avanzate utilizzano algoritmi che riducono o eliminano la necessità di lock:

1. **Lock-Free**: Garantiscono che almeno un thread possa fare progressi
   - Utilizzano operazioni atomiche come Compare-and-Swap (CAS)
   - Resistenti ai problemi di priorità dei thread e preemption
   - Non soffrono di deadlock o livelock

2. **Wait-Free**: Garantiscono che tutti i thread possano fare progressi in un numero finito di passi
   - Offrono le migliori garanzie di progresso
   - Estremamente difficili da implementare
   - Spesso hanno overhead maggiore per operazioni semplici

Questi algoritmi sono alla base di molte collezioni concorrenti in Java, come `ConcurrentHashMap` e `ConcurrentLinkedQueue`.

## Il Package java.util.concurrent

Introdotto in Java 5, il package `java.util.concurrent` fornisce implementazioni di collezioni ottimizzate per l'uso in ambienti multi-thread. Queste collezioni sono progettate per offrire alta concorrenza mantenendo la coerenza dei dati.

Le principali categorie di collezioni concorrenti sono:

1. **Mappe Concorrenti**: `ConcurrentHashMap`, `ConcurrentSkipListMap`
2. **Liste Concorrenti**: `CopyOnWriteArrayList`
3. **Set Concorrenti**: `CopyOnWriteArraySet`, `ConcurrentSkipListSet`
4. **Code Concorrenti**: `ConcurrentLinkedQueue`, `ConcurrentLinkedDeque`
5. **Code Bloccanti**: `ArrayBlockingQueue`, `LinkedBlockingQueue`, `PriorityBlockingQueue`

## Quando Usare le Collezioni Concorrenti

Le collezioni concorrenti sono particolarmente utili nei seguenti scenari:

1. **Alta Concorrenza**: Quando molti thread accedono contemporaneamente alla stessa collezione
2. **Operazioni di Lunga Durata**: Quando le operazioni sulla collezione possono richiedere tempo
3. **Bilanciamento Lettura/Scrittura**: Quando il pattern di accesso è sbilanciato (es. molte letture, poche scritture)
4. **Necessità di Scalabilità**: Quando l'applicazione deve scalare su sistemi multi-core

Tuttavia, non sono sempre la scelta migliore:

1. **Overhead**: Hanno un overhead maggiore per operazioni semplici in scenari a bassa concorrenza
2. **Complessità**: Alcune hanno semantiche diverse dalle collezioni standard
3. **Memoria**: Alcune implementazioni richiedono più memoria delle controparti non concorrenti

## Esempio di Base: ConcurrentHashMap

Ecco un esempio semplice che mostra l'uso di `ConcurrentHashMap` rispetto a `HashMap` in un ambiente multi-thread:

```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ConcurrentMapExample {
    // Questo causerà probabilmente problemi di concorrenza
    private static final Map<String, Integer> unsafeMap = new HashMap<>();
    
    // Questa è thread-safe
    private static final Map<String, Integer> safeMap = new ConcurrentHashMap<>();
    
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Sottometti 1000 incrementi per ogni mappa
        for (int i = 0; i < 1000; i++) {
            executor.submit(() -> {
                // Operazione non sicura su HashMap
                incrementKey(unsafeMap, "counter");
                
                // Operazione sicura su ConcurrentHashMap
                incrementKey(safeMap, "counter");
            });
        }
        
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        
        System.out.println("HashMap (non sicura): " + unsafeMap.get("counter"));
        System.out.println("ConcurrentHashMap: " + safeMap.get("counter"));
    }
    
    private static void incrementKey(Map<String, Integer> map, String key) {
        // Questa operazione non è atomica senza sincronizzazione esterna
        Integer value = map.getOrDefault(key, 0);
        map.put(key, value + 1);
    }
}
```

Eseguendo questo codice, probabilmente vedrai che il valore finale in `unsafeMap` è inferiore a 1000 (a causa delle race condition), mentre `safeMap` conterrà il valore corretto 1000.

## Conclusione

Le collezioni concorrenti rappresentano un'evoluzione significativa rispetto alle collezioni sincronizzate tradizionali, offrendo migliori prestazioni e scalabilità in ambienti multi-thread. Comprendere le loro caratteristiche e i casi d'uso appropriati è fondamentale per sviluppare applicazioni Java concorrenti efficienti.

Nei prossimi capitoli, esploreremo in dettaglio le principali collezioni concorrenti, iniziando con `ConcurrentHashMap`, una delle implementazioni più utilizzate e potenti.

## Navigazione

- [Indice del Modulo](./README.md)
- Successivo: [ConcurrentHashMap](./02-ConcurrentHashMap.md)