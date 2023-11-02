package GUI;

import Controleur.Controleur;

import javax.swing.*;

public class App extends JFrame{
    private JPanel panel1;
    private JTextField SetLogin;
    private JTextField SetPassword;
    private JCheckBox isNouveauCheckBox;
    private JButton supprimerButton;
    private JButton viderPanierButton;
    private JButton confirmerButton;
    private JTextField textField1;
    private JButton payerButton;
    private JButton loginButton;
    private JButton logoutButton;
    private JTable table1;
    private JLabel SetMotdepasse;
    private JLabel SetLogine;
    private JLabel Nouveau;
    private JScrollPane Article;
    private JLabel Total;
    private JButton button1Gauche;
    private JButton button2Droite;
    private JLabel Label;
    private JSpinner spinner1;
    private JLabel NomMaraiche;
    private JLabel Prix;
    private JLabel Stock;
    private JTextField BIENVENUMARAICHERENLIGNETextField;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
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
