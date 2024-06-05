import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Simulacija {
    public static volatile ArrayBlockingQueue<Vozilo> granicniRed = new ArrayBlockingQueue<>(50);
    public static volatile ArrayBlockingQueue<Vozilo> carinskiRed = new ArrayBlockingQueue<>(2);
    final static int BROJ_AUTOBUSA = 5;
    final static int BROJ_AUTA = 35;
    final static int BROJ_KAMIONA = 10;
    public static volatile CarinskiTerminal carinskiTerminalZaKamione = new CarinskiTerminal();
    public static volatile CarinskiTerminal carinskiTerminal = new CarinskiTerminal();
    public static volatile PolicijskiTerminal policijskiTerminalZaKamione = new PolicijskiTerminal();
    public static volatile PolicijskiTerminal policijskiTerminal1 = new PolicijskiTerminal();
    public static volatile PolicijskiTerminal policijskiTerminal2 = new PolicijskiTerminal();
    public static volatile ArrayList<Vozilo> pomocniRed = new ArrayList<>(50);
    public static final Object lock = new Object();
    public static volatile boolean pauza = true;
    public static long localDateTime=System.currentTimeMillis();
    public static String nazivFajla1=localDateTime+".txt";
    public static String nazivFajla2=localDateTime+".bin";
    public static boolean postojiDatoteka(String imeDatoteke){
        File datoteka=new File(imeDatoteke);
        return datoteka.exists();
    }

    public static synchronized void serijalizujTekstualno(Incident incident){
            try {
                FileWriter fileWriter = new FileWriter(nazivFajla1,true);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                PrintWriter printWriter=new PrintWriter(writer);
                printWriter.println(incident.getId()+"#"+incident.getProblem());
                printWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }
    }
    public static synchronized void serijalizujBinarno(Incident incident){
        if(postojiDatoteka(nazivFajla2)){

            try(ObjectOutputStreamWithoutHeader out = new ObjectOutputStreamWithoutHeader(new FileOutputStream(nazivFajla2, true))){
                out.writeObject(incident);
                out.flush();
            } catch (Exception e) {
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }
        }
        else{

            try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nazivFajla2))){
                out.writeObject(incident);
                out.flush();
            } catch (Exception e) {
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }
        }
    }

    public static synchronized String citajIzBinarne(int id){

        Incident incident=null;
        String opisProblema="";

        try {
            FileInputStream fileInputStream = new FileInputStream(nazivFajla2);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try{
                while ((incident = (Incident) objectInputStream.readObject()) != null) {

                    if(id == incident.id){
                        opisProblema=incident.getProblem();
                        break;
                    }
                }
            }
            catch(ClassNotFoundException e){
                Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
            }
            objectInputStream.close();
        }catch(IOException e){
            Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }
        return opisProblema;
    }
    public static synchronized String citajIzTekstualne(int id){

        Incident incident=null;
        String opisIncidenta="", linija, temp;
        String[] razdvojeni;
        int t;

        try (BufferedReader bufferedReader=new BufferedReader(new FileReader(nazivFajla1))){
            while((linija=bufferedReader.readLine())!=null){

                razdvojeni=linija.split("#");
                t=Integer.parseInt(razdvojeni[0]);
                temp=razdvojeni[1];
                incident=new Incident(t,opisIncidenta);

                if(incident.id==id){
                    opisIncidenta=temp;
                    break;
                }
            }
        }
        catch(IOException e){
            Logger.getLogger(Simulacija.class.getName()).log(Level.WARNING, e.fillInStackTrace().toString());
        }
        return opisIncidenta;
    }
    private  static class ObjectOutputStreamWithoutHeader extends ObjectOutputStream {
        public ObjectOutputStreamWithoutHeader(OutputStream out) throws IOException {
            super(out);
        }
        @Override
        protected void writeStreamHeader() throws IOException {}
    }


}