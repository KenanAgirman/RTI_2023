package Protocol;

import BD.DataBean;
import MyCrypto.MyCrypto;
import ServeurGenerique.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.HashMap;



public class VESPAS implements Protocole {
    private HashMap<String, Socket> clientsConnectes;
    private Logger logger;
    private DataBean dataBean;
    private PublicKey clePubliqueClient;
    private SecretKey cleSession;


    public VESPAS(Logger logger) throws Exception {
        this.logger = logger;
        Security.addProvider(new BouncyCastleProvider());


        clientsConnectes = new HashMap<>();
        dataBean = new DataBean();

    }

    @Override
    public String getNom() {
        return "VESPAS";
    }

    @Override
    public ReponseLoginS TraiteRequete(Requete requete, Socket socket) throws Exception {

        if(requete instanceof RequeteLoginS) {
            System.out.println("Login");
            if (requete instanceof RequeteLoginS) return TraiteRequeteLOGINS((RequeteLoginS) requete, socket);

        }

        return null;
    }

    private synchronized ReponseLoginS TraiteRequeteLOGINS(RequeteLoginS requete, Socket socket) throws Exception {
        logger.Trace("RequeteLOGIN reçue de " + requete.getLogin());

        if(clientsConnectes.containsKey(requete.getLogin()))
        {
            logger.Trace(requete.getLogin() + " --> erreur de login, Client deja logge");
            return new ReponseLoginS(false);
        }

        String password = dataBean.getPassword(requete.getLogin());

        if (password == null)
        {
            logger.Trace(requete.getLogin() + " --> Client inconnu !");
            return new ReponseLoginS(false);
        }

        try {
            if(!requete.VerifyPassword(password)){
                logger.Trace(requete.getLogin() + " --> Mauvais mot de passe !");
                return new ReponseLoginS(false);
            }
        }
        catch (NoSuchAlgorithmException | IOException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }

        int id = dataBean.SelectLogin(requete.getLogin(), password);

        if(id != 0)
        {
            String ipPortClient = socket.getInetAddress().getHostAddress() + "/" + socket.getPort();
            logger.Trace(requete.getLogin() + " correctement loggé de " + ipPortClient);
            clientsConnectes.put(requete.getLogin(),socket);

            ReponseLoginS reponse = new ReponseLoginS(true);
            reponse.setIdUser(id);

            byte[] cleSessionCrypte;
            try {
                cleSessionCrypte = MyCrypto.CryptAsymRSA(clePubliqueClient,cleSession.getEncoded());
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Cryptage asymétrique de la clé de session : " + new String(cleSessionCrypte));

            reponse.setCleSession(cleSessionCrypte);
            return reponse;
        }
        else
        {
            logger.Trace(requete.getLogin() + " --> erreur de login");
            return new ReponseLoginS(false);
        }

    }
    private PublicKey recupereClePubliqueClient() throws IOException, ClassNotFoundException {

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePubliqueClients.ser"));
        PublicKey cle = (PublicKey) ois.readObject();
        ois.close();
        return cle;
    }

    public static PublicKey RecupereClePubliqueClient() throws IOException, ClassNotFoundException{
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePubliqueClient.ser"));
        PublicKey cle = (PublicKey) ois.readObject();
        ois.close();
        System.out.println("Cle publique recuperer");
        return cle;
    }
}

