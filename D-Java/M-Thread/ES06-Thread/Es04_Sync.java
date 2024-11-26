class Caller implements Runnable {
    private String msg;
    private CallMe target;

    public Caller(CallMe t, String s) {
        target = t;
        msg = s;
        new Thread(this).start();
    }

    public void run() {
        synchronized (target) {// sincronizzazione
            target.call(msg);
        }
    }
}

class CallMe {
    /* synchronized */
    public void call(String msg) {
        System.out.print("[" + msg);
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        System.out.println("]");
    }
}

public class Es04_Sync {
    public static void main(String args[]) {
        CallMe target = new CallMe();
        new Caller(target, "Hello");
        new Caller(target, "Synchronized");
        new Caller(target, "World");
    }
}