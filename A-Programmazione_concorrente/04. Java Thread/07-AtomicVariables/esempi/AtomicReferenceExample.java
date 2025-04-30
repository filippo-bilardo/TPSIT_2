package esempi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Esempio di utilizzo di AtomicReference per aggiornare atomicamente oggetti complessi.
 * Questo esempio mostra come aggiornare un oggetto in modo thread-safe senza utilizzare lock.
 */
public class AtomicReferenceExample {
    
    /**
     * Classe che rappresenta un utente con nome e età.
     */
    private static class User {
        private final String name;
        private final int age;
        
        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        public String getName() {
            return name;
        }
        
        public int getAge() {
            return age;
        }
        
        @Override
        public String toString() {
            return "User{name='" + name + "', age=" + age + "}";
        }
    }
    
    /**
     * Classe che gestisce un utente utilizzando AtomicReference.
     */
    public static class AtomicUserManager {
        private final AtomicReference<User> userRef;
        
        public AtomicUserManager(User initialUser) {
            userRef = new AtomicReference<>(initialUser);
        }
        
        /**
         * Aggiorna l'età dell'utente in modo atomico.
         * 
         * @param newAge La nuova età da impostare
         * @return true se l'aggiornamento è riuscito, false altrimenti
         */
        public boolean updateAge(int newAge) {
            User currentUser;
            User newUser;
            
            do {
                currentUser = userRef.get();
                newUser = new User(currentUser.getName(), newAge);
            } while (!userRef.compareAndSet(currentUser, newUser));
            
            return true;
        }
        
        /**
         * Tenta di aggiornare il nome dell'utente solo se l'età corrisponde al valore atteso.
         * 
         * @param expectedAge L'età attesa per l'aggiornamento
         * @param newName Il nuovo nome da impostare
         * @return true se l'aggiornamento è riuscito, false altrimenti
         */
        public boolean updateNameIfAgeMatches(int expectedAge, String newName) {
            User currentUser = userRef.get();
            
            if (currentUser.getAge() != expectedAge) {
                return false; // L'età non corrisponde, aggiornamento fallito
            }
            
            User newUser = new User(newName, currentUser.getAge());
            return userRef.compareAndSet(currentUser, newUser);
        }
        
        /**
         * Ottiene l'utente corrente.
         * 
         * @return L'utente corrente
         */
        public User getUser() {
            return userRef.get();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Creazione di un gestore utente con un utente iniziale
        User initialUser = new User("Mario", 30);
        AtomicUserManager userManager = new AtomicUserManager(initialUser);
        
        System.out.println("Utente iniziale: " + userManager.getUser());
        
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // Aggiornamento dell'età da parte di più thread
        for (int i = 0; i < 5; i++) {
            final int newAge = 30 + i;
            executor.submit(() -> {
                boolean success = userManager.updateAge(newAge);
                System.out.println("Thread " + Thread.currentThread().getId() + 
                        ": Aggiornamento età a " + newAge + " - " + 
                        (success ? "successo" : "fallimento"));
            });
        }
        
        // Attesa del completamento dei thread di aggiornamento età
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        
        System.out.println("\nUtente dopo aggiornamenti età: " + userManager.getUser());
        
        // Creazione di un nuovo pool di thread
        executor = Executors.newFixedThreadPool(3);
        
        // Tentativo di aggiornamento del nome da parte di più thread
        String[] newNames = {"Luigi", "Giovanni", "Paolo"};
        int currentAge = userManager.getUser().getAge();
        
        for (String newName : newNames) {
            executor.submit(() -> {
                boolean success = userManager.updateNameIfAgeMatches(currentAge, newName);
                System.out.println("Thread " + Thread.currentThread().getId() + 
                        ": Aggiornamento nome a '" + newName + "' - " + 
                        (success ? "successo" : "fallimento"));
            });
        }
        
        // Attesa del completamento dei thread di aggiornamento nome
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.SECONDS);
        
        System.out.println("\nUtente finale: " + userManager.getUser());
        
        // Dimostrazione di AtomicReference con operazioni di base
        System.out.println("\nDimostrazione di operazioni base con AtomicReference:");
        AtomicReference<String> atomicString = new AtomicReference<>("valore iniziale");
        
        System.out.println("Valore iniziale: " + atomicString.get());
        
        // Set atomico
        atomicString.set("nuovo valore");
        System.out.println("Dopo set: " + atomicString.get());
        
        // CompareAndSet
        boolean success = atomicString.compareAndSet("nuovo valore", "valore aggiornato");
        System.out.println("CAS (nuovo valore -> valore aggiornato): " + 
                (success ? "successo" : "fallimento") + ", valore: " + atomicString.get());
        
        // CompareAndSet fallito
        success = atomicString.compareAndSet("valore errato", "altro valore");
        System.out.println("CAS (valore errato -> altro valore): " + 
                (success ? "successo" : "fallimento") + ", valore: " + atomicString.get());
    }
}