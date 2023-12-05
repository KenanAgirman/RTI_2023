package Controleur;

import CONFIG.ConfigReader;
import ControleurFacture.ControleurFacture;
import GUI.Facture;
import GUI.LoginGui;
import Protocol.ReponseLOGIN;
import Protocol.ReponseLOGOUT;
import Protocol.RequeteLOGIN;
import Protocol.RequeteLOGOUT;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Controleur extends WindowAdapter implements ActionListener {

    private Socket socket;
    private ConfigReader configReader;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private LoginGui view;

    private ControleurFacture controleurFactures;
    private Facture factureView;

    private String login;
    public int idClient;

    public Controleur(LoginGui view) {
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


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getLOGINButton()) {
            LoginClient();
        }

        if (e.getSource() == view.getLOGOUTButton()) {
            LogoutClient();
        }
    }

    public void LoginClient() {
        try {
            login = view.getTextFieldLogin().getText();
            String password = view.getTextPassword().getText();

            RequeteLOGIN requete = new RequeteLOGIN(login, password);
            oos.writeObject(requete);
            ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();

            if (reponse != null && reponse.getIdUser() != 0) {
                System.out.println("Réponse du serveur : " + reponse);
                System.out.println("IDCLIENT " + reponse.getIdUser());
                idClient = reponse.getIdUser();
                view.setVisible(false);
                Facture factureView = new Facture();
                ControleurFacture controleurFacture = new ControleurFacture(factureView, idClient, oos, ois, this);

                factureView.setControleur(controleurFacture);

            } else {
                System.out.println("Erreur de login");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
        }
    }
    public void LogoutClient() {
        try {
            String login = view.getTextFieldLogin().getText();
            RequeteLOGOUT requete = new RequeteLOGOUT(login);
            System.out.println("Réponse du serveur : " + requete);
            oos.writeObject(requete);
            ReponseLOGOUT reponse = (ReponseLOGOUT) ois.readObject();
            System.out.println("Réponse du serveur : " + reponse);

        }catch (Exception exception){
            JOptionPane.showMessageDialog(null,exception.getMessage(),"Erreur",JOptionPane.ERROR_MESSAGE);
            System.out.println(exception.getMessage());

        }
    }

    public String getLogin() {
        return login;
    }
}