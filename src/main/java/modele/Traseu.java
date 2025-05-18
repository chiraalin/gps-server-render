package aplicatie.modele;


import java.time.LocalDateTime;

public class Traseu {
    private int id;
    private int idMasina;
    private LocalDateTime dataOra;
    private double latitudine;
    private double longitudine;

    public Traseu(int id, int idMasina, LocalDateTime dataOra, double latitudine, double longitudine) {
        this.id = id;
        this.idMasina = idMasina;
        this.dataOra = dataOra;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public Traseu(int idMasina, LocalDateTime dataOra, double latitudine, double longitudine) {
        this.idMasina = idMasina;
        this.dataOra = dataOra;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdMasina() { return idMasina; }
    public void setIdMasina(int idMasina) { this.idMasina = idMasina; }

    public LocalDateTime getDataOra() { return dataOra; }
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    public double getLatitudine() { return latitudine; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }

    public double getLongitudine() { return longitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }
}

