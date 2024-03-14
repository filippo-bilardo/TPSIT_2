public class EsempiIndexOf {

    public static void main(String[] args) {

        String testo = "Ciao Mario come stai!";
        
        //indexOf
        int pos = testo.indexOf("Mario");
        System.out.println("Mario inizia alla posizione: "+pos);
        //indexOf 'M'
        pos = testo.indexOf('M');
        System.out.println("La prima occorrenza di 'M' è alla posizione: "+pos);

        //indexOf 'o'
        pos = testo.indexOf('o');
        System.out.println("La prima occorrenza di 'o' è alla posizione: "+pos);

        //indexOf 'o' a partire dalla posizione 5
        pos = testo.indexOf('o', 5);
        System.out.println("La prima occorrenza di 'o' a partire dalla posizione 5 è alla posizione: "+pos);
    }
}
