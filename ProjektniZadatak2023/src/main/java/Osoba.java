import java.io.Serializable;

public class Osoba implements Serializable {
    private IdentifikacioniDokument identifikacioniDokument;
    public Osoba()
    {
        identifikacioniDokument=new IdentifikacioniDokument();
    }

    public void setIdentifikacioniDokument(IdentifikacioniDokument identifikacioniDokument) {
        this.identifikacioniDokument = identifikacioniDokument;
    }
    public IdentifikacioniDokument getIdentifikacioniDokument() {
        return identifikacioniDokument;
    }
    @Override
    public String toString()
    {
        return "Osoba "+identifikacioniDokument;
    }
}
