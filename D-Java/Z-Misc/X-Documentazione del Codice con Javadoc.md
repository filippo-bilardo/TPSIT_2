### **X.1 Documentazione del Codice con Javadoc**

Javadoc è uno strumento standard di Java per generare automaticamente documentazione in formato HTML direttamente dal codice sorgente. Questa documentazione è derivata dai commenti strutturati inseriti nel codice e segue una sintassi specifica.

---

### **1. Importanza della Documentazione del Codice**

Una documentazione chiara e ben organizzata facilita:
- La comprensione del codice da parte di altri sviluppatori.
- La manutenzione e l’aggiornamento del software.
- La comunicazione delle intenzioni del programmatore.

Javadoc si concentra sulla descrizione delle classi, dei metodi e degli attributi di una classe Java.

---

### **2. Sintassi dei Commenti Javadoc**

I commenti Javadoc iniziano con `/**` e terminano con `*/`. Al loro interno, si utilizzano tag specifici per descrivere dettagli del codice.

#### **Esempio Base:**
```java
/**
 * Questa classe rappresenta un Rettangolo.
 * Contiene metodi per calcolare area e perimetro.
 */
public class Rettangolo {
    private int larghezza;
    private int altezza;

    /**
     * Costruttore per creare un Rettangolo.
     * @param larghezza larghezza del rettangolo
     * @param altezza altezza del rettangolo
     */
    public Rettangolo(int larghezza, int altezza) {
        this.larghezza = larghezza;
        this.altezza = altezza;
    }

    /**
     * Calcola l'area del rettangolo.
     * @return l'area del rettangolo
     */
    public int calcolaArea() {
        return larghezza * altezza;
    }

    /**
     * Calcola il perimetro del rettangolo.
     * @return il perimetro del rettangolo
     */
    public int calcolaPerimetro() {
        return 2 * (larghezza + altezza);
    }
}
```

---

### **3. Principali Tag Javadoc**

#### **a. Tag Generali**

- `@author`: Indica l'autore del codice.
  ```java
  /**
   * @author Mario Rossi
   */
  ```

- `@version`: Specifica la versione della classe o del metodo.
  ```java
  /**
   * @version 1.0
   */
  ```

#### **b. Tag per Metodi e Parametri**

- `@param`: Descrive un parametro del metodo.
  ```java
  /**
   * @param larghezza larghezza del rettangolo
   */
  ```

- `@return`: Descrive il valore restituito da un metodo.
  ```java
  /**
   * @return l'area del rettangolo
   */
  ```

- `@throws` o `@exception`: Indica le eccezioni che un metodo può lanciare.
  ```java
  /**
   * @throws IllegalArgumentException se i parametri sono negativi
   */
  ```

#### **c. Tag per Collegamenti e Riferimenti**

- `@see`: Fornisce un riferimento a un metodo, classe o altra risorsa correlata.
  ```java
  /**
   * @see java.lang.Math
   */
  ```

- `{@link}`: Aggiunge un collegamento inline a un metodo o classe.
  ```java
  /**
   * {@link Rettangolo#calcolaArea()}
   */
  ```

#### **d. Tag per Descrizioni Avanzate**

- `@deprecated`: Segnala che un metodo o classe non è più consigliato per l'uso.
  ```java
  /**
   * @deprecated Usare invece calcolaNuovaArea()
   */
  ```

---

### **4. Generazione della Documentazione con Javadoc**

#### **a. Utilizzo della Linea di Comando**

1. Posizionarsi nella directory del file sorgente.
2. Eseguire il comando:
   ```
   javadoc -d nomeCartellaOutput NomeClasse.java
   ```
   Questo comando crea una documentazione HTML nella cartella specificata.

#### **b. Strumenti IDE**

Gli IDE come IntelliJ IDEA ed Eclipse offrono strumenti integrati per generare Javadoc. Ad esempio:
- In IntelliJ IDEA: **Tools > Generate Javadoc**
- In Eclipse: **Project > Generate Javadoc**

---

### **5. Best Practice per Scrivere Javadoc**

1. **Sii Conciso ma Completo:** Fornisci informazioni utili senza essere prolisso.
2. **Usa Tag Consistenti:** Documenta sempre parametri, valori restituiti ed eccezioni.
3. **Aggiorna la Documentazione:** Mantieni i commenti sincronizzati con il codice.
4. **Evita il Superfluo:** Non documentare ciò che è ovvio dal contesto del codice.
5. **Standardizza:** Segui uno stile uniforme per tutta la documentazione del progetto.

---

### **6. Conclusione**

Javadoc è uno strumento essenziale per creare una documentazione leggibile e professionale. Utilizzando correttamente i tag e seguendo le best practice, puoi migliorare significativamente la qualità del tuo codice e facilitare la collaborazione con altri sviluppatori.

