package Controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.net.Socket;

import GUI.App;
import Model.model;

import javax.swing.*;

import static Model.model.Send;
import static Model.model.csocket;

public class Controleur extends WindowAdapter implements ActionListener {
    private App app;
    private String nom;
    private String mdp;
    private model m1;

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
            String message = "LOGIN#" +nom +"#" + mdp +"#" +0;

            System.out.println(message);

            byte[] messageBytes = message.getBytes("UTF-8");
            int nbEcrits = Send(csocket, messageBytes,messageBytes.length);

        }catch (Exception exception)
        {
            JOptionPane.showMessageDialog(null, exception.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);

        }
    }
}
