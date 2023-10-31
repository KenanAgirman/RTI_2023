import GUI.App;
import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        App app = new App();
        app.setVisible(true);
    }
}