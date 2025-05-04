# Introduzione ai Thread

## Descrizione

Questo modulo introduce i concetti fondamentali della programmazione multi-thread in Java. Imparerai cosa sono i thread, come crearli e gestirli, e comprenderai il loro ciclo di vita. Attraverso esempi pratici, scoprirai come implementare thread in Java utilizzando sia l'estensione della classe `Thread` che l'implementazione dell'interfaccia `Runnable`.

## Indice degli Argomenti Teorici

1. [Concetti Base dei Thread](01-ConcettiBase.md)
   - Definizione di thread e processo
   - Vantaggi della programmazione multi-thread
   - Differenze tra concorrenza e parallelismo

2. [Creazione di Thread in Java](02-CreazioneThread.md)
   - [Estendere la classe Thread](02a-EstenderelaClasseThread.md)
   - [Implementare l'interfaccia Runnable](02b-ImplementareRunnable.md)
   - [Lambda expressions per thread (Java 8+)](02c-ThreadLambda.md)

3. [Ciclo di Vita di un Thread](03-CicloVita.md)
   - Stati di un thread (New, Runnable, Blocked, Waiting, Timed Waiting, Terminated)
   - Metodi per controllare il ciclo di vita (start, sleep, join, yield)
   - Priorità dei thread

## Esercitazioni Pratiche

Consulta l'[Indice degli Esempi](esempi/INDICE_ESEMPI.md) per una panoramica completa.

1. **HelloThread**: Crea il tuo primo thread che stampa un messaggio.
   - File: [ES01_CreazioneDiThread.java](esempi/ES01_CreazioneDiThread.java)

2. **DueApprocci**: Confronta l'implementazione di thread usando l'estensione di Thread e l'interfaccia Runnable (vedi `ES01_CreazioneDiThread.java`).
   - File: [ES01_CreazioneDiThread.java](esempi/ES01_CreazioneDiThread.java)

3. **CicloVitaDemo**: Osserva e manipola il ciclo di vita di un thread.
   - File: [ES03_CicloVitaThread.java](esempi/ES03_CicloVitaThread.java)

4. **PrioritaThread**: Sperimenta con le priorità dei thread.
   - File: [ES04_PrioritaThread.java](esempi/ES04_PrioritaThread.java)

## Sfide

1. **Contatore Concorrente**: Implementa un contatore che viene incrementato da più thread.
2. **Timer Multipli**: Crea più timer che eseguono operazioni a intervalli diversi.
3. **Simulazione di Download**: Simula il download di più file contemporaneamente con barre di progresso.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [➡️ Sincronizzazione](../02-Sincronizzazione/README.md)
- [⭐ Priorità dei Thread](ThreadPriority.md)