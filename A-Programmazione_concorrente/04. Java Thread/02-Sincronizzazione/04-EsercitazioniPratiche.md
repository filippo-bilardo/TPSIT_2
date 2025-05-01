### **Esercitazioni Pratiche sulla Sincronizzazione dei Thread**

---

### **1. Esercizio: Contatore Thread-Safe**

**Obiettivo:** Implementare un contatore thread-safe utilizzando diversi meccanismi di sincronizzazione.

#### **Istruzioni:**
1. Creare una classe `ContatoreNonSicuro` con:
   - Un attributo `valore` (int) inizializzato a 0.
   - Un metodo `incrementa()` che aumenta il valore di 1.
   - Un metodo `getValore()` che restituisce il valore corrente.
2. Creare una classe `ContatoreSincronizzato` che estende `ContatoreNonSicuro`:
   - Sovrascrivere il metodo `incrementa()` utilizzando la keyword `synchronized`.
   - Sovrascrivere il metodo `getValore()` utilizzando la keyword `synchronized`.
3. Creare una classe `ContatoreLock` che estende `ContatoreNonSicuro`:
   - Aggiungere un attributo privato di tipo `Object` chiamato `lock`.
   - Sovrascrivere il metodo `incrementa()` utilizzando un blocco `synchronized(lock)`.
   - Sovrascrivere il metodo `getValore()` utilizzando un blocco `synchronized(lock)`.
4. Nel metodo `main`:
   - Creare un'istanza di ciascuna classe di contatore.
   - Per ogni contatore, creare e avviare 5 thread che incrementano il contatore 1000 volte ciascuno.
   - Attendere che tutti i thread completino l'esecuzione.
   - Stampare il valore finale di ciascun contatore e verificare se corrisponde al valore atteso (5000).

**Esempio di Output:**
```
Test ContatoreNonSicuro:
Valore atteso: 5000
Valore effettivo: 4328 (risultato non corretto a causa di race condition)

Test ContatoreSincronizzato:
Valore atteso: 5000
Valore effettivo: 5000 (risultato corretto)

Test ContatoreLock:
Valore atteso: 5000
Valore effettivo: 5000 (risultato corretto)
```

---

### **2. Esercizio: Simulazione di un Conto Bancario**

**Obiettivo:** Implementare un sistema di gestione di un conto bancario thread-safe.

#### **Istruzioni:**
1. Creare una classe `ContoBancario` con:
   - Un attributo `saldo` (double) inizializzato con un valore fornito nel costruttore.
   - Un metodo `deposita(double importo)` che aumenta il saldo.
   - Un metodo `preleva(double importo)` che diminuisce il saldo se c'è disponibilità, altrimenti stampa un messaggio di errore.
   - Un metodo `getSaldo()` che restituisce il saldo corrente.
   - Tutti i metodi devono essere sincronizzati.
2. Creare una classe `Cliente` che implementi `Runnable`:
   - La classe deve ricevere un nome, un'istanza di `ContoBancario` e un array di operazioni (depositi e prelievi) nel costruttore.
   - Nel metodo `run()`, eseguire tutte le operazioni sul conto bancario.
3. Nel metodo `main`:
   - Creare un'istanza di `ContoBancario` con un saldo iniziale di 1000€.
   - Creare due clienti con diverse operazioni da eseguire sullo stesso conto.
   - Avviare i thread dei clienti e attendere il loro completamento.
   - Stampare il saldo finale del conto.

**Esempio di Output:**
```
Saldo iniziale: 1000.0€
Cliente Mario: deposito di 200.0€ effettuato. Nuovo saldo: 1200.0€
Cliente Giulia: prelievo di 150.0€ effettuato. Nuovo saldo: 1050.0€
Cliente Mario: prelievo di 500.0€ effettuato. Nuovo saldo: 550.0€
Cliente Giulia: deposito di 300.0€ effettuato. Nuovo saldo: 850.0€
Cliente Mario: prelievo di 900.0€ fallito. Saldo insufficiente (850.0€)
Cliente Giulia: prelievo di 400.0€ effettuato. Nuovo saldo: 450.0€
Saldo finale: 450.0€
```

