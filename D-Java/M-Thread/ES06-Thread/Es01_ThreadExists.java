import java.lang.*;

public class Es01_ThreadExists {
    public static void main(String args[]) {
        // currentThread() della classe Thread. 
        Thread t = Thread.currentThread(); 
        // Questo metodo restituisce l'indirizzo dell'oggetto Thread che 
        // sta eseguendo l'istruzione, che viene assegnato al reference t.
        t.setName("Thread principale");// gli assegno un nome
        t.setPriority(10); // ne definisco una priorità rispetto ad altri
        // thread
        System.out.println("Thread in esecuzione: " + t);// ne scrivo
        try { // le informazioni
            for (int n = 5; n > 0; n--) {
                System.out.println("" + n);
                Thread.sleep(1000); // ne fermo l’esecuzione 5 volte per un test
            }
        } catch (InterruptedException e) {
            System.out.println("Thread interrotto");
        }
    }
}
