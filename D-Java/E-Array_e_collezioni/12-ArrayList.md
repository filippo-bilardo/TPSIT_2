## 12 La Classe `ArrayList`

In Java, la classe `ArrayList` è una delle implementazioni più usate dell'interfaccia `List` nel Java Collections Framework. `ArrayList` rappresenta una lista dinamica di oggetti che può cambiare dimensione in modo automatico, consentendo inserimenti, rimozioni e accessi agli elementi in modo efficiente e flessibile.

Mentre gli array tradizionali hanno una dimensione fissa stabilita al momento dell'inizializzazione, un `ArrayList` può espandersi o ridursi automaticamente, rendendolo particolarmente adatto per gestire raccolte di dati in cui la dimensione è variabile o sconosciuta in fase di sviluppo.

#### 7.16.1 Creazione di un ArrayList

Per creare un `ArrayList`, devi importare il pacchetto `java.util.ArrayList`. Puoi specificare il tipo degli elementi da memorizzare all'interno della lista utilizzando la sintassi dei **generics** (`<Tipo>`). Ecco alcuni esempi:

```java
import java.util.ArrayList;

public class EsempioArrayList {
    public static void main(String[] args) {
        ArrayList<String> nomi = new ArrayList<>();  // ArrayList di Stringhe
        ArrayList<Integer> numeri = new ArrayList<>();  // ArrayList di Interi
    }
}
```

In questo esempio, `nomi` è una lista che memorizza stringhe (`String`), mentre `numeri` è una lista che memorizza interi (`Integer`). Usando i generics, garantiamo che l'`ArrayList` possa contenere solo elementi del tipo specificato.

### Metodi Principali di ArrayList

La classe `ArrayList` fornisce numerosi metodi per manipolare la lista. Vediamo i più utilizzati.

- **Aggiunta di Elementi**: per aggiungere un elemento alla fine della lista, si utilizza il metodo `add()`.

    ```java
    nomi.add("Alice");
    numeri.add(10);
    ```

- **Inserimento di Elementi in Posizioni Specifiche**: puoi inserire un elemento in una posizione specifica usando `add(indice, elemento)`.

    ```java
    nomi.add(1, "Bob");  // Inserisce "Bob" alla posizione 1
    ```

- **Accesso agli Elementi**: per recuperare un elemento in una determinata posizione, usa il metodo `get(indice)`.

    ```java
    String nome = nomi.get(0);  // Recupera il primo elemento della lista
    ```

- **Modifica degli Elementi**: puoi cambiare il valore di un elemento in una posizione specifica con `set(indice, elemento)`.

    ```java
    nomi.set(0, "Charlie");  // Modifica il primo elemento
    ```

- **Rimozione di Elementi**: per rimuovere un elemento, usa `remove(indice)` per rimuovere in base alla posizione o `remove(oggetto)` per rimuovere in base al valore.

    ```java
    nomi.remove(1);         // Rimuove l'elemento alla posizione 1
    numeri.remove(Integer.valueOf(10));  // Rimuove il valore 10 dalla lista
    ```

- **Dimensione della Lista**: per ottenere il numero di elementi in un `ArrayList`, usa `size()`.

    ```java
    int numeroDiElementi = nomi.size();
    ```

- **Verifica Presenza di un Elemento**: usa `contains(oggetto)` per verificare se un elemento è presente nella lista.

    ```java
    if (nomi.contains("Alice")) {
        System.out.println("Alice è nella lista.");
    }
    ```

- **Svuotare la Lista**: per rimuovere tutti gli elementi, usa `clear()`.

    ```java
    nomi.clear();
    ```

- **Verificare se l’ArrayList è Vuoto** (`isEmpty`)
   ```java
   boolean vuoto = nomi.isEmpty(); // Restituisce true se l'ArrayList è vuoto
   ```

### Iterazione su un ArrayList

Ci sono diversi modi per scorrere gli elementi di un `ArrayList`:

- **Ciclo `for` Tradizionale**:

    ```java
    for (int i = 0; i < nomi.size(); i++) {
        System.out.println(nomi.get(i));
    }
    ```

- **Ciclo `for-each`**:

    ```java
    for (String nome : nomi) {
        System.out.println(nome);
    }
    ```

- **Uso di un Iterator**:

    ```java
    Iterator<String> iteratore = nomi.iterator();
    while (iteratore.hasNext()) {
        System.out.println(iteratore.next());
    }
    ```

### Esempio Completo: Gestione di una Lista di Studenti

Nel seguente esempio, creiamo e gestiamo una lista di studenti utilizzando `ArrayList`.

```java
import java.util.ArrayList;

public class GestioneStudenti {
    public static void main(String[] args) {
        ArrayList<String> studenti = new ArrayList<>();

        // Aggiunta di elementi
        studenti.add("Alice");
        studenti.add("Bob");
        studenti.add("Charlie");

        // Modifica di un elemento
        studenti.set(1, "Beatrice");

        // Stampa degli elementi
        System.out.println("Lista degli studenti:");
        for (String studente : studenti) {
            System.out.println(studente);
        }

        // Rimozione di un elemento
        studenti.remove("Alice");

        // Controllo e stampa finale
        System.out.println("\nLista aggiornata:");
        for (String studente : studenti) {
            System.out.println(studente);
        }
    }
}
```

**Output**:
```
Lista degli studenti:
Alice
Beatrice
Charlie

Lista aggiornata:
Beatrice
Charlie
```

### Vantaggi e Svantaggi di ArrayList

**Vantaggi**:
- **Dimensione Dinamica**: l’`ArrayList` si ridimensiona automaticamente con l’aggiunta o la rimozione di elementi.
- **Accesso Rapido**: il tempo di accesso agli elementi (con `get` o `set`) è costante, grazie alla rappresentazione interna basata su array.

**Svantaggi**:
- **Rimozione e Inserzione Lente**: l’inserimento o la rimozione di elementi, specialmente nelle posizioni intermedie, può essere più lento rispetto ad altre strutture, come la `LinkedList`, poiché richiede uno spostamento degli elementi.
- **Non Sincronizzato**: `ArrayList` non è thread-safe per impostazione predefinita, il che significa che non è sicuro usarlo in ambienti multi-threaded senza sincronizzazione.

### Differenza tra Array e ArrayList

| Caratteristica         | Array                | ArrayList           |
|------------------------|----------------------|----------------------|
| **Dimensione**         | Fissa                | Variabile           |
| **Tipo di Elementi**   | Elementi primitivi o oggetti | Solo oggetti        |
| **Performance**        | Migliore per accesso sequenziale e posizionale | Più versatile, leggermente più lenta |
| **Sincronizzazione**   | Non sincronizzato    | Non sincronizzato (può essere sincronizzato con `Collections.synchronizedList`) |

### Conclusione

La classe `ArrayList` rappresenta una soluzione flessibile e potente per gestire collezioni di dati che richiedono una dimensione variabile. Grazie ai numerosi metodi disponibili, `ArrayList` offre grande semplicità di utilizzo e versatilità rispetto agli array tradizionali. Tuttavia, in contesti che richiedono una gestione efficiente delle operazioni di inserimento e rimozione, specialmente in mezzo alla lista, potrebbero essere più adatte altre strutture come la `LinkedList`.

[11-La classe Arrays](11-La%20classe%20Arrays.md) - [INDICE](README.md) - [13-Quiz di autovalutazione](13-Quiz%20di%20autovalutazione.md)