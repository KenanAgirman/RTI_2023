package Protocol;

import ServeurGenerique.*;

import java.net.Socket;
import java.util.HashMap;

public class VESPA implements Protocole {
    private HashMap<String,String> passwords;
    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;

    public VESPA(Logger logger) {
        this.logger = logger;
        clientsConnectes = new HashMap<>();
    }

    @Override
    public String getNom() {
        return "VESPA";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if(requete instanceof RequeteLOGIN) {
            System.out.println("KENAN ET LEILALALALAL");
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }

        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException
    {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());
        String password = passwords.get(requete.getLogin());
        if (password != null)
            if (password.equals(requete.getPassword()))
            {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(),socket);
                return new ReponseLOGIN(true);
            }
        logger.Trace(requete.getLogin() + " --> erreur de login");
        throw new FinConnexionException(new ReponseLOGIN(false));
    }

}
