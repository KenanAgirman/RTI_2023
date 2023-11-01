package Model;
import java.io.*;
import java.net.Socket;

public class model  {
    public static int TAILLE_MAX_DATA = 1000;

    public static Socket csocket;

    public void Connect() throws IOException {
        csocket = new Socket("192.168.146.128", 50000);
        System.out.println("Connexion établie.");

        // Caractéristiques de la socket
        System.out.println("--- Socket ---");
        System.out.println("Adresse IP locale : " +
                csocket.getLocalAddress().getHostAddress());
        System.out.println("Port local : " + csocket.getLocalPort());
        System.out.println("Adresse IP distante : " +
                csocket.getInetAddress().getHostAddress());
        System.out.println("Port distant : " + csocket.getPort());
    }

    public static int Send(Socket socket, byte[] data, int taille) {
        try {
            if (taille > TAILLE_MAX_DATA)
                return -1;

            // Préparation de la charge utile
            byte[] trame = new byte[taille + 2];
            System.arraycopy(data, 0, trame, 0, taille);
            trame[taille] = '%';
            trame[taille + 1] = ')';

            // Obtenir le flux de sortie de la socket
            OutputStream outputStream = socket.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            // Écriture sur le flux de sortie
            dataOutputStream.write(trame, 0, taille + 2);
            dataOutputStream.flush();

            return taille;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public model() {
    }

    public void disconnect() {
        try {
            if (csocket != null) {
                csocket.close();
                System.out.println("Déconnexion.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
