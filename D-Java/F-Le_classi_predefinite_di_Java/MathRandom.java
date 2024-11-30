public class MathRandom {
  final static int TANTI = 8;
  final static int MIN = 10;
  final static int MAX = 30;

  public static void main(String[] args) {
    // creo il vettore di TANTI elementi
    int mioVettore[] = new int[TANTI];

    // riempio il vettori con numeri casuali tra 0 e MAX
    for (int x = 0; x < mioVettore.length; x++)
      // il casting è necessario per evitare che il risultato sia un double
      mioVettore[x] = (int) (MAX * Math.random());

    // visualizzo il contenuto del vettore
    System.out.println("Stama del vettore con numeri tra 0 e " + MAX);
    for (int x = 0; x < mioVettore.length; x++)
      System.out.print(mioVettore[x] + " ");
    System.out.println();

    // riempio il vettori con numeri casuali tra 0 e MAX
    for (int x = 0; x < mioVettore.length; x++)
      mioVettore[x] = MIN +(int)((MAX-MIN) * Math.random());

    // visualizzo il contenuto del vettore
    System.out.println("Stama del vettore con numeri tra " + MIN + " e " + MAX);
    for (int x = 0; x < mioVettore.length; x++)
      System.out.print(mioVettore[x] + " ");
    System.out.println();
    }
}
