package Protocol;

import BD.DataBean;
import MyCrypto.MyCrypto;
import ServeurGenerique.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static MyCrypto.MyCrypto.*;
import static MyCrypto.MyCrypto.encodePrivateKey;


public class VESPAS implements Protocole {
    private HashMap<String, Socket> clientsConnectes;
    private Logger logger;
    private DataBean dataBean;
    private PublicKey clePubliqueClient;
    private SecretKey cleSession;

    private KeyPair keyPair = null;

    public VESPAS(Logger logger) throws NoSuchAlgorithmException {
        KeyPair keyPair = generateKeyPair();

        // Encodage des clés
        String encodedPublicKey = encodePublicKey(keyPair.getPublic());
        String encodedPrivateKey = encodePrivateKey(keyPair.getPrivate());

        this.keyPair = generateKeyPair();

        this.logger = logger;
        Security.addProvider(new BouncyCastleProvider());


        clientsConnectes = new HashMap<>();
        dataBean = new DataBean();


        try {
            clePubliqueClient = recupereClePubliqueClient();
        } catch (IOException | ClassNotFoundException  e) {
            throw new RuntimeException(e);
        }


        // Génération d'une clé de session
        KeyGenerator cleGen;
        try {
            cleGen = KeyGenerator.getInstance("DES","BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
        cleGen.init(new SecureRandom());
        cleSession = cleGen.generateKey();
        System.out.println("Génération d'une clé de session : " + cleSession);
    }


    @Override
    public String getNom() {
        return "VESPA";
    }

    @Override
    public Reponse TraiteRequete(Requete requete, Socket socket) throws Exception {
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
            } catch (NoSuchPaddingException e) {
                throw new RuntimeException(e);
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        if(requete instanceof RequeteGetFacturesS){
            System.out.println("Factures");
            return TraiteRequeteGetFactures((RequeteGetFacturesS) requete, socket);

        }

        if(requete instanceof RequetePayFactureS){
            System.out.println("Payess");
            return TraiteRequeteRequetePayFactureS((RequetePayFactureS) requete, socket);

        }
        return null;
    }

    private Reponse TraiteRequeteRequetePayFactureS(RequetePayFactureS requete, Socket socket) throws SQLException, NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidKeyException {
        logger.Trace("RequetePayFacture reçue");
        // Décryptage symétrique du message
        byte[] messageDecrypte;
        System.out.println("Message reçu = " + new String(requete.getData()));
        try {
            messageDecrypte = MyCrypto.DecryptSymDES(cleSession,requete.getData());
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Decryptage symétrique du message...");

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
        DataInputStream dis = new DataInputStream(bais);

        boolean payement = dataBean.PayemenetFactureVisa(requete.getIdFacture());

        if(payement==true){
            logger.Trace("Payement réussi reçue");
            ReponsePayFactureS reponse = new ReponsePayFactureS(true,false,cleSession);
            return reponse;
        }else{
            logger.Trace("Payement pas réussi reçue");
            ReponsePayFactureS reponse = new ReponsePayFactureS(true,true,cleSession);
            return reponse;
        }

    }

    private synchronized ReponseLoginS TraiteRequeteLOGIN(RequeteLoginS requete, Socket socket) throws Exception {
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

    private synchronized ReponseGetFacturesS TraiteRequeteGetFactures(RequeteGetFacturesS requete, Socket socket) throws Exception {
        logger.Trace("RequeteGetFactures reçue");
        System.out.println("KENENENENEN");
        try {
            if(!requete.VerifySignature(clePubliqueClient)){
                logger.Trace("Mauvais signature !");
                return new ReponseGetFacturesS(false, null);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | IOException | SignatureException e) {
            throw new RuntimeException(e);
        }

        LinkedList<FactureBill> factures = DataBean.getALLFactures(requete.isPaye(), requete.getIdClient());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(factures);
            oos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] messageClair = baos.toByteArray();
        System.out.println("Construction du message à envoyer");

        byte[] messageCrypte;
        try {
            messageCrypte = MyCrypto.CryptSymDES(cleSession,messageClair);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | NoSuchProviderException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Cryptage symétrique du message : " + new String(messageCrypte));

        return new ReponseGetFacturesS(true, messageCrypte);
    }
    private PublicKey recupereClePubliqueClient() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePubliqueClient.ser"));
        clePubliqueClient = (PublicKey) ois.readObject();
        ois.close();
        return clePubliqueClient;
    }

}

