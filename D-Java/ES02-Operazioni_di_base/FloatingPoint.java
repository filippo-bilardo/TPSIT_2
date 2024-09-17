import java.math.BigDecimal;

public class FloatingPoint {

	public static void main(String[] args) {
		
		// il numero 0.1 è periodico in base 2
		double d = 0.1;
		System.out.println("Visualizzazione normale di d: " + d);
		System.out.println("Il vero valore di d: " + new BigDecimal(d));

		// 0.2 è uguale a 0.1 + 0.1
		double x1 = 0.2;
		double x2 = 0.1 + 0.1;;
		System.out.print("0.2 è uguale a 0.1 + 0.1: ");
		System.out.println(x1 == x2);
		System.out.println("Il vero valore di x1: " + new BigDecimal(x1));
		System.out.println("Il vero valore di x2: " + new BigDecimal(x2));

		// 0.3 non è uguale a 0.1 + 0.1
		x1 = 0.3;
		x2 = 0.1 + 0.1 + 0.1;
		System.out.print("0.3 è uguale a 0.1 + 0.1 + 0.1: ");
		System.out.println(x1 == x2);
		System.out.println("Il vero valore di x1: " + new BigDecimal(x1));
		System.out.println("Il vero valore di x2: " + new BigDecimal(x2));
		
		// 0.1 in double e float
		float f = 0.1f;
		System.out.println("Il vero valore di 0.1 in double: " + new BigDecimal(d));
		System.out.println("Il vero valore di 0.1 in float : " + new BigDecimal(f));	
	}
}
