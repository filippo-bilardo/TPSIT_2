## X Liste di Argomenti di Lunghezza Variabile

Java supporta l'uso di **liste di argomenti di lunghezza variabile** (o **varargs**), che consentono a un metodo di accettare un numero variabile di argomenti dello stesso tipo. Questa caratteristica è particolarmente utile quando il numero di argomenti da passare al metodo può variare in base alla situazione, senza la necessità di creare e passare un array separato.

### Sintassi delle Liste di Argomenti di Lunghezza Variabile

Per dichiarare una lista di argomenti variabili, si utilizza una sintassi simile a quella di un array, con tre punti `...` dopo il tipo dell’argomento. Ad esempio, la seguente dichiarazione definisce un metodo `somma` che può accettare un numero variabile di argomenti di tipo `int`:

```java
public static int somma(int... numeri) {
    int somma = 0;
    for (int numero : numeri) {
        somma += numero;
    }
    return somma;
}
```

In questo esempio, `numeri` è una lista di argomenti variabili che il metodo `somma` interpreta come un array di interi. All'interno del metodo, `numeri` può essere trattato come un normale array.

### Utilizzo delle Liste di Argomenti Variabili

Quando si chiama un metodo che accetta una lista di argomenti variabili, è possibile passare un numero qualsiasi di argomenti:

```java
public class VarargsEsempio {
    public static void main(String[] args) {
        System.out.println(somma(1, 2, 3));      // Output: 6
        System.out.println(somma(4, 5, 6, 7));   // Output: 22
        System.out.println(somma());             // Output: 0
    }

    public static int somma(int... numeri) {
        int somma = 0;
        for (int numero : numeri) {
            somma += numero;
        }
        return somma;
    }
}
```

**Output**:
```
6
22
0
```

Nell’esempio, `somma` calcola la somma di tutti gli argomenti passati. Se nessun argomento viene passato, `numeri` è un array vuoto, quindi la somma risultante sarà `0`.

### Regole per l’Uso delle Liste di Argomenti Variabili

1. **Posizione nella Firma del Metodo**: L'argomento variabile deve essere l'ultimo nella lista dei parametri. Ad esempio, la seguente dichiarazione è valida:
   ```java
   public static void stampaNumeri(String messaggio, int... numeri) { ... }
   ```

   Tuttavia, non è possibile avere un altro argomento dopo una lista variabile:
   ```java
   // Non valido
   public static void metodoInvalido(int... numeri, String messaggio) { ... }
   ```

2. **Un Solo Argomento Variabile per Metodo**: Un metodo può avere una sola lista di argomenti variabili. Quindi, non è consentito dichiarare un metodo con più di una lista variabile.

### Esempio: Metodo per Calcolare la Media di Numeri Variabili

Nel seguente esempio, il metodo `calcolaMedia` accetta un numero variabile di argomenti di tipo `double` e calcola la media dei valori.

```java
public class MediaVarargs {
    public static void main(String[] args) {
        System.out.println("Media di 2, 4, 6: " + calcolaMedia(2, 4, 6));         // Output: 4.0
        System.out.println("Media di 10, 20, 30, 40: " + calcolaMedia(10, 20, 30, 40)); // Output: 25.0
        System.out.println("Media di nessun valore: " + calcolaMedia());           // Output: 0.0
    }

    public static double calcolaMedia(double... valori) {
        if (valori.length == 0) return 0.0; // Se nessun valore è fornito, restituisce 0.0

        double somma = 0;
        for (double valore : valori) {
            somma += valore;
        }
        return somma / valori.length;
    }
}
```

**Output**:
```
Media di 2, 4, 6: 4.0
Media di 10, 20, 30, 40: 25.0
Media di nessun valore: 0.0
```

Il metodo `calcolaMedia` utilizza `valori.length` per determinare il numero di argomenti e calcolare la media in modo dinamico, in base al numero di valori passati.

### Differenze tra Array e Liste di Argomenti Variabili
Sebbene le liste di argomenti variabili siano simili agli array, presentano alcune differenze:

- **Sintassi di Invocazione**: Quando si chiama un metodo con un array, è necessario passare esplicitamente un array; invece, con varargs si possono passare i valori direttamente.
- **Maggiore Flessibilità**: Le liste di argomenti variabili rendono il codice più flessibile e leggibile, evitando di dover creare array temporanei quando il numero di argomenti può variare.

### Conclusione

Le liste di argomenti di lunghezza variabile sono uno strumento potente e flessibile in Java. Consentono di scrivere metodi che accettano un numero variabile di argomenti, mantenendo il codice pulito e facile da leggere. Grazie ai varargs, è possibile passare facilmente una quantità variabile di dati senza dover creare array espliciti, migliorando la leggibilità e la flessibilità del codice Java. 