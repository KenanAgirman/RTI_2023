package Protocol;

import BD.DataBean;
import ServeurGenerique.*;

import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class VESPA implements Protocole {
    private HashMap<String,Socket> clientsConnectes;
    private Logger logger;

    private List<FactureBill> factures;
    private DataBean dataBean;
    public VESPA(Logger logger) {
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
        if(requete instanceof RequeteLOGIN) {
            System.out.println("Login");
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }
        if(requete instanceof RequeteLOGOUT) {
            System.out.println("LOUGOUT");
            return TraiteRequeteLOGOUT((RequeteLOGOUT) requete);
        }

        if(requete instanceof RequeteFacture){
            System.out.println("Facture");
            return TraiteRequeteFacture((RequeteFacture) requete);

        }

        if(requete instanceof RequetePayFacture){
            System.out.println("GETPAYE");
            try {
                return TraiteRequeteFacture((RequetePayFacture) requete);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket)
    {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());

        String password = (requete.getPassword());
        int id = dataBean.SelectLogin(requete.getLogin(),requete.getPassword());

        if (password != null && id!=0)
            {
                String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
                logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
                clientsConnectes.put(requete.getLogin(),socket);
                ReponseLOGIN reponseLOGIN = new ReponseLOGIN(true);
                reponseLOGIN.setIdUser(id);
                return reponseLOGIN;
            }else{
                logger.Trace(requete.getLogin() + " --> erreur de login");

                ReponseLOGIN reponseLOGIN = new ReponseLOGIN(false);
                reponseLOGIN.setIdUser(id);
                return reponseLOGIN;
            }
    }


    private synchronized ReponseLOGOUT TraiteRequeteLOGOUT(RequeteLOGOUT requete)
    {
        logger.Trace("RequeteLOGOUT reçue de " + requete.getUser());
        clientsConnectes.remove(requete.getUser());
        logger.Trace(requete.getUser() + "Client est définitevement déloggué");

        return new ReponseLOGOUT(true);
    }


    private synchronized ReponseFacture TraiteRequeteFacture(RequeteFacture requete)
    {
        logger.Trace("ReponseFacture reçue");
        List<FactureBill> factures = dataBean.getALLFactures(requete.getidClient(),requete.getPaye());
        ReponseFacture reponse = new ReponseFacture(factures, requete.getPaye());
        return reponse;
    }
    private synchronized ReponsePayFacture TraiteRequeteFacture(RequetePayFacture requete) throws SQLException {
        logger.Trace("RequetePayFacture reçue");
        boolean payement = dataBean.PayemenetFactureVisa(requete.getIdFacture());

        if(payement==true){
            logger.Trace("Payement réussi reçue");
            ReponsePayFacture reponse = new ReponsePayFacture(true);
            return reponse;
        }else{
            logger.Trace("Payement pas réussi reçue");
            ReponsePayFacture reponse = new ReponsePayFacture(false);
            return reponse;
        }
    }
}
