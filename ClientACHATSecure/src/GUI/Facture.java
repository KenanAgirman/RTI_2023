package GUI;

import Controleur.Controleur;
import ControleurFacture.ControleurFacture;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class Facture extends JFrame {
    private JPanel panel1;
    private JTextField textField1;
    private JTable table1;
    private JTable table2;
    private JLabel FactureAPayez;
    private JButton Payez;
    private JLabel FactureapayezText;
    private JLabel FactureDejaPayezText;

    public void setControleur(Controleur c) {
        getButtoonPayez().addActionListener(c);
        getPayez().addActionListener(c);
        this.addWindowListener(c);
    }

    public Facture() {
        setContentPane(panel1);
        setSize(600, 600);
        setTitle("Facture Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        DefaultTableModel table1Model = (DefaultTableModel) table1.getModel();
        String[] nomsColonnes = {"ID", "DATE","MONTANT"};
        table1Model.setColumnIdentifiers(nomsColonnes);
        table1Model.addRow(nomsColonnes);

        DefaultTableModel table1Model1 = (DefaultTableModel) table2.getModel();
        table1Model1.setColumnIdentifiers(nomsColonnes);
        table1Model1.addRow(nomsColonnes);

    }

    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        Facture view = new Facture();
    }

    public JButton getButtoonPayez() {
        return Payez;
    }

    public JTable getTable1() {
        return table1;
    }

    public JButton getPayez() {
        return Payez;
    }

    public JTable getTable2() {
        return table2;
    }
}

