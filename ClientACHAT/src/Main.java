import java.io.IOException;
import java.net.Socket;

public class Main {
        public static void main(String args[]) throws IOException, IOException {
            Socket csocket;

            // Création de la socket et connexion sur le serveur
            csocket = new Socket("192.168.15.1",50000);
            System.out.println("Connexion établie.");

            // Caractéristiques de la socket
            System.out.println("--- Socket ---");
            System.out.println("Adresse IP locale : " +
                    csocket.getLocalAddress().getHostAddress());
            System.out.println("Port local : " + csocket.getLocalPort());
            System.out.println("Adresse IP distante : " +
                    csocket.getInetAddress().getHostAddress());
            System.out.println("Port distant : " + csocket.getPort());

            csocket.close();
        }
}