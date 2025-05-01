// Esempio di Deadlock
// Due thread si bloccano a vicenda aspettando risorse detenute dall'altro
public class ES07_Deadlock {
    private static final Object risorsa1 = new Object();
    private static final Object risorsa2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (risorsa1) {
                System.out.println("Thread 1: bloccato risorsa1");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 1: tenta di bloccare risorsa2");
                synchronized (risorsa2) {
                    System.out.println("Thread 1: bloccato risorsa2");
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (risorsa2) {
                System.out.println("Thread 2: bloccato risorsa2");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 2: tenta di bloccare risorsa1");
                synchronized (risorsa1) {
                    System.out.println("Thread 2: bloccato risorsa1");
                }
            }
        });
        t1.start();
        t2.start();
    }
}