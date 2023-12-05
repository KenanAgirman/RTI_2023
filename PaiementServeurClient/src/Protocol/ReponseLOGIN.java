package Protocol;

import ServeurGenerique.Reponse;

public class ReponseLOGIN implements Reponse
{
    private int idUser;
    private boolean valide;

    ReponseLOGIN(boolean v) {
        valide = v;
        this.idUser = idUser;
    }
    public boolean isValide() {
        return valide;
    }

    public int getIdUser(){
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}