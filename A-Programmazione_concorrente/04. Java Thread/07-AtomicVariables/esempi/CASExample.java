package esempi;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Esempio di utilizzo dell'algoritmo Compare-and-Swap (CAS) per implementare
 * una struttura dati lock-free: una pila (stack) concorrente.
 */
public class CASExample {
    
    /**
     * Classe che rappresenta un nodo della pila.
     */
    private static class Node<T> {
        private final T value;
        private Node<T> next;
        
        public Node(T value) {
            this.value = value;
        }
    }
    
    /**
     * Implementazione di una pila concorrente lock-free utilizzando CAS.
     */
    public static class ConcurrentStack<T> {
        private final AtomicReference<Node<T>> head = new AtomicReference<>(null);
        
        /**
         * Inserisce un elemento in cima alla pila.
         * 
         * @param value Il valore da inserire
         */
        public void push(T value) {
            Node<T> newHead = new Node<>(value);
            Node<T> oldHead;
            
            do {
                oldHead = head.get();
                newHead.next = oldHead;
            } while (!head.compareAndSet(oldHead, newHead));
        }
        
        /**
         * Rimuove e restituisce l'elemento in cima alla pila.
         * 
         * @return L'elemento rimosso, o null se la pila è vuota
         */
        public T pop() {
            Node<T> oldHead;
            Node<T> newHead;
            
            do {
                oldHead = head.get();
                if (oldHead == null) {
                    return null; // Pila vuota
                }
                newHead = oldHead.next;
            } while (!head.compareAndSet(oldHead, newHead));
            
            return oldHead.value;
        }
        
        /**
         * Verifica se la pila è vuota.
         * 
         * @return true se la pila è vuota, false altrimenti
         */
        public boolean isEmpty() {
            return head.get() == null;
        }
    }
    
    public static void main(String[] args) {
        // Creazione di una pila concorrente
        ConcurrentStack<Integer> stack = new ConcurrentStack<>();
        
        // Inserimento di elementi
        System.out.println("Inserimento di elementi nella pila...");
        for (int i = 1; i <= 5; i++) {
            stack.push(i);
            System.out.println("Inserito: " + i);
        }
        
        // Estrazione di elementi
        System.out.println("\nEstrazione di elementi dalla pila...");
        Integer value;
        while ((value = stack.pop()) != null) {
            System.out.println("Estratto: " + value);
        }
        
        // Verifica che la pila sia vuota
        System.out.println("\nLa pila è vuota? " + stack.isEmpty());
        
        // Dimostrazione del ciclo di retry CAS
        System.out.println("\nDimostrazione del ciclo di retry CAS:");
        AtomicReference<Integer> atomicRef = new AtomicReference<>(10);
        
        boolean success = atomicRef.compareAndSet(10, 20);
        System.out.println("Primo CAS (10 -> 20): " + (success ? "successo" : "fallimento"));
        
        success = atomicRef.compareAndSet(10, 30);
        System.out.println("Secondo CAS (10 -> 30): " + (success ? "successo" : "fallimento"));
        
        success = atomicRef.compareAndSet(20, 30);
        System.out.println("Terzo CAS (20 -> 30): " + (success ? "successo" : "fallimento"));
        
        System.out.println("Valore finale: " + atomicRef.get());
    }
}