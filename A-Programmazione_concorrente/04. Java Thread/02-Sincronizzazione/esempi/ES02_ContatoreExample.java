/**
 * Esempio 02: Contatore
 * 
 * In questo esempio, abbiamo una classe Contatore che mantiene un conteggio
 * intero e fornisce metodi per incrementarlo e recuperare il valore corrente.
 *
 * La classe Contatore è sincronizzata, quindi può essere utilizzata da più
 * thread in modo sicuro.
 *
 * La classe Worker implementa l'interfaccia Runnable e rappresenta un thread
 * che incrementa il conteggio del contatore.
 *
 * Nel metodo main, creiamo tre thread che condividono lo stesso oggetto Contatore
 * e li avviamo. Ogni thread incrementa il conteggio del contatore 1000 volte.
 *
 * Dopo aver avviato i thread, attendiamo che tutti abbiano terminato utilizzando
 * il metodo join() per ciascun thread.
 *
 * Infine, stampiamo il conteggio finale ottenuto dal contatore.
 *
 * Il risultato finale dovrebbe essere 3000, poiché ciascun thread ha incrementato
 * il conteggio del contatore 1000 volte.
 *
 */
class Contatore {
    private int conteggio = 0;

    public synchronized void incrementa() {
        conteggio++;
    }

    public synchronized int getConteggio() {
        return conteggio;
    }
}

class Worker implements Runnable {
    private Contatore contatore;

    public Worker(Contatore contatore) {
        this.contatore = contatore;
    }

    public void run() {
        for (int i = 0; i < 1000; i++) {
            contatore.incrementa();
        }
    }
}

public class ES02_ContatoreExample {
    public static void main(String[] args) throws InterruptedException {
        Contatore contatore = new Contatore();

        Thread t1 = new Thread(new Worker(contatore));
        Thread t2 = new Thread(new Worker(contatore));
        Thread t3 = new Thread(new Worker(contatore));

        t1.start();
        t2.start();
        t3.start();

        // Attendi che tutti i thread abbiano finito
        // prima di stampare il conteggio finale e terminare il programma
        // Il metodo join() blocca il thread chiamante fino a quando il thread chiamato non ha terminato.
        // In questo caso, il thread chiamante è il thread principale (main)
        t1.join();
        t2.join();
        t3.join();

        System.out.println("Conteggio finale: " + contatore.getConteggio());
    }
}