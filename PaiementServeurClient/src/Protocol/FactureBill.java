package Protocol;

import java.io.Serializable;

public class FactureBill implements Serializable{
    private int idFacture;
    private String DateFacture;
    private Float Montant;

    public FactureBill(int idFacture, String dateFacture, Float montant) {
        this.idFacture = idFacture;
        DateFacture = dateFacture;
        Montant = montant;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public void setDateFacture(String dateFacture) {
        DateFacture = dateFacture;
    }

    public void setMontant(Float montant) {
        Montant = montant;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public String getDateFacture() {
        return DateFacture;
    }

    public Float getMontant() {
        return Montant;
    }
}
