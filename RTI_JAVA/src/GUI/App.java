package GUI;

import Controleur.Controleur;

import javax.swing.*;
import java.awt.event.ActionListener;

public class App extends JFrame{
    private JPanel panel1;
    private JTextField SetLogin;
    private JTextField SetPassword;
    private JCheckBox isNouveauCheckBox;
    private JButton supprimerButton;
    private JButton viderPanierButton;
    private JButton confirmerButton;
    private JTextField TotalArticle;
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
    private JLabel LabelImage;
    private JSpinner spinner1;
    private JLabel NomMaraiche;
    private JLabel Prix;
    private JLabel Stock;
    private JTextField BIENVENUMARAICHERENLIGNETextField;
    private JTextField NomArticleDB;
    private JTextField PrixArticleDB;
    private JTextField ArticleStock;


    public App(){
    setContentPane(panel1);
    setSize(900,600);
    setTitle("Maraicher");
    pack();
    }
    public void setControleur(Controleur c){
        loginButton.addActionListener(c);
        button2Droite.addActionListener(c);
        button1Gauche.addActionListener(c);
        this.addWindowListener(c);
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


    public JTextField getNomArticleDB() {
        return NomArticleDB;
    }

    public JTextField getPrixArticleDB() {
        return PrixArticleDB;
    }

    public JTextField getArticleStock() {
        return ArticleStock;
    }

    public void setLabelImage(JLabel labelImage) {
        LabelImage = labelImage;
    }

    public JButton getButton1Gauche() {
        return button1Gauche;
    }

    public JButton getButton2Droite() {
        return button2Droite;
    }

    public JLabel getLabelImage() {
        return LabelImage;
    }
}
