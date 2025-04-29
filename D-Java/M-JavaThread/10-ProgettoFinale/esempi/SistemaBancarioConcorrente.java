/**
 * Esempio 01: Sistema Bancario Concorrente
 * 
 * Questo progetto finale dimostra l'applicazione di vari concetti di concorrenza
 * in un sistema bancario simulato che gestisce:
 * - Conti correnti con operazioni concorrenti
 * - Transazioni tra conti
 * - Monitoraggio delle attività
 * - Gestione degli errori
 */
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SistemaBancarioConcorrente {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Sistema Bancario Concorrente - Progetto Finale");
        System.out.println("===============================================");
        
        // Crea il sistema bancario
        BancaSimulata banca = new BancaSimulata("Banca Concorrente SpA");
        
        // Crea alcuni conti di esempio
        String[] conti = new String[5];
        for (int i = 0; i < conti.length; i++) {
            conti[i] = banca.creaConto("Cliente-" + (i+1), 1000.0);
            System.out.println("Creato conto: " + conti[i] + " per Cliente-" + (i+1));
        }
        
        System.out.println("\nAvvio simulazione operazioni bancarie...");
        
        // Crea un pool di thread per simulare operazioni concorrenti
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100); // Per attendere il completamento
        
        // Simula varie operazioni concorrenti
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            final int operationId = i;
            executor.submit(() -> {
                try {
                    // Sceglie casualmente un'operazione da eseguire
                    int operationType = random.nextInt(4);
                    String contoCasuale = conti[random.nextInt(conti.length)];
                    String altroConto = conti[random.nextInt(conti.length)];
                    
                    switch (operationType) {
                        case 0: // Deposito
                            double importoDeposito = 50 + random.nextDouble() * 200;
                            banca.deposita(contoCasuale, importoDeposito);
                            break;
                            
                        case 1: // Prelievo
                            double importoPrelievo = 20 + random.nextDouble() * 100;
                            banca.preleva(contoCasuale, importoPrelievo);
                            break;
                            
                        case 2: // Trasferimento
                            if (!contoCasuale.equals(altroConto)) {
                                double importoTrasferimento = 10 + random.nextDouble() * 50;
                                banca.trasferisci(contoCasuale, altroConto, importoTrasferimento);
                            }
                            break;
                            
                        case 3: // Verifica saldo
                            banca.verificaSaldo(contoCasuale);
                            break;
                    }
                    
                    // Simula un po' di lavoro
                    Thread.sleep(random.nextInt(50));
                    
                } catch (Exception e) {
                    System.err.println("Errore nell'operazione " + operationId + ": " + e.getMessage());
                } finally {
                    latch.countDown();
                }
                return null;
            });
        }
        
        // Attende il completamento di tutte le operazioni
        latch.await();
        
        // Stampa statistiche finali
        System.out.println("\nStatistiche finali:");
        banca.stampaStatistiche();
        
        // Stampa i saldi finali
        System.out.println("\nSaldi finali:");
        for (String conto : conti) {
            System.out.printf("Conto %s: %.2f EUR\n", conto, banca.verificaSaldo(conto));
        }
        
        // Chiude il sistema
        executor.shutdown();
        banca.chiudi();
        
        System.out.println("\nConcetti di concorrenza dimostrati in questo progetto:");
        System.out.println("1. Collezioni concorrenti (ConcurrentHashMap, BlockingQueue)");
        System.out.println("2. Lock a lettura/scrittura (ReadWriteLock)");
        System.out.println("3. Variabili atomiche (AtomicInteger)");
        System.out.println("4. Thread pool (ExecutorService)");
        System.out.println("5. Sincronizzazione (CountDownLatch)");
        System.out.println("6. Gestione delle eccezioni in ambiente concorrente");
    }
}

/**
 * Classe che simula una banca con operazioni concorrenti
 */
class BancaSimulata {
    private final String nome;
    private final Map<String, ContoCorrente> conti;
    private final Queue<String> logOperazioni;
    private final ExecutorService monitoraggioExecutor;
    
    // Statistiche
    private final AtomicInteger numDepositi = new AtomicInteger(0);
    private final AtomicInteger numPrelievi = new AtomicInteger(0);
    private final AtomicInteger numTrasferimenti = new AtomicInteger(0);
    private final AtomicInteger numOperazioniFallite = new AtomicInteger(0);
    
    public BancaSimulata(String nome) {
        this.nome = nome;
        this.conti = new ConcurrentHashMap<>();
        this.logOperazioni = new LinkedBlockingQueue<>();
        
        // Crea un thread separato per il monitoraggio
        this.monitoraggioExecutor = Executors.newSingleThreadExecutor();
        avviaMonitoraggio();
    }
    
    /**
     * Crea un nuovo conto corrente
     */
    public String creaConto(String titolare, double saldoIniziale) {
        String numeroConto = generaNumeroConto();
        conti.put(numeroConto, new ContoCorrente(numeroConto, titolare, saldoIniziale));
        logOperazione("Creato conto " + numeroConto + " per " + titolare + 
                " con saldo iniziale " + saldoIniziale);
        return numeroConto;
    }
    
    /**
     * Deposita denaro su un conto
     */
    public void deposita(String numeroConto, double importo) throws BancaException {
        ContoCorrente conto = trovaConto(numeroConto);
        try {
            conto.deposita(importo);
            numDepositi.incrementAndGet();
            logOperazione("Depositati " + importo + " EUR su conto " + numeroConto);
        } catch (Exception e) {
            numOperazioniFallite.incrementAndGet();
            throw new BancaException("Errore durante il deposito: " + e.getMessage());
        }
    }
    
