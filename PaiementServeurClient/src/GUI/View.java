package GUI;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class View extends JFrame{
    private JPanel panel1;
    private JLabel LabelProtocol;
    private JPanel VESPATEXT;
    private JTextField VESPATextField;
    private JButton LOGINButton;
    private JButton LOGOUTButton;
    private JTable table1;
    private JButton CLEANTABLEButton;

    public View() {
        setContentPane(panel1);
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Serveur GUI");
        DefaultTableModel tableModelEmp = (DefaultTableModel) table1.getModel();
        String[] nomsColonnes = {"THREAD", "ACTION"};
        tableModelEmp.setColumnIdentifiers(nomsColonnes);
        tableModelEmp.addRow(nomsColonnes);

        setVisible(true);
    }

    public static void main(String[] args){
        FlatDarculaLaf.setup();
        View view = new View();

    }

    public JTable getTable1() {
        return table1;
    }
}
