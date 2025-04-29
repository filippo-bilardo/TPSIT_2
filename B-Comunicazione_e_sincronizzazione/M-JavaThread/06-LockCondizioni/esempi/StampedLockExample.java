package esempi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * Esempio di utilizzo di StampedLock per implementare una struttura dati concorrente
 * con accesso ottimistico in lettura.
 * Questo esempio mostra come utilizzare le tre modalità di StampedLock: lettura, scrittura
 * e lettura ottimistica.
 */
public class StampedLockExample {
    
    /**
     * Implementazione di una mappa di punti 2D thread-safe con StampedLock.
     */
    private static class PointMap {
        private final Map<String, Point> points = new HashMap<>();
        private final StampedLock lock = new StampedLock();
        
        /**
         * Aggiunge un punto alla mappa.
         * Utilizza un lock di scrittura per garantire l'esclusività dell'operazione.
         */
        public void addPoint(String id, double x, double y) {
            long stamp = lock.writeLock();
            try {
                points.put(id, new Point(x, y));
                System.out.println(Thread.currentThread().getName() + ": Aggiunto punto " + id + " = (" + x + ", " + y + ")");
            } finally {
                lock.unlockWrite(stamp);
            }
        }
        
        /**
         * Sposta un punto esistente.
         * Utilizza un lock di scrittura per garantire l'esclusività dell'operazione.
         */
        public void movePoint(String id, double deltaX, double deltaY) {
            long stamp = lock.writeLock();
            try {
                Point p = points.get(id);
                if (p != null) {
                    p.move(deltaX, deltaY);
                    System.out.println(Thread.currentThread().getName() + ": Spostato punto " + id + " di (" + deltaX + ", " + deltaY + ")");
                }
            } finally {
                lock.unlockWrite(stamp);
            }
        }
        
        /**
         * Ottiene un punto dalla mappa.
         * Utilizza un lock di lettura tradizionale.
         */
        public Point getPoint(String id) {
            long stamp = lock.readLock();
            try {
                Point p = points.get(id);
                if (p != null) {
                    // Crea una copia per evitare modifiche esterne
                    return new Point(p.x, p.y);
                }
                return null;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        
        /**
         * Calcola la distanza di un punto dall'origine.
         * Utilizza una lettura ottimistica per migliorare le prestazioni.
         */
        public double distanceFromOrigin(String id) {
            // Primo tentativo: lettura ottimistica
            long stamp = lock.tryOptimisticRead();
            Point p = points.get(id);
            
            if (p == null) {
                return -1.0;  // Punto non trovato
            }
            
            // Leggi le coordinate
            double currentX = p.x;
            double currentY = p.y;
            
            // Verifica se i dati sono stati modificati durante la lettura
            if (!lock.validate(stamp)) {
                // Lettura ottimistica fallita, fallback a un lock di lettura tradizionale
                stamp = lock.readLock();
                try {
                    p = points.get(id);
                    if (p == null) {
                        return -1.0;  // Punto non trovato
                    }
                    currentX = p.x;
                    currentY = p.y;
                } finally {
                    lock.unlockRead(stamp);
                }
            }
            
            // Calcola la distanza dall'origine
            return Math.sqrt(currentX * currentX + currentY * currentY);
        }
        
        /**
         * Aggiorna un punto se necessario.
         * Dimostra la conversione da lettura a scrittura.
         */
        public void normalizeIfTooFar(String id, double maxDistance) {
            // Inizia con un lock di lettura
            long stamp = lock.readLock();
            try {
                Point p = points.get(id);
                if (p == null) {
                    return;  // Punto non trovato
                }
                
                double distance = Math.sqrt(p.x * p.x + p.y * p.y);
                if (distance > maxDistance) {
                    // Necessario normalizzare il punto, tenta di convertire a lock di scrittura
                    long writeStamp = lock.tryConvertToWriteLock(stamp);
                    if (writeStamp != 0L) {
                        // Conversione riuscita
                        stamp = writeStamp;
                        double scale = maxDistance / distance;
                        p.x *= scale;
                        p.y *= scale;
                        System.out.println(Thread.currentThread().getName() + ": Normalizzato punto " + id + " a (" + p.x + ", " + p.y + ")");
                    } else {
                        // Conversione fallita, rilascia il lock di lettura e acquisisce un lock di scrittura
                        lock.unlockRead(stamp);
                        stamp = lock.writeLock();
                        // Ricontrolla dopo aver acquisito il lock di scrittura
                        p = points.get(id);
                        if (p != null) {
                            distance = Math.sqrt(p.x * p.x + p.y * p.y);
                            if (distance > maxDistance) {
                                double scale = maxDistance / distance;
                                p.x *= scale;
                                p.y *= scale;
                                System.out.println(Thread.currentThread().getName() + ": Normalizzato punto " + id + " a (" + p.x + ", " + p.y + ")");
                            }
                        }
                    }
                }
            } finally {
                // Rilascia il lock (sia di lettura che di scrittura)
                lock.unlock(stamp);
            }
        }
        
        /**
         * Restituisce il numero di punti nella mappa.
         */
        public int size() {
            long stamp = lock.readLock();
            try {
                return points.size();
            } finally {
                lock.unlockRead(stamp);
            }
        }
    }
    
    /**
     * Classe che rappresenta un punto 2D.
     */
    private static class Point {
        private double x, y;
        
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public void move(double deltaX, double deltaY) {
            this.x += deltaX;
            this.y += deltaY;
        }
        
        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        // Creazione della mappa di punti
        PointMap pointMap = new PointMap();
        
        // Aggiunta di alcuni punti iniziali
        pointMap.addPoint("A", 3.0, 4.0);
        pointMap.addPoint("B", 1.0, 1.0);
        pointMap.addPoint("C", 5.0, 12.0);
        
        // Creazione di un pool di thread
        ExecutorService executor = Executors.newFixedThreadPool(10);
        
        // Thread che eseguono letture ottimistiche
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 3; j++) {
                    String id = String.valueOf((char)('A' + ThreadLocalRandom.current().nextInt(3)));
                    double distance = pointMap.distanceFromOrigin(id);
                    System.out.println(Thread.currentThread().getName() + ": Distanza del punto " + id + " dall'origine: " + distance);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Thread che eseguono spostamenti (scritture)
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 2; j++) {
                    String id = String.valueOf((char)('A' + ThreadLocalRandom.current().nextInt(3)));
                    double deltaX = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
                    double deltaY = ThreadLocalRandom.current().nextDouble(-1.0, 1.0);
                    pointMap.movePoint(id, deltaX, deltaY);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Thread che eseguono normalizzazioni (conversione da lettura a scrittura)
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 3; j++) {
                    String id = String.valueOf((char)('A' + ThreadLocalRandom.current().nextInt(3)));
                    pointMap.normalizeIfTooFar(id, 10.0);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Attesa di un po' di tempo per l'esecuzione
        Thread.sleep(5000);
        
        // Stampa finale dei punti
        System.out.println("\nPunti finali:");
        for (char id = 'A'; id <= 'C'; id++) {
            Point p = pointMap.getPoint(String.valueOf(id));
            System.out.println(id + ": " + p + ", distanza: " + pointMap.distanceFromOrigin(String.valueOf(id)));
        }
        
        // Chiusura ordinata del pool di thread
        executor.shutdownNow();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}