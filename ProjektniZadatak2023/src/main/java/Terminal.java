public abstract class Terminal {
    protected volatile boolean slobodan=true;
    public volatile boolean radi=true;
    public abstract void obradaVozila(Vozilo vozilo);
    public boolean jeSlobodan() {
        return slobodan;
    }
    public synchronized void setSlobodan(boolean slobodan) {
        this.slobodan = slobodan;
    }
    public boolean daLiRadi() {
        return radi;
    }
    public void setRadi(boolean radi) {
        this.radi = radi;
    }
}
