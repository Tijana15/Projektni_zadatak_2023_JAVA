import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatchTerminalStatus extends Thread {
    private String filename = "terminali.txt";
    public WatchTerminalStatus() {
        setDaemon(true);
    }
    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }

            try {
                Path path = Paths.get(System.getProperty("user.dir"), filename);
                List<String> fileContent = Files.readAllLines(path);

                String[] part1 = fileContent.get(0).split(" ");
                Simulacija.policijskiTerminal1.setRadi("P1".equals(part1[0]) && part1[1].equals("R"));

                String[] part2 = fileContent.get(1).split(" ");
                Simulacija.policijskiTerminal2.setRadi("P2".equals(part2[0]) && part2[1].equals("R"));

                String[] part3 = fileContent.get(2).split(" ");
                Simulacija.policijskiTerminalZaKamione.setRadi("P3".equals(part3[0]) && part3[1].equals("R"));

                String[] part4 = fileContent.get(3).split(" ");
                Simulacija.carinskiTerminal.setRadi("C1".equals(part4[0]) && part4[1].equals("R"));

                String[] part5 = fileContent.get(4).split(" ");
                Simulacija.carinskiTerminalZaKamione.setRadi("C2".equals(part5[0]) && part5[1].equals("R"));

            } catch (IOException | NumberFormatException e) {
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }
        }
    }
}
