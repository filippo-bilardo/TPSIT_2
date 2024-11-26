## 8 Array Multidimensionali

In Java, gli **array multidimensionali** sono array che contengono più dimensioni, come una griglia di righe e colonne in una tabella o in una matrice. Gli array bidimensionali, che sono i più comuni, possono essere rappresentati come un array di array. Tuttavia, Java consente anche la creazione di array di dimensioni superiori, ad esempio array tridimensionali e oltre.

### Dichiarazione di un Array Multidimensionale

Un array bidimensionale può essere dichiarato specificando il tipo e utilizzando due set di parentesi quadre `[][]`, uno per ogni dimensione. La sintassi è la seguente:

```java
tipo[][] nomeArray = new tipo[righe][colonne];
```

Ad esempio, per creare una matrice 3x4 di interi:

```java
int[][] matrice = new int[3][4];
```

Questa dichiarazione crea un array bidimensionale con 3 righe e 4 colonne, dove ogni elemento è inizializzato al valore predefinito di `0` per il tipo `int`.

### Inizializzazione di un Array Bidimensionale

Un array bidimensionale può essere inizializzato anche al momento della dichiarazione, specificando i valori per ogni elemento tra parentesi graffe `{}`:

```java
int[][] matrice = {
    {1, 2, 3, 4},
    {5, 6, 7, 8},
    {9, 10, 11, 12}
};
```

In questo caso, la variabile `matrice` rappresenta una tabella di interi 3x4, con ogni riga contenente quattro elementi.

### Accesso agli Elementi di un Array Multidimensionale

Gli elementi di un array bidimensionale sono accessibili specificando la riga e la colonna desiderate. L’indice di riga viene indicato per primo, seguito dall’indice di colonna:

```java
int valore = matrice[1][2]; // Accede all'elemento nella seconda riga, terza colonna
```

In questo esempio, `valore` conterrà `7`, poiché `matrice[1][2]` rappresenta l'elemento nella seconda riga e terza colonna della matrice inizializzata in precedenza.

### Iterare su un Array Multidimensionale

Per scorrere tutti gli elementi di un array bidimensionale, si può utilizzare un ciclo `for` annidato. Il primo ciclo `for` attraversa le righe, mentre il secondo ciclo `for` attraversa le colonne di ciascuna riga.

```java
for (int i = 0; i < matrice.length; i++) {
    for (int j = 0; j < matrice[i].length; j++) {
        System.out.print(matrice[i][j] + " ");
    }
    System.out.println();
}
```

In questo esempio, ogni elemento dell’array `matrice` viene stampato in forma tabellare.

### Array Irregolari (Array a Denti di Sega)

In Java, è possibile creare array multidimensionali irregolari, detti anche **array a denti di sega** (*jagged arrays*), dove ogni riga può avere una lunghezza diversa. Ad esempio:

```java
int[][] jaggedArray = new int[3][];
jaggedArray[0] = new int[2]; // Prima riga con 2 elementi
jaggedArray[1] = new int[3]; // Seconda riga con 3 elementi
jaggedArray[2] = new int[4]; // Terza riga con 4 elementi
```

Questo codice crea un array bidimensionale in cui la prima riga ha 2 colonne, la seconda 3 colonne e la terza 4 colonne. Gli array irregolari offrono flessibilità, ma richiedono maggiore attenzione durante l'accesso agli elementi.

### Array Multidimensionali di Ordine Superiore

Java permette di dichiarare e utilizzare array multidimensionali con più di due dimensioni, come array tridimensionali. Ad esempio, un array tridimensionale potrebbe essere usato per rappresentare un cubo di valori:

```java
int[][][] cubo = new int[3][3][3];
```

Qui, `cubo` rappresenta una struttura tridimensionale 3x3x3. Per accedere a un elemento specifico, si utilizzano tre indici, uno per ogni dimensione:

```java
cubo[0][1][2] = 10; // Assegna il valore 10 al livello 0, riga 1, colonna 2
```

### Esempio Completo: Somma degli Elementi di una Matrice

Nel seguente esempio, calcoliamo la somma di tutti gli elementi di un array bidimensionale.

```java
public class SommaMatrice {
    public static void main(String[] args) {
        int[][] matrice = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        int somma = 0;
        for (int i = 0; i < matrice.length; i++) {
            for (int j = 0; j < matrice[i].length; j++) {
                somma += matrice[i][j];
            }
        }

        System.out.println("La somma degli elementi della matrice è: " + somma);
    }
}
```

**Output**:
```
La somma degli elementi della matrice è: 45
```

In questo esempio, utilizziamo due cicli `for` per attraversare ogni elemento della matrice e sommarli tutti insieme. La variabile `somma` contiene il risultato finale.

### Conclusione
Gli array multidimensionali in Java permettono di rappresentare strutture di dati complesse come matrici, tabelle, cubi e altre forme di dati multi-livello. Sebbene la gestione degli array multidimensionali richieda cicli annidati e una sintassi di accesso più complessa, essi sono strumenti potenti per l’organizzazione di dati in applicazioni avanzate.

[07-Array di oggetti](07-Array%20di%20oggetti.md) - [INDICE](README.md) - [09-Passaggio di array come argomenti](09-Passaggio%20di%20array%20come%20argomenti.md)