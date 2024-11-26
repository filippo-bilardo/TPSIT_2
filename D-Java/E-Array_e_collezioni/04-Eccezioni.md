## 4 - Gestione delle Eccezioni con gli Array

In Java, l’uso degli array comporta la gestione di eccezioni comuni che possono verificarsi in fase di esecuzione. Le eccezioni consentono di intercettare e gestire errori relativi agli array, come l'accesso a un indice fuori dai limiti o il tentativo di utilizzare un array non inizializzato. In questo sottocapitolo esploreremo come prevenire e gestire le eccezioni più comuni associate agli array.

### 4.1 Eccezioni Comuni con gli Array

Gli errori di esecuzione con gli array possono causare il fallimento di un programma, ma Java offre diversi tipi di eccezioni per aiutare a identificare e gestire questi problemi.

1. **ArrayIndexOutOfBoundsException**: si verifica quando si tenta di accedere a un indice che non esiste nell’array. Questo errore avviene, ad esempio, se si cerca di accedere a un indice negativo o a un indice pari o superiore alla lunghezza dell’array.

    ```java
    int[] numeri = {1, 2, 3};
    System.out.println(numeri[3]);  // ArrayIndexOutOfBoundsException
    ```

2. **NullPointerException**: si verifica quando si tenta di accedere a un array che non è stato ancora inizializzato. Questo errore si presenta se si dichiara un array senza assegnargli una nuova istanza.

    ```java
    int[] numeri = null;
    System.out.println(numeri[0]);  // NullPointerException
    ```

### 4.2 Prevenzione delle Eccezioni

Per evitare le eccezioni sugli array, è possibile adottare alcune pratiche:

- **Verifica della lunghezza dell’array**: prima di accedere a un elemento, verifica che l’indice rientri nei limiti dell’array.

    ```java
    if (indice >= 0 && indice < numeri.length) {
        System.out.println(numeri[indice]);
    } else {
        System.out.println("Indice fuori dai limiti!");
    }
    ```

- **Inizializzazione degli array**: assicurati sempre che l’array sia inizializzato prima dell’utilizzo, specialmente quando viene creato in modo dinamico.

    ```java
    if (numeri != null) {
        System.out.println(numeri[0]);
    } else {
        System.out.println("Array non inizializzato!");
    }
    ```

### 4.3 Gestione delle Eccezioni con Blocchi try-catch

Java permette di gestire le eccezioni con blocchi `try-catch`, che consentono di intercettare gli errori e di reagire in modo appropriato senza causare l’arresto del programma. Questo approccio è utile per situazioni in cui l’accesso a un array può generare eccezioni e si vuole garantire che il programma continui a funzionare.

```java
int[] numeri = {1, 2, 3};

try {
    System.out.println(numeri[3]);  // Potenziale errore
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("Errore: indice fuori dai limiti dell'array.");
}
```

### 4.4 Esempio Completo: Somma degli Elementi di un Array con Gestione delle Eccezioni

Nell’esempio seguente, sommiamo gli elementi di un array con una gestione delle eccezioni per evitare errori dovuti a un array non inizializzato o a tentativi di accesso a elementi fuori dai limiti.

```java
public class GestioneArray {

    public static int sommaElementi(int[] array) {
        int somma = 0;

        try {
            for (int i = 0; i < array.length; i++) {
                somma += array[i];
            }
        } catch (NullPointerException e) {
            System.out.println("Errore: l'array non è stato inizializzato.");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Errore: indice fuori dai limiti dell'array.");
        }

        return somma;
    }

    public static void main(String[] args) {
        int[] numeri = null;
        System.out.println("Somma degli elementi: " + sommaElementi(numeri));
    }
}
```

In questo esempio, se l’array `numeri` è nullo, verrà catturata una `NullPointerException` e il programma stamperà un messaggio di errore senza interrompersi. Inoltre, grazie alla struttura `try-catch`, ogni eccezione viene gestita separatamente, facilitando l'identificazione del problema specifico.

### Conclusione

La gestione delle eccezioni negli array è essenziale per creare applicazioni robuste e affidabili in Java. Capire come e quando gestire le eccezioni può migliorare la stabilità del codice e prevenire arresti anomali dovuti a errori di accesso agli elementi dell'array. Utilizzando i blocchi `try-catch` e applicando controlli preventivi, è possibile scrivere programmi più sicuri e resistenti agli errori.

[03-Esempi](03-Esempi.md) - [INDICE](README.md) - [05-Esempio mazzo di carte.md](05-Esempio%20mazzo%20di%20carte.md)