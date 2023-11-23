package Controleur;

import CONFIG.ConfigReader;
import GUI.LoginGui;
import Protocol.ReponseLOGIN;
import Protocol.RequeteLOGIN;

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
    public int ip;
    public int port;
    public String nom;
    public String password;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private LoginGui view;
    public Controleur(LoginGui view) throws IOException {
        this.view = view;
        this.configReader = new ConfigReader();
        String ip = configReader.getIP();
        int port = (configReader.getPort());

        view.getTextFieldIpServeur().setText(ip);
        view.getTextFieldPortServeur().setText(String.valueOf(port));
        socket = new Socket("localhost", port);

        oos = new ObjectOutputStream(socket.getOutputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==view.getLOGINButton()){
            try {
                System.out.println("LEILA");
                LoginClient();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void LoginClient() throws IOException, ClassNotFoundException {
        try {
        nom = view.getTextFieldLogin().getText();
        password = view.getTextPassword().getText();

        System.out.println("Nom " + nom);
        System.out.println("Password " + password);

        RequeteLOGIN requete = new RequeteLOGIN(nom, password);

        oos = new ObjectOutputStream(socket.getOutputStream());

        System.out.println("REQUETE " + requete);
        oos.writeObject(requete);
    } catch (IOException e) {
        e.printStackTrace();
    }

    }
}
