import GUI.App;
import com.formdev.flatlaf.FlatDarculaLaf;
import Model.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlatDarculaLaf.setup();
        //App app = new App();
        model m1 = new model();
        m1.Connect();

        //app.setVisible(true);
    }
}