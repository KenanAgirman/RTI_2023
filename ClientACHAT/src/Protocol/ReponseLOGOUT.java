package Protocol;

import ServeurGenerique.Reponse;

public class ReponseLOGOUT implements Reponse{
    private Boolean valide;

    public ReponseLOGOUT(Boolean v) {
        valide = v;
    }

    public Boolean getValide() {
        return valide;
    }
}
