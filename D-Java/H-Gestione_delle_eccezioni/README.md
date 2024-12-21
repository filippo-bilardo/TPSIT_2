## H - Gestione delle eccezioni
### Esercitazioni
### Esercitazioni
- [ES01](<>)

### Teoria
   - [01.1 Introduzione alla Gestione delle Eccezioni](<01.1 Introduzione alla Gestione delle Eccezioni.md>) 
      - Concetti di base delle eccezioni: cosa sono e perché sono necessarie.
      - Differenze tra errori ed eccezioni.
      - Architettura delle eccezioni in Java: `Throwable`, `Exception` ed `Error`.
      - Esempi introduttivi di eccezioni comuni in Java (ad esempio, `NullPointerException`, `ArrayIndexOutOfBoundsException`).
   - [01.2 Uso dei Blocchi try, catch, finally](<01.2 Uso dei Blocchi try, catch, finally.md>) 
      - Sintassi e struttura dei blocchi `try` e `catch`.
      - Utilizzo del blocco `finally` per operazioni di pulizia.
      - Gestione di eccezioni multiple: blocchi multipli `catch`.
      - Esempi pratici di gestione delle eccezioni con risorse esterne (ad esempio, file o connessioni di rete).
   - [01.3 Gerarchia delle Eccezioni e Personalizzazione](<01.3 Gerarchia delle Eccezioni e Personalizzazione.md>) 
      - Gerarchia delle classi delle eccezioni: checked vs unchecked.
      - Creazione di eccezioni personalizzate: estendere la classe `Exception` o `RuntimeException`.
      - Quando usare eccezioni personalizzate.
      - Esempi di utilizzo pratico di eccezioni personalizzate in progetti reali.
   - [01.4 Lanciare e Propagare Eccezioni](<01.4 Lanciare e Propagare Eccezioni.md>) 
      - Utilizzo della parola chiave `throw` per lanciare eccezioni.
      - La parola chiave `throws` e la propagazione delle eccezioni.
      - Gestione delle eccezioni nelle catene di chiamate a metodi.
      - Esempi di propagazione in applicazioni multi-livello.
   - [01.5 Gestione Avanzata delle Eccezioni](<01.5 Gestione Avanzata delle Eccezioni.md>) 
      - Best practice per la gestione delle eccezioni (ad esempio, evitare eccezioni generiche).
      - Uso delle risorse con il blocco `try-with-resources`.
      - Differenza tra gestione preventiva e gestione correttiva.
      - Logging e tracciamento delle eccezioni in applicazioni di grandi dimensioni.
   - [01.6 Esercitazioni Pratiche](<01.6 Esercitazioni Pratiche.md>) 
      - Risolvere problemi comuni usando eccezioni.
      - Progettare un'applicazione robusta che include:
      - Validazione dell'input utente.
      - Gestione di errori durante la lettura/scrittura di file.
      - Connessioni a database con gestione delle transazioni.
      - Implementare eccezioni personalizzate per un caso d'uso specifico.

--- 
[INDICE](../README.md) 