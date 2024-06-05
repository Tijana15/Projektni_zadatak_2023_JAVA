import java.util.logging.Level;
import java.util.logging.Logger;

public class CarinskiTerminal extends Terminal {
    @Override
    public void obradaVozila(Vozilo vozilo) {

        String opisProb="";

        if (vozilo instanceof AutomobilInterface) {

            try {
                Thread.sleep(2000);
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

            vozilo.setPaoCarinski(false);

        } else if(vozilo instanceof AutobusInterface) {
            if (vozilo.getPutnici().size() != 0) {
                for (int i = 0; i < vozilo.getPutnici().size(); i++) {

                    try {
                        Thread.sleep(100);
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
                    if (vozilo.getPutnici().get(i).ImaLiKofer()) {
                        if (vozilo.getPutnici().get(i).getKofer().ImaLiNedozvoljeneStvari()) {
                            vozilo.setImaoCarinskiIncident(true);
                            opisProb+="Putnik"+vozilo.getPutnici().get(i)+" imao nedozvoljene stvari u koferu.";
                            vozilo.ukloniPutnikaIzVozila(vozilo.getPutnici().get(i));
                        }
                    }
                }
                if(vozilo.jeImaoCarinskiIncident()){
                    Simulacija.serijalizujTekstualno(new Incident(vozilo.id ,opisProb));
                }
            } else {
                System.out.println("Autobus je prazan. Prolazi carinsku kontrolu odmah.");
                vozilo.setPaoCarinski(false);
            }
        } else if (vozilo instanceof KamionInterface) {

            try {
                Thread.sleep(500);
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

            Kamion kamion = (Kamion)vozilo;
            if (kamion.daLiJePotrebnaCarinskaDokumentacija()) {
                if (kamion.daLiKamionImaVeciTeret()) {
                    kamion.setImaoCarinskiIncident(true);
                    opisProb+="Preopterecen.";
                    System.out.println("Kamion " + kamion + " ne moze da predje carinsku kontrolu jer je preopterecen.");
                    Simulacija.serijalizujTekstualno(new Incident(kamion.id ,opisProb));
                    kamion.setPaoCarinski(true);
                }
            }
        }
    }
}
