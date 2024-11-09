## X Passaggio per Valore e Passaggio per Riferimento

Uno degli argomenti più importanti quando si lavora con metodi e funzioni è capire il concetto di **passaggio per valore** e **passaggio per riferimento**. In Java, tutti i parametri vengono passati ai metodi **per valore**, ma questo può comportarsi diversamente a seconda che si stia passando una **variabile di tipo primitivo** o un **oggetto**.

### Passaggio per Valore in Java
In Java, il **passaggio per valore** significa che una copia del valore della variabile viene passata al metodo. Ogni modifica apportata al parametro all'interno del metodo non influenzerà la variabile originale al di fuori del metodo.

Questo comportamento vale sia per i tipi primitivi (come `int`, `double`, `boolean`) che per le referenze a oggetti. Tuttavia, l’impatto di questo può variare.

#### Esempio con Tipi Primitivi
Quando si passano variabili di tipo primitivo, il metodo lavora su una copia indipendente della variabile originale. Le modifiche apportate all’interno del metodo non influenzeranno il valore originale.

```java
public class PassaggioPrimitivi {
    public static void main(String[] args) {
        int numero = 5;
        modificaValore(numero);
        System.out.println("Valore originale dopo il metodo: " + numero);
    }

    public static void modificaValore(int n) {
        n = 10; // Modifica il valore della copia, non della variabile originale
    }
}
```

**Output**:
```
Valore originale dopo il metodo: 5
```

In questo esempio, il valore di `numero` nel metodo `main` rimane `5` perché `modificaValore` riceve solo una copia di `numero`. La modifica a `n` non influisce sulla variabile `numero`.

### Passaggio per Valore con Oggetti (Riferimenti)
In Java, quando si passa un oggetto a un metodo, si passa comunque una **copia del riferimento** dell'oggetto e non l'oggetto stesso. Tuttavia, poiché la copia del riferimento punta allo stesso oggetto in memoria, le modifiche all’oggetto all’interno del metodo saranno riflesse anche al di fuori di esso.

```java
public class PassaggioOggetto {
    public static void main(String[] args) {
        int[] numeri = {1, 2, 3};
        modificaArray(numeri);
        System.out.println("Array dopo il metodo: ");
        for (int numero : numeri) {
            System.out.print(numero + " ");
        }
    }

    public static void modificaArray(int[] array) {
        array[0] = 99; // Modifica il primo elemento dell'array originale
    }
}
```

**Output**:
```
Array dopo il metodo:
99 2 3
```

In questo esempio, il metodo `modificaArray` modifica il primo elemento di `numeri`, e il cambiamento è visibile nel `main`. Questo accade perché l’array è un oggetto e il metodo riceve una copia del riferimento, non una copia dell’array. Di conseguenza, qualsiasi modifica apportata all’oggetto tramite il riferimento si riflette sull'oggetto originale.

### Passaggio di Oggetti e il Concetto di Immutabilità
Per prevenire modifiche involontarie, si possono utilizzare **oggetti immutabili** come `String`. Anche se il riferimento a un oggetto immutabile viene passato a un metodo, non sarà possibile alterare direttamente i dati interni dell’oggetto.

```java
public class PassaggioString {
    public static void main(String[] args) {
        String testo = "Originale";
        modificaStringa(testo);
        System.out.println("Stringa dopo il metodo: " + testo);
    }

    public static void modificaStringa(String str) {
        str = "Modificato"; // Crea una nuova stringa, ma non cambia l'originale
    }
}
```

**Output**:
```
Stringa dopo il metodo: Originale
```

Qui, `str` viene riassegnato alla stringa `"Modificato"` all'interno del metodo `modificaStringa`, ma non ha effetto su `testo` nel `main`. Questo accade perché le stringhe in Java sono immutabili e ogni modifica crea una nuova stringa.

### Riepilogo: Passaggio per Valore in Java
- In Java, **tutto** viene passato per valore, anche gli oggetti.
- Per i **tipi primitivi**, viene passata una copia del valore, quindi il metodo non può modificare il valore della variabile originale.
- Per gli **oggetti**, viene passata una copia del riferimento, che punta allo stesso oggetto in memoria. Quindi, le modifiche allo stato dell’oggetto all'interno del metodo sono visibili anche all'esterno.
- Gli oggetti immutabili come `String` non possono essere modificati direttamente, quindi il passaggio per valore non avrà effetto sul valore originale.

### Conclusione
Capire il **passaggio per valore** in Java è fondamentale per evitare errori nella manipolazione dei dati all'interno dei metodi. Per modificare i valori primitivi, è necessario restituire il valore modificato o usare wrapper come `Integer` o `AtomicInteger`. Con gli oggetti, invece, è possibile modificare lo stato dell’oggetto, ma non riassegnare il riferimento per cambiare l’oggetto a cui si punta.