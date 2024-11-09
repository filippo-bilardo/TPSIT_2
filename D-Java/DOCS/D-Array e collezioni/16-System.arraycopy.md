## 16 System.arraycopy()

Il metodo `System.arraycopy()` in Java è un metodo molto efficiente per copiare elementi da un array a un altro. È utile quando si desidera copiare una parte di un array esistente o trasferire tutti i suoi elementi in un altro array senza iterare manualmente.

### Sintassi di `System.arraycopy()`

```java
System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
```

Dove:
- `src`: l’array di origine.
- `srcPos`: l'indice iniziale da cui partire nell'array di origine.
- `dest`: l’array di destinazione.
- `destPos`: l'indice iniziale dell'array di destinazione dove iniziare a copiare.
- `length`: il numero di elementi da copiare.

### Esempio di Utilizzo di `System.arraycopy()`

Ecco un esempio in cui copiamo alcuni elementi da un array di origine a un array di destinazione:

```java
public class ArrayCopyExample {
    public static void main(String[] args) {
        int[] arrayOrigine = {1, 2, 3, 4, 5};
        int[] arrayDestinazione = new int[5];

        // Copia gli ultimi 3 elementi di arrayOrigine negli ultimi 3 elementi di arrayDestinazione
        System.arraycopy(arrayOrigine, 2, arrayDestinazione, 2, 3);

        // Stampa dell'array di destinazione
        System.out.println("Array di destinazione: ");
        for (int elemento : arrayDestinazione) {
            System.out.print(elemento + " ");
        }
    }
}
```

**Output**:
```
Array di destinazione:
0 0 3 4 5
```

**Spiegazione del Codice**

- **Array di origine**: `{1, 2, 3, 4, 5}`.
- **Array di destinazione**: Un array di dimensione 5, inizialmente vuoto `{0, 0, 0, 0, 0}`.
- **Operazione**: `System.arraycopy(arrayOrigine, 2, arrayDestinazione, 2, 3);`
  - Copia a partire dall’indice `2` di `arrayOrigine` (`3, 4, 5`) e li inserisce a partire dall’indice `2` di `arrayDestinazione`.

Il metodo `System.arraycopy()` è molto veloce, in quanto è implementato a basso livello ed è generalmente più efficiente dei cicli manuali per copiare elementi in Java.