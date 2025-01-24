## H - Gestione delle eccezioni
### Esercitazioni
### Esercitazioni
- [ES01](<>)

### Teoria
1. **Introduzione**
  - [01.0 Introduzione alla Gestione delle Eccezioni](<01.0 Introduzione alla Gestione delle Eccezioni.md>)
  - [01.1 Concetti di Base delle Eccezioni](<01.1 Concetti di Base delle Eccezioni.md>)
  - [01.2 Differenze tra Errori ed Eccezioni](<01.2 Differenze tra Errori ed Eccezioni.md>)
  - [01.3 Architettura delle eccezioni in Java](<01.3 Architettura delle eccezioni in Java.md>)
  - [01.4 Esempi di Eccezioni Comuni in Java](<1.4 Esempi di Eccezioni Comuni in Java.md>)
  - [01.5 Meccanismo di gestione delle eccezioni da parte della JVM](<01.5 Meccanismo di gestione delle eccezioni da parte della JVM.md>)

2. **I Blocchi try, catch, finally**
  - [02.0 Uso dei Blocchi try, catch, finally](<02.0 Uso dei Blocchi try, catch, finally.md>) 
  - [02.1 Sintassi e struttura dei blocchi try e catch](<02.1 Sintassi e struttura dei blocchi try e catch.md>) 
  - [02.2 Utilizzo del blocco finally](<02.2 Utilizzo del blocco finally.md>) 
  - [02.3 Gestione di eccezioni multiple](<02.3 Gestione di eccezioni multiple.md>) 
  - [02.4 Buone Pratiche nella Gestione dei Blocchi try-catch-finally](<02.4 Buone Pratiche nella Gestione dei Blocchi try-catch-finally.md>) 
  - [02.5 Esempi pratici di gestione delle eccezioni](<02.5 Esempi pratici di gestione delle eccezioni.md>)
  - [02.6 Quiz di autovalutazione](<02.6 Quiz di autovalutazione.md>)
  - [02.7 Esercitazioni pratiche](<02.7 Esercitazioni pratiche.md>)

3. **Gerarchia delle classi delle eccezioni**
  - [03.1 Gerarchia delle classi delle eccezioni](<03.1 Gerarchia delle classi delle eccezioni.md>) 
  - Creazione di eccezioni personalizzate: estendere la classe `Exception` o `RuntimeException`.
  - Quando usare eccezioni personalizzate.
  - Esempi di utilizzo pratico di eccezioni personalizzate in progetti reali.

4. **Lanciare e Propagare Eccezioni**
  - [01.4 Lanciare e Propagare Eccezioni](<01.4 Lanciare e Propagare Eccezioni.md>) 
  - Utilizzo della parola chiave `throw` per lanciare eccezioni.
  - La parola chiave `throws` e la propagazione delle eccezioni.
  - Gestione delle eccezioni nelle catene di chiamate a metodi.
  - Esempi di propagazione in applicazioni multi-livello.

5. **Gestione Avanzata delle Eccezioni**
  - [01.5 Gestione Avanzata delle Eccezioni](<01.5 Gestione Avanzata delle Eccezioni.md>) 
  - Best practice per la gestione delle eccezioni (ad esempio, evitare eccezioni generiche).
  - Uso delle risorse con il blocco `try-with-resources`.
  - Differenza tra gestione preventiva e gestione correttiva.
  - Logging e tracciamento delle eccezioni in applicazioni di grandi dimensioni.
  - Gestione delle eccezioni multi-livello: Lancio di eccezioni controllate; Gestione delle eccezioni a più livelli
  - [02.8 Risorsa Automatica di Chiusura (try-with-resources)](<05.8 Risorsa Automatica di Chiusura (try-with-resources).md>)

6. **Esercitazioni Pratiche**
  - [01.6 Esercitazioni Pratiche](<01.6 Esercitazioni Pratiche.md>) 
  - Risolvere problemi comuni usando eccezioni.
  - Progettare un'applicazione robusta che include:
    - Validazione dell'input utente.
    - Gestione di errori durante la lettura/scrittura di file.
    - Connessioni a database con gestione delle transazioni.
  - Implementare eccezioni personalizzate per un caso d'uso specifico.

--- 
[INDICE](../README.md) 