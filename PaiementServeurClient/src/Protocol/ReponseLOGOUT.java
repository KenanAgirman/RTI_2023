package Protocol;

import ServeurGenerique.Reponse;

public class ReponseLOGOUT implements Reponse{
    private Boolean valide;

    public ReponseLOGOUT(Boolean valide) {
        this.valide = valide;
    }

    public Boolean getValide() {
        return valide;
    }
}
