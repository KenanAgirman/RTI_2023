package Protocol;

import BD.DataBean;
import MyCrypto.MyCrypto;
import ServeurGenerique.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.sql.SQLException;
import java.util.HashMap;

import static java.lang.System.exit;

public class VESPAS implements Protocole {
    private HashMap<String, Socket> clientsConnectes;
    private Logger logger;
    private DataBean dataBean;

    public VESPAS(Logger logger) {
        this.logger = logger;
        clientsConnectes = new HashMap<>();
        dataBean = new DataBean();
    }

    @Override
    public String getNom() {
        return "VESPA";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws FinConnexionException {
        if(requete instanceof RequeteLoginS) {
            System.out.println("Login");
            try {
                return TraiteRequeteLOGIN((RequeteLoginS) requete, socket);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (NoSuchProviderException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private synchronized ReponseLoginS TraiteRequeteLOGIN(RequeteLoginS requete, Socket socket) throws SQLException, NoSuchAlgorithmException, IOException, NoSuchProviderException {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());

        String password = dataBean.getPassword(requete.getLogin());

        if (password == null) {
            System.out.println("Client inconnu");
            return new ReponseLoginS(false);
        }

        if (requete.VerifyPassword(password)) {
            System.out.println("Bienvenue " + requete.getLogin() + " !");
            clientsConnectes.put(requete.getLogin(), socket);
            int id = dataBean.SelectLogin(requete.getLogin(), password);

            if (id != 0) {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(), socket);

                ReponseLoginS reponseLOGIN = new ReponseLoginS(true);
                reponseLOGIN.setIdUser(id);
                return reponseLOGIN;
            } else {
                logger.Trace(requete.getLogin() + " --> erreur de login");

                ReponseLoginS reponseLOGIN = new ReponseLoginS(false);
                reponseLOGIN.setIdUser(id);
                return reponseLOGIN;
            }
        } else {
            System.out.println("Mauvais mot de passe pour " + requete.getLogin() + "...");
            ReponseLoginS reponseLOGIN = new ReponseLoginS(false);
            return reponseLOGIN;
        }
    }
}
