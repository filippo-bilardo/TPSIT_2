
## Quiz di Autovalutazione su Array e ArrayList

### Sezione 1: Array
1. **Vero o Falso**: Una volta dichiarato un array, è possibile modificare la sua dimensione.
   - A) Vero
   - B) Falso

2. Qual è l’output del seguente codice?
   ```java
   int[] numeri = {10, 20, 30, 40};
   System.out.println(numeri[1]);
   ```
   - A) 10
   - B) 20
   - C) 30
   - D) Errore di compilazione

3. Quale metodo della classe `Arrays` viene utilizzato per ordinare un array in ordine crescente?
   - A) `sort()`
   - B) `order()`
   - C) `arrange()`
   - D) `sortArray()`

4. Come si crea un array di interi di lunghezza 5 in Java?
   - A) `int[] numeri = new int(5);`
   - B) `int[] numeri = {5};`
   - C) `int[] numeri = new int[5];`
   - D) `int numeri[5];`

5. Qual è la differenza tra `Arrays.toString()` e `Arrays.deepToString()`?
   - A) `Arrays.toString()` è usato per array di stringhe, mentre `Arrays.deepToString()` è usato per array di interi.
   - B) `Arrays.toString()` è usato per array unidimensionali, mentre `Arrays.deepToString()` è usato per array multidimensionali.
   - C) `Arrays.toString()` restituisce una rappresentazione in formato JSON, mentre `Arrays.deepToString()` restituisce una rappresentazione XML.
   - D) Non esiste una differenza; entrambi i metodi funzionano allo stesso modo.

6. **Vero o Falso**: `Arrays.binarySearch()` può essere utilizzato su un array non ordinato.
   - A) Vero
   - B) Falso

### Sezione 2: ArrayList
7. Quale metodo di `ArrayList` restituisce il numero di elementi contenuti nella lista?
   - A) `length()`
   - B) `getSize()`
   - C) `size()`
   - D) `count()`

8. Qual è la sintassi corretta per creare un `ArrayList` di stringhe in Java?
   - A) `ArrayList<String> lista = new ArrayList<String>();`
   - B) `ArrayList lista = new ArrayList<String>();`
   - C) `ArrayList<String> lista = new ArrayList<>();`
   - D) Entrambe A e C

9. Come si aggiunge un elemento alla fine di un `ArrayList`?
   - A) `add(elemento)`
   - B) `append(elemento)`
   - C) `insert(elemento)`
   - D) `put(elemento)`

10. Quale metodo viene utilizzato per rimuovere un elemento da una posizione specifica in un `ArrayList`?
    - A) `delete(index)`
    - B) `remove(index)`
    - C) `erase(index)`
    - D) `discard(index)`

11. Quale affermazione è corretta riguardo l’utilizzo di `ArrayList` rispetto agli array?
    - A) Gli `ArrayList` possono contenere tipi primitivi direttamente.
    - B) La dimensione degli `ArrayList` è fissa dopo la dichiarazione.
    - C) Gli `ArrayList` permettono l’aggiunta e la rimozione dinamica degli elementi.
    - D) Gli `ArrayList` sono più efficienti in termini di memoria rispetto agli array.

### Sezione 3: Array Multidimensionali
12. Qual è l’output del seguente codice?
    ```java
    int[][] matrice = {{1, 2}, {3, 4}};
    System.out.println(matrice[1][1]);
    ```
    - A) 1
    - B) 2
    - C) 3
    - D) 4

13. Quale dei seguenti metodi può essere utilizzato per copiare solo una parte di un array in Java?
    - A) `copyOf()`
    - B) `copyOfRange()`
    - C) `clone()`
    - D) `subArray()`

14. Qual è la differenza principale tra il metodo `copyOf()` e il metodo `copyOfRange()` della classe `Arrays`?
    - A) `copyOf()` permette di copiare array multidimensionali, mentre `copyOfRange()` no.
    - B) `copyOf()` copia l'intero array, mentre `copyOfRange()` copia solo una parte specificata dell’array.
    - C) `copyOf()` è più veloce di `copyOfRange()`.
    - D) `copyOfRange()` può essere utilizzato solo con array di interi.

### Sezione 4: Introduzione alle Collezioni
15. Quale di queste caratteristiche è vera per una `ArrayList`?
    - A) È di dimensione fissa
    - B) Supporta l'accesso agli elementi tramite indice
    - C) Non permette valori duplicati
    - D) È più efficiente di un array in termini di prestazioni

16. Qual è il metodo utilizzato per verificare se un `ArrayList` contiene un elemento specifico?
    - A) `exists()`
    - B) `contains()`
    - C) `has()`
    - D) `find()`

17. **Vero o Falso**: È possibile creare un `ArrayList` di tipi primitivi in Java (come `int` o `double`).
    - A) Vero
    - B) Falso

18. Qual è l’output del seguente codice?
    ```java
    ArrayList<Integer> numeri = new ArrayList<>();
    numeri.add(10);
    numeri.add(20);
    numeri.add(30);
    numeri.set(1, 25);
    System.out.println(numeri);
    ```
    - A) `[10, 20, 30]`
    - B) `[10, 25, 30]`
    - C) `[10, 30, 25]`
    - D) `[10, 20, 25]`

---

## Chiave delle Risposte

1. B  
2. B  
3. A  
4. C  
5. B  
6. B  
7. C  
8. D  
9. A  
10. B  
11. C  
12. D  
13. B  
14. B  
15. B  
16. B  
17. B  
18. B  

[12-ArrayList](12-ArrayList.md) - [INDICE](README.md) - [14-Panoramica del Framework delle Collezioni di Java.md](14-Panoramica%20del%20Framework%20delle%20Collezioni%20di%20Java.md)
