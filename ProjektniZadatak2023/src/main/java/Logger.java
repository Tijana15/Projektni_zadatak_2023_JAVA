import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;

public class Logger {
    public static Handler handler;

    static{
        try {
            // ime logger-a je naziv klase
            handler = new FileHandler("Logger.log");
            java.util.logging.Logger.getLogger(Simulacija.class.getName()).addHandler(handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
