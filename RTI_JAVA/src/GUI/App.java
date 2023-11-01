package GUI;

import Controleur.Controleur;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App extends JFrame{
    private JPanel panel1;
    private JTextField SetLogin;
    private JTextField SetPassword;
    private JCheckBox isNouveauCheckBox;
    private JTextField SetPublicite;
    private JButton supprimerButton;
    private JButton viderPanierButton;
    private JButton confirmerButton;
    private JTextField textField1;
    private JButton payerButton;
    private JButton loginButton;
    private JButton logoutButton;
    private JTable table1;
    private String nom;
    private String mdp;

    public App(){
    setContentPane(panel1);
    setSize(900,600);
    setTitle("Maraicher");
    pack();

    }
    public void setControleur(Controleur c){
        loginButton.addActionListener(c);
        this.addWindowListener(c);
    }

    public String getNom() {
        return nom;
    }

    public String getMdp() {
        return mdp;
    }

    public JButton getLoginButton() {
        return loginButton;
    }

    public JTextField getSetLogin() {
        return SetLogin;
    }

    public JTextField getSetPassword() {
        return SetPassword;
    }
}
