Confronto tra la classe `java.util.Random` e il metodo statico `Math.random()` 

### Classe `java.util.Random`

La classe `Random` è un generatore di numeri pseudo-casuali più completo e flessibile. Ecco le sue principali caratteristiche:

1. **Costruzione e Inizializzazione**
   ```java
   Random random = new Random(); // Usa un seme generato automaticamente
   Random random = new Random(long seed); // Usa un seme specifico
   ```

2. **Metodi per generare numeri casuali**
   - `nextInt()`: Genera un intero casuale
   - `nextInt(int bound)`: Genera un intero casuale tra 0 e bound-1
   - `nextDouble()`: Genera un double tra 0.0 e 1.0
   - `nextFloat()`: Genera un float tra 0.0 e 1.0
   - `nextBoolean()`: Genera un valore booleano casuale
   - `nextLong()`: Genera un long casuale

Esempio di utilizzo:
```java
Random random = new Random();
int randomNumber = random.nextInt(100); // Numero casuale tra 0 e 99
```

### Metodo `Math.random()`

`Math.random()` è un metodo statico più semplice che genera un numero casuale:

1. **Caratteristiche**
   - Restituisce sempre un `double`
   - Intervallo: da 0.0 (incluso) a 1.0 (escluso)
   - Implementato internamente usando `Random`

Esempio di utilizzo:
```java
double randomValue = Math.random(); // Numero tra 0.0 e 1.0
int randomInt = (int)(Math.random() * 100); // Numero tra 0 e 99
```

### Differenze Principali

1. **Flessibilità**
   - `Random`: Più flessibile, offre metodi per diversi tipi di numeri
   - `Math.random()`: Limitato ai double tra 0 e 1

2. **Controllo del Seme**
   - `Random`: Permette di impostare un seme specifico
   - `Math.random()`: Seme gestito internamente

3. **Performance**
   - `Random`: Leggermente più efficiente per generazioni multiple
   - `Math.random()`: Comodo per usi occasionali

### Raccomandazioni

- Usa `Random` per applicazioni che richiedono generazione ripetuta di numeri
- Usa `Math.random()` per casi semplici e rapidi
- Per applicazioni critiche o che richiedono alta qualità di casualità, considera `SecureRandom`

### Esempio Completo

```java
import java.util.Random;

public class RandomExample {
    public static void main(String[] args) {
        // Usando Random
        Random random = new Random();
        System.out.println("Numero intero casuale: " + random.nextInt(100));
        
        // Usando Math.random()
        int randomInt = (int)(Math.random() * 100);
        System.out.println("Numero intero con Math.random(): " + randomInt);
    }
}
```
