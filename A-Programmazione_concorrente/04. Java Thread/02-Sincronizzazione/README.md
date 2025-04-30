# Sincronizzazione

## Descrizione

Questo modulo affronta uno degli aspetti più critici della programmazione multi-thread: la sincronizzazione. Quando più thread accedono e modificano risorse condivise, possono verificarsi problemi come race condition, inconsistenza dei dati e deadlock. In questo modulo, imparerai come utilizzare i meccanismi di sincronizzazione di Java per garantire l'accesso sicuro alle risorse condivise.

## Indice degli Argomenti Teorici

1. [Problemi di Concorrenza](./01-ProblemiConcorrenza.md)
   - Race condition
   - Inconsistenza dei dati
   - Visibilità delle variabili tra thread
   - Esempio pratico di race condition

2. [Keyword synchronized](./02-Synchronized.md)
   - Concetto di monitor in Java
   - Metodi sincronizzati
   - Blocchi sincronizzati
   - Sincronizzazione su oggetti vs classi

3. [Lock e Oggetti di Sincronizzazione](./03-LockOggetti.md)
   - Oggetti come lock
   - Granularità del lock
   - Lock rientranti
   - Deadlock: cause e prevenzione

## Esercitazioni Pratiche

1. **ContatoreConcorrente**: Implementa un contatore thread-safe utilizzando la sincronizzazione.
   - File: [ContatoreConcorrente.java](./esempi/ContatoreConcorrente.java)

2. **BancaSicura**: Simula operazioni bancarie (deposito/prelievo) con accesso sincronizzato.
   - File: [BancaSicura.java](./esempi/BancaSicura.java)

3. **DeadlockDemo**: Dimostra come si verifica un deadlock e come prevenirlo.
   - File: [DeadlockDemo.java](./esempi/DeadlockDemo.java)

4. **SincronizzazioneOggetti**: Esplora diversi livelli di granularità nella sincronizzazione.
   - File: [SincronizzazioneOggetti.java](./esempi/SincronizzazioneOggetti.java)

## Sfide

1. **Lettori-Scrittori**: Implementa una soluzione al problema dei lettori-scrittori.
2. **Filosofi a Cena**: Risolvi il classico problema dei filosofi a cena evitando deadlock.
3. **Cache Thread-Safe**: Crea una cache concorrente che permetta accessi multipli in lettura ma accessi esclusivi in scrittura.

## Navigazione del Corso
- [📑 Indice](../README.md)
- [⬅️ Introduzione ai Thread](../01-IntroduzioneThread/README.md)
- [➡️ Comunicazione tra Thread](../03-ComunicazioneThread/README.md)