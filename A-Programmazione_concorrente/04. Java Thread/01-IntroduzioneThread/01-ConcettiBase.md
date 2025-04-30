# Concetti Base dei Thread

## Definizione di Thread e Processo

Per comprendere la programmazione multi-thread, è fondamentale distinguere tra processi e thread:

### Processo

Un **processo** è un'istanza di un programma in esecuzione. Ogni processo ha:
- Il proprio spazio di memoria isolato
- Risorse del sistema operativo dedicate
- Almeno un thread (thread principale)

I processi sono indipendenti tra loro e non condividono direttamente la memoria.

### Thread

Un **thread** è un'unità di esecuzione all'interno di un processo. Caratteristiche principali:
- Condivide lo spazio di memoria con altri thread dello stesso processo
- Ha un proprio stack di esecuzione
- Ha un proprio program counter
- Condivide le risorse del processo (file aperti, socket, ecc.)

I thread vengono anche chiamati "processi leggeri" proprio perché condividono risorse all'interno dello stesso processo.

## Vantaggi della Programmazione Multi-thread

L'utilizzo di più thread offre numerosi vantaggi:

1. **Migliore reattività dell'applicazione**: Un'applicazione può rimanere reattiva agli input dell'utente mentre esegue operazioni lunghe in background.

2. **Utilizzo efficiente delle risorse**: I thread condividono memoria e risorse, richiedendo meno overhead rispetto all'utilizzo di processi separati.

3. **Sfruttamento di sistemi multi-core**: I thread possono essere eseguiti in parallelo su diversi core della CPU, aumentando le prestazioni complessive.

4. **Semplificazione del design**: Alcuni problemi sono naturalmente modellabili come attività concorrenti.

5. **Riduzione dei tempi di risposta**: Operazioni indipendenti possono essere eseguite contemporaneamente.

## Differenze tra Concorrenza e Parallelismo

Spesso i termini "concorrenza" e "parallelismo" vengono usati in modo intercambiabile, ma hanno significati distinti:

### Concorrenza

La **concorrenza** si riferisce alla capacità di gestire più attività in corso contemporaneamente, anche se non necessariamente eseguite nello stesso istante. È un concetto di progettazione:

- Più thread possono avanzare nel tempo anche su un singolo core CPU
- Il sistema operativo alterna l'esecuzione dei thread (time-slicing)
- Si concentra sulla struttura e sulla composizione

### Parallelismo

Il **parallelismo** si riferisce all'esecuzione simultanea di più attività, tipicamente su hardware multi-core o distribuito:

- Richiede hardware in grado di eseguire più operazioni contemporaneamente
- I thread vengono eseguiti letteralmente nello stesso istante
- Si concentra sull'esecuzione

In sintesi: la concorrenza riguarda la gestione di più attività, mentre il parallelismo riguarda l'esecuzione simultanea di queste attività.

## Sfide della Programmazione Multi-thread

Nonostante i vantaggi, la programmazione multi-thread presenta alcune sfide significative:

1. **Race condition**: Quando il risultato dipende dall'ordine di esecuzione dei thread.

2. **Deadlock**: Situazione in cui due o più thread si bloccano a vicenda, aspettando risorse che sono bloccate dall'altro thread.

3. **Starvation**: Un thread non ottiene mai accesso alle risorse necessarie.

4. **Livelock**: I thread rispondono continuamente alle azioni degli altri senza fare progressi.

5. **Overhead**: La creazione e la gestione dei thread comporta un costo in termini di risorse.

6. **Debugging complesso**: I bug legati alla concorrenza sono spesso difficili da riprodurre e diagnosticare.

Nei prossimi moduli, esploreremo tecniche per affrontare queste sfide e scrivere codice multi-thread robusto ed efficiente.

## Thread in Java

Java offre un supporto nativo per la programmazione multi-thread attraverso:

- La classe `Thread`
- L'interfaccia `Runnable`
- Parole chiave come `synchronized`
- Package `java.util.concurrent` (introdotto in Java 5)

Nel prossimo capitolo, vedremo come creare e gestire thread in Java utilizzando questi strumenti.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Introduzione ai Thread](./README.md)
- [➡️ Creazione di Thread in Java](./02-CreazioneThread.md)