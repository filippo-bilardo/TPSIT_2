/**
 * Generazione di numeri casuali con la classe Random
 * 
 * @author Filippo Bilardo
 * @version 1.0
 * @since 30/11/2024
 */
import java.util.Random;

public class NumeriRandom {
    final static int NUMELEM = 10;
    final static int MAX = 100;
    final static int MIN = 50;

    public static void stampaVettore(int[] v) {
        for (int i = 0; i < v.length; i++)
            System.out.print(v[i] + " ");
        System.out.println();
    }
    
    public static void main(String[] args) {
        // creo il vettore di NUMELEM elementi
        int mioVettore[] = new int[NUMELEM];
    
        // creo un oggetto Random
        Random r = new Random();

        // Riempio il vettori con numeri casuali
        // e ne visualizzo il contenuto
        for (int i = 0; i < mioVettore.length; i++)
            mioVettore[i] = r.nextInt();
        System.out.println("Stama del vettore con numeri casuali");
        stampaVettore(mioVettore);

        // Riempio il vettori con numeri casuali tra 0 e MAX
        // e ne visualizzo il contenuto
        for (int i = 0; i < mioVettore.length; i++)
            mioVettore[i] = r.nextInt(MAX);
        System.out.println("Stama del vettore con numeri tra 0 e " + MAX);
        stampaVettore(mioVettore);

        // Riempio il vettori con numeri casuali tra MIN e MAX compresi
        // e ne visualizzo il contenuto    
        for (int i = 0; i < mioVettore.length; i++)
            mioVettore[i] = r.nextInt(MAX-MIN+1)+MIN;
        System.out.println("Stama del vettore con numeri tra " + MIN + " e " + MAX + " compresi");
        stampaVettore(mioVettore);
    }
    
}
