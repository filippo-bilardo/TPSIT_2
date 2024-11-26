## 11 La Classe `Arrays`

In Java, la classe `Arrays` fa parte del pacchetto `java.util` e fornisce una serie di **metodi statici** per lavorare con gli array. La classe `Arrays` semplifica molte operazioni comuni sugli array, come l’ordinamento, la ricerca, il riempimento e la conversione in stringhe. Questa classe rende più efficiente e leggibile il lavoro con gli array, evitando la scrittura di codice complesso per operazioni frequenti.

### Importare la Classe `Arrays`

La classe `Arrays` è inclusa nel pacchetto `java.util`, quindi per utilizzarla basta importarla:

```java
import java.util.Arrays;
```

### Metodi Principali della Classe `Arrays`

Ecco alcuni dei metodi più comuni e utili della classe `Arrays`.

#### 1. Metodo `toString()`

Il metodo `toString()` converte un array in una stringa leggibile, utile per stampare il contenuto di un array senza dover scrivere un ciclo manuale.

```java
int[] numeri = {1, 2, 3, 4, 5};
System.out.println(Arrays.toString(numeri));
```

**Output**:
```
[1, 2, 3, 4, 5]
```

#### 2. Metodo `sort()`

Il metodo `sort()` ordina gli elementi di un array in ordine crescente. Funziona su array di tipi primitivi e su array di oggetti che implementano l’interfaccia `Comparable`.

```java
int[] numeri = {5, 3, 1, 4, 2};
Arrays.sort(numeri);
System.out.println(Arrays.toString(numeri));
```

**Output**:
```
[1, 2, 3, 4, 5]
```

Per ordinare array di oggetti in ordine personalizzato, è possibile utilizzare il metodo `Arrays.sort(array, Comparator)`.

#### 3. Metodo `binarySearch()`

`binarySearch()` cerca un elemento in un array ordinato e restituisce l’indice dell’elemento trovato, oppure un valore negativo se l’elemento non è presente. È importante che l'array sia ordinato prima di usare `binarySearch()`.

```java
int[] numeri = {1, 2, 3, 4, 5};
int indice = Arrays.binarySearch(numeri, 3);
System.out.println("Elemento trovato all'indice: " + indice);
```

**Output**:
```
Elemento trovato all'indice: 2
```

#### 4. Metodo `fill()`

Il metodo `fill()` riempie un array con un valore specificato, utile per inizializzare un array o reimpostare i suoi valori.

```java
int[] numeri = new int[5];
Arrays.fill(numeri, 7);
System.out.println(Arrays.toString(numeri));
```

**Output**:
```
[7, 7, 7, 7, 7]
```

#### 5. Metodo `copyOf()`

`copyOf()` crea una copia di un array fino a una lunghezza specificata. Questo metodo è utile per ridimensionare un array o per clonarne il contenuto.

```java
int[] numeri = {1, 2, 3, 4, 5};
int[] copia = Arrays.copyOf(numeri, 3);
System.out.println(Arrays.toString(copia));
```

**Output**:
```
[1, 2, 3]
```

In questo esempio, `copyOf` crea una copia dei primi tre elementi dell’array `numeri`.

#### 6. Metodo `equals()`

`equals()` confronta due array e restituisce `true` se sono identici (stessa lunghezza e stessi valori in tutti gli elementi), altrimenti restituisce `false`.

```java
int[] numeri1 = {1, 2, 3};
int[] numeri2 = {1, 2, 3};
System.out.println(Arrays.equals(numeri1, numeri2));
```

**Output**:
```
true
```

#### 7. Metodo `deepToString()` e `deepEquals()`

Per array multidimensionali, `deepToString()` converte un array multidimensionale in una stringa leggibile, mentre `deepEquals()` confronta array multidimensionali per verificarne l’uguaglianza.

```java
int[][] matrice = {{1, 2}, {3, 4}};
System.out.println(Arrays.deepToString(matrice));
```

**Output**:
```
[[1, 2], [3, 4]]
```

#### 8. Metodo `asList()`

`asList()` converte un array in una `List` (fissa in dimensioni). Questo è utile per utilizzare metodi delle collezioni sulle liste create da array.

```java
String[] nomi = {"Alice", "Bob", "Carol"};
List<String> listaNomi = Arrays.asList(nomi);
System.out.println(listaNomi);
```

**Output**:
```
[Alice, Bob, Carol]
```

> **Nota**: la lista ottenuta è di dimensioni fisse e non supporta aggiunte o rimozioni.

#### Esempio Completo: Utilizzo dei Metodi della Classe `Arrays`

Nel seguente esempio, utilizziamo vari metodi della classe `Arrays` per manipolare e visualizzare i contenuti di un array.