    /**
     * Preleva denaro da un conto
     */
    public void preleva(String numeroConto, double importo) throws BancaException {
        ContoCorrente conto = trovaConto(numeroConto);
        try {
            conto.preleva(importo);
            numPrelievi.incrementAndGet();
            logOperazione("Prelevati " + importo + " EUR da conto " + numeroConto);
        } catch (Exception e) {
            numOperazioniFallite.incrementAndGet();
            throw new BancaException("Errore durante il prelievo: " + e.getMessage());
        }
    }
    
    /**
     * Trasferisce denaro tra due conti
     */
    public void trasferisci(String contoOrigine, String contoDestinazione, double importo) 
            throws BancaException {
        if (contoOrigine.equals(contoDestinazione)) {
            throw new BancaException("Impossibile trasferire sullo stesso conto");
        }
        
        ContoCorrente origine = trovaConto(contoOrigine);
        ContoCorrente destinazione = trovaConto(contoDestinazione);
        
        // Acquisisce i lock in ordine per evitare deadlock
        ContoCorrente primo, secondo;
        if (contoOrigine.compareTo(contoDestinazione) < 0) {
            primo = origine;
            secondo = destinazione;
        } else {
            primo = destinazione;
            secondo = origine;
        }
        
        // Esegue il trasferimento con entrambi i lock acquisiti
        primo.getLock().writeLock().lock();
        try {
            secondo.getLock().writeLock().lock();
            try {
                if (origine.getSaldo() < importo) {
                    numOperazioniFallite.incrementAndGet();
                    throw new BancaException("Saldo insufficiente per il trasferimento");
                }
                
                origine.preleva(importo);
                destinazione.deposita(importo);
                numTrasferimenti.incrementAndGet();
                
                logOperazione("Trasferiti " + importo + " EUR da " + 
                        contoOrigine + " a " + contoDestinazione);
            } finally {
                secondo.getLock().writeLock().unlock();
            }
        } finally {
            primo.getLock().writeLock().unlock();
        }
    }
    
    /**
     * Verifica il saldo di un conto
     */
    public double verificaSaldo(String numeroConto) throws BancaException {
        ContoCorrente conto = trovaConto(numeroConto);
        conto.getLock().readLock().lock();
        try {
            return conto.getSaldo();
        } finally {
            conto.getLock().readLock().unlock();
        }
    }
    
    /**
     * Stampa le statistiche delle operazioni
     */
    public void stampaStatistiche() {
        System.out.println("Statistiche per " + nome + ":");
        System.out.println("- Numero di conti: " + conti.size());
        System.out.println("- Depositi effettuati: " + numDepositi.get());
        System.out.println("- Prelievi effettuati: " + numPrelievi.get());
        System.out.println("- Trasferimenti effettuati: " + numTrasferimenti.get());
        System.out.println("- Operazioni fallite: " + numOperazioniFallite.get());
        System.out.println("- Totale operazioni: " + 
                (numDepositi.get() + numPrelievi.get() + numTrasferimenti.get()));
    }
    
    /**
     * Chiude la banca e le sue risorse
     */
    public void chiudi() {
        monitoraggioExecutor.shutdownNow();
        logOperazione("Banca chiusa");
    }
    
    /**
     * Trova un conto dal suo numero
     */
    private ContoCorrente trovaConto(String numeroConto) throws BancaException {
        ContoCorrente conto = conti.get(numeroConto);
        if (conto == null) {
            throw new BancaException("Conto non trovato: " + numeroConto);
        }
        return conto;
    }
    
    /**
     * Genera un numero di conto univoco
     */
    private String generaNumeroConto() {
        return "CC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Registra un'operazione nel log
     */
    private void logOperazione(String messaggio) {
        String log = System.currentTimeMillis() + " - " + messaggio;
        logOperazioni.offer(log);
    }
    
    /**
     * Avvia il thread di monitoraggio
     */
    private void avviaMonitoraggio() {
        monitoraggioExecutor.execute(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    // Processa i log ogni secondo
                    Thread.sleep(1000);
                    int size = logOperazioni.size();
                    if (size > 0) {
                        System.out.println("[Monitor] " + size + " operazioni in coda");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("[Monitor] Thread di monitoraggio terminato");
            }
        });
    }
}

/**
 * Classe che rappresenta un conto corrente
 */
class ContoCorrente {
    private final String numeroConto;
    private final String titolare;
    private double saldo;
    private final ReadWriteLock lock;
    
    public ContoCorrente(String numeroConto, String titolare, double saldoIniziale) {
        this.numeroConto = numeroConto;
        this.titolare = titolare;
        this.saldo = saldoIniziale;
        this.lock = new ReentrantReadWriteLock(true); // Lock equo
    }
    
    /**
     * Deposita denaro sul conto
     */
    public void deposita(double importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        
        lock.writeLock().lock();
        try {
            saldo += importo;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Preleva denaro dal conto
     */
    public void preleva(double importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        
        lock.writeLock().lock();
        try {
            if (saldo < importo) {
                throw new IllegalStateException("Saldo insufficiente");
            }
            saldo -= importo;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Ottiene il saldo corrente
     */
    public double getSaldo() {
        lock.readLock().lock();
        try {
            return saldo;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /**
     * Ottiene il lock del conto
     */
    public ReadWriteLock getLock() {
        return lock;
    }
    
    /**
     * Ottiene il numero del conto
     */
    public String getNumeroConto() {
        return numeroConto;
    }
    
    /**
     * Ottiene il titolare del conto
     */
    public String getTitolare() {
        return titolare;
    }
}

/**
 * Eccezione specifica per le operazioni bancarie
 */
class BancaException extends Exception {
    public BancaException(String message) {
        super(message);
    }
}