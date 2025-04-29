### **Esercitazioni Pratiche sui Thread**

---

### **1. Esercizio: Il Mio Primo Thread**

**Obiettivo:** Creare e avviare thread utilizzando i due approcci principali in Java.

#### **Istruzioni:**
1. Creare una classe `MioThread` che estenda la classe `Thread`:
   - Sovrascrivere il metodo `run()` per stampare "Esecuzione del thread tramite ereditarietà" seguito dal nome del thread.
   - Aggiungere un costruttore che accetti un nome per il thread.
2. Creare una classe `MioRunnable` che implementi l'interfaccia `Runnable`:
   - Implementare il metodo `run()` per stampare "Esecuzione del thread tramite Runnable" seguito dal nome del thread.
   - Aggiungere un costruttore che accetti un nome per il thread.
3. Nel metodo `main`:
   - Creare e avviare un'istanza di `MioThread`.
   - Creare un'istanza di `MioRunnable` e passarla al costruttore di `Thread`, quindi avviare il thread.
   - Creare e avviare un thread utilizzando un'espressione lambda che stampi "Esecuzione del thread tramite lambda".

**Esempio di Output:**
```
Esecuzione del thread tramite ereditarietà: Thread-1
Esecuzione del thread tramite Runnable: Thread-2
Esecuzione del thread tramite lambda
```

---

### **2. Esercizio: Contatore Concorrente**

**Obiettivo:** Creare più thread che incrementano un contatore condiviso.

#### **Istruzioni:**
1. Creare una classe `Contatore` con:
   - Un attributo `valore` (int) inizializzato a 0.
   - Un metodo `incrementa()` che aumenta il valore di 1.
   - Un metodo `getValore()` che restituisce il valore corrente.
2. Creare una classe `ThreadContatore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Contatore` nel costruttore.
   - Nel metodo `run()`, incrementare il contatore 1000 volte in un ciclo.
3. Nel metodo `main`:
   - Creare un'istanza di `Contatore`.
   - Creare e avviare 5 thread che utilizzano la stessa istanza di `Contatore`.
   - Attendere che tutti i thread completino l'esecuzione usando `join()`.
   - Stampare il valore finale del contatore.

**Esempio di Output:**
```
Valore finale del contatore: 5000
```

**Nota:** Eseguire il programma più volte e osservare se il risultato è sempre quello atteso. Discutere le possibili cause di eventuali discrepanze.

---

### **3. Esercizio: Ciclo di Vita dei Thread**

**Obiettivo:** Osservare e manipolare il ciclo di vita di un thread.

#### **Istruzioni:**
1. Creare una classe `ThreadConStati` che estenda `Thread`:
   - Sovrascrivere il metodo `run()` per eseguire un ciclo che stampi lo stato corrente del thread.
   - Il ciclo deve eseguire 5 iterazioni con una pausa di 1 secondo tra ciascuna.
   - Aggiungere un metodo `pausaThread()` che metta il thread in stato di attesa (WAITING) utilizzando `wait()`.
   - Aggiungere un metodo `riprendiThread()` che risvegli il thread utilizzando `notify()`.
2. Nel metodo `main`:
   - Creare e avviare un'istanza di `ThreadConStati`.
   - Dopo 2 secondi, chiamare `pausaThread()` per mettere il thread in attesa.
   - Dopo altri 2 secondi, chiamare `riprendiThread()` per risvegliare il thread.
   - Attendere che il thread completi l'esecuzione.
   - Stampare lo stato finale del thread.

**Esempio di Output:**
```
Thread in stato: RUNNABLE
Thread in stato: RUNNABLE
Thread messo in attesa
Thread in stato: WAITING
Thread risvegliato
Thread in stato: RUNNABLE
Thread in stato: RUNNABLE
Thread completato, stato: TERMINATED
```

---

### **4. Esercizio: Priorità dei Thread**

**Obiettivo:** Sperimentare con le priorità dei thread e osservarne l'effetto.

#### **Istruzioni:**
1. Creare una classe `ThreadConPriorita` che implementi `Runnable`:
   - La classe deve ricevere un nome e un numero di iterazioni nel costruttore.
   - Nel metodo `run()`, eseguire un ciclo che stampi il nome del thread per il numero specificato di iterazioni.
2. Nel metodo `main`:
   - Creare tre thread con priorità diverse (MIN_PRIORITY, NORM_PRIORITY, MAX_PRIORITY).
   - Assegnare a ciascun thread un nome descrittivo (es. "Thread a bassa priorità").
   - Avviare tutti i thread contemporaneamente.
   - Attendere che tutti i thread completino l'esecuzione.

