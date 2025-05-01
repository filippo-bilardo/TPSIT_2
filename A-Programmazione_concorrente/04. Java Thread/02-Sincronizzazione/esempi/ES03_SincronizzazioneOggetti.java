/**
 * SincronizzazioneOggetti.java
 * 
 * Questo esempio esplora diversi livelli di granularità nella sincronizzazione,
 * dimostrando come la scelta degli oggetti di lock influisce sulle prestazioni
 * e sulla concorrenza in un'applicazione multi-thread.
 */
public class ES03_SincronizzazioneOggetti {
    public static void main(String[] args) {
        System.out.println("Dimostrazione di diversi livelli di granularità nella sincronizzazione");
        System.out.println("================================================================\n");
        
        // Creiamo un'istanza di ciascuna implementazione
        InventarioGranaGrossa inventarioGG = new InventarioGranaGrossa();
        InventarioGranaFine inventarioGF = new InventarioGranaFine();
        
        // Test con granularità grossa
        System.out.println("Test con sincronizzazione a grana grossa:");
        System.out.println("----------------------------------------");
        eseguiTest(inventarioGG);
        
        System.out.println("\n");
        
        // Test con granularità fine
        System.out.println("Test con sincronizzazione a grana fine:");
        System.out.println("---------------------------------------");
        eseguiTest(inventarioGF);
    }
    
    /**
     * Esegue un test di concorrenza sull'implementazione dell'inventario fornita
     */
    private static void eseguiTest(Inventario inventario) {
        // Inizializziamo l'inventario con alcuni prodotti
        inventario.aggiungiProdotto("Laptop", 5);
        inventario.aggiungiProdotto("Smartphone", 10);
        inventario.aggiungiProdotto("Tablet", 7);
        inventario.aggiungiProdotto("Monitor", 3);
        
        // Stampiamo lo stato iniziale
        inventario.stampaInventario();
        
        // Creiamo thread che simulano operazioni di aggiornamento dell'inventario
        Thread threadAggiunta = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                inventario.aggiungiProdotto("Laptop", 2);
                inventario.aggiungiProdotto("Monitor", 1);
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }, "Thread-Aggiunta");
        
        Thread threadRimozione = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                inventario.rimuoviProdotto("Smartphone", 1);
                inventario.rimuoviProdotto("Tablet", 2);
                try { Thread.sleep(50); } catch (InterruptedException e) { }
            }
        }, "Thread-Rimozione");
        
        Thread threadLettura1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                inventario.getQuantita("Laptop");
                inventario.getQuantita("Smartphone");
                try { Thread.sleep(30); } catch (InterruptedException e) { }
            }
        }, "Thread-Lettura1");
        
        Thread threadLettura2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                inventario.getQuantita("Tablet");
                inventario.getQuantita("Monitor");
                try { Thread.sleep(30); } catch (InterruptedException e) { }
            }
        }, "Thread-Lettura2");
        
        // Misuriamo il tempo di esecuzione
        long inizio = System.currentTimeMillis();
        
        // Avviamo i thread
        threadAggiunta.start();
        threadRimozione.start();
        threadLettura1.start();
        threadLettura2.start();
        
        // Attendiamo che tutti i thread completino
        try {
            threadAggiunta.join();
            threadRimozione.join();
            threadLettura1.join();
            threadLettura2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        long fine = System.currentTimeMillis();
        
        // Stampiamo lo stato finale e il tempo impiegato
        System.out.println("\nStato finale dell'inventario:");
        inventario.stampaInventario();
        System.out.println("\nTempo di esecuzione: " + (fine - inizio) + " ms");
    }
}

/**
 * Interfaccia che definisce le operazioni di base di un inventario
 */
interface Inventario {
    void aggiungiProdotto(String nome, int quantita);
    boolean rimuoviProdotto(String nome, int quantita);
    int getQuantita(String nome);
    void stampaInventario();
}

/**
 * Implementazione dell'inventario con sincronizzazione a grana grossa.
 * Utilizza un unico lock per proteggere l'intero inventario.
 */
class InventarioGranaGrossa implements Inventario {
    private final java.util.Map<String, Integer> prodotti = new java.util.HashMap<>();
    private final Object lock = new Object(); // Lock unico per tutto l'inventario
    
    @Override
    public void aggiungiProdotto(String nome, int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva");
        }
        
