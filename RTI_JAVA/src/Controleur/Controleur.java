package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.List;

import GUI.App;
import Model.*;

import javax.swing.*;


public class Controleur extends WindowAdapter implements ActionListener {
    private App app;
    private String nom;
    private String mdp;
    private List<article> articlesCourant;
    model modele = model.getInstance();

    public Controleur() throws IOException {
        modele.Connect();
    }

    public String getNom() {
        return nom;
    }

    public String getMdp() {
        return mdp;
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
                EnvoyerServeur();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void EnvoyerServeur() throws Exception {
        try {
            if(app.getSetLogin().getText().isEmpty()) throw  new Exception("Veuillez entrez un Login !!");
            else nom = app.getSetLogin().getText();

            if(app.getSetPassword().getText().isEmpty()) throw new Exception("Veuillez entrez un MOT DE PASSE !!");
            else mdp = app.getSetPassword().getText();

            System.out.println("Login  " + nom + " Mot de passe " + mdp);


            JOptionPane.showMessageDialog(app, nom, "Informations de Connexion", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(app, mdp, "Informations de Connexion", JOptionPane.INFORMATION_MESSAGE);


            modele.Login(nom, mdp);


            modele.getArticle(21);
            Article();

        }catch (Exception exception)
        {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }

    public void Article(){
        article arti;

        arti = modele.getArticleCourant();
        String cheminImage = arti.getImage();
        String path;

        String filepath = "src/images/" + arti.getImage();
        ImageIcon imageIcon = new ImageIcon(filepath);
        System.out.println(imageIcon);
        app.getNomArticleDB().setText(arti.getIntitule());
        app.getPrixArticleDB().setText(Float.toString(arti.getPrix()));
        app.getArticleStock().setText(Float.toString(arti.getStock()));
        app.getLabelImage().setIcon(imageIcon);

    }
}