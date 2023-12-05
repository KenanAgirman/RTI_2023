package Protocol;

import ServeurGenerique.Reponse;

import java.util.List;

public class ReponseFacture implements Reponse {
    private List<FactureBill> factures;
    private int check;

    public ReponseFacture(List<FactureBill> factures, int check) {
        this.factures = factures;
        this.check = check;
    }

    public int getCheck() {
        return check;
    }

    public void setFactures(List<FactureBill> factures) {
        this.factures = factures;
    }


    public List<FactureBill> getFactures() {
        return factures;
    }
}
