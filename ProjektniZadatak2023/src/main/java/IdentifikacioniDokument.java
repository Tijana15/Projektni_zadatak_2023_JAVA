import java.io.Serializable;
import java.util.Random;
public class IdentifikacioniDokument implements Serializable{
    private  boolean ispravan;
    private int broj;
    private static int brojac=0;
    public IdentifikacioniDokument()
    {
        this.ispravan=new Random().nextInt(100)<97;
        brojac++;
        broj=brojac;
    }
    public boolean jeIspravan() {
        return ispravan;
    }
    @Override
    public String toString()
    {
        return" ID"+broj;
    }
}
