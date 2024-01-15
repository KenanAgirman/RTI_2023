import BD.DatabaseConnection;
import Handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class    Main {
    public static void main(String[] args) {
        HttpServer serveur = null;
        DatabaseConnection connection = null;

        try {
            connection = new DatabaseConnection(); // Assurez-vous d'avoir un constructeur approprié
            serveur = HttpServer.create(new InetSocketAddress(8080), 0);
            serveur.createContext("/", new HandlerHtml());
            serveur.createContext("/css", new HandlerCSS());
            serveur.createContext("/images", new HandlerImages());
            serveur.createContext("/javascript", new HandlerJS());
            serveur.createContext("/FormArticle", new HandlerFormulaire(connection));
            System.out.println("Demarrage du serveur HTTP...");
            serveur.start();
        } catch (IOException e) {
            System.out.println("Erreur: " + e.getMessage());
        }

    }
}