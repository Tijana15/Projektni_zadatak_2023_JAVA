import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Autobus extends Vozilo implements AutobusInterface {
    private final TeretniProstor teretniProstor;
    public static final int MAX_BROJ_PUTNIKA_AUTOBUSA = 52;
    public Autobus() {
        super(new Random().nextInt(MAX_BROJ_PUTNIKA_AUTOBUSA) + 1);
        teretniProstor = new TeretniProstor();
        for (int i = 0; i < getPutnici().size(); i++) {
            if (getPutnici().get(i).ImaLiKofer()) {
                teretniProstor.dodajKoferUTeretniProstor(getPutnici().get(i).kofer);
            }
        }
    }
    public void run() {

        boolean prosaoPolicijskiTerminal = false;
        int saKogPolicijskogDolazi = 0;
        String finalniOpis="Autobus"+id+": ";

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


            if (Simulacija.policijskiTerminal1.daLiRadi() && Simulacija.granicniRed.size() > 0
                    && Simulacija.granicniRed.peek() == this && Simulacija.policijskiTerminal1.jeSlobodan()) {

                saKogPolicijskogDolazi = 1;
                Simulacija.policijskiTerminal1.setSlobodan(false);
                System.out.println("Autobus" + this + " je zauzeo policijski terminal 1");
                Main.prebaciIzRedaNaPolicijski(saKogPolicijskogDolazi);
                Simulacija.granicniRed.poll();

                Simulacija.policijskiTerminal1.obradaVozila(this);

                if (paoPolicijski) {

                    System.out.println("Autobus" + this + " pao policijsku kontrolu, oslobađa se policijski terminal 1");
                    Main.izbrisiSaPolicijskog(saKogPolicijskogDolazi);
                    finalniOpis+=Simulacija.citajIzBinarne(id);
                    Main.dodajNaTrecuScenu(this,finalniOpis);
                    Simulacija.policijskiTerminal1.setSlobodan(true);
                } else {

                    Simulacija.carinskiRed.add(this);
                    if(imaoPolicijskiIncident) {
                        finalniOpis += Simulacija.citajIzBinarne(id);
                    }
                }

                prosaoPolicijskiTerminal = true;
            } else if (Simulacija.policijskiTerminal2.daLiRadi() && Simulacija.granicniRed.size() > 0
                    && Simulacija.granicniRed.peek() == this && Simulacija.policijskiTerminal2.jeSlobodan()) {

                saKogPolicijskogDolazi = 2;
                Simulacija.policijskiTerminal2.setSlobodan(false);
                System.out.println("Autobus" + this + " je zauzeo policijski terminal 2");
                Main.prebaciIzRedaNaPolicijski(saKogPolicijskogDolazi);
                Simulacija.granicniRed.poll();

                Simulacija.policijskiTerminal2.obradaVozila(this);

                if (paoPolicijski) {

                    System.out.println("Autobus" + this + " pao policijsku kontrolu, oslobađa se policijski terminal 2");
                    Main.izbrisiSaPolicijskog(saKogPolicijskogDolazi);
                    finalniOpis+=Simulacija.citajIzBinarne(id);
                    Main.dodajNaTrecuScenu(this,finalniOpis);
                    Simulacija.policijskiTerminal2.setSlobodan(true);

                } else {

                    Simulacija.carinskiRed.add(this);
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
                if (Simulacija.carinskiTerminal.daLiRadi() && Simulacija.carinskiRed.size() > 0
                        && Simulacija.carinskiRed.peek() == this && Simulacija.carinskiTerminal.jeSlobodan()) {

                    Simulacija.carinskiTerminal.setSlobodan(false);
                    if (saKogPolicijskogDolazi == 1) {

                        Main.prebaciSaPolicijskihNaCarinski(saKogPolicijskogDolazi);
                        System.out.println("Autobus" + this + " je zauzeo carinski terminal 1");
                        Simulacija.carinskiRed.poll();
                        Simulacija.policijskiTerminal1.setSlobodan(true);
                        System.out.println("Autobus" + this + " izašao iz policijskog 1.");
                    } else {

                        Main.prebaciSaPolicijskihNaCarinski(saKogPolicijskogDolazi);
                        System.out.println("Autobus" + this + " je zauzeo carinski terminal 1.");
                        Simulacija.carinskiRed.poll();
                        Simulacija.policijskiTerminal2.setSlobodan(true);
                        System.out.println("Auobus" + this + " izašao iz policijskog 2.");
                    }

                    Simulacija.carinskiTerminal.obradaVozila(this);
                    if(imaoCarinskiIncident){
                        finalniOpis+=Simulacija.citajIzTekstualne(id);
                    }

                    System.out.println("Autobus" + this + " je presao carinu. Slijedi oslobađanje carinskog terminala.");
                    if(imaoPolicijskiIncident || imaoCarinskiIncident){
                        Main.dodajNaTrecuScenu(this,finalniOpis);
                    }

                    Main.izbrisiSaCarinskog(1);
                    Simulacija.carinskiTerminal.setSlobodan(true);
                    prosaoCarinskiTerminal = true;
                }
            }
        }
    }
}
