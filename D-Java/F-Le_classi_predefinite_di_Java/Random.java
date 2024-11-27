public class Random {
  final static int TANTI = 8; // costanti condivise
  final static int MAX = 30;

  public static void main(String[] args) {
    // creo il vettore di TANTI elementi
    int mioVettore[] = new int[TANTI];

    // riempio il vettori con numeri casuali tra 0 e MAX
    for (int x = 0; x < mioVettore.length; x++)
      // il casting è necessario per evitare che il risultato sia un double
      mioVettore[x] = (int) (MAX * Math.random());

    // visualizzo il contenuto del vettore
    for (int x = 0; x < mioVettore.length; x++)
      System.out.print(mioVettore[x] + " ");
  }
}
