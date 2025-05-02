/**
 * Esempio di utilizzo di una classe anonima per creare un thread
 * in Java. In questo esempio, viene creata una classe anonima
 * che implementa l'interfaccia Runnable e viene utilizzata
 * per avviare un thread. Inoltre, viene mostrato come
 * utilizzare le espressioni lambda per semplificare il codice.
 */

public class ES02d_ThreadClasseAnonima {
    public static void main(String[] args) {
        
        // Thread anonimo con classe anonima
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread anonimo in esecuzione");
            }
        });
        thread.start();
        
        // Con Java 8+ si può usare la lambda expression per creare un thread in modo più conciso.
        // 1. L'espressione lambda () -> System.out.println("Thread lambda in esecuzione") 
        //    rappresenta un'implementazione del metodo run()
        // 2. Il compilatore Java sa che il costruttore di Thread accetta un Runnable
        // 3. Poiché Runnable è un'interfaccia funzionale con un solo metodo astratto,
        //    il compilatore può abbinare automaticamente l'espressione lambda al metodo run()
        new Thread(() -> System.out.println("Thread lambda in esecuzione")).start();
    }
}
/**
 * Output:
 * 
    Thread anonimo in esecuzione
    Thread lambda in esecuzione
 */