```java
import java.util.Arrays;

public class ArraysEsempio {
    public static void main(String[] args) {
        // Creazione di un array
        int[] numeri = {5, 3, 8, 1, 9};

        // Stampa dell'array
        System.out.println("Array originale: " + Arrays.toString(numeri));

        // Ordinamento dell'array
        Arrays.sort(numeri);
        System.out.println("Array ordinato: " + Arrays.toString(numeri));

        // Ricerca binaria
        int indice = Arrays.binarySearch(numeri, 8);
        System.out.println("Elemento 8 trovato all'indice: " + indice);

        // Riempimento dell'array
        Arrays.fill(numeri, 7);
        System.out.println("Array riempito con 7: " + Arrays.toString(numeri));

        // Copia dell'array
        int[] copia = Arrays.copyOf(numeri, 3);
        System.out.println("Copia dei primi 3 elementi: " + Arrays.toString(copia));
    }
}
```

**Output**:
```
Array originale: [5, 3, 8, 1, 9]
Array ordinato: [1, 3, 5, 8, 9]
Elemento 8 trovato all'indice: 3
Array riempito con 7: [7, 7, 7, 7, 7]
Copia dei primi 3 elementi: [7, 7, 7]
```

#### Altri metodi della classe `Arrays`
Oltre ai metodi di base, la classe `Arrays` include anche metodi avanzati per operazioni specifiche che possono essere utili nella programmazione quotidiana.

`copyOfRange()` consente di copiare una **sottosezione** di un array. È simile a `copyOf()`, ma permette di specificare un intervallo di indici di partenza e di fine.

`parallelSort()` è una versione ottimizzata di `sort()` che sfrutta il **multithreading** per ordinare array di grandi dimensioni in modo più rapido. Questo metodo è utile per array di grandi dimensioni e sfrutta le risorse di sistema in modo più efficiente.

`setAll()` permette di **inizializzare** o **modificare tutti gli elementi** di un array in base a una funzione. È utile per generare rapidamente valori specifici o sequenze personalizzate.

`stream()` converte un array in uno **stream**, consentendo di applicare operazioni della libreria `Stream` di Java, come `filter`, `map`, `reduce` e altre operazioni funzionali.

`mismatch()` confronta due array e restituisce l’indice del primo elemento che differisce tra i due. Se gli array sono uguali, restituisce `-1`.

`compare()` confronta due array **lessicograficamente** (cioè, elemento per elemento in ordine) e restituisce:
- Un valore negativo se il primo array è "minore" del secondo.
- Zero se gli array sono uguali.
- Un valore positivo se il primo array è "maggiore" del secondo.

`hashCode()` calcola un valore di **hash** per l’array, utile per confronti o per l’uso in strutture di dati come le mappe. Ogni array genera un hash diverso, basato sui valori e sull’ordine degli elementi.

#### Esempio Completo con Vari Metodi di `Arrays`

Ecco un esempio che utilizza diversi metodi avanzati di `Arrays` per dimostrare il loro utilizzo:

```java
import java.util.Arrays;

public class MetodiAvanzatiArrays {
    public static void main(String[] args) {
        int[] numeri = {4, 2, 8, 5, 1};

        // 1. Ordinamento parallelo
        Arrays.parallelSort(numeri);
        System.out.println("Array ordinato parallelamente: " + Arrays.toString(numeri));

        // 2. Sottosezione dell'array
        int[] sottosezione = Arrays.copyOfRange(numeri, 1, 4);
        System.out.println("Sottosezione dell'array: " + Arrays.toString(sottosezione));

        // 3. Stream e somma
        int somma = Arrays.stream(numeri).sum();
        System.out.println("Somma degli elementi: " + somma);

        // 4. SetAll per inizializzare con potenze di 2
        Arrays.setAll(numeri, i -> (int) Math.pow(2, i));
        System.out.println("Array inizializzato con potenze di 2: " + Arrays.toString(numeri));

        // 5. HashCode dell'array
        int hash = Arrays.hashCode(numeri);
        System.out.println("Hash dell'array: " + hash);

        // 6. Mismatch tra due array
        int[] numeri2 = {1, 2, 3, 4, 5};
        int indiceMismatch = Arrays.mismatch(numeri, numeri2);
        System.out.println("Indice della prima differenza: " + indiceMismatch);
    }
}
```

**Output**:
```
Array ordinato parallelamente: [1, 2, 4, 5, 8]
Sottosezione dell'array: [2, 4, 5]
Somma degli elementi: 20
Array inizializzato con potenze di 2: [1, 2, 4, 8, 16]
Hash dell'array: 1537
Indice della prima differenza: 0
```

### Conclusione

La classe `Arrays` fornisce numerosi metodi potenti che rendono la manipolazione degli array più semplice, efficiente e leggibile. Da operazioni di base come il riempimento e l'ordinamento fino a metodi avanzati come `parallelSort`, `stream` e `setAll`, questa classe consente di svolgere operazioni complesse su array con poche righe di codice. Conoscere questi metodi consente di scrivere codice Java più chiaro e ottimizzato per operazioni che coinvolgono la gestione di dati in strutture di array.

[10-Passaggio di argomenti dalla riga di comando](10-Passaggio%20di%20argomenti%20dalla%20riga%20di%20comando.md) - [INDICE](README.md) - [12-ArrayList.md](12-ArrayList.md)