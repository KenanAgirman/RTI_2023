package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import GUI.App;
import Model.*;

import javax.swing.*;


public class Controleur extends WindowAdapter implements ActionListener {
    private App app;
    private String nom;
    private String mdp;
    model modele = model.getInstance();

    public Controleur() throws IOException {
        //modele.Connect();
    }



    public void setApp(App app) {
        this.app = app;
    }

    public App getApp() {
        return app;
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
    }

    public void LoginServeur() throws Exception {
        try {
            int check;
            String reponse;
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


            modele.Login(nom, mdp,check);


            modele.getArticle(1);
            SetArticle();

        }catch (Exception exception)
        {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }
    public void Payez() {
        try {
            int quant = (int) app.getSpinner1().getValue();
            System.out.println("QUANT " + quant);

            if(quant==0) throw new Exception("Veuillez avoir au moins 1 quantité");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void SetArticle(){
        article arti;

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
}