---

### **3. Esercizio: Produttore-Consumatore con Wait/Notify**

**Obiettivo:** Implementare il classico problema del produttore-consumatore utilizzando i metodi `wait()` e `notify()`.

#### **Istruzioni:**
1. Creare una classe `Buffer` con:
   - Un attributo `dato` (int) che rappresenta il dato condiviso.
   - Un attributo booleano `disponibile` inizializzato a `false`.
   - Un metodo sincronizzato `produci(int valore)` che:
     - Attende (usando `wait()`) se `disponibile` è `true`.
     - Imposta `dato` al valore fornito.
     - Imposta `disponibile` a `true`.
     - Notifica i thread in attesa con `notify()`.
   - Un metodo sincronizzato `consuma()` che:
     - Attende (usando `wait()`) se `disponibile` è `false`.
     - Legge e restituisce il valore di `dato`.
     - Imposta `disponibile` a `false`.
     - Notifica i thread in attesa con `notify()`.
2. Creare una classe `Produttore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Buffer` nel costruttore.
   - Nel metodo `run()`, produrre 10 valori interi e inserirli nel buffer con una pausa tra ciascuno.
3. Creare una classe `Consumatore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Buffer` nel costruttore.
   - Nel metodo `run()`, consumare 10 valori dal buffer con una pausa tra ciascuno.
4. Nel metodo `main`:
   - Creare un'istanza di `Buffer`.
   - Creare e avviare un thread produttore e un thread consumatore.
   - Attendere che entrambi i thread completino l'esecuzione.

**Esempio di Output:**
```
Produttore: prodotto valore 1
Consumatore: consumato valore 1
Produttore: prodotto valore 2
Consumatore: consumato valore 2
...
Produttore: prodotto valore 10
Consumatore: consumato valore 10
```

---

### **4. Esercizio: Simulazione di Deadlock**

**Obiettivo:** Creare una situazione di deadlock e poi risolverla.

#### **Istruzioni:**
1. Creare una classe `Risorsa` con:
   - Un attributo `nome` (String) inizializzato nel costruttore.
   - Un metodo `usa()` che stampa un messaggio indicando che la risorsa è in uso.
2. Creare una classe `ThreadConDeadlock` che implementi `Runnable`:
   - La classe deve ricevere due istanze di `Risorsa` nel costruttore.
   - Nel metodo `run()`, acquisire il lock sulla prima risorsa, attendere un po', poi tentare di acquisire il lock sulla seconda risorsa.
   - Dopo aver acquisito entrambi i lock, utilizzare entrambe le risorse e rilasciare i lock.
3. Nel metodo `main`:
   - Creare due istanze di `Risorsa` ("Risorsa A" e "Risorsa B").
   - Creare due thread che tentano di acquisire le risorse in ordine opposto.
   - Avviare i thread e osservare il deadlock.
4. Modificare la classe `ThreadSenzaDeadlock` per evitare il deadlock:
   - Utilizzare un approccio di ordinamento delle risorse (acquisire sempre prima la risorsa con il nome "minore" in ordine alfabetico).
   - Dimostrare che con questa modifica il deadlock non si verifica più.

**Esempio di Output (con deadlock):**
```
Thread-1: acquisito lock su Risorsa A
Thread-2: acquisito lock su Risorsa B
[Il programma si blocca qui a causa del deadlock]
```

**Esempio di Output (senza deadlock):**
```
Thread-1: acquisito lock su Risorsa A
Thread-1: acquisito lock su Risorsa B
Thread-1: utilizzando Risorsa A
Thread-1: utilizzando Risorsa B
Thread-1: rilasciati i lock
Thread-2: acquisito lock su Risorsa A
Thread-2: acquisito lock su Risorsa B
Thread-2: utilizzando Risorsa A
Thread-2: utilizzando Risorsa B
Thread-2: rilasciati i lock
```

---

### **5. Esercizio: Lettori-Scrittori**

