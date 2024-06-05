import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
public class PolicijskiTerminal extends Terminal {
    @Override
    public void obradaVozila(Vozilo vozilo) {

        String opisProb="";
        int vrijemeObradePutnika;

        if (vozilo instanceof KamionInterface || vozilo instanceof AutomobilInterface) {
            vrijemeObradePutnika = 500;
        } else {
            vrijemeObradePutnika = 100;
        }

        try {
            Thread.sleep(vrijemeObradePutnika);
        } catch (InterruptedException e) {
            Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }

        if (Simulacija.pauza) {
            synchronized (Simulacija.lock) {
                try {
                    Simulacija.lock.wait();
                } catch (InterruptedException e) {
                    Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                }
            }
        }

        if (vozilo.getVozac().getIdentifikacioniDokument().jeIspravan()) {
            //System.out.println(vozilo+" moze uspješno da prođe policijski terminal, id vozaca ispravan.");
        } else {

            vozilo.setImaoPolicijskiIncident(true);
            opisProb+="Vozac"+vozilo.getVozac()+" nije imao validan identifikacioni dokument.";
            Simulacija.serijalizujBinarno(new Incident(vozilo.id,opisProb));
            vozilo.setPaoPolicijski(true);
            return;
        }

        Iterator<Putnik> iterator = vozilo.getPutnici().iterator();

        while (iterator.hasNext()) {

            try {
                Thread.sleep(vrijemeObradePutnika);
            } catch (InterruptedException e) {
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }

            if (Simulacija.pauza) {
                synchronized (Simulacija.lock) {
                    try {
                        Simulacija.lock.wait();
                    } catch (InterruptedException e) {
                        Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                    }
                }
            }

            Putnik putnik = iterator.next();

            if (!putnik.getIdentifikacioniDokument().jeIspravan()) {
                vozilo.setImaoPolicijskiIncident(true);
                opisProb+="Putnik"+putnik.getIdentifikacioniDokument()+" nije imao validan identifikacioni dokument.";
                iterator.remove(); //izbacuje se iz vozila ako nema validan id
            }
        }
        if(vozilo.jeImaoPolicijskiIncident()){
            Simulacija.serijalizujBinarno(new Incident(vozilo.id,opisProb));
        }
    }
}
