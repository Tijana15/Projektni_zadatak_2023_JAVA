import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Kamion extends Vozilo implements KamionInterface {
    private final Teret teret;
    private final boolean potrebnaCarinskaDokumentacija;
    private final double deklarisanaMasaTereta;
    private final boolean kamionImaVeciTeret;
    private static int globalniBrojacKamiona = 0;

    public Kamion() {
        super(new Random().nextInt(2) + 1);
        globalniBrojacKamiona++;
        deklarisanaMasaTereta = new Random().nextDouble(1000);
        teret = new Teret(deklarisanaMasaTereta);
        potrebnaCarinskaDokumentacija = new Random().nextBoolean();
        kamionImaVeciTeret = new Random().nextInt(100) < 20;
        if (kamionImaVeciTeret) {
            teret.setStvarnaMasaTereta(deklarisanaMasaTereta + new Random().nextDouble(31) * deklarisanaMasaTereta);
        }
    }
    public Teret getTeret() {
        return teret;
    }
    public double getDeklarisanaMasaTereta() {
        return deklarisanaMasaTereta;
    }
    public boolean daLiJePotrebnaCarinskaDokumentacija() {
        return potrebnaCarinskaDokumentacija;
    }
    public boolean daLiKamionImaVeciTeret() {
        return kamionImaVeciTeret;
    }

    public void run() {

        String finalniOpis="Kamion"+id+": ";
        boolean prosaoPolicijskiTerminal = false;

        while (!prosaoPolicijskiTerminal) {
            if (Simulacija.pauza) {
                try {
                    synchronized (Simulacija.lock) {
                        Simulacija.lock.wait();
                    }
                } catch (InterruptedException e) {
                    Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
                }
            }
            if (Simulacija.policijskiTerminalZaKamione.daLiRadi() && Simulacija.granicniRed.size() > 0
                    && Simulacija.granicniRed.peek() == this && Simulacija.policijskiTerminalZaKamione.jeSlobodan()) {

                Simulacija.policijskiTerminalZaKamione.setSlobodan(false);
                System.out.println("Kamion" + this + " je zauzeo policijski terminal.");
                Main.prebaciIzRedaNaPolicijski(3);
                Simulacija.granicniRed.poll();

                Simulacija.policijskiTerminalZaKamione.obradaVozila(this);

                if (paoPolicijski) {

                    System.out.println("Kamion" + this + " pao policijsku kontrolu.");
                    Main.izbrisiSaPolicijskog(3);
                    finalniOpis+=Simulacija.citajIzBinarne(id);
                    Main.dodajNaTrecuScenu(this,finalniOpis);
                    Simulacija.policijskiTerminalZaKamione.setSlobodan(true);

                }else{
                    if(imaoPolicijskiIncident){
                        finalniOpis+=Simulacija.citajIzBinarne(id);
                    }
                }
                prosaoPolicijskiTerminal = true;
            }
        }
        boolean prosaoCarinskiTerminal = false;
        if (!paoPolicijski) {
            while (!prosaoCarinskiTerminal) {

                if (Simulacija.carinskiTerminalZaKamione.daLiRadi() && Simulacija.carinskiTerminalZaKamione.jeSlobodan()) {
                    Simulacija.carinskiTerminalZaKamione.setSlobodan(false);
                    System.out.println("Kamion" + this + " je zauzeo carinski terminal");
                    Main.prebaciSaPolicijskihNaCarinski(3);

                    Simulacija.policijskiTerminalZaKamione.setSlobodan(true);
                    Simulacija.carinskiTerminalZaKamione.obradaVozila(this);
                    Main.izbrisiSaCarinskog(2);

                    if (paoCarinski) {
                        finalniOpis+=Simulacija.citajIzTekstualne(id);
                        System.out.println("Kamion" + this + " je pao na carinskoj kontroli. Oslobađamo carinski terminal za sljedeće kamione.");

                    } else {
                        System.out.println("Kamion" + this + " je presao carinu. Slijedi oslobađanje carinskog terminala.");
                    }
                    if(imaoPolicijskiIncident || imaoCarinskiIncident){
                        Main.dodajNaTrecuScenu(this,finalniOpis);
                    }
                    Simulacija.carinskiTerminalZaKamione.setSlobodan(true);
                    prosaoCarinskiTerminal = true;
                }
            }
        }
    }
}