**Obiettivo:** Implementare il problema dei lettori-scrittori con priorità ai lettori.

#### **Istruzioni:**
1. Creare una classe `Database` con:
   - Un attributo `contenuto` (String) che rappresenta i dati.
   - Un attributo `lettoriAttivi` (int) inizializzato a 0.
   - Un attributo booleano `scrittoreAttivo` inizializzato a `false`.
   - Un metodo sincronizzato `iniziaLettura()` che:
     - Attende se c'è uno scrittore attivo.
     - Incrementa il contatore dei lettori.
   - Un metodo sincronizzato `fineLettura()` che decrementa il contatore dei lettori e notifica eventuali thread in attesa se non ci sono più lettori.
   - Un metodo sincronizzato `iniziaScrittura()` che:
     - Attende se ci sono lettori attivi o uno scrittore attivo.
     - Imposta `scrittoreAttivo` a `true`.
   - Un metodo sincronizzato `fineScrittura()` che imposta `scrittoreAttivo` a `false` e notifica tutti i thread in attesa.
   - Un metodo `leggi()` che restituisce il contenuto.
   - Un metodo `scrivi(String nuovoContenuto)` che aggiorna il contenuto.
2. Creare una classe `Lettore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Database` e un ID nel costruttore.
   - Nel metodo `run()`, eseguire più operazioni di lettura con pause tra ciascuna.
3. Creare una classe `Scrittore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Database` e un ID nel costruttore.
   - Nel metodo `run()`, eseguire più operazioni di scrittura con pause tra ciascuna.
4. Nel metodo `main`:
   - Creare un'istanza di `Database`.
   - Creare e avviare più thread lettori e scrittori.
   - Attendere che tutti i thread completino l'esecuzione.

**Esempio di Output:**
```
Lettore 1: inizia lettura
Lettore 2: inizia lettura
Lettore 1: legge "Contenuto iniziale"
Lettore 2: legge "Contenuto iniziale"
Lettore 1: fine lettura
Lettore 2: fine lettura
Scrittore 1: inizia scrittura
Scrittore 1: scrive "Nuovo contenuto 1"
Scrittore 1: fine scrittura
Lettore 3: inizia lettura
Lettore 3: legge "Nuovo contenuto 1"
Lettore 3: fine lettura
```

---

### **6. Esercizio: Barriera di Sincronizzazione**

**Obiettivo:** Implementare una barriera di sincronizzazione che permetta a più thread di attendere che tutti raggiungano un certo punto prima di procedere.

#### **Istruzioni:**
1. Creare una classe `Barriera` con:
   - Un attributo `soglia` (int) che rappresenta il numero di thread da attendere.
   - Un attributo `contatore` (int) inizializzato a 0.
   - Un metodo sincronizzato `attendi()` che:
     - Incrementa il contatore.
     - Se il contatore è inferiore alla soglia, mette il thread in attesa con `wait()`.
     - Se il contatore raggiunge la soglia, resetta il contatore e notifica tutti i thread in attesa con `notifyAll()`.
