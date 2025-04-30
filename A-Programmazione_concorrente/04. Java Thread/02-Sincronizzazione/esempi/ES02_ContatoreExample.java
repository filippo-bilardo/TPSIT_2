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