package GUI;

import javax.swing.*;

public class App extends  JFrame{
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


    public App(){
        setContentPane(panel1);
        setSize(900,600);
    }
}
