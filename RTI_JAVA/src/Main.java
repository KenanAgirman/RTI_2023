import GUI.App;
import com.formdev.flatlaf.FlatDarculaLaf;
import Controleur.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        Controleur controleur = new Controleur();

        App app = new App();
        app.setControleur(controleur);
        controleur.setApp(app);
        app.setVisible(true);
    }
}
