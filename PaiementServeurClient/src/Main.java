import ServeurGenerique.Serveur;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Serveur serveur = new Serveur();
        serveur.LoginServeur();
        System.out.println("AU tour du client");

    }
}