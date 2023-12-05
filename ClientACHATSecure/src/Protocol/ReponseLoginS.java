package Protocol;

import ServeurGenerique.Reponse;

public class ReponseLoginS implements Reponse {
    private boolean isvalide;
    private int idUser;
    private byte[] cleSession;

    public ReponseLoginS(boolean isvalide) {
        this.isvalide = isvalide;
    }

    public void setIsvalide(boolean isvalide) {
        this.isvalide = isvalide;
    }

    public void setIdUser(int idClient) {
        this.idUser = idClient;
    }

    public void setCleSession(byte[] cleSession) {
        this.cleSession = cleSession;
    }

    public boolean isIsvalide() {
        return isvalide;
    }

    public int getIdUser() {
        return idUser;
    }

    public byte[] getCleSession() {
        return cleSession;
    }
}
