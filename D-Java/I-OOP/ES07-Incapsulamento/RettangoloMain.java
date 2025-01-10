class Rettangolo {
    private int base;
    public int altezza;

    public setBase(int base) {
        this.base = base;
    }

    public int Area() {
        return base * altezza;
    }    
}
public class RettangoloMain {
    public static void main(String[] args) {
        Rettangolo r1 = new Rettangolo();
        r1.base = 10;
        r1.altezza = 20;
        System.out.println(r1.Area());
    }
}