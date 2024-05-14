package lat.jack.etl.utils;

public class utils {

    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }
}
