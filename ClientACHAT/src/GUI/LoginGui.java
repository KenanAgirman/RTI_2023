package GUI;

import Controleur.Controleur;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.io.IOException;

public class LoginGui extends JFrame{
    private JPanel panel1;
    private JTextField textFieldIpServeur;
    private JTextField textFieldPortServeur;
    private JPasswordField textPassword;
    private JButton LOGINButton;
    private JButton LOGOUTButton;
    private JLabel labelIPServeur;
    private JLabel labelPortServeur;
    private JLabel labelLogin;
    private JTextField textFieldLogin;
    private JLabel labelPassword;

    public LoginGui() {
        setContentPane(panel1);
        setSize(500, 200);
        setTitle("CLIENT VESPA");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void setControleur(Controleur c){
        getLOGINButton().addActionListener(c);
        this.addWindowListener(c);
    }
    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        LoginGui view = new LoginGui();
        Controleur controleur = new Controleur(view);
        view.setControleur(controleur);
    }

    public JButton getLOGINButton() {
        return LOGINButton;
    }

    public JTextField getTextFieldIpServeur() {
        return textFieldIpServeur;
    }

    public JTextField getTextFieldPortServeur() {
        return textFieldPortServeur;
    }

    public JTextField getTextFieldLogin() {
        return textFieldLogin;
    }

    public JPasswordField getTextPassword() {
        return textPassword;
    }
}
