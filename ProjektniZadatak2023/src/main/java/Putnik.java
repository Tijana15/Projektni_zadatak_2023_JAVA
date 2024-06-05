import java.util.Random;
public class Putnik extends Osoba {
    Kofer kofer;
    private boolean imaKofer;
    public Putnik()
    {
        super();
        imaKofer=new Random().nextInt(100)<70;
        if(imaKofer){
            kofer = new Kofer();
        }
    }
    public Kofer getKofer() {
        return kofer;
    }
    public boolean ImaLiKofer() {
        return imaKofer;
    }
    @Override
    public String toString()
    {
        return ""+getIdentifikacioniDokument();
    }
}
