## 9 Passaggio di Array come Argomenti

In Java, è possibile passare gli array come argomenti ai metodi, esattamente come si fa per altri tipi di variabili. Gli array in Java sono passati per **riferimento**, il che significa che quando un array viene passato a un metodo, il metodo riceve un riferimento alla posizione di memoria dell’array originale e può quindi modificarne i valori.

### Dichiarare un Metodo con Array come Argomento

Per dichiarare un metodo che accetta un array come argomento, è sufficiente indicare il tipo dell’array nei parametri del metodo. Ad esempio, un metodo che calcola la somma di un array di interi potrebbe essere dichiarato come segue:

```java
public static int sommaArray(int[] array) {
    int somma = 0;
    for (int elemento : array) {
        somma += elemento;
    }
    return somma;
}
```

### Esempio di Passaggio di un Array a un Metodo

Nel seguente esempio, il metodo `sommaArray` calcola la somma di tutti gli elementi di un array passato come argomento.

```java
public class CalcoloArray {
    public static void main(String[] args) {
        int[] numeri = {10, 20, 30, 40, 50};
        int somma = sommaArray(numeri);
        System.out.println("La somma degli elementi dell'array è: " + somma);
    }

    // Metodo che accetta un array e calcola la somma degli elementi
    public static int sommaArray(int[] array) {
        int somma = 0;
        for (int elemento : array) {
            somma += elemento;
        }
        return somma;
    }
}
```

**Output**:
```
La somma degli elementi dell'array è: 150
```

In questo caso, l’array `numeri` viene passato come argomento al metodo `sommaArray`, che restituisce la somma dei suoi elementi.

### Modifica di un Array Passato come Argomento

Poiché gli array sono passati per riferimento, un metodo può modificare direttamente gli elementi di un array. Ad esempio, nel seguente codice, un metodo incrementa ciascun elemento di un array passato come argomento:

```java
public class IncrementoArray {
    public static void main(String[] args) {
        int[] numeri = {1, 2, 3, 4, 5};
        incrementaElementi(numeri);

        System.out.println("Array incrementato:");
        for (int numero : numeri) {
            System.out.print(numero + " ");
        }
    }

    // Metodo che incrementa ogni elemento dell'array
    public static void incrementaElementi(int[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] += 1;
        }
    }
}
```

**Output**:
```
Array incrementato:
2 3 4 5 6
```

In questo esempio, il metodo `incrementaElementi` incrementa ogni elemento dell’array `numeri`. Poiché gli array sono passati per riferimento, le modifiche apportate al loro interno nel metodo `incrementaElementi` influenzano direttamente l’array originale `numeri` nel `main`.

### Passaggio di Array Multidimensionali

Java supporta anche il passaggio di array multidimensionali come argomenti ai metodi. Ad esempio, possiamo scrivere un metodo per calcolare la somma di tutti gli elementi in un array bidimensionale:

```java
public class SommaArrayBidimensionale {
    public static void main(String[] args) {
        int[][] matrice = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        
        int somma = sommaMatrice(matrice);
        System.out.println("La somma degli elementi della matrice è: " + somma);
    }

    // Metodo che accetta un array bidimensionale e calcola la somma degli elementi
    public static int sommaMatrice(int[][] matrice) {
        int somma = 0;
        for (int[] riga : matrice) {
            for (int elemento : riga) {
                somma += elemento;
            }
        }
        return somma;
    }
}
```

**Output**:
```
La somma degli elementi della matrice è: 45
```

Il metodo `sommaMatrice` accetta un array bidimensionale e somma tutti i suoi elementi. Ogni riga dell’array `matrice` è un array a sé stante, quindi possiamo utilizzare un ciclo `for` potenziato per iterare su ogni riga e, al suo interno, su ciascun elemento.

### Considerazioni sul Passaggio di Array come Argomenti

- **Efficienza**: Poiché gli array sono passati per riferimento, il passaggio di grandi array come argomenti è efficiente in termini di memoria. Tuttavia, ciò significa che un metodo può modificare l'array originale, il che potrebbe non essere desiderato in tutte le situazioni.
  
- **Immutabilità**: Se si desidera evitare che un metodo modifichi un array passato come argomento, si può utilizzare una copia dell’array invece dell’array originale. Ad esempio:
  ```java
  int[] copia = Arrays.copyOf(arrayOriginale, arrayOriginale.length);
  ```

### Conclusione
Il passaggio di array come argomenti nei metodi è una funzionalità molto utile in Java, che permette di elaborare dati complessi in modo modulare e flessibile. Grazie al passaggio per riferimento, Java consente di manipolare gli array direttamente, semplificando molte operazioni su collezioni di dati e permettendo di strutturare il codice in modo più organizzato e riutilizzabile.

[08-Array multidimensionali](08-Array%20multidimensionali.md) - [INDICE](README.md) - [10-Passaggio di argomenti dalla riga di comando](10-Passaggio%20di%20argomenti%20dalla%20riga%20di%20comando.md)