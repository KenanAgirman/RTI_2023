package Protocol;

import ServeurGenerique.Requete;

public class RequeteLOGOUT implements Requete {
    private String user;

    public RequeteLOGOUT(String user) {
        this.user = user;
    }

    public String getUser() {
        System.out.println("User " + user);
        return user;
    }
}
