package Controleur;

import CONFIG.ConfigReader;
import GUI.View;
import Protocol.VESPA;
import Protocol.VESPAS;
import ServeurGenerique.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.util.Vector;

public class Controleur extends WindowAdapter implements Logger,ActionListener {
    private View view;
    private ConfigReader configReader;

    ThreadServeur threadServeur;
    Protocole protocole;
    public Controleur(View view) {
        this.view = view;
        this.configReader = new ConfigReader();
    }

    public ThreadServeur getThreadServeur() {
        return threadServeur;
    }

    public ConfigReader getConfigReader() {
        return configReader;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getLOGINButton()) {
           DEMARRER();
        }

        if(e.getSource()==view.getCLEANTABLEButton()){
            CLEANALL();
        }

        if(e.getSource()==view.getLOGOUTButton()){
            LOGOUT();
        }
    }

    public void DEMARRER() {
        try {
            System.out.println("Kenan");
            int port = configReader.getPort();
            protocole = new VESPAS(this);
            threadServeur = new ThreadServeurDemande(port, protocole, this);
            threadServeur.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void CLEANALL(){
        DefaultTableModel articleTables = (DefaultTableModel) view.getTable1().getModel();

        for (int i = 0; i < articleTables.getRowCount(); i++) {

            articleTables.removeRow(i);
        }
    }

    public void LOGOUT(){

        if(threadServeur!=null){
            threadServeur.interrupt();

        }
    }

    @Override
    public void Trace(String message) {
        DefaultTableModel articleTables = (DefaultTableModel) view.getTable1().getModel();
        Vector<String> ligne = new Vector<>();
        ligne.add(Thread.currentThread().getName());
        ligne.add(message);
        articleTables.insertRow(articleTables.getRowCount(),ligne);
    }
}

