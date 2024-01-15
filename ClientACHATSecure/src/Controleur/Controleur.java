package Controleur;

import CONFIG.ConfigReader;
import ControleurFacture.ControleurFacture;
import GUI.CarteBancaireVisa;
import GUI.Facture;
import GUI.LoginGui;
import MyCrypto.MyCrypto;
import Protocol.*;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import static MyCrypto.MyCrypto.*;
import static MyCrypto.MyCrypto.encodePrivateKey;


public class Controleur extends WindowAdapter implements ActionListener {

    private Socket socket;
    private ConfigReader configReader;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private LoginGui view;

    private ControleurFacture controleurFactures;
    private Facture factureView;

    private CarteBancaireVisa carteBancaireVisaView;
    private PrivateKey clePriveeClient;
    private SecretKey cleSession;
    private List<FactureBill> factures;

    private String login;
    public int idClient;
    KeyPair keyPair = generateKeyPair();

    String encodedPublicKey = encodePublicKey(keyPair.getPublic());
    String encodedPrivateKey = encodePrivateKey(keyPair.getPrivate());

    public Controleur(LoginGui view) throws NoSuchAlgorithmException {
        try {

            this.view = view;
            this.configReader = new ConfigReader();
            String ip = configReader.getIP();
            int port = configReader.getPort();
            view.getTextFieldIpServeur().setText(ip);
            view.getTextFieldPortServeur().setText(String.valueOf(port));
            socket = new Socket("localhost", port);

            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
            clePriveeClient = RecupereClePriveeClient();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getLOGINButton()) {
            LoginClient();
        }
        if(e.getSource()== factureView.getButtoonPayez()){
            try {
                PayezFacture();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource()==carteBancaireVisaView.getValiderButton()){
            try {
                ValiderPayement();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void LoginClient() {
        try {
            login = view.getTextFieldLogin().getText();
            String password = view.getTextPassword().getText();

            RequeteLoginS requete = new RequeteLoginS(login,password);
            oos.writeObject(requete);
            ReponseLoginS reponse = (ReponseLoginS) ois.readObject();

            if (reponse.isIsvalide()){
                idClient = reponse.getIdUser();

                byte[] cleSessionDecryptee;
                System.out.println("Clé session cryptée reçue = " + new String(reponse.getCleSession()));
                cleSessionDecryptee = MyCrypto.DecryptAsymRSA(clePriveeClient,reponse.getCleSession());
                cleSession = new SecretKeySpec(cleSessionDecryptee,"DES");
                System.out.println("Decryptage asymétrique de la clé de session...");

                System.out.println("Génération d'une clé de session : " + cleSession);
                view.setVisible(false);
                factureView = new Facture();
                factureView.setControleur(this);
                factureView.setVisible(true);


                getFactures();


            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }

    }
    public void PayezFacture() throws Exception {
        int indice = factureView.getTable2().getSelectedRow();
        if(indice == -1)
        {
            JOptionPane JOptionPane = new JOptionPane();
            javax.swing.JOptionPane.showMessageDialog(factureView,"Veuillez Selectionnez un article !!","Information",JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            if (carteBancaireVisaView == null) {
                carteBancaireVisaView = new CarteBancaireVisa();
                carteBancaireVisaView.setControleur(this);
            } else {
                carteBancaireVisaView.setVisible(true);

            }
        }
    }
    public String getLogin() {
        return login;

    }
    public void ValiderPayement() throws Exception {
        System.out.println("APPUIYEZ APPUITEZ");
        if(carteBancaireVisaView.getTextField1NumCarte().getText().isEmpty()){
            throw new Exception("Veuillez remplir le champ NumCarte");
        }
        if(carteBancaireVisaView.getTextFieldDate().getText().isEmpty()) {
            throw new Exception("Veuillez remplir le champ date");
        }
        if(carteBancaireVisaView.getTextFieldCVV().getText().isEmpty()) {
            throw new Exception("Veuillez remplir le champ CVV");
        }

        int indice = factureView.getTable2().getSelectedRow();
        if (indice == -1) {
            throw new Exception("Veuillez sélectionner une facture impayée.");
        }

        int idFactureSelectionnee = (int) factureView.getTable2().getValueAt(indice, 0);
        String nom = view.getName();
        String numero = carteBancaireVisaView.getTextField1NumCarte().getText();
        String cvv = carteBancaireVisaView.getTextFieldCVV().getText();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(nom);
        dos.writeUTF(numero);
        dos.writeUTF(cvv);
        byte[] messageClair = baos.toByteArray();
        System.out.println("Construction du message à envoyer");

        byte[] messageCrypte;
        messageCrypte = MyCrypto.CryptSymDES(cleSession,messageClair);
        System.out.println("Cryptage symétrique du message : " + new String(messageCrypte));

        RequetePayFactureS requete = new RequetePayFactureS(messageCrypte);
        oos.writeObject(requete);

        ReponsePayFactureS reponse = (ReponsePayFactureS) ois.readObject();

        if (reponse.isValide()){
            if(!reponse.isEchec()){
                System.out.println("ok");
                carteBancaireVisaView.setVisible(false);
                view.setVisible(true);
            }
        }

    }
    public static PrivateKey RecupereClePriveeClient() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePriveeClient.ser"))) {
            Object obj = ois.readObject();

            if (obj instanceof BCRSAPrivateCrtKey) {
                BCRSAPrivateCrtKey bouncyCastlePrivateKey = (BCRSAPrivateCrtKey) obj;
                System.out.println("Cle privee récupérée kenan : " + bouncyCastlePrivateKey);
                return bouncyCastlePrivateKey;
            } else {
                System.err.println("Le type de la clé privée n'est pas pris en charge : " + obj.getClass().getName());
                return null; // Ou lancez une exception selon vos besoins
            }
        }
    }
    public void getFactures() {
        try {
            getFacturesPayees();
            //getFacturesImpayees();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }

    private void getFacturesPayees() throws Exception {

        RequeteGetFacturesS requete = new RequeteGetFacturesS(false, idClient,clePriveeClient);
        oos.writeObject(requete);

        ReponseGetFacturesS reponse = (ReponseGetFacturesS) ois.readObject();

        if (reponse.isValide()) {
            byte[] messageDecrypte;
            System.out.println("Message reçu = " + new String(reponse.getFactures()));
            messageDecrypte = MyCrypto.DecryptSymDES(cleSession, reponse.getFactures());
            System.out.println("Decryptage symétrique du message...");

            // Récupération des données claires
            ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
            ObjectInputStream objectOut = new ObjectInputStream(bais);
            LinkedList<FactureBill> list = (LinkedList<FactureBill>) objectOut.readObject();

            DefaultTableModel articleTables = (DefaultTableModel) factureView.getTable2().getModel();

            for (FactureBill factureBill : list) {
                Vector ligne = new Vector();
                ligne.add(factureBill.getIdFacture());
                ligne.add(factureBill.getDateFacture());
                ligne.add(factureBill.getMontant());
                articleTables.addRow(ligne);
            }
        }
    }
}