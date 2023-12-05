package GUI;

import ControleurFacture.ControleurFacture;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class CarteBancaireVisa extends JFrame{
    private JPanel panel1;
    private JTextField textField1NumCarte;
    private JButton validerButton;
    private JTextField textFieldDate;
    private JTextField textFieldCVV;
    private JLabel LabelDateExpiration;
    private JLabel LabelNumCarte;
    private JLabel CVVLabel;

    public CarteBancaireVisa(){
        setTitle("Carte visa");
        setSize(400, 200);
        setContentPane(panel1);

    }

    public JLabel getLabelNumCarte() {
        return LabelNumCarte;
    }

    public void setControleur(ControleurFacture c){
        getValiderButton().addActionListener(c);
        this.addWindowListener(c);
    }
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
    }

    public JTextField getTextField1NumCarte() {
        return textField1NumCarte;
    }

    public JTextField getTextFieldDate() {
        return textFieldDate;
    }

    public JButton getValiderButton() {
        return validerButton;
    }

    public JTextField getTextFieldCVV() {
        return textFieldCVV;
    }
}
