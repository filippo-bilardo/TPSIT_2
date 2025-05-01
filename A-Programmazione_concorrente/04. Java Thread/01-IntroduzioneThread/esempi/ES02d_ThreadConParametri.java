/**
 * Esempio 02: Thread con parametri
 *
 * Questo esempio mostra come creare un thread che riceve parametri in ingresso.
 */
public class ES02d_ThreadConParametri implements Runnable {
    private String messaggio;
    private int ripetizioni;
    
    public ES02d_ThreadConParametri(String messaggio, int ripetizioni) {
        this.messaggio = messaggio;
        this.ripetizioni = ripetizioni;
    }
    
    @Override
    public void run() { 
        System.out.println("Inizio " + Thread.currentThread().getName());
        for (int i = 0; i < ripetizioni; i++) {
            System.out.println(messaggio + " (" + (i+1) + "/" + ripetizioni + ")");
            try {
                Thread.sleep(1000); // Pausa di 1 secondo
            } catch (InterruptedException e) {
                System.out.println("Thread interrotto");
                return;
            }
        }
    }
    
    public static void main(String[] args) {
        ES02d_ThreadConParametri runnable1 = new ES02d_ThreadConParametri("Primo thread", 5);
        Thread t1 = new Thread(runnable1, "Thread1");
        Thread t2 = new Thread(new ES02d_ThreadConParametri("Secondo thread", 3), "Thread2");
        
        t1.start();
        t2.start();
    }
}
