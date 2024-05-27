class Contatore {
    private int conteggio = 0;

    public void incrementa() {
        conteggio++;
    }

    public int getConteggio() {
        return conteggio;
    }
}

class Worker implements Runnable {
    private Contatore contatore;

    public Worker(Contatore contatore) {
        this.contatore = contatore;
    }

    public void run() {
        for (int i = 0; i < 10000; i++) {
            contatore.incrementa();
        }
    }
}

public class ContatoreExampleBad {
    public static void main(String[] args) throws InterruptedException {
        Contatore contatore = new Contatore();

        Thread t1 = new Thread(new Worker(contatore));
        Thread t2 = new Thread(new Worker(contatore));
        Thread t3 = new Thread(new Worker(contatore));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Conteggio finale: " + contatore.getConteggio());
    }
}