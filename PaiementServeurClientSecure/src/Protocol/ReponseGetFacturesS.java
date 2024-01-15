package Protocol;

import ServeurGenerique.Reponse;

public class ReponseGetFacturesS implements Reponse {

    private boolean valide;
    private byte[] factures;
    public ReponseGetFacturesS(boolean v, byte[] list) {
        valide = v;
        factures = list;
    }

    public boolean isValide() {
        return valide;
    }

    public byte[] getFactures() {
        return factures;
    }
}