        synchronized (lock) {
            // Simuliamo un'operazione che richiede tempo
            try { Thread.sleep(20); } catch (InterruptedException e) { }
            
            int quantitaAttuale = prodotti.getOrDefault(nome, 0);
            prodotti.put(nome, quantitaAttuale + quantita);
            
            System.out.println(Thread.currentThread().getName() + 
                    ": Aggiunti " + quantita + " " + nome + 
                    " (Nuovo totale: " + (quantitaAttuale + quantita) + ")");
        }
    }
    
    @Override
    public boolean rimuoviProdotto(String nome, int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva");
        }
        
        synchronized (lock) {
            // Simuliamo un'operazione che richiede tempo
            try { Thread.sleep(20); } catch (InterruptedException e) { }
            
            int quantitaAttuale = prodotti.getOrDefault(nome, 0);
            
            if (quantitaAttuale < quantita) {
                System.out.println(Thread.currentThread().getName() + 
                        ": Impossibile rimuovere " + quantita + " " + nome + 
                        " (Disponibili: " + quantitaAttuale + ")");
                return false;
            }
            
            prodotti.put(nome, quantitaAttuale - quantita);
            
            System.out.println(Thread.currentThread().getName() + 
                    ": Rimossi " + quantita + " " + nome + 
                    " (Nuovo totale: " + (quantitaAttuale - quantita) + ")");
            return true;
        }
    }
    
    @Override
    public int getQuantita(String nome) {
        synchronized (lock) {
            // Simuliamo un'operazione di lettura che richiede tempo
            try { Thread.sleep(10); } catch (InterruptedException e) { }
            
            int quantita = prodotti.getOrDefault(nome, 0);
            
            System.out.println(Thread.currentThread().getName() + 
                    ": Lettura quantità di " + nome + ": " + quantita);
            
            return quantita;
        }
    }
    
    @Override
    public void stampaInventario() {
        synchronized (lock) {
            System.out.println("Inventario (Grana Grossa):");
            if (prodotti.isEmpty()) {
                System.out.println("  [Vuoto]");
            } else {
                prodotti.forEach((nome, quantita) -> 
                    System.out.println("  - " + nome + ": " + quantita)
                );
            }
        }
    }
}

/**
 * Implementazione dell'inventario con sincronizzazione a grana fine.
 * Utilizza lock separati per ogni prodotto, permettendo operazioni
 * concorrenti su prodotti diversi.
 */
class InventarioGranaFine implements Inventario {
    private final java.util.Map<String, Integer> prodotti = new java.util.HashMap<>();
    private final java.util.Map<String, Object> locks = new java.util.HashMap<>();
    private final Object inventarioLock = new Object(); // Lock per l'intero inventario, usato solo quando necessario
    
    /**
     * Ottiene il lock per un prodotto specifico, creandolo se non esiste
     */
    private Object getLockPerProdotto(String nome) {
        synchronized (inventarioLock) {
            return locks.computeIfAbsent(nome, k -> new Object());
        }
    }
    
    @Override
    public void aggiungiProdotto(String nome, int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva");
        }
        
        // Otteniamo il lock specifico per questo prodotto
        Object prodottoLock = getLockPerProdotto(nome);
        
        synchronized (prodottoLock) {
            // Simuliamo un'operazione che richiede tempo
            try { Thread.sleep(20); } catch (InterruptedException e) { }
            
            int quantitaAttuale;
            
            // Leggiamo la quantità attuale (richiede lock sull'inventario)
            synchronized (inventarioLock) {
                quantitaAttuale = prodotti.getOrDefault(nome, 0);
            }
            
            // Aggiorniamo la quantità (richiede lock sull'inventario)
            synchronized (inventarioLock) {
                prodotti.put(nome, quantitaAttuale + quantita);
            }
            
            System.out.println(Thread.currentThread().getName() + 
                    ": Aggiunti " + quantita + " " + nome + 
                    " (Nuovo totale: " + (quantitaAttuale + quantita) + ")");
        }
    }
    
    @Override
    public boolean rimuoviProdotto(String nome, int quantita) {
        if (quantita <= 0) {
            throw new IllegalArgumentException("La quantità deve essere positiva");
        }
        
        // Otteniamo il lock specifico per questo prodotto
        Object prodottoLock = getLockPerProdotto(nome);
        
        synchronized (prodottoLock) {
            // Simuliamo un'operazione che richiede tempo
            try { Thread.sleep(20); } catch (InterruptedException e) { }
            
            int quantitaAttuale;
            
            // Leggiamo la quantità attuale (richiede lock sull'inventario)
            synchronized (inventarioLock) {
                quantitaAttuale = prodotti.getOrDefault(nome, 0);
            }
            
            if (quantitaAttuale < quantita) {
                System.out.println(Thread.currentThread().getName() + 
                        ": Impossibile rimuovere " + quantita + " " + nome + 
                        " (Disponibili: " + quantitaAttuale + ")");
                return false;
            }
            
            // Aggiorniamo la quantità (richiede lock sull'inventario)
            synchronized (inventarioLock) {
                prodotti.put(nome, quantitaAttuale - quantita);
            }
            
            System.out.println(Thread.currentThread().getName() + 
                    ": Rimossi " + quantita + " " + nome + 
                    " (Nuovo totale: " + (quantitaAttuale - quantita) + ")");
            return true;
        }
    }
    
    @Override
    public int getQuantita(String nome) {
        // Otteniamo il lock specifico per questo prodotto
        Object prodottoLock = getLockPerProdotto(nome);
        
        synchronized (prodottoLock) {
            // Simuliamo un'operazione di lettura che richiede tempo
            try { Thread.sleep(10); } catch (InterruptedException e) { }
            
            int quantita;
            
            // Leggiamo la quantità (richiede lock sull'inventario)
            synchronized (inventarioLock) {
                quantita = prodotti.getOrDefault(nome, 0);
            }
            
            System.out.println(Thread.currentThread().getName() + 
                    ": Lettura quantità di " + nome + ": " + quantita);
            
            return quantita;
        }
    }
    
    @Override
    public void stampaInventario() {
        // Per stampare l'intero inventario, abbiamo bisogno del lock globale
        synchronized (inventarioLock) {
            System.out.println("Inventario (Grana Fine):");
            if (prodotti.isEmpty()) {
                System.out.println("  [Vuoto]");
            } else {
                prodotti.forEach((nome, quantita) -> 
                    System.out.println("  - " + nome + ": " + quantita)
                );
            }
        }
    }
}