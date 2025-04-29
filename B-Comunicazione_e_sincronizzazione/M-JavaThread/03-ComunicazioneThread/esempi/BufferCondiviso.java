/**
 * Esempio di implementazione di un buffer thread-safe per lo scambio di dati tra thread.
 * Questo buffer supporta operazioni di inserimento e prelievo in modo thread-safe,
 * gestendo le condizioni di buffer pieno e buffer vuoto.
 */
public class BufferCondiviso<T> {
    private final T[] buffer;
    private int count = 0;      // Numero di elementi nel buffer
    private int putIndex = 0;   // Indice per l'inserimento
    private int getIndex = 0;   // Indice per il prelievo
    private final int capacita; // Capacità massima del buffer
    
    /**
     * Costruttore che inizializza il buffer con la capacità specificata.
     * 
     * @param capacita La capacità massima del buffer
     */
    @SuppressWarnings("unchecked")
    public BufferCondiviso(int capacita) {
        this.capacita = capacita;
        this.buffer = (T[]) new Object[capacita];
    }
    
    /**
     * Inserisce un elemento nel buffer.
     * Se il buffer è pieno, il thread chiamante viene messo in attesa.
     * 
     * @param item L'elemento da inserire
     * @throws InterruptedException Se il thread viene interrotto durante l'attesa
     */
    public synchronized void put(T item) throws InterruptedException {
        // Attende finché il buffer non è pieno
        while (count == capacita) {
            wait();
        }
        
        // Inserisce l'elemento nel buffer
        buffer[putIndex] = item;
        
        // Aggiorna l'indice di inserimento in modo circolare
        putIndex = (putIndex + 1) % capacita;
        
        // Incrementa il contatore degli elementi
        count++;
        
        // Notifica i thread in attesa (consumatori)
        notifyAll();
    }
    
    /**
     * Preleva un elemento dal buffer.
     * Se il buffer è vuoto, il thread chiamante viene messo in attesa.
     * 
     * @return L'elemento prelevato
     * @throws InterruptedException Se il thread viene interrotto durante l'attesa
     */
    public synchronized T get() throws InterruptedException {
        // Attende finché il buffer non è vuoto
        while (count == 0) {
            wait();
        }
        
        // Preleva l'elemento dal buffer
        T item = buffer[getIndex];
        
        // Rimuove il riferimento per aiutare il garbage collector
        buffer[getIndex] = null;
        
        // Aggiorna l'indice di prelievo in modo circolare
        getIndex = (getIndex + 1) % capacita;
        
        // Decrementa il contatore degli elementi
        count--;
        
        // Notifica i thread in attesa (produttori)
        notifyAll();
        
        return item;
    }
    
    /**
     * Restituisce il numero di elementi attualmente presenti nel buffer.
     * 
     * @return Il numero di elementi nel buffer
     */
    public synchronized int size() {
        return count;
    }
    
    /**
     * Verifica se il buffer è vuoto.
     * 
     * @return true se il buffer è vuoto, false altrimenti
     */
    public synchronized boolean isEmpty() {
        return count == 0;
    }
    
    /**
     * Verifica se il buffer è pieno.
     * 
     * @return true se il buffer è pieno, false altrimenti
     */
    public synchronized boolean isFull() {
        return count == capacita;
    }
    
    /**
     * Metodo main per testare il funzionamento del buffer condiviso.
     */
    public static void main(String[] args) {
        // Crea un buffer condiviso di interi con capacità 5
        BufferCondiviso<Integer> buffer = new BufferCondiviso<>(5);
        
        // Thread produttore
        Thread produttore = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    buffer.put(i);
                    System.out.println("Produttore: inserito " + i);
                    Thread.sleep(500); // Simula il tempo di produzione
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Thread consumatore
        Thread consumatore = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    int valore = buffer.get();
                    System.out.println("Consumatore: prelevato " + valore);
                    Thread.sleep(1000); // Simula il tempo di elaborazione
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Avvia i thread
        produttore.start();
        consumatore.start();
    }
}