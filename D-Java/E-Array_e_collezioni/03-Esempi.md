## 3 Esempi con gli Array

Gli array in Java sono strumenti versatili e potenti per la gestione di sequenze di dati. Vediamo alcuni esempi pratici che illustrano l’utilizzo di questa struttura dati per risolvere problemi comuni.

### Esempio 1: Somma degli Elementi di un Array
In questo esempio, dichiariamo un array di numeri interi e utilizziamo un ciclo `for` per calcolare la somma di tutti gli elementi contenuti nell'array.

```java
public class SommaArray {
    public static void main(String[] args) {
        int[] numeri = {5, 10, 15, 20, 25};
        int somma = 0;

        for (int i = 0; i < numeri.length; i++) {
            somma += numeri[i];  // Somma ciascun elemento
        }

        System.out.println("La somma degli elementi è: " + somma);
    }
}
```

**Output**:
```
La somma degli elementi è: 75
```

In questo codice, usiamo `numeri.length` per ottenere la dimensione dell'array, un modo comodo per iterare su tutti gli elementi senza dover conoscere la dimensione in anticipo.

### Esempio 2: Ricerca di un Elemento nell'Array
Un'operazione comune sugli array è la **ricerca** di un elemento specifico. Qui cerchiamo il valore `20` nell’array e stampiamo il suo indice, se presente.

```java
public class RicercaArray {
    public static void main(String[] args) {
        int[] numeri = {5, 10, 15, 20, 25};
        int daTrovare = 20;
        boolean trovato = false;

        for (int i = 0; i < numeri.length; i++) {
            if (numeri[i] == daTrovare) {
                System.out.println("Elemento trovato all'indice: " + i);
                trovato = true;
                break;
            }
        }

        if (!trovato) {
            System.out.println("Elemento non trovato.");
        }
    }
}
```

**Output**:
```
Elemento trovato all'indice: 3
```

Se l'elemento esiste nell'array, il ciclo `for` lo trova e interrompe la ricerca con il comando `break`, riducendo il tempo di esecuzione.

### Esempio 3: Invertire l'Ordine degli Elementi
Invertire l’ordine degli elementi in un array è una tecnica utile in molte applicazioni. In questo esempio, scambiamo gli elementi del primo e ultimo posto, procedendo verso il centro dell’array.

```java
public class InvertiArray {
    public static void main(String[] args) {
        int[] numeri = {5, 10, 15, 20, 25};
        int temp;

        for (int i = 0; i < numeri.length / 2; i++) {
            temp = numeri[i];
            numeri[i] = numeri[numeri.length - 1 - i];
            numeri[numeri.length - 1 - i] = temp;
        }

        System.out.print("Array invertito: ");
        for (int numero : numeri) {
            System.out.print(numero + " ");
        }
    }
}
```

**Output**:
```
Array invertito: 25 20 15 10 5
```

Questo esempio mostra come usare una variabile temporanea `temp` per scambiare due elementi. L'array viene così invertito con un ciclo `for` che scorre fino alla metà dell’array.

### Esempio 4: Trova il Minimo e il Massimo
Calcolare il **minimo** e il **massimo** di un array è una delle operazioni più comuni. Qui usiamo due variabili per memorizzare i valori minimi e massimi man mano che scansioniamo l'array.

```java
public class MinimoMassimoArray {
    public static void main(String[] args) {
        int[] numeri = {5, 10, 15, 20, 25};
        int minimo = numeri[0];
        int massimo = numeri[0];

        for (int i = 1; i < numeri.length; i++) {
            if (numeri[i] < minimo) {
                minimo = numeri[i];
            }
            if (numeri[i] > massimo) {
                massimo = numeri[i];
            }
        }

        System.out.println("Il minimo è: " + minimo);
        System.out.println("Il massimo è: " + massimo);
    }
}
```

**Output**:
```
Il minimo è: 5
Il massimo è: 25
```

Questa tecnica di ricerca è semplice ed efficiente, poiché richiede solo una singola scansione dell’array.


### Esempio 5: Esempi con numeri random

In Java, possiamo utilizzare la classe `Random` per generare numeri casuali e riempire gli array con valori random. Di seguito, mostro come inizializzare due array con valori casuali:

1. Un array con valori casuali da 0 a 100.
2. Un altro array con valori casuali da 100 a 1000.
<br><br>

```java
import java.util.Random;

public class ArrayRandom {
    public static void main(String[] args) {
        Random random = new Random();

        // Array con numeri casuali da 0 a 100
        int[] array0_100 = new int[10]; // Array di 10 elementi (puoi cambiare la dimensione)
        for (int i = 0; i < array0_100.length; i++) {
            array0_100[i] = random.nextInt(101); // Genera numeri tra 0 e 100 (inclusi)
        }

        // Array con numeri casuali da 100 a 1000
        int[] array100_1000 = new int[10]; // Array di 10 elementi (puoi cambiare la dimensione)
        for (int i = 0; i < array100_1000.length; i++) {
            array100_1000[i] = random.nextInt(901) + 100; // Genera numeri tra 100 e 1000
        }

        // Stampa degli array
        System.out.println("Array con numeri da 0 a 100:");
        for (int num : array0_100) {
            System.out.print(num + " ");
        }

        System.out.println("\nArray con numeri da 100 a 1000:");
        for (int num : array100_1000) {
            System.out.print(num + " ");
        }
    }
}
```
**Spiegazione del Codice**

   - Utilizziamo `random.nextInt(101)` per generare numeri casuali tra `0` e `100` inclusi. Il metodo `nextInt(101)` genera un valore intero casuale da `0` (incluso) a `101` (escluso).

   - Utilizziamo `random.nextInt(901) + 100` per generare numeri casuali da `100` a `1000` inclusi. Qui, `nextInt(901)` produce un numero da `0` a `900`, e sommiamo `100` per ottenere l'intervallo desiderato da `100` a `1000`.

**Output**
```
Array con numeri da 0 a 100:
56 23 90 34 12 65 87 43 70 29 

Array con numeri da 100 a 1000:
823 478 356 703 244 690 932 815 117 142
```

Con questo codice, otteniamo due array con numeri casuali rispettivamente tra `0-100` e `100-1000`, riempiendo automaticamente ciascun array con valori generati randomicamente all'interno degli intervalli desiderati.

[02-Array](02-Array.md) - [INDICE](README.md) - [04-Eccezioni.md](04-Eccezioni.md)