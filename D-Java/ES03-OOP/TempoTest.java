public class TempoTest {
    public static void main(String[] args) {
        // Creazione di oggetti Tempo
        Tempo t1 = new Tempo(23, 59, 59); // 23:59:59
        Tempo t2 = new Tempo(12, 30, 0);  // 12:30:00
        Tempo t3 = new Tempo();           // 00:00:00

        // Incremento dei secondi
        System.out.println("Prima dell'incremento: " + t1);
        t1.incrementaSecondi();  // Incrementa di 1 secondo
        System.out.println("Dopo l'incremento: " + t1);  // Output: 00:00:00

        // Calcolo della differenza tra t1 e t2
        Tempo differenza = t1.calcolaDifferenza(t2);
        System.out.println("Differenza tra t1 e t2: " + differenza);  // Output: differenza in hh:mm:ss

        // Verifica dell'uguaglianza
        Tempo t4 = new Tempo(0, 0, 0);
        System.out.println("t1 è uguale a t3? " + t1.equals(t3));  // Output: true
        System.out.println("t1 è uguale a t4? " + t1.equals(t4));  // Output: true
    }
}
