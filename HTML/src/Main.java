import Controleur.Controleur;
import GUI.View;
import ServeurGenerique.Serveur;
import com.formdev.flatlaf.FlatDarculaLaf;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        View view = new View();

        Controleur controleur = new Controleur(view);
        view.setControleur(controleur);

    }
}