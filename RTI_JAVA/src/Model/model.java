package Model;
import java.io.*;
import java.net.Socket;

public class model  {
    public static int TAILLE_MAX_DATA = 1000;

    public static Socket csocket;
    private static model INSTANCE;

    // Ajoutez un constructeur privé pour empêcher l'instanciation directe
    private model() {
    }

    public static model getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new model();
        }

        return INSTANCE;
    }
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

    public int Receive(InputStream inputStream, byte[] data) throws IOException {
        boolean fini = false;
        int nbLus, i = 0;
        byte lu1, lu2;

        while (!fini) {
            if ((nbLus = inputStream.read()) == -1) {
                return -1;
            }

            if (nbLus == 0) {
                return i; // connexion fermée par le client
            }

            lu1 = (byte) nbLus;

            if (lu1 == '%') {
                if ((nbLus = inputStream.read()) == -1) {
                    return -1;
                }

                if (nbLus == 0) {
                    return i; // connexion fermée par le client
                }

                lu2 = (byte) nbLus;

                if (lu2 == ')') {
                    fini = true;
                } else {
                    data[i] = lu1;
                    data[i + 1] = lu2;
                    i += 2;
                }
            } else {
                data[i] = lu1;
                i++;
            }
        }

        return i;
    }
    public boolean Login(String name, String password) throws UnsupportedEncodingException {
        String message = "LOGIN#" + name + "#" + password + "#" + 0;
        byte[] messageBytes = message.getBytes("UTF-8");
        int nbEcrits = model.getInstance().Send(model.csocket, messageBytes, messageBytes.length);

        if (nbEcrits > 0) {
            try {
                byte[] responseBuffer = new byte[1024]; // Ajustez la taille du tampon en conséquence
                int nbLus = Receive(model.csocket.getInputStream(), responseBuffer);

                if (nbLus > 0) {
                    // Traitement de la réponse du serveur
                    String response = new String(responseBuffer, 0, nbLus, "UTF-8");
                    System.out.println("Réponse du serveur : " + response);
                    // Vous pouvez faire plus de traitement ici selon la réponse du serveur
                } else {
                    System.out.println("Aucune réponse du serveur.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
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