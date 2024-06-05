import java.io.Serializable;
import java.util.ArrayList;

public abstract class Vozilo extends Thread implements Serializable
{
    volatile boolean paoPolicijski=false;
    volatile boolean paoCarinski=false;
    volatile boolean imaoPolicijskiIncident=false;
    volatile boolean imaoCarinskiIncident=false;
    private Vozac vozac;
    private static int globalniBrojac=0;
    public int id;
    private ArrayList<Putnik> putnici;
    public Vozilo(int brojPutnika)
    {
        globalniBrojac++;
        id =globalniBrojac;
        putnici=new ArrayList<Putnik>();
        vozac=new Vozac();
        for(int i=0; i<brojPutnika-1;i++) {
            putnici.add(new Putnik());
        }
    }
    public boolean jePaoCarinski() {
        return paoCarinski;
    }
    public void setPaoCarinski(boolean a) {
        this.paoCarinski = a;
    }
    public boolean jePaoPolicijski() {
        return paoPolicijski;
    }
    public void setPaoPolicijski(boolean b) {
        this.paoPolicijski = b;
    }
    public void dodajPutnikaUVozilo(Putnik putnik) {
        putnici.add(putnik);
    }
    public void ukloniPutnikaIzVozila(Putnik putnik) {
        putnici.remove(putnik);
    }
    public ArrayList<Putnik> getPutnici()
    {
        return putnici;
    }
    public Vozac getVozac() {
        return vozac;
    }
    public void setVozac(Vozac vozac) {
        this.vozac = vozac;
    }
    @Override
    public String toString()
    {
        return ""+id;
    }
    public int getBrojPutnika()
    {
        return putnici.size();
    }
    public boolean jeImaoCarinskiIncident() {
        return imaoCarinskiIncident;
    }
    public boolean jeImaoPolicijskiIncident() {
        return imaoPolicijskiIncident;
    }
    public void setImaoCarinskiIncident(boolean imaoCarinskiIncident) {
        this.imaoCarinskiIncident = imaoCarinskiIncident;
    }
    public void setImaoPolicijskiIncident(boolean imaoPolicijskiIncident) {
        this.imaoPolicijskiIncident = imaoPolicijskiIncident;
    }
}

