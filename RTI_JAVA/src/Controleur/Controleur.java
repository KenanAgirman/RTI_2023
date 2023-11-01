package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;

import GUI.App;

import javax.swing.*;

public class Controleur extends WindowAdapter implements ActionListener {
    private App app;
    private String nom;
    private String mdp;

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
        }catch (Exception exception)
        {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }
}
