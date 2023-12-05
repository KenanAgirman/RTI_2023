package Protocol;

import ServeurGenerique.Requete;

public class RequetePayFacture implements Requete {
    private int idFacture;
    private String nom;
    private String numVisa;

    public RequetePayFacture(int idFacture, String nom, String numVisa) {
        this.idFacture = idFacture;
        this.nom = nom;
        this.numVisa = numVisa;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public String getNom() {
        return nom;
    }

    public String getNumVisa() {
        return numVisa;
    }
}
