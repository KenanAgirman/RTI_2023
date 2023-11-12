package GUI;

import Controleur.Controleur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        setSize(900, 600);
        setTitle("Maraicher");
        pack();
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        /*
        getPayerButton().setEnabled(false);
        getButton2Droite().setEnabled(false);
        getButton1Gauche().setEnabled(false);
        getLogoutButton().setEnabled(false);
        getLabelImage().setEnabled(false);
         */
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
        spinner1.setModel(spinnerModel);
        DefaultTableModel tableModelEmp = (DefaultTableModel) table1.getModel();
        String[] nomsColonnes = {"Id","Article", "Prix à l'unité", "Quantité"};
        tableModelEmp.setColumnIdentifiers(nomsColonnes);

    }
    public void setControleur(Controleur c){
        loginButton.addActionListener(c);
        button2Droite.addActionListener(c);
        button1Gauche.addActionListener(c);
        getPayerButton().addActionListener(c);
        getLogoutButton().addActionListener(c);
        getTotalArticle().addActionListener(c);
        getSupprimerButton().addActionListener(c);
        getViderPanierButton().addActionListener(c);
        getViderPanierButton().addActionListener(c);
        getConfirmerButton().addActionListener(c);
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

    public JButton getPayerButton() {
        return payerButton;
    }

    public JCheckBox getIsNouveauCheckBox() {
        return isNouveauCheckBox;
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

    public JButton getLogoutButton() {
        return logoutButton;
    }

    public JTextField getTotalArticle() {
        return TotalArticle;
    }

    public JTable getTable1() {
        return table1;
    }

    public JButton getConfirmerButton() {
        return confirmerButton;
    }

    public JButton getViderPanierButton() {
        return viderPanierButton;
    }

    public JButton getSupprimerButton() {
        return supprimerButton;
    }

    public JSpinner getSpinner1() {
        return spinner1;
    }
}
