package ControleurFacture;

import Controleur.Controleur;
import GUI.CarteBancaireVisa;
import GUI.Facture;
import Protocol.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Vector;

public class ControleurFacture extends WindowAdapter implements ActionListener {
    private Facture factureView;
    private CarteBancaireVisa carteBancaireVisaView;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private List<FactureBill> factures;
    private int idlient;
    private Controleur controleur;

    public ControleurFacture(Facture factureView, int idclient, ObjectOutputStream oos, ObjectInputStream ois, Controleur controleur) {
        this.factureView = factureView;
        this.oos = oos;
        this.ois = ois;
        this.idlient = idclient;
        this.controleur = controleur;
        getFactures();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==factureView.getButtoonPayez()){
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
                carteBancaireVisaView.getLabelNumCarte().setText("Numero de carte de " + controleur.getLogin());

            }
        }
    }

    public void getFactures() {
        try {
            getFacturesPayees();
            getFacturesImpayees();
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


        RequetePayFacture requete = new RequetePayFacture(idFactureSelectionnee,nom,cvv);
        oos.writeObject(requete);
        ReponsePayFacture reponse = (ReponsePayFacture) ois.readObject();

        System.out.println("Reponse " + reponse);

        if(reponse.isValide())
        {
            JOptionPane.showMessageDialog(null, "Payement réussi", "Ok", JOptionPane.INFORMATION_MESSAGE);
        }else{
            JOptionPane.showMessageDialog(null, "Payement pas réussi", "Ko", JOptionPane.INFORMATION_MESSAGE);

        }
    }


    private void getFacturesPayees() throws Exception {
        RequeteFacture requeteFacture = new RequeteFacture(idlient, 1);
        oos.writeObject(requeteFacture);
        ReponseFacture reponseFacture = (ReponseFacture) ois.readObject();
        System.out.println("reponse facture " + reponseFacture);

        if (reponseFacture.getCheck() == 1) {
            factures = reponseFacture.getFactures();
            DefaultTableModel articleTables = (DefaultTableModel) factureView.getTable1().getModel();

            for (FactureBill factureBill : factures) {
                    Vector ligne = new Vector();
                    ligne.add(factureBill.getIdFacture());
                    ligne.add(factureBill.getDateFacture());
                    ligne.add(factureBill.getMontant());
                    articleTables.addRow(ligne);
            }
        }
    }

    private void getFacturesImpayees() throws Exception {
        System.out.println("Facture pas payees");
        RequeteFacture requeteFacture = new RequeteFacture(idlient, 0);
        oos.writeObject(requeteFacture);
        ReponseFacture reponseFacture = (ReponseFacture) ois.readObject();
        System.out.println("reponse facture " + reponseFacture);
        System.out.println("reponseFacture.getCheck() " + reponseFacture.getCheck());

        if (reponseFacture.getCheck() == 0) {
            factures = reponseFacture.getFactures();
            DefaultTableModel articleTables = (DefaultTableModel) factureView.getTable2().getModel();

            for (FactureBill factureBill : factures) {
                    Vector ligne = new Vector();
                    ligne.add(factureBill.getIdFacture());
                    ligne.add(factureBill.getDateFacture());
                    ligne.add(factureBill.getMontant());
                    articleTables.addRow(ligne);
            }
        }
    }
}
