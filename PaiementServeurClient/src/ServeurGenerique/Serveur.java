package ServeurGenerique;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Serveur {
    public void LoginServeur() throws IOException {
        ServerSocket ssocket;
        Socket csocket;

        // Création de la socket
        ssocket = new ServerSocket(50000);

        // Attente d'une connexion
        System.out.println("Attente d'une 1ere connexion...");
        csocket = ssocket.accept();
        System.out.println("Connexion établie.");

        // Caractéristiques des sockets
        System.out.println("--- ServerSocket ---");
        System.out.println("Adresse IP locale : " +
                ssocket.getInetAddress().getHostAddress());
        System.out.println("Port local : " + ssocket.getLocalPort());

        System.out.println("--- Socket ---");
        System.out.println("Adresse IP locale : " +
                csocket.getLocalAddress().getHostAddress());
        System.out.println("Port local : " + csocket.getLocalPort());
        System.out.println("Adresse IP distante : " +
                csocket.getInetAddress().getHostAddress());
        System.out.println("Port distant : " + csocket.getPort());
        System.out.println("LEILA ");
        // Fermeture de la connexion
        ssocket.close();
        csocket.close();

        // Attente d'une connexion avec Time Out
        ssocket = new ServerSocket(50000);
        ssocket.setSoTimeout(3000);
        System.out.println("Attente d'une 2eme connexion...");
        try
        {
            csocket = ssocket.accept();
            System.out.println("Connexion établie.");
            ssocket.close();
            csocket.close();
        }
        catch(SocketTimeoutException e)
        {
            System.out.println("Time Out : " + e.getMessage());
        }
    }
}