2. Creare una classe `LavoratoreConBarriera` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Barriera` e un ID nel costruttore.
   - Nel metodo `run()`, simulare tre fasi di lavoro, utilizzando la barriera tra ciascuna fase per sincronizzarsi con gli altri thread.
3. Nel metodo `main`:
   - Creare un'istanza di `Barriera` con una soglia pari al numero di thread.
   - Creare e avviare più thread lavoratori.
   - Attendere che tutti i thread completino l'esecuzione.

**Esempio di Output:**
```
Lavoratore 1: completata fase 1
Lavoratore 3: completata fase 1
Lavoratore 2: completata fase 1
[Tutti i lavoratori hanno raggiunto la barriera]
Lavoratore 2: completata fase 2
Lavoratore 1: completata fase 2
Lavoratore 3: completata fase 2
[Tutti i lavoratori hanno raggiunto la barriera]
Lavoratore 3: completata fase 3
Lavoratore 1: completata fase 3
Lavoratore 2: completata fase 3
[Tutti i lavoratori hanno raggiunto la barriera]
Lavoratore 1: lavoro completato
Lavoratore 2: lavoro completato
Lavoratore 3: lavoro completato
```

---

### **7. Esercizio: Pool di Risorse Limitate**

**Obiettivo:** Implementare un pool di risorse limitate che possono essere acquisite e rilasciate da più thread.

#### **Istruzioni:**
1. Creare una classe `Risorsa` con:
   - Un attributo `id` (int) inizializzato nel costruttore.
   - Un metodo `utilizza()` che simula l'utilizzo della risorsa.
2. Creare una classe `PoolRisorse` con:
   - Un array di `Risorsa` che rappresenta le risorse disponibili.
   - Una coda di risorse disponibili (può essere implementata con una `List`).
   - Un metodo sincronizzato `acquisisci()` che:
     - Attende se non ci sono risorse disponibili.
     - Restituisce una risorsa dalla coda delle disponibili.
   - Un metodo sincronizzato `rilascia(Risorsa r)` che rimette la risorsa nella coda delle disponibili e notifica i thread in attesa.
3. Creare una classe `Utilizzatore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `PoolRisorse` e un ID nel costruttore.
   - Nel metodo `run()`, acquisire una risorsa, utilizzarla, e poi rilasciarla, ripetendo l'operazione più volte.
4. Nel metodo `main`:
   - Creare un'istanza di `PoolRisorse` con un numero limitato di risorse (es. 3).
   - Creare e avviare più thread utilizzatori (es. 5).
   - Attendere che tutti i thread completino l'esecuzione.

**Esempio di Output:**
```
Utilizzatore 1: acquisita risorsa 1
Utilizzatore 2: acquisita risorsa 2
Utilizzatore 3: acquisita risorsa 3
Utilizzatore 4: in attesa di una risorsa...
Utilizzatore 5: in attesa di una risorsa...
Utilizzatore 1: rilasciata risorsa 1
Utilizzatore 4: acquisita risorsa 1
Utilizzatore 2: rilasciata risorsa 2
Utilizzatore 5: acquisita risorsa 2
```

---

### **8. Esercizio: Semaforo**

**Obiettivo:** Implementare un semaforo che limiti l'accesso a una sezione critica.

#### **Istruzioni:**
1. Creare una classe `Semaforo` con:
   - Un attributo `permessi` (int) inizializzato nel costruttore.
   - Un metodo sincronizzato `acquire()` che:
     - Attende se non ci sono permessi disponibili.
     - Decrementa il contatore dei permessi.
   - Un metodo sincronizzato `release()` che incrementa il contatore dei permessi e notifica un thread in attesa.
2. Creare una classe `SezioneCritica` con:
   - Un metodo `accedi()` che simula l'accesso a una risorsa condivisa.
