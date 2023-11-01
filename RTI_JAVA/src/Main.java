import GUI.App;
import Model.model;
import com.formdev.flatlaf.FlatDarculaLaf;
import Controleur.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        Controleur controleur = new Controleur();
        model m = new model();
        m.Connect();

        App app = new App();
        app.setControleur(controleur);
        controleur.setApp(app);
        app.setVisible(true);
    }
}