**Esempio di Output:**
```
Thread ad alta priorità: iterazione 1
Thread ad alta priorità: iterazione 2
...
Thread a priorità normale: iterazione 1
...
Thread a bassa priorità: iterazione 1
...
```

**Nota:** L'effetto delle priorità può variare a seconda del sistema operativo e della JVM. Discutere le osservazioni fatte durante l'esecuzione.

---

### **5. Esercizio: Interruzione di Thread**

**Obiettivo:** Implementare e gestire l'interruzione di thread in modo corretto.

#### **Istruzioni:**
1. Creare una classe `ThreadInterrompibile` che implementi `Runnable`:
   - Nel metodo `run()`, implementare un ciclo infinito che stampi un messaggio ogni secondo.
   - Il ciclo deve controllare regolarmente se il thread è stato interrotto utilizzando `Thread.interrupted()` o `isInterrupted()`.
   - Se il thread viene interrotto, il metodo deve terminare in modo pulito, stampando un messaggio di uscita.
2. Nel metodo `main`:
   - Creare e avviare un'istanza di `ThreadInterrompibile`.
   - Attendere 5 secondi.
   - Interrompere il thread chiamando il metodo `interrupt()`.
   - Verificare che il thread termini correttamente.

**Esempio di Output:**
```
Thread in esecuzione: 1 secondi
Thread in esecuzione: 2 secondi
Thread in esecuzione: 3 secondi
Thread in esecuzione: 4 secondi
Thread in esecuzione: 5 secondi
Thread interrotto, uscita in corso...
Thread terminato correttamente
```

---

### **6. Esercizio: Simulazione di Download Concorrenti**

**Obiettivo:** Simulare il download di più file in parallelo utilizzando thread.

#### **Istruzioni:**
1. Creare una classe `Download` che implementi `Runnable`:
   - La classe deve ricevere il nome del file e la dimensione (in MB) nel costruttore.
   - Nel metodo `run()`, simulare il download mostrando l'avanzamento (0-100%) ogni secondo.
   - La velocità di download simulata deve essere di 1MB al secondo.
2. Creare una classe `GestoreDownload`:
   - Con un metodo `aggiungiDownload(String nomeFile, int dimensione)` che crea e avvia un nuovo thread di download.
   - Con un metodo `attendiCompletamento()` che attende il completamento di tutti i download.
3. Nel metodo `main`:
   - Creare un'istanza di `GestoreDownload`.
   - Aggiungere almeno 3 download di dimensioni diverse.
   - Attendere il completamento di tutti i download.
   - Stampare un messaggio di completamento.

**Esempio di Output:**
```
Download di file1.zip (5MB): 20% completato
Download di file2.zip (3MB): 33% completato
Download di file3.zip (10MB): 10% completato
...
Download di file2.zip (3MB): 100% completato
...
Download di file1.zip (5MB): 100% completato
...
Download di file3.zip (10MB): 100% completato
Tutti i download sono stati completati!
```

---

### **7. Esercizio: Produttore-Consumatore**

**Obiettivo:** Implementare il classico problema del produttore-consumatore utilizzando thread.

#### **Istruzioni:**
1. Creare una classe `Buffer` con:
   - Un attributo `contenuto` (int) che rappresenta il dato condiviso.
   - Un attributo booleano `pieno` che indica se il buffer contiene un dato.
   - Metodi sincronizzati `produci(int valore)` e `consuma()` che rispettivamente inseriscono e prelevano dati dal buffer.
2. Creare una classe `Produttore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Buffer` nel costruttore.
   - Nel metodo `run()`, produrre 10 valori interi e inserirli nel buffer con una pausa tra ciascuno.
3. Creare una classe `Consumatore` che implementi `Runnable`:
   - La classe deve ricevere un'istanza di `Buffer` nel costruttore.
   - Nel metodo `run()`, consumare 10 valori dal buffer con una pausa tra ciascuno.
4. Nel metodo `main`:
   - Creare un'istanza di `Buffer`.
   - Creare e avviare un thread produttore e un thread consumatore che utilizzano lo stesso buffer.
   - Attendere che entrambi i thread completino l'esecuzione.

**Esempio di Output:**
```
Produttore: valore 1 prodotto
Consumatore: valore 1 consumato
Produttore: valore 2 prodotto
Consumatore: valore 2 consumato
...
Produttore: valore 10 prodotto
Consumatore: valore 10 consumato
```

