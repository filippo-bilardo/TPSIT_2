## 2 Array
Un **array** in Java è una collezione di elementi dello stesso tipo, organizzati in una sequenza ordinata e accessibili tramite un indice numerico. Questa struttura dati permette di gestire facilmente insiemi di dati correlati, come liste di numeri, caratteri o oggetti, senza dover creare variabili separate per ciascun elemento. Una volta creato, un array ha una dimensione fissa che non può essere modificata, il che rende gli array altamente efficienti in termini di utilizzo della memoria, ma meno flessibili rispetto ad altre strutture dati.

Ogni elemento di un array è identificato da un indice, partendo da 0 per il primo elemento fino a `n-1` per l’ultimo, dove `n` è la dimensione dell'array. La struttura sequenziale degli array li rende ideali per applicazioni dove la gestione della memoria e la velocità di accesso ai dati sono fondamentali, come algoritmi di ordinamento o gestione di grandi quantità di dati in memoria.

### Dichiarazione e creazione di array
In Java, un array deve essere dichiarato e inizializzato prima dell’uso. La dichiarazione crea una variabile array, specificando il tipo degli elementi che conterrà, ma non crea lo spazio per i valori stessi. Per esempio:
```java
int[] numeri;   // Dichiarazione di un array di interi
```
L'istruzione sopra crea una variabile `numeri` che può contenere un array di interi, ma non riserva ancora alcuno spazio in memoria per i valori.

Quando in ciascuna dichiarazione viene dichiarata una sola variabile, le parentesi quadre possono essere inserite dopo il tipo o dopo il nome della variabile
```java
int numeri[];
```

Dopo la dichiarazione, è necessario creare l’array con la dimensione desiderata tramite la parola chiave `new`, specificando il numero di elementi che l'array dovrà contenere:
```java
numeri = new int[5];  // Crea un array di 5 interi inizializzati a zero
```
In un'unica linea, possiamo anche dichiarare e creare l’array:
```java
int[] valori = new int[10];  // Dichiarazione e creazione di un array di 10 interi
```
Ogni elemento dell’array `valori` è automaticamente inizializzato al valore predefinito per il tipo `int`, ovvero zero.

In alternativa, Java permette di inizializzare un array con valori specifici al momento della dichiarazione:
```java
int[] numeriIniziali = {1, 2, 3, 4, 5};  // Array inizializzato con valori specifici
```
Con questo approccio, l’array `numeriIniziali` contiene esattamente cinque elementi con i valori specificati. La dimensione dell'array è determinata automaticamente dal numero di valori forniti tra le parentesi graffe `{}`.

Attraverso queste diverse modalità di dichiarazione e creazione, Java consente di scegliere l’approccio più adatto per gestire array sia statici, per dati costanti, sia dinamici, per configurare i dati al momento della creazione.

### Array di Tipi Non Primitivi
Oltre ai tipi primitivi, gli array possono memorizzare oggetti:
```java
String[] nomi = new String[3];  // Array di stringhe
```

### Accesso agli Elementi
Gli elementi di un array sono accessibili tramite l'indice, che parte sempre da 0. Per esempio, per accedere al primo elemento di un array:

```java
int primoElemento = numeri[0];
```

Per modificare un elemento specifico:

```java
numeri[2] = 10; // Cambia il terzo elemento a 10
```

### Caratteristiche degli Array in Java
- **Indice fisso**: La dimensione dell'array è fissa una volta dichiarata. Non può essere cambiata dinamicamente.
- **Omogeneità**: Tutti gli elementi devono essere dello stesso tipo.
- **Indice base**: Gli array in Java sono indicizzati a partire da 0.
- **Memoria continua**: Gli elementi sono memorizzati in posizioni di memoria adiacenti, permettendo un accesso rapido.

### Vantaggi degli Array
- **Accesso rapido**: L'accesso a un elemento è un'operazione costante, O(1), grazie all'indicizzazione diretta.
- **Efficienza di memoria**: Gli array non hanno overhead di memoria aggiuntivo come altre strutture dati.

### Limitazioni degli Array
- **Dimensione fissa**: Una volta creato, l'array non può essere ridimensionato, il che può portare a sprechi di memoria o alla necessità di copiare i dati in un nuovo array più grande.
- **Gestione complessa**: Operazioni come l'inserimento o la rimozione di elementi sono meno flessibili e richiedono la manipolazione diretta degli indici.

[01-Introduzione](01-Introduzione.md) - [INDICE](README.md) - [03-Esempi](03-Esempi.md)