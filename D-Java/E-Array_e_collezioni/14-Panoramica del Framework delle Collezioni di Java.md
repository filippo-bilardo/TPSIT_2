## 13 Panoramica del Framework delle Collezioni di Java

Il **Java Collections Framework (JCF)** è un insieme di interfacce e classi che forniscono strutture dati e algoritmi standard per la gestione di collezioni di oggetti. Introdotto a partire da Java 1.2, il framework offre un approccio omogeneo per lavorare con gruppi di dati, migliorando la produttività e la leggibilità del codice. Le principali collezioni incluse sono liste, insiemi e mappe, che consentono di organizzare e manipolare i dati in modo efficiente.

Il JCF è progettato per essere flessibile e scalabile, permettendo di scegliere e personalizzare la struttura dati più adatta alle necessità del programma. 

### Architettura del Framework delle Collezioni

L’architettura del Java Collections Framework è organizzata intorno a un gruppo di **interfacce principali** che rappresentano tipi generali di collezioni e **classi concrete** che forniscono implementazioni specifiche.

#### Interfacce Principali

1. **Collection**: rappresenta un gruppo generico di elementi. Questa interfaccia è la radice di tutte le altre collezioni, esclusa `Map`.
   
2. **List**: rappresenta una collezione ordinata di elementi in cui le duplicazioni sono consentite. Le classi più comuni che implementano `List` sono:
   - `ArrayList`: lista dinamica basata su array.
   - `LinkedList`: lista basata su nodi collegati, più efficiente per inserzioni e cancellazioni intermedie.

3. **Set**: rappresenta una collezione che non permette elementi duplicati. Le implementazioni più comuni di `Set` sono:
   - `HashSet`: usa una tabella hash per memorizzare elementi unici, senza mantenere l'ordine.
   - `TreeSet`: memorizza gli elementi in un ordine specifico (naturale o tramite comparatore).

4. **Queue**: rappresenta una collezione pensata per gestire elementi in modalità FIFO (First-In, First-Out). Le implementazioni includono:
   - `LinkedList`: utilizzabile anche come coda.
   - `PriorityQueue`: mantiene gli elementi ordinati in base a una priorità.

5. **Map**: rappresenta una struttura dati basata su coppie chiave-valore. Le implementazioni più usate sono:
   - `HashMap`: utilizza una tabella hash per memorizzare coppie chiave-valore senza ordine.
   - `TreeMap`: ordina le chiavi in modo naturale o tramite comparatore.
   - `LinkedHashMap`: mantiene l'ordine di inserimento delle chiavi.

#### Gerarchia delle Interfacce

La gerarchia di queste interfacce è la base per implementazioni concrete che forniscono comportamenti e caratteristiche specifiche. Ad esempio:

```java
Collection
├── List
│   ├── ArrayList
│   └── LinkedList
├── Set
│   ├── HashSet
│   └── TreeSet
└── Queue
    ├── LinkedList
    └── PriorityQueue
Map
├── HashMap
├── TreeMap
└── LinkedHashMap
```

### Classi di Implementazione

Il Java Collections Framework fornisce classi di implementazione concrete che mettono in pratica le funzionalità definite dalle interfacce principali. Vediamo alcune delle classi più comuni:

- **ArrayList**: ideale per liste che richiedono un accesso rapido agli elementi, ma meno efficiente per inserimenti e rimozioni in posizioni intermedie.
- **LinkedList**: adatta per inserzioni e rimozioni rapide alle estremità, grazie alla struttura a nodi collegati.
- **HashSet**: ottima per operazioni di ricerca veloce e controllo di unicità, basata su una tabella hash.
- **TreeSet**: mantiene un ordine ordinato degli elementi, utile per collezioni ordinate.
- **HashMap**: offre associazioni chiave-valore senza mantenere l’ordine, ottima per accessi veloci tramite chiavi.
- **TreeMap**: mantiene le chiavi in ordine crescente (o secondo un ordinamento personalizzato), utile per cercare in base a intervalli di chiavi.

### Operazioni Comuni

Il framework delle collezioni supporta varie operazioni comuni, standardizzate e accessibili tramite metodi specifici:

- **Aggiunta di Elementi**: utilizzando il metodo `add()` o `put()` (nel caso di `Map`).
- **Rimozione di Elementi**: utilizzando `remove()` o `remove(key)` per rimuovere una coppia chiave-valore.
- **Ricerca**: i metodi `contains()` e `containsKey()`/`containsValue()` permettono di verificare la presenza di elementi.
- **Accesso e Iterazione**: per scorrere gli elementi di una collezione, si possono usare cicli `for-each`, `iterator()`, o `forEach()`.

### Ordinamento e Comparatori

Alcune collezioni, come `TreeSet` e `TreeMap`, ordinano gli elementi automaticamente in base all’ordine naturale degli oggetti o a un `Comparator` specifico. Anche altre collezioni come `ArrayList` possono essere ordinate manualmente utilizzando il metodo `Collections.sort()`:

```java
ArrayList<String> nomi = new ArrayList<>();
nomi.add("Alice");
nomi.add("Charlie");
nomi.add("Bob");

Collections.sort(nomi);  // Ordina in ordine alfabetico
```

È possibile passare un `Comparator` personalizzato per definire un ordine specifico:

```java
Collections.sort(nomi, (a, b) -> b.compareTo(a));  // Ordina in ordine decrescente
```

### Sincronizzazione delle Collezioni

Le collezioni standard del JCF non sono thread-safe, il che significa che non possono essere usate in modo sicuro in ambienti multi-threaded senza sincronizzazione. Per creare versioni sincronizzate delle collezioni, si può usare il metodo `Collections.synchronizedCollection()`:

```java
List<String> sincronizzata = Collections.synchronizedList(new ArrayList<>());
```

### Vantaggi del Java Collections Framework

Il JCF offre molti vantaggi, tra cui:

- **Uniformità**: tutte le collezioni condividono interfacce e metodi comuni, rendendo il codice più coerente e leggibile.
- **Flessibilità**: grazie a un’ampia varietà di implementazioni, il JCF permette di scegliere la struttura dati più adatta.
- **Algoritmi Integrati**: con `Collections`, sono disponibili metodi per ordinamento, ricerca, riempimento e sincronizzazione.

### Conclusione

Il Java Collections Framework rappresenta un elemento fondamentale per la gestione dei dati nelle applicazioni Java. Comprendere le differenze tra le varie interfacce e classi e quando utilizzarle permette di scrivere codice più efficiente, leggibile e scalabile. Dalla gestione di semplici liste a strutture dati avanzate come mappe e code, il JCF fornisce un set completo di strumenti per affrontare esigenze di memorizzazione e manipolazione dati.

[13-Quiz di autovalutazione](13-Quiz%20di%20autovalutazione.md) - [INDICE](README.md)