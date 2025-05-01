/**
 * BancaSicura.java
 * 
 * Questo esempio simula operazioni bancarie (deposito/prelievo) con accesso sincronizzato.
 * Dimostra come utilizzare la sincronizzazione per garantire la consistenza dei dati
 * quando più thread accedono e modificano lo stesso conto bancario.
 */
public class BancaSicura {
    public static void main(String[] args) {
        // Creiamo un conto con saldo iniziale di 1000 euro
        ContoBancario conto = new ContoBancario("IT123456789", 1000);
        
        System.out.println("Simulazione di operazioni bancarie concorrenti");
        System.out.println("===========================================\n");
        System.out.println("Saldo iniziale: " + conto.getSaldo() + " euro\n");
        
        // Creiamo thread per simulare depositi multipli
        Thread threadDepositi = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                double importo = 100 + (Math.random() * 100); // Importo casuale tra 100 e 200
                conto.deposita(importo);
                try {
                    Thread.sleep(100); // Pausa tra le operazioni
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-Depositi");
        
        // Creiamo thread per simulare prelievi multipli
        Thread threadPrelievi = new Thread(() -> {
            for (int i = 0; i < 7; i++) {
                double importo = 50 + (Math.random() * 150); // Importo casuale tra 50 e 200
                boolean successo = conto.preleva(importo);
                if (!successo) {
                    System.out.println(Thread.currentThread().getName() + 
                            ": Prelievo di " + String.format("%.2f", importo) + 
                            " euro fallito - Fondi insufficienti");
                }
                try {
                    Thread.sleep(150); // Pausa tra le operazioni
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-Prelievi");
        
        // Creiamo thread per verificare il saldo periodicamente
        Thread threadVerifica = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                conto.verificaSaldo();
                try {
                    Thread.sleep(200); // Verifica periodica
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Thread-Verifica");
        
        // Avviamo i thread
        threadDepositi.start();
        threadPrelievi.start();
        threadVerifica.start();
        
        // Attendiamo che tutti i thread completino
        try {
            threadDepositi.join();
            threadPrelievi.join();
            threadVerifica.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nOperazioni completate");
        System.out.println("Saldo finale: " + conto.getSaldo() + " euro");
    }
}

/**
 * Classe che rappresenta un conto bancario thread-safe.
 * Tutti i metodi che accedono o modificano il saldo sono sincronizzati
 * per garantire la consistenza dei dati in ambiente multi-thread.
 */
class ContoBancario {
    private final String numeroConto;
    private double saldo;
    private int numeroOperazioni;
    
    public ContoBancario(String numeroConto, double saldoIniziale) {
        this.numeroConto = numeroConto;
        this.saldo = saldoIniziale;
        this.numeroOperazioni = 0;
    }
    
    /**
     * Deposita un importo nel conto.
     * Metodo sincronizzato per garantire l'accesso esclusivo al saldo.
     */
    public synchronized void deposita(double importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        
        double saldoPrecedente = saldo;
        // Simuliamo un'operazione che richiede tempo
        try { Thread.sleep(20); } catch (InterruptedException e) { }
        
        saldo += importo;
        numeroOperazioni++;
        
        System.out.println(Thread.currentThread().getName() + 
                ": Depositati " + String.format("%.2f", importo) + 
                " euro. Saldo: " + String.format("%.2f", saldoPrecedente) + 
                " -> " + String.format("%.2f", saldo));
    }
    
    /**
     * Preleva un importo dal conto se ci sono fondi sufficienti.
     * Metodo sincronizzato per garantire l'accesso esclusivo al saldo.
     * 
     * @return true se il prelievo è andato a buon fine, false altrimenti
     */
    public synchronized boolean preleva(double importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        
        if (saldo < importo) {
            return false; // Fondi insufficienti
        }
        
        double saldoPrecedente = saldo;
        // Simuliamo un'operazione che richiede tempo
        try { Thread.sleep(20); } catch (InterruptedException e) { }
        
        saldo -= importo;
        numeroOperazioni++;
        
        System.out.println(Thread.currentThread().getName() + 
                ": Prelevati " + String.format("%.2f", importo) + 
                " euro. Saldo: " + String.format("%.2f", saldoPrecedente) + 
                " -> " + String.format("%.2f", saldo));
        
        return true;
    }
    
    /**
     * Verifica il saldo corrente.
     * Anche questo metodo è sincronizzato per garantire una lettura consistente.
     */
    public synchronized void verificaSaldo() {
        System.out.println(Thread.currentThread().getName() + 
                ": Verifica saldo - Conto: " + numeroConto + 
                ", Saldo attuale: " + String.format("%.2f", saldo) + 
                ", Operazioni effettuate: " + numeroOperazioni);
    }
    
    /**
     * Restituisce il saldo corrente.
     * Metodo sincronizzato per garantire una lettura consistente.
     */
    public synchronized double getSaldo() {
        return saldo;
    }
    
    /**
     * Restituisce il numero di conto.
     * Non necessita di sincronizzazione poiché è un campo immutabile.
     */
    public String getNumeroConto() {
        return numeroConto;
    }
}