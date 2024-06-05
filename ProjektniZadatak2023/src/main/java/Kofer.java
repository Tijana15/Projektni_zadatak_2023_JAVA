import java.io.Serializable;
import java.util.*;
public class Kofer implements Serializable {
    private boolean nedozvoljeneStvari;
    public Kofer()
    {
        this.nedozvoljeneStvari=new Random().nextInt(100)<10;
    }
    public boolean ImaLiNedozvoljeneStvari() {
        return nedozvoljeneStvari;
    }
}
