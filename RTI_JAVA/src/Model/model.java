package Model;

import java.io.IOException;
import java.net.Socket;

public class model {
    public static void Connect() throws IOException{
        Socket csocket;

        csocket = new Socket("192.168.146.128",50000);
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

    public model() {
    }
}