3. Creare una classe `ThreadConSemaforo` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Semaforo`, un'istanza di `SezioneCritica` e un ID nel costruttore.
   - Nel metodo `run()`, acquisire il semaforo, accedere alla sezione critica, e poi rilasciare il semaforo.
4. Nel metodo `main`:
   - Creare un'istanza di `Semaforo` con un numero limitato di permessi (es. 2).
   - Creare un'istanza di `SezioneCritica`.
   - Creare e avviare più thread (es. 5).
   - Attendere che tutti i thread completino l'esecuzione.

**Esempio di Output:**
```
Thread 1: acquisito permesso
Thread 2: acquisito permesso
Thread 1: accesso alla sezione critica
Thread 3: in attesa di un permesso...
Thread 4: in attesa di un permesso...
Thread 5: in attesa di un permesso...
Thread 1: rilasciato permesso
Thread 3: acquisito permesso
Thread 2: accesso alla sezione critica
Thread 2: rilasciato permesso
Thread 4: acquisito permesso
```

---

### **9. Esercizio: Filosofi a Cena**

**Obiettivo:** Implementare il classico problema dei filosofi a cena e risolvere il potenziale deadlock.

#### **Istruzioni:**
1. Creare una classe `Forchetta` con:
   - Un attributo `id` (int) inizializzato nel costruttore.
   - Un attributo booleano `inUso` inizializzato a `false`.
   - Un metodo sincronizzato `prendi()` che attende se la forchetta è già in uso, altrimenti la imposta come in uso.
   - Un metodo sincronizzato `lascia()` che imposta la forchetta come non in uso e notifica i thread in attesa.
2. Creare una classe `Filosofo` che implementi `Runnable`:
   - La classe deve ricevere un ID, due istanze di `Forchetta` (sinistra e destra) nel costruttore.
   - Nel metodo `run()`, simulare il ciclo di vita del filosofo: pensare, prendere le forchette, mangiare, lasciare le forchette.
   - Implementare una soluzione per evitare il deadlock (es. un filosofo prende prima la forchetta con ID minore).
3. Nel metodo `main`:
   - Creare 5 istanze di `Forchetta`.
   - Creare 5 istanze di `Filosofo`, assegnando a ciascuno le forchette appropriate.
   - Avviare i thread dei filosofi e lasciarli eseguire per un certo periodo.
   - Terminare i thread e stampare statistiche (es. quante volte ciascun filosofo ha mangiato).

**Esempio di Output:**
```
Filosofo 1: sta pensando
Filosofo 2: sta pensando
Filosofo 3: sta pensando
Filosofo 4: sta pensando
Filosofo 5: sta pensando
Filosofo 1: ha preso la forchetta 1 (sinistra)
Filosofo 1: ha preso la forchetta 2 (destra)
Filosofo 1: sta mangiando
Filosofo 3: ha preso la forchetta 3 (sinistra)
Filosofo 5: ha preso la forchetta 5 (sinistra)
Filosofo 1: ha lasciato le forchette
Filosofo 1: sta pensando
Filosofo 2: ha preso la forchetta 2 (sinistra)
```

---

### **10. Esercizio: Cache Thread-Safe**

**Obiettivo:** Implementare una cache thread-safe che memorizza risultati di operazioni costose.

#### **Istruzioni:**
1. Creare un'interfaccia `Calcolatore` con:
   - Un metodo `calcola(String input)` che restituisce un risultato (String).
2. Creare una classe `CalcolatoreConRitardo` che implementa `Calcolatore`:
   - Nel metodo `calcola(String input)`, simulare un'operazione costosa con un ritardo e restituire un risultato.
3. Creare una classe `CacheThreadSafe` con:
   - Una mappa che associa input (String) a risultati (String).
   - Un metodo sincronizzato `getResult(String input, Calcolatore calc)` che:
     - Controlla se il risultato è già in cache.
     - Se presente, lo restituisce.
     - Se non presente, utilizza il calcolatore per ottenerlo, lo memorizza in cache e lo restituisce.
4. Creare una classe `ClientCache` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `CacheThreadSafe`, un'istanza di `Calcolatore` e un array di input nel costruttore.
   - Nel metodo `run()`, richiedere i risultati per ciascun input e misurare il tempo necessario.
5. Nel metodo `main`:
   - Creare un'istanza di `CalcolatoreConRitardo`.
   - Creare un'istanza di `CacheThreadSafe`.
   - Creare e avviare più thread client con gli stessi input.
   - Attendere che tutti i thread completino l'esecuzione.
   - Verificare che i risultati successivi alla prima richiesta siano ottenuti più velocemente grazie alla cache.

**Esempio di Output:**
```
Client 1: richiesto risultato per "input1"
Client 1: calcolo in corso...
Client 2: richiesto risultato per "input1"
Client 2: risultato trovato in cache
Client 1: risultato ottenuto in 2000ms
Client 2: risultato ottenuto in 5ms
Client 1: richiesto risultato per "input2"
Client 1: calcolo in corso...
Client 2: richiesto risultato per "input2"
Client 2: risultato trovato in cache
```

---

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Lock e Oggetti di Sincronizzazione](./03-LockOggetti.md)
- [➡️ Collezioni Concorrenti](../03-CollezioniConcorrenti/01-CollezioniConcorrenti.md)