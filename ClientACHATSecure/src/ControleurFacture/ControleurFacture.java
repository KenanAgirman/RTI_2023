package ControleurFacture;

import Controleur.Controleur;
import GUI.CarteBancaireVisa;
import GUI.Facture;
import MyCrypto.MyCrypto;
import Protocol.*;

import javax.crypto.SecretKey;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.*;
import java.security.PrivateKey;

import java.util.List;
import java.util.Vector;

public class ControleurFacture extends WindowAdapter implements ActionListener {
    private Facture factureView;
    private CarteBancaireVisa carteBancaireVisaView;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private List<FactureBill> factures;
    public int idclient;
    private Controleur controleur;
    public PrivateKey clePriveeClient;
    private SecretKey cleSession;

    public ControleurFacture(Facture factureView, int idclient, ObjectOutputStream oos, ObjectInputStream ois, Controleur controleur) {
        this.factureView = factureView;
        this.oos = oos;
        this.ois = ois;
        this.idclient = idclient;
        this.controleur = controleur;
        getFactures();
    }

    @Override
    public void actionPerformed(ActionEvent e) {


        if(e.getSource()==carteBancaireVisaView.getValiderButton()){
            try {
                ValiderPayement();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
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
        String nom = controleur.getLogin();
        String numero = carteBancaireVisaView.getTextField1NumCarte().getText();
        String cvv = carteBancaireVisaView.getTextFieldCVV().getText();

    }


    private void getFacturesPayees() throws Exception {


        RequeteGetFacturesS requete = new RequeteGetFacturesS(false, idclient, clePriveeClient);
        oos.writeObject(requete);
        System.out.println("JE SUIS KENAN");

        ReponseGetFacturesS reponse = (ReponseGetFacturesS) ois.readObject();

        if (reponse.isValide()) {
            // Décryptage symétrique du message
            byte[] messageDecrypte;
            System.out.println("Message reçu = " + new String(reponse.getFactures()));
            messageDecrypte = MyCrypto.DecryptSymDES(cleSession, reponse.getFactures());
            System.out.println("Decryptage symétrique du message...");

            // Récupération des données claires
            ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);


            DefaultTableModel articleTables = (DefaultTableModel) factureView.getTable1().getModel();

            for (FactureBill factureBill : factures) {
                Vector ligne = new Vector();
                ligne.add(factureBill.getIdFacture());
                ligne.add(factureBill.getDateFacture());
                ligne.add(factureBill.getMontant());
                articleTables.addRow(ligne);
            }
        }


/*
    private void getFacturesImpayees() throws Exception {

    }


 */
    }
}
