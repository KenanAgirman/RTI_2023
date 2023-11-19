package GUI;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

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

    public static void main(String[] args){
        FlatDarculaLaf.setup();
        LoginGui view = new LoginGui();

    }
}
