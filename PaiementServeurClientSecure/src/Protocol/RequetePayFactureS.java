package Protocol;

import ServeurGenerique.Requete;

public class RequetePayFactureS implements Requete {
    private byte[] data;
    private int idFacture;

    public RequetePayFactureS(byte[] data, int idFacture) {
        this.data = data;
        this.idFacture = idFacture;
    }

    public byte[] getData() {
        return data;
    }

    public int getIdFacture() {
        return idFacture;
    }
}
