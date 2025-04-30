# Atomic Variables

In questa esercitazione esploreremo le classi Atomic del package `java.util.concurrent.atomic`, che forniscono operazioni atomiche su singole variabili senza la necessità di utilizzare lock espliciti.

Le variabili atomiche sono progettate per supportare la programmazione lock-free e thread-safe, utilizzando primitive hardware come Compare-and-Swap (CAS) per garantire l'atomicità delle operazioni. Questo approccio offre prestazioni migliori rispetto ai meccanismi di sincronizzazione tradizionali in molti scenari di concorrenza.

## Indice degli Argomenti Teorici

1. [Introduzione alle Operazioni Atomiche](./01-IntroduzioneOperazioniAtomiche.md)
   - Problemi di race condition
   - Atomicità delle operazioni
   - Primitive hardware per la concorrenza
   - Vantaggi della programmazione lock-free

2. [Classi Atomic di Base](./02-ClassiAtomicBase.md)
   - AtomicInteger, AtomicLong, AtomicBoolean
   - AtomicReference
   - Operazioni atomiche di lettura e scrittura
   - Operazioni atomiche di aggiornamento condizionale

3. [Compare-and-Swap (CAS)](./03-CompareAndSwap.md)
   - Funzionamento dell'algoritmo CAS
   - Implementazione in Java
   - Problema dell'ABA
   - Soluzioni al problema dell'ABA

4. [Classi Atomic Avanzate](./04-ClassiAtomicAvanzate.md)
   - AtomicIntegerArray, AtomicLongArray, AtomicReferenceArray
   - AtomicMarkableReference e AtomicStampedReference
   - DoubleAdder e LongAdder
   - DoubleAccumulator e LongAccumulator

5. [Atomic Fieldupdater](./05-AtomicFieldUpdater.md)
   - AtomicIntegerFieldUpdater
   - AtomicLongFieldUpdater
   - AtomicReferenceFieldUpdater
   - Casi d'uso e limitazioni

## Esempi Pratici

In questa sezione troverai esempi di codice che illustrano l'uso delle variabili atomiche:

- [AtomicCounterExample.java](./esempi/AtomicCounterExample.java): Contatore thread-safe con AtomicInteger
- [CASExample.java](./esempi/CASExample.java): Implementazione di strutture dati lock-free
- [AtomicReferenceExample.java](./esempi/AtomicReferenceExample.java): Aggiornamento atomico di oggetti
- [AdderAccumulatorExample.java](./esempi/AdderAccumulatorExample.java): Utilizzo di LongAdder per contatori ad alte prestazioni

## Esercizi

1. Implementare una cache concorrente lock-free utilizzando AtomicReference
2. Creare un contatore distribuito con LongAdder
3. Sviluppare una coda lock-free utilizzando operazioni CAS
4. Confrontare le prestazioni di synchronized, Lock e variabili atomiche

## Navigazione

- [Indice del Corso](../README.md)
- Precedente: [Lock e Condizioni](../06-LockCondizioni/README.md)
- Successivo: [Pattern Concorrenti](../08-PatternConcorrenti/README.md)