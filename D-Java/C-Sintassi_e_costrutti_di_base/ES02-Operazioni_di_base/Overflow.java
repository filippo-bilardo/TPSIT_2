
public class Overflow {

	public static void main(String[] args) {

		// Il tipo byte in Java occupa 8 bit e usa la codifica in 
		// complemento a 2. I valori rappresentabili sono da -128 a +127.
		byte b;
		
		b = 124;
		b += 1;
		System.out.println("Corretto: " + b);
		
		b = 127;
		b += 1;
		System.out.println("Overflow: " + b);
		
		// Il tipo int in java occupa 32 bit e usa la codifica
		// in complemento a 2. I valori rappresentabili so da
		// -2147483648  a 2147483647. 
		int i;
		
		i = 127;
		i += 1;
		System.out.println("Corretto: " + i);
		
		i = 2147483647;
		i += 1;
		System.out.println("Overflow: " + i);
		
		int x = 2000000000;
		int y = 2000000000;
		int z = x + y;
		System.out.println("Overflow: " + z);
		
	}

}
