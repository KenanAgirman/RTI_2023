package Protocol;

import ServeurGenerique.Requete;

public class RequeteFacture implements Requete {
    private int idClient;
    private int paye;

    public RequeteFacture(int idClient,int paye) {
        this.idClient = idClient;
        this.paye = paye;
    }

    public int getPaye() {
        return paye;
    }

    public int getidClient() {
        return idClient;
    }
}
