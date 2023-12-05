package CONFIG;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties properties;

    public ConfigReader() {
        properties = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/Files/configuration.properties")) {
            if (input == null) {
                System.out.println("Désolé, le fichier de configuration n'a pas été trouvé.");
                return;
            }
            System.out.println("Le fichier a été trouvé");
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNbThread() {
        return Integer.parseInt(properties.getProperty("NBTHREAD", "3"));
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("PORTSecure", "60000"));
    }

    public String getIP() {
        return properties.getProperty("IP", "192.168.15.1");
    }
}