---

### **8. Esercizio: Timer con Thread**

**Obiettivo:** Implementare un timer utilizzando thread che esegua azioni a intervalli regolari.

#### **Istruzioni:**
1. Creare una classe `Timer` che implementi `Runnable`:
   - La classe deve ricevere un intervallo di tempo (in secondi) e un numero di ripetizioni nel costruttore.
   - Nel metodo `run()`, eseguire un ciclo che stampi il tempo trascorso a intervalli regolari.
   - Aggiungere un metodo `aggiungiAzione(Runnable azione)` che permetta di specificare un'azione da eseguire ad ogni intervallo.
2. Nel metodo `main`:
   - Creare un'istanza di `Timer` con un intervallo di 2 secondi e 5 ripetizioni.
   - Aggiungere un'azione che stampi un messaggio personalizzato.
   - Avviare il timer e attendere il completamento.

**Esempio di Output:**
```
Timer: 2 secondi trascorsi
Azione personalizzata eseguita!
Timer: 4 secondi trascorsi
Azione personalizzata eseguita!
Timer: 6 secondi trascorsi
Azione personalizzata eseguita!
Timer: 8 secondi trascorsi
Azione personalizzata eseguita!
Timer: 10 secondi trascorsi
Azione personalizzata eseguita!
Timer completato
```

---

### **9. Esercizio: Thread Pool**

**Obiettivo:** Utilizzare un thread pool per gestire l'esecuzione di più task.

#### **Istruzioni:**
1. Creare una classe `Task` che implementi `Runnable`:
   - La classe deve ricevere un ID e una durata (in secondi) nel costruttore.
   - Nel metodo `run()`, simulare un'operazione che richiede il tempo specificato.
2. Nel metodo `main`:
   - Creare un thread pool fisso con 3 thread utilizzando `Executors.newFixedThreadPool(3)`.
   - Sottomettere 10 task con durate diverse al thread pool.
   - Attendere il completamento di tutti i task.
   - Chiudere il thread pool.

**Esempio di Output:**
```
Task 1 iniziato
Task 2 iniziato
Task 3 iniziato
Task 1 completato (durata: 2 secondi)
Task 4 iniziato
Task 2 completato (durata: 3 secondi)
Task 5 iniziato
...
Task 10 completato (durata: 1 secondo)
Tutti i task sono stati completati!
```

---

### **10. Esercizio: Simulazione di un Sistema di Prenotazione**

**Obiettivo:** Implementare un sistema di prenotazione concorrente utilizzando thread.

#### **Istruzioni:**
1. Creare una classe `SistemaPrenotazione`:
   - Con un attributo `postiDisponibili` (int) inizializzato a 10.
   - Con un metodo sincronizzato `prenota(String cliente, int numeroPostiRichiesti)` che verifica la disponibilità e effettua la prenotazione.
2. Creare una classe `Cliente` che implementi `Runnable`:
   - La classe deve ricevere un nome, il numero di posti da prenotare e un'istanza di `SistemaPrenotazione` nel costruttore.
   - Nel metodo `run()`, tentare di effettuare la prenotazione e stampare il risultato.
3. Nel metodo `main`:
   - Creare un'istanza di `SistemaPrenotazione`.
   - Creare e avviare 8 thread cliente con richieste di prenotazione diverse.
   - Attendere che tutti i thread completino l'esecuzione.
   - Stampare il numero di posti ancora disponibili.

**Esempio di Output:**
```
Cliente Mario: prenotazione di 2 posti effettuata con successo
Cliente Giulia: prenotazione di 3 posti effettuata con successo
Cliente Paolo: prenotazione di 4 posti effettuata con successo
Cliente Anna: prenotazione di 2 posti fallita (posti richiesti: 2, disponibili: 1)
Cliente Marco: prenotazione di 1 posto effettuata con successo
Cliente Laura: prenotazione di 3 posti fallita (posti richiesti: 3, disponibili: 0)
Cliente Giovanni: prenotazione di 2 posti fallita (posti richiesti: 2, disponibili: 0)
Cliente Sofia: prenotazione di 1 posto fallita (posti richiesti: 1, disponibili: 0)
Posti ancora disponibili: 0
```

---

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Interruzione di Thread](./03-CicloVita.md)
- [➡️ Sincronizzazione](../02-SincronizzazioneThread/01-Sincronizzazione.md)