## 15 Array e Algoritmi

In Java, gli array sono una struttura dati essenziale, spesso utilizzata per implementare **algoritmi di ordinamento**, **ricerca**, e **manipolazione dei dati**. Gli algoritmi sugli array consentono di organizzare, filtrare e analizzare i dati in modo efficiente, rendendo possibile lo sviluppo di applicazioni veloci e performanti.

### Ordinamento degli Array

L’**ordinamento** è un’operazione comune su array che riarrangia gli elementi in un ordine specifico, come quello crescente o decrescente. L’ordinamento rende più semplice l'accesso ai dati, facilitando altre operazioni come la ricerca.

Java offre sia metodi di ordinamento integrati, come `Arrays.sort()`, sia la possibilità di implementare algoritmi di ordinamento personalizzati, come **Bubble Sort**, **Selection Sort** e **Quick Sort**.

#### Esempio: Ordinamento con `Arrays.sort()`

```java
import java.util.Arrays;

public class OrdinamentoArray {
    public static void main(String[] args) {
        int[] numeri = {5, 2, 9, 1, 3};
        Arrays.sort(numeri);

        System.out.println("Array ordinato: " + Arrays.toString(numeri));
    }
}
```

**Output**:
```
Array ordinato: [1, 2, 3, 5, 9]
```

---

### Algoritmi di Ricerca

La **ricerca** è un'altra operazione comune sugli array che permette di trovare la posizione o verificare la presenza di un elemento specifico all’interno di un array. Esistono due algoritmi di ricerca principali:

1. **Ricerca Lineare**: scorre sequenzialmente ogni elemento dell'array. È semplice ma inefficiente per array di grandi dimensioni.
2. **Ricerca Binaria**: divide ripetutamente l'array a metà. È molto più veloce, ma richiede che l'array sia ordinato.

#### Esempio: Ricerca Binaria con `Arrays.binarySearch()`

```java
import java.util.Arrays;

public class RicercaBinariaArray {
    public static void main(String[] args) {
        int[] numeri = {1, 3, 5, 7, 9};
        int indice = Arrays.binarySearch(numeri, 7);

        if (indice >= 0) {
            System.out.println("Elemento trovato all'indice: " + indice);
        } else {
            System.out.println("Elemento non presente nell'array.");
        }
    }
}
```

**Output**:
```
Elemento trovato all'indice: 3
```

---

### Algoritmi di Manipolazione degli Array

Gli array possono essere manipolati per svolgere varie operazioni, come l'inversione, la rotazione o la fusione di più array.

#### Esempio: Inversione di un Array

Invertire un array significa scambiare l’ordine degli elementi, facendo sì che l’ultimo diventi il primo e così via.

```java
public class InversioneArray {
    public static void main(String[] args) {
        int[] numeri = {1, 2, 3, 4, 5};
        invertiArray(numeri);

        System.out.println("Array invertito: " + Arrays.toString(numeri));
    }

    public static void invertiArray(int[] array) {
        int inizio = 0;
        int fine = array.length - 1;
        while (inizio < fine) {
            int temp = array[inizio];
            array[inizio] = array[fine];
            array[fine] = temp;
            inizio++;
            fine--;
        }
    }
}
```

**Output**:
```
Array invertito: [5, 4, 3, 2, 1]
```

---

### Algoritmo per la Somma Massima di una Sottosequenza Contigua (Kadane’s Algorithm)

Questo algoritmo trova la sottosequenza contigua con la somma massima in un array di numeri interi. È utile in situazioni in cui si vogliono identificare i "blocchi" di dati di maggior valore.

```java
public class SommaMassimaSottosequenza {
    public static int sommaMassima(int[] array) {
        int sommaMassima = array[0];
        int sommaCorrente = array[0];

        for (int i = 1; i < array.length; i++) {
            sommaCorrente = Math.max(array[i], sommaCorrente + array[i]);
            sommaMassima = Math.max(sommaMassima, sommaCorrente);
        }
        return sommaMassima;
    }

    public static void main(String[] args) {
        int[] array = {-2, 1, -3, 4, -1, 2, 1, -5, 4};
        System.out.println("Somma massima della sottosequenza: " + sommaMassima(array));
    }
}
```

**Output**:
```
Somma massima della sottosequenza: 6
```

---

### Conclusione

Gli algoritmi per array sono fondamentali per la gestione efficiente dei dati. Comprendere come implementare algoritmi di ordinamento, ricerca e manipolazione permette di creare programmi più ottimizzati e versatili. Queste competenze sono essenziali per affrontare problemi complessi in ambiti come la gestione di grandi dataset e lo sviluppo di applicazioni performanti.