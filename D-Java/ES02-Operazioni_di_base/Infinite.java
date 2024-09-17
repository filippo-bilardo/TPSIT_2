public class Infinite {

	public static void main(String[] args) {
		// 1 diviso 0 da errore con i numeri interi		
		try {
			int intinf = 1/0;
			System.out.println("1/0 in int è: "+intinf);
		} catch (ArithmeticException e) {
			System.out.println("1/0 in int genera una eccezione");
		}
		double plusinf = 1.0/0;
		double minusinf = -1.0/0;
		double nan = 0.0/0;
		System.out.println("1/0 in double è: " + plusinf);
		System.out.println("-1/0 in double è: " + minusinf);
		System.out.println("0/0 in double è: " + nan);
		
		// I valori infiniti si comportano come i limiti
		System.out.println("3.0 + Infinity = " + (3.0 + plusinf) );
		System.out.println("Infinity + Infinity = " + (plusinf + plusinf) );
		System.out.println("Infinity - Infinity = " + (plusinf - plusinf) );
		
		// Una volta ottenuto NaN non se ne esce più
		System.out.println("NaN + 3 = " + (nan + 3.0) );
		System.out.println("NaN * 0 = " + (nan + 0.0) );
		
		if (Double.isInfinite(plusinf)) {
			System.out.println("Si può controllare se un valore è infinito");
		}
	}

}
