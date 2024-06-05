import java.io.Serializable;
import java.util.Random;

public class Teret implements Serializable {
    private double stvarnaMasaTereta;
    public Teret(double stvarnaMasaTereta)
    {
        this.stvarnaMasaTereta=stvarnaMasaTereta;
    }
    public double getStvarnaMasaTereta() {
        return stvarnaMasaTereta;
    }
    public void setStvarnaMasaTereta(double stvarnaMasaTereta) {
        this.stvarnaMasaTereta = stvarnaMasaTereta;
    }

}
