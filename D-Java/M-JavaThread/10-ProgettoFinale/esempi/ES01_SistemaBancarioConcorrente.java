/**
 * Esempio 01: Sistema Bancario Concorrente
 * 
 * Questo esempio integra diversi concetti di concorrenza visti nei moduli precedenti
 * in un'applicazione che simula un sistema bancario semplificato con:
 * - Gestione concorrente di conti bancari
 * - Elaborazione asincrona di transazioni
 * - Monitoraggio delle operazioni
 * - Prevenzione di race condition e deadlock
 */
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ES01_SistemaBancarioConcorrente {
    // Logger thread-safe per il monitoraggio
    private static final Logger LOGGER = Logger.getLogger(ES01_SistemaBancarioConcorrente.class.getName());
    
    // Configurazione del logger
    static {
        LOGGER.setLevel(Level.ALL);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);
        LOGGER.setUseParentHandlers(false);
    }
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Simulazione di Sistema Bancario Concorrente");
        System.out.println("==========================================");
        
        // Inizializza il sistema bancario
        BankSystem bankSystem = new BankSystem();
        
        // Crea alcuni conti di esempio
        bankSystem.createAccount("Alice", 1000.0);
        bankSystem.createAccount("Bob", 1500.0);
        bankSystem.createAccount("Charlie", 2000.0);
        bankSystem.createAccount("Diana", 3000.0);
        
        System.out.println("\nConti creati. Avvio simulazione...");
        
        // Avvia la simulazione
        bankSystem.startSimulation();
        
        // Attende che la simulazione sia in esecuzione per un po'
        Thread.sleep(10000);
        
        // Termina la simulazione
        bankSystem.stopSimulation();
        
        // Stampa lo stato finale
        System.out.println("\nStato finale dei conti:");
        bankSystem.printAccountStatus();
        
        // Stampa statistiche
        System.out.println("\nStatistiche delle transazioni:");
        bankSystem.printTransactionStatistics();
    }
    
    /**
     * Classe che rappresenta il sistema bancario
     */
    static class BankSystem {
        // Mappa thread-safe per i conti bancari
        private final ConcurrentHashMap<String, BankAccount> accounts = new ConcurrentHashMap<>();
        
        // Coda delle transazioni da elaborare
        private final BlockingQueue<Transaction> transactionQueue = new LinkedBlockingQueue<>();
        
        // Pool di thread per elaborare le transazioni
        private final ExecutorService transactionProcessors;
        
        // Thread per generare transazioni casuali
        private final ScheduledExecutorService transactionGenerator;
        
        // Flag per controllare l'esecuzione della simulazione
        private volatile boolean isRunning = false;
        
        // Contatori per le statistiche
        private final AtomicInteger totalTransactions = new AtomicInteger(0);
        private final AtomicInteger successfulTransactions = new AtomicInteger(0);
        private final AtomicInteger failedTransactions = new AtomicInteger(0);
        
        public BankSystem() {
            // Inizializza il pool di thread per elaborare le transazioni
            transactionProcessors = Executors.newFixedThreadPool(3);
            
            // Inizializza il generatore di transazioni
            transactionGenerator = Executors.newScheduledThreadPool(1);
        }
        
        /**
         * Crea un nuovo conto bancario
         */
        public void createAccount(String owner, double initialBalance) {
            BankAccount account = new BankAccount(owner, initialBalance);
            accounts.put(owner, account);
            LOGGER.info("Creato conto per " + owner + " con saldo iniziale di " + initialBalance);
        }
        
        /**
         * Avvia la simulazione del sistema bancario
         */
        public void startSimulation() {
            if (isRunning) return;
            
            isRunning = true;
            LOGGER.info("Avvio simulazione del sistema bancario");
            
            // Avvia i thread per elaborare le transazioni
            for (int i = 0; i < 3; i++) {
                final int processorId = i;
                transactionProcessors.submit(() -> processTransactions(processorId));
            }
            
            // Pianifica la generazione di transazioni casuali ogni 200ms
            transactionGenerator.scheduleAtFixedRate(
                this::generateRandomTransaction, 0, 200, TimeUnit.MILLISECONDS);
        }
        
        /**
         * Ferma la simulazione del sistema bancario
         */
        public void stopSimulation() throws InterruptedException {
            if (!isRunning) return;
            
            LOGGER.info("Arresto simulazione del sistema bancario");
            isRunning = false;
            
            // Ferma il generatore di transazioni
            transactionGenerator.shutdown();
            transactionGenerator.awaitTermination(1, TimeUnit.SECONDS);
            
            // Attende che tutte le transazioni in coda vengano elaborate
            while (!transactionQueue.isEmpty()) {
                Thread.sleep(100);
            }
            
            // Ferma i processori di transazioni
            transactionProcessors.shutdown();
            transactionProcessors.awaitTermination(2, TimeUnit.SECONDS);
            
            LOGGER.info("Simulazione terminata");
        }
        
        /**
         * Genera una transazione casuale
         */
        private void generateRandomTransaction() {
            if (!isRunning) return;
            
            try {
                // Ottiene la lista dei proprietari di conti
                List<String> accountOwners = new ArrayList<>(accounts.keySet());
                if (accountOwners.size() < 2) return;
                
                Random random = new Random();
                
                // Seleziona casualmente mittente e destinatario diversi
                String fromOwner = accountOwners.get(random.nextInt(accountOwners.size()));
                String toOwner;
                do {
                    toOwner = accountOwners.get(random.nextInt(accountOwners.size()));
                } while (toOwner.equals(fromOwner));
                
                // Genera un importo casuale tra 10 e 200
                double amount = 10 + random.nextDouble() * 190;
                amount = Math.round(amount * 100) / 100.0; // Arrotonda a 2 decimali
                
                // Crea e accoda la transazione
                Transaction transaction = new Transaction(
                    fromOwner, toOwner, amount, TransactionType.TRANSFER);
                
                transactionQueue.put(transaction);
                LOGGER.fine("Generata transazione: " + transaction);
                
                totalTransactions.incrementAndGet();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Errore durante la generazione della transazione", e);
            }
        }
        
        /**
         * Elabora le transazioni dalla coda
         */
        private void processTransactions(int processorId) {
            LOGGER.info("Avviato processore di transazioni #" + processorId);
            
            while (isRunning || !transactionQueue.isEmpty()) {
                try {
                    // Preleva una transazione dalla coda, attende fino a 500ms
                    Transaction transaction = transactionQueue.poll(500, TimeUnit.MILLISECONDS);
                    if (transaction == null) continue;
                    
                    LOGGER.info("Processore #" + processorId + ": elaborazione " + transaction);
                    
                    // Elabora la transazione
                    boolean success = executeTransaction(transaction);
                    
                    if (success) {
                        successfulTransactions.incrementAndGet();
                        LOGGER.info("Transazione completata con successo: " + transaction);
                    } else {
                        failedTransactions.incrementAndGet();
                        LOGGER.warning("Transazione fallita: " + transaction);
                    }
                    
                    // Simula il tempo di elaborazione
                    Thread.sleep(100 + (long)(Math.random() * 300));
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    LOGGER.warning("Processore #" + processorId + " interrotto");
                    break;
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Errore durante l'elaborazione della transazione", e);
                }
            }
            
            LOGGER.info("Processore di transazioni #" + processorId + " terminato");
        }
        
        /**
         * Esegue una transazione tra conti
         */
        private boolean executeTransaction(Transaction transaction) {
            // Ottiene i conti coinvolti
            BankAccount fromAccount = accounts.get(transaction.getFromAccount());
            BankAccount toAccount = accounts.get(transaction.getToAccount());
            
            if (fromAccount == null || toAccount == null) {
                LOGGER.warning("Conto non trovato per la transazione: " + transaction);
                return false;
            }
            
            // Determina l'ordine di acquisizione dei lock per prevenire deadlock
            // Acquisisce sempre prima il lock del conto con ID "minore" lessicograficamente
            BankAccount firstLock = fromAccount.getOwner().compareTo(toAccount.getOwner()) < 0 ?
                                    fromAccount : toAccount;
            BankAccount secondLock = firstLock == fromAccount ? toAccount : fromAccount;
            
            // Acquisisce i lock in ordine
            firstLock.getLock().writeLock().lock();
            try {
                secondLock.getLock().writeLock().lock();
                try {
                    // Verifica se c'è saldo sufficiente
                    if (transaction.getType() == TransactionType.TRANSFER) {
                        if (fromAccount.getBalance() < transaction.getAmount()) {
                            LOGGER.warning("Saldo insufficiente per " + fromAccount.getOwner() + 
                                         ": " + fromAccount.getBalance() + ", richiesto: " + 
                                         transaction.getAmount());
                            return false;
                        }
                        
                        // Esegue il trasferimento
                        fromAccount.withdraw(transaction.getAmount());
                        toAccount.deposit(transaction.getAmount());
                        
                        return true;
                    }
                    
                    return false; // Tipo di transazione non supportato
                } finally {
                    secondLock.getLock().writeLock().unlock();
                }
            } finally {
                firstLock.getLock().writeLock().unlock();
            }
        }
        
        /**
         * Stampa lo stato di tutti i conti
         */
        public void printAccountStatus() {
            for (BankAccount account : accounts.values()) {
                account.getLock().readLock().lock();
                try {
                    System.out.println(account);
                } finally {
                    account.getLock().readLock().unlock();
                }
            }
        }
        
        /**
         * Stampa le statistiche delle transazioni
         */
        public void printTransactionStatistics() {
            System.out.println("Totale transazioni generate: " + totalTransactions.get());
            System.out.println("Transazioni completate con successo: " + successfulTransactions.get());
            System.out.println("Transazioni fallite: " + failedTransactions.get());
            
            if (totalTransactions.get() > 0) {
                double successRate = (double) successfulTransactions.get() / totalTransactions.get() * 100;
                System.out.printf("Tasso di successo: %.2f%%\n", successRate);
            }
        }
    }
    
    /**
     * Classe che rappresenta un conto bancario thread-safe
     */
    static class BankAccount {
        private final String owner;
        private double balance;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        private final List<String> transactionHistory = Collections.synchronizedList(new ArrayList<>());
        
        public BankAccount(String owner, double initialBalance) {
            this.owner = owner;
            this.balance = initialBalance;
            addToHistory("Apertura conto con saldo iniziale di " + initialBalance);
        }
        
        public String getOwner() {
            return owner;
        }
        
        public double getBalance() {
            lock.readLock().lock();
            try {
                return balance;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        public ReadWriteLock getLock() {
            return lock;
        }
        
        public void deposit(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("L'importo deve essere positivo");
            
            lock.writeLock().lock();
            try {
                balance += amount;
                addToHistory("Deposito di " + amount + ", nuovo saldo: " + balance);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        public void withdraw(double amount) {
            if (amount <= 0) throw new IllegalArgumentException("L'importo deve essere positivo");
            
            lock.writeLock().lock();
            try {
                if (balance < amount) {
                    throw new IllegalStateException("Saldo insufficiente");
                }
                balance -= amount;
                addToHistory("Prelievo di " + amount + ", nuovo saldo: " + balance);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        private void addToHistory(String entry) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            String timestamp = sdf.format(new Date());
            transactionHistory.add(timestamp + " - " + entry);
            
            // Mantiene la cronologia limitata alle ultime 10 transazioni
            while (transactionHistory.size() > 10) {
                transactionHistory.remove(0);
            }
        }
        
        @Override
        public String toString() {
            lock.readLock().lock();
            try {
                return "Conto di " + owner + ", Saldo: " + balance;
            } finally {
                lock.readLock().unlock();
            }
        }
    }
    
    /**
     * Classe che rappresenta una transazione bancaria
     */
    static class Transaction {
        private final String fromAccount;
        private final String toAccount;
        private final double amount;
        private final TransactionType type;
        private final long timestamp;
        
        public Transaction(String fromAccount, String toAccount, double amount, TransactionType type) {
            this.fromAccount = fromAccount;
            this.toAccount = toAccount;
            this.amount = amount;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getFromAccount() {
            return fromAccount;
        }
        
        public String getToAccount() {
            return toAccount;
        }
        
        public double getAmount() {
            return amount;
        }
        
        public TransactionType getType() {
            return type;
        }
        
        @Override
        public String toString() {
            return type + " di " + amount + " da " + fromAccount + " a " + toAccount;
        }
    }
    
    /**
     * Enum che rappresenta i tipi di transazione supportati
     */
    enum TransactionType {
        TRANSFER,   // Trasferimento tra conti
        DEPOSIT,    // Deposito (non implementato in questo esempio)
        WITHDRAWAL  // Prelievo (non implementato in questo esempio)
    }
}