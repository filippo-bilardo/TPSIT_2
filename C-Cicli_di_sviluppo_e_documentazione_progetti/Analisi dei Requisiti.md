### Guida: Analisi dei Requisiti e Documento di Specifiche Tecniche

L'**Analisi dei Requisiti** e la creazione del **Documento di Specifiche Tecniche** sono fasi fondamentali nel ciclo di sviluppo del software. L'obiettivo è comprendere a fondo cosa il cliente desidera e tradurre queste necessità in un documento formale che guidi tutte le fasi successive dello sviluppo.

---

## 1. **Analisi dei Requisiti**

L'analisi dei requisiti è il processo attraverso il quale si identificano e documentano le funzionalità e le caratteristiche richieste dal software. Esistono due tipi principali di requisiti:

### 1.1 Requisiti Funzionali
I requisiti funzionali descrivono **cosa** deve fare il software. Si concentrano sulle funzionalità che devono essere presenti nel sistema e sul modo in cui interagiranno con l'utente o altri sistemi.

#### Esempi di requisiti funzionali:
- L'utente deve poter registrarsi inserendo nome, cognome, email e password.
- Il sistema deve inviare una mail di conferma all'utente dopo la registrazione.
- L'applicazione deve calcolare il totale della spesa in base ai prodotti aggiunti.

### 1.2 Requisiti Non Funzionali
I requisiti non funzionali descrivono **come** il sistema deve comportarsi. Si concentrano su aspetti come le prestazioni, la sicurezza, la manutenibilità e l'usabilità.

#### Esempi di requisiti non funzionali:
- Il tempo di risposta per l'inserimento dei dati deve essere inferiore a 2 secondi.
- Il sistema deve garantire un uptime del 99,9%.
- Tutte le password devono essere salvate con un algoritmo di hashing sicuro.

### 1.3 Raccolta dei Requisiti
La fase di raccolta dei requisiti coinvolge diversi strumenti e metodi per ottenere informazioni dai clienti, dagli stakeholder e dagli utenti finali. Tra i più comuni:
- **Interviste**: Conversazioni individuali con i clienti e gli utenti per raccogliere esigenze e aspettative.
- **Workshop**: Riunioni di gruppo per discutere i requisiti e trovare soluzioni collaborative.
- **Questionari**: Distribuzione di domande per ottenere feedback dettagliati su particolari aspetti del progetto.
- **Analisi di Sistemi Esistenti**: Valutazione di software simili o precedenti per comprendere funzionalità da includere o evitare.

---

## 2. **Creazione del Documento di Specifiche Tecniche (SRS)**

Il **Documento di Specifiche dei Requisiti del Software** (SRS - Software Requirement Specification) è un documento formale che descrive in dettaglio tutte le funzionalità e le caratteristiche che il software deve avere. Serve come contratto tra il team di sviluppo e gli stakeholder.

### 2.1 Struttura del Documento SRS

Di seguito un esempio della struttura tipica di un documento di specifica tecnica:

#### 2.1.1 Introduzione
In questa sezione viene presentato il progetto e gli obiettivi principali del software. Include:
- **Scopo del documento**: Una breve descrizione del perché viene redatto questo documento e a chi è destinato.
- **Ambito del progetto**: Panoramica delle funzionalità principali del sistema.
- **Obiettivi del sistema**: Gli obiettivi principali che il software deve raggiungere.

#### 2.1.2 Descrizione Generale
Questa sezione fornisce una visione d'insieme del sistema e delle sue componenti principali. Può includere:
- **Panoramica del sistema**: Descrizione generale del sistema e della sua interazione con l'ambiente esterno.
- **Ambiente Operativo**: Le piattaforme hardware e software su cui il sistema funzionerà (es. sistemi operativi, linguaggi di programmazione).
- **Utenti target**: Elenco dei diversi tipi di utenti che interagiranno con il sistema.

#### 2.1.3 Requisiti Funzionali
Ogni requisito funzionale dovrebbe essere elencato e descritto in dettaglio, indicando il comportamento atteso del sistema. È buona pratica numerare i requisiti per riferimento futuro.

##### Esempio:
- **RF1: Registrazione Utente**
    - **Descrizione**: L'utente deve poter creare un nuovo account inserendo i seguenti dati: nome, cognome, email e password.
    - **Precondizione**: L'utente non deve essere già registrato nel sistema.
    - **Postcondizione**: L'account viene creato e una mail di conferma viene inviata all'utente.
    - **Attori**: Utente.

#### 2.1.4 Requisiti Non Funzionali
Elenco dei requisiti non funzionali che riguardano il comportamento del sistema, come prestazioni, sicurezza, manutenibilità e altro.

##### Esempio:
- **RNF1: Prestazioni**
    - **Descrizione**: Il sistema deve rispondere alle richieste dell'utente in meno di 2 secondi durante le ore di punta.
    - **Obiettivo**: Garantire una buona esperienza utente anche durante periodi di alta richiesta.

#### 2.1.5 Casi d’Uso (Use Case)
Un caso d’uso è una rappresentazione dettagliata di una funzione del sistema, descrivendo i passi che un attore (utente o sistema) compie per completare un'azione.

##### Esempio:
- **Caso d’Uso: Aggiunta di un Prodotto al Carrello**
    - **Attori principali**: Utente.
    - **Descrizione**: L'utente seleziona un prodotto e lo aggiunge al carrello durante la fase di acquisto.
    - **Precondizione**: L'utente deve essere autenticato.
    - **Postcondizione**: Il prodotto è visibile nel carrello e il prezzo totale viene aggiornato.

#### 2.1.6 Vincoli e Assunzioni
In questa sezione si elencano i vincoli tecnici o normativi che influenzano lo sviluppo del sistema, come i limiti di piattaforma o i requisiti legali, e le assunzioni su cui il progetto si basa.

##### Esempio:
- Il sistema deve supportare browser moderni come Chrome, Firefox, Safari e Edge.
- Si assume che l'utente medio abbia familiarità con l'uso delle app mobili.

#### 2.1.7 Glossario
Il glossario fornisce una spiegazione di termini tecnici o specifici usati nel documento.

---

## 3. **Best Practices per la Scrittura del Documento SRS**

### 3.1 Linguaggio Chiaro e Conciso
Il documento deve essere scritto in un linguaggio chiaro e privo di ambiguità. Ogni requisito deve essere descritto in modo preciso, con informazioni sufficienti a guidare lo sviluppo.

### 3.2 Usare uno Standard
Solitamente, le organizzazioni seguono standard come **IEEE 830** per la redazione del documento SRS. Utilizzare uno standard garantisce coerenza e completezza.

### 3.3 Requisiti Verificabili
Ogni requisito deve essere misurabile e verificabile. È importante che un tester possa confermare se un requisito è stato soddisfatto o meno.

### 3.4 Aggiornamenti e Versionamento
Il documento di specifiche tecniche non è statico. Deve essere aggiornato regolarmente per riflettere eventuali cambiamenti nei requisiti. Utilizzare il controllo di versione per tracciare i cambiamenti.

---

## 4. **Conclusione**

L'**analisi dei requisiti** e la stesura del **documento di specifiche tecniche** sono cruciali per la riuscita di un progetto software. Un documento SRS ben redatto aiuta a prevenire fraintendimenti e a garantire che tutte le parti coinvolte abbiano una visione chiara di ciò che deve essere realizzato.

---

[INDICE](README.md)