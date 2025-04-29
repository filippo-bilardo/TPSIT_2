/**
 * Implementazione di un sistema di eventi con publisher e subscriber.
 * Questo esempio mostra come implementare un meccanismo di comunicazione
 * basato su eventi tra diversi componenti di un'applicazione.
 */
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class EventDispatcher {
    
    /**
     * Metodo main che dimostra l'utilizzo del sistema di eventi.
     */
    public static void main(String[] args) {
        // Crea il dispatcher di eventi
        EventBus eventBus = new EventBus();
        
        // Registra alcuni subscriber
        eventBus.subscribe("login", event -> {
            System.out.println("Subscriber 1: Utente " + event.getData() + " ha effettuato il login");
        });
        
        eventBus.subscribe("login", event -> {
            System.out.println("Subscriber 2: Notifica di sicurezza per login di " + event.getData());
        });
        
        eventBus.subscribe("logout", event -> {
            System.out.println("Subscriber 3: Utente " + event.getData() + " ha effettuato il logout");
        });
        
        // Crea alcuni publisher che pubblicano eventi in thread separati
        new Thread(() -> {
            // Simula eventi di login
            eventBus.publish(new Event("login", "user1@example.com"));
            try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            eventBus.publish(new Event("login", "user2@example.com"));
        }).start();
        
        new Thread(() -> {
            // Simula eventi di logout
            try { Thread.sleep(2000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            eventBus.publish(new Event("logout", "user1@example.com"));
        }).start();
    }
    
    /**
     * Interfaccia funzionale per i subscriber di eventi.
     */
    @FunctionalInterface
    interface EventSubscriber {
        void onEvent(Event event);
    }
    
    /**
     * Classe che rappresenta un evento nel sistema.
     */
    static class Event {
        private final String tipo;
        private final Object data;
        private final long timestamp;
        
        public Event(String tipo, Object data) {
            this.tipo = tipo;
            this.data = data;
            this.timestamp = System.currentTimeMillis();
        }
        
        public String getTipo() {
            return tipo;
        }
        
        public Object getData() {
            return data;
        }
        
        public long getTimestamp() {
            return timestamp;
        }
    }
    
    /**
     * Classe che implementa un bus di eventi thread-safe.
     */
    static class EventBus {
        // Mappa che associa ogni tipo di evento ai suoi subscriber
        private final Map<String, Set<EventSubscriber>> subscribers = new ConcurrentHashMap<>();
        
        /**
         * Registra un subscriber per un determinato tipo di evento.
         */
        public void subscribe(String eventType, EventSubscriber subscriber) {
            // Ottiene o crea il set di subscriber per questo tipo di evento
            Set<EventSubscriber> eventSubscribers = subscribers.computeIfAbsent(
                eventType, k -> new CopyOnWriteArraySet<>()
            );
            
            // Aggiunge il subscriber al set
            eventSubscribers.add(subscriber);
            
            System.out.println("Subscriber registrato per l'evento: " + eventType);
        }
        
        /**
         * Cancella la registrazione di un subscriber per un determinato tipo di evento.
         */
        public void unsubscribe(String eventType, EventSubscriber subscriber) {
            // Ottiene il set di subscriber per questo tipo di evento
            Set<EventSubscriber> eventSubscribers = subscribers.get(eventType);
            
            if (eventSubscribers != null) {
                // Rimuove il subscriber dal set
                eventSubscribers.remove(subscriber);
                System.out.println("Subscriber rimosso per l'evento: " + eventType);
            }
        }
        
        /**
         * Pubblica un evento a tutti i subscriber interessati.
         */
        public void publish(Event event) {
            // Ottiene il set di subscriber per questo tipo di evento
            Set<EventSubscriber> eventSubscribers = subscribers.get(event.getTipo());
            
            if (eventSubscribers != null && !eventSubscribers.isEmpty()) {
                System.out.println("Pubblicazione evento: " + event.getTipo() + ", dati: " + event.getData());
                
                // Notifica tutti i subscriber
                for (EventSubscriber subscriber : eventSubscribers) {
                    // Esegue la notifica in un thread separato per non bloccare il publisher
                    new Thread(() -> subscriber.onEvent(event)).start();
                }
            } else {
                System.out.println("Nessun subscriber per l'evento: " + event.getTipo());
            }
        }
    }
}