package Protocol;

import ServeurGenerique.Reponse;

public class ReponsePayFacture implements Reponse{
    private boolean valide;

    public ReponsePayFacture(boolean valide) {
        this.valide = valide;
    }

    public boolean isValide() {
        return valide;
    }
}
