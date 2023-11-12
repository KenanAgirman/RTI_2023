package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import GUI.App;
import Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

import static java.lang.String.valueOf;

public class Controleur extends WindowAdapter implements ActionListener {
    private App app;
    private String nom;
    private String mdp;
    private float totalCaddie = 0;
    public article arti;
    private boolean annulationEffectuee = false;
    model modele = model.getInstance();

    public Controleur() throws IOException {
        modele.Connect();
    }

    public void setApp(App app) {
        this.app = app;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==app.getLoginButton())
        {
            try {
                LoginServeur();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource()==app.getButton2Droite()){
            try {
                BoutonSuivantDroite();
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource()==app.getButton1Gauche()){
            try {
                BoutonSuivantGauche();
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }

        }

        if(e.getSource()==app.getPayerButton()){
            Payez();
        }
        if(e.getSource()==app.getLogoutButton()){
            LOGOUT();
        }

        if(e.getSource()==app.getSupprimerButton()){
            SupprimerLigne();
        }

        if(e.getSource()==app.getViderPanierButton()){
            SupprimerTOUT();
        }
    }

    public void LoginServeur(){
        try {
            String reponse;
            int check;
            if(app.getIsNouveauCheckBox().isSelected())
            {
                check = 1;
            }
            else check = 0;
            if(app.getSetLogin().getText().isEmpty()) throw  new Exception("Veuillez entrez un Login !!");
            else nom = app.getSetLogin().getText();

            if(app.getSetPassword().getText().isEmpty()) throw new Exception("Veuillez entrez un MOT DE PASSE !!");
            else mdp = app.getSetPassword().getText();

            System.out.println("Login  " + nom + " Mot de passe " + mdp);


            JOptionPane.showMessageDialog(app, nom, "Informations de Connexion", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(app, mdp, "Informations de Connexion", JOptionPane.INFORMATION_MESSAGE);


            reponse = modele.Login(nom, mdp,check);
            String[] tokens;

            tokens = reponse.split("#");

            if(tokens[0].equals("LOGIN")) {
                if (tokens[1].equals("ko")){
                    JOptionPane.showMessageDialog(null, "Vous etez deja inscrit", "Login", JOptionPane.INFORMATION_MESSAGE);
                }
                else {

                    JOptionPane.showMessageDialog(null, "Bienvenue " +nom ,"Login", JOptionPane.INFORMATION_MESSAGE);
                    modele.getArticle(1);
                    LOGIN();
                    SetArticle();
                }
            }

        }catch (Exception exception)
        {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }
    public void Payez() {
        try {
            int quant = (int) app.getSpinner1().getValue();
            String reponse;
            article arti = modele.getArticleCourant();
            System.out.println("QUANT " + quant);
            int artC = arti.getId();
            if (quant == 0) throw new Exception("Veuillez avoir au moins 1 quantité");

            reponse = modele.Achat(artC, quant);

            System.out.println("REPONSE " + reponse);

            String[] token;

            token = reponse.split("#");

            if (token[0].equals("ACHAT")) {

                arti.setId(Integer.parseInt(token[1]));
                arti.setIntitule(token[2]);
                arti.setStock(Integer.parseInt(token[3]));
                arti.setPrix(Float.parseFloat(token[4]));
                int idArticle = arti.getId();
                String inti = arti.getIntitule();
                Float prix = arti.getPrix();

                totalCaddie += quant * prix;
                app.getTotalArticle().setText(valueOf(totalCaddie));

                DefaultTableModel articleTables = (DefaultTableModel) app.getTable1().getModel();
                int rowCount = articleTables.getRowCount();
                boolean articleTrouve = false;

                for (int i = 0; i < rowCount; i++) {
                    int idCaddie = (int) articleTables.getValueAt(i, 0); // Utilisez l'indice 0 pour la colonne d'id
                    if (idCaddie == idArticle) {
                        int ancienneQuantite = (int) articleTables.getValueAt(i, 3); // Utilisez l'indice 3 pour la colonne de quantité
                        int nouvelleQuantite = ancienneQuantite + quant;
                        articleTables.setValueAt(nouvelleQuantite, i, 3);
                        articleTrouve = true;
                        break;
                    }
                }

                if (!articleTrouve) {
                    ajouterArticleTablePanier(idArticle, inti, prix, quant);
                }


                arti.nbArticles++;
            }

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void SetArticle(){

        arti = modele.getArticleCourant();

        String filepath = "src/images/" + arti.getImage();
        ImageIcon imageIcon = new ImageIcon(filepath);
        System.out.println(imageIcon);
        app.getNomArticleDB().setText(arti.getIntitule());
        app.getPrixArticleDB().setText(Float.toString(arti.getPrix()));
        app.getArticleStock().setText(Float.toString(arti.getStock()));
        app.getLabelImage().setIcon(imageIcon);
    }

    public void BoutonSuivantDroite() throws UnsupportedEncodingException {
        int idArticleSuivant = modele.getArticleCourant().getId() + 1;

        if(idArticleSuivant<=22)
        {
            modele.getArticle(idArticleSuivant);
            SetArticle();
        } else JOptionPane.showMessageDialog(app,"Vous avez atteint tous les elements du maraicher","Information",JOptionPane.INFORMATION_MESSAGE);
    }

    public void BoutonSuivantGauche() throws UnsupportedEncodingException {
        int idArticleSuivant = modele.getArticleCourant().getId() -1;

        if(idArticleSuivant>=1)
        {
            modele.getArticle(idArticleSuivant);
            SetArticle();
        } else JOptionPane.showMessageDialog(app,"Vous ne pouvez pas aller plus bas !!","Information",JOptionPane.INFORMATION_MESSAGE);
    }

    public void LOGOUT(){
        modele.Logout();
        JOptionPane JOptionPane = new JOptionPane();
        JOptionPane.showMessageDialog(app,"AU REVOIR","Information",JOptionPane.INFORMATION_MESSAGE);
        app.getPayerButton().setEnabled(false);
        app.getButton2Droite().setEnabled(false);
        app.getButton1Gauche().setEnabled(false);
        app.getLogoutButton().setEnabled(false);
        app.getLabelImage().setEnabled(false);
    }
    public void LOGIN(){
        app.getPayerButton().setEnabled(true);
        app.getButton2Droite().setEnabled(true);
        app.getButton1Gauche().setEnabled(true);
        app.getLogoutButton().setEnabled(true);
        app.getLabelImage().setEnabled(true);

    }
    private void ajouterArticleTablePanier(int idArticle, String intitule, float prix, int quantite)
    {
        DefaultTableModel articleTables = (DefaultTableModel) app.getTable1().getModel();

        Vector ligne = new Vector();
        ligne.add(idArticle);
        ligne.add(intitule);
        ligne.add(prix);
        ligne.add(quantite);

        articleTables.addRow(ligne);
    }

    public void SupprimerLigne(){
        String reponse;
        int indice = app.getTable1().getSelectedRow();
        if(indice == -1)
        {
            JOptionPane JOptionPane = new JOptionPane();
            JOptionPane.showMessageDialog(app,"Veuillez Selectionnez un article !!","Information",JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            DefaultTableModel articleTables = (DefaultTableModel) app.getTable1().getModel();

            // Récupérer les informations de la ligne sélectionnée
            int IdArticle = (int) articleTables.getValueAt(indice, 0);
            String intitule = (String) articleTables.getValueAt(indice, 1);
            Float prix = (Float) articleTables.getValueAt(indice, 2);
            int quantite = (int) articleTables.getValueAt(indice, 3);

            articleTables.setValueAt(quantite - 1, indice, 3);


            if (quantite - 1 == 0) {
                articleTables.removeRow(indice);
            }
            totalCaddie -= prix;
            app.getTotalArticle().setText(String.valueOf(totalCaddie));
            reponse = modele.Cancel(IdArticle, quantite);
            System.out.println("Reponse " +  reponse);
            arti.nbArticles--;
        }
    }

    public void SupprimerTOUT() {
        String reponse;

        DefaultTableModel articleTables = (DefaultTableModel) app.getTable1().getModel();

        // Vérifiez si l'annulation a déjà été effectuée
        if (!annulationEffectuee) {
            reponse = modele.cancelAll(articleTables);
            System.out.println("Reponse " + reponse);
            annulationEffectuee = true; // Marquez l'annulation comme effectuée après l'avoir envoyée au serveur
            arti.nbArticles = 0;

            System.out.println("NB Articles après annulation : " + arti.nbArticles);
        }

        int rowCount = articleTables.getRowCount();

        for (int i = rowCount - 1; i >= 0; i--) {
            articleTables.removeRow(i);
        }

        System.out.println("TO " + arti.nbArticles);
        // Mettez à jour le totalCaddie et l'affichage
        totalCaddie = 0;
        app.getTotalArticle().setText(String.valueOf(totalCaddie));
    }



}