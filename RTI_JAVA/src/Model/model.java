package Model;
import GUI.App;

import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.net.Socket;

public class model  {
    public static int TAILLE_MAX_DATA = 1000;

    private article articleCourant;

    public static Socket csocket;

    private static model INSTANCE;

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
                return i;
            }

            lu1 = (byte) nbLus;

            if (lu1 == '%') {
                if ((nbLus = inputStream.read()) == -1) {
                    return -1;
                }

                if (nbLus == 0) {
                    return i;
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
    public String echange(String requete) {
        try {
            byte[] requeteBytes = requete.getBytes("UTF-8");

            int nbEcrits = model.getInstance().Send(model.csocket, requeteBytes, requeteBytes.length);

            if (nbEcrits > 0) {
                byte[] reponseBuffer = new byte[model.TAILLE_MAX_DATA];
                int nbLus = model.getInstance().Receive(model.csocket.getInputStream(), reponseBuffer);

                if (nbLus > 0) {
                    String reponse = new String(reponseBuffer, 0, nbLus, "UTF-8");
                    return reponse;
                } else {
                    return "Aucune réponse du serveur.";
                }
            } else {
                return "Échec de l'envoi de la requête au serveur.";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Erreur lors de la communication avec le serveur.";
        }
    }

    public String Login(String name, String password,int check) {
        String message = "LOGIN#" + name + "#" + password + "#" + check;
        String response = echange(message);

        if (response != null) {
            System.out.println("Réponse du serveur : " + response);
        } else {
            System.out.println("Aucune réponse du serveur.");
        }

        return response;
    }

    public String Cancel(int id ,int idstock){
        String message = "CANCEL#" + id + "#" + idstock;
        String response = echange(message);

        if (response != null) {
            System.out.println("Réponse du serveur : " + response);
        } else {
            System.out.println("Aucune réponse du serveur.");
        }

        return response;
    }

    public String cancelAll(DefaultTableModel articleTables) {
        StringBuilder messageBuilder = new StringBuilder("CANCELALL#" + articleTables.getRowCount());

        for (int i = 0; i < articleTables.getRowCount(); i++) {
            int currentId = (int) articleTables.getValueAt(i, 0);
            int currentQuantite = (int) articleTables.getValueAt(i, 3);

            // Utilisez la quantité correcte pour chaque article
            messageBuilder.append("#").append(currentId).append("#").append(currentQuantite);
        }

        String message = messageBuilder.toString();
        System.out.println("Message d'annulation : " + message);

        String response = echange(message);
        System.out.println("Reponse " + response);

        return response;
    }

    public String CONFIRMER(String name,float totalCaddie) {
        article.numFactures++;
        String message = "CONFIRMER#" + name + "#" + article.numFactures + "#" + totalCaddie;
        String response = echange(message);
        if (response != null) {
            System.out.println("Réponse du serveur : " + response);
        } else {
            System.out.println("Aucune réponse du serveur.");
        }

        return response;
    }

    public String Achat(int id,int check) {
        String message = "ACHAT#" + id + "#" + check;
        String response = echange(message);
        if (response != null) {
            System.out.println("Réponse du serveur : " + response);
        } else {
            System.out.println("Aucune réponse du serveur.");
        }

        return response;
    }

    public void Logout(){
        String message = "LOGOUT";
        String response = echange(message);
        if (response != null) {
            System.out.println("Réponse du serveur : " + response);
        } else {
            System.out.println("Aucune réponse du serveur.");
        }
    }
    public void getArticle(int id) throws UnsupportedEncodingException {

        article article;

        String message = "CONSULT#" + id;
        System.out.println("CONSULT " + message);
        String reponse = echange(message);

        if (reponse != null) {
            System.out.println("Réponse du serveur : " + reponse);
        } else {
            System.out.println("Aucune réponse du serveur.");
        }
        System.out.println("REPONSE " + reponse);

        String [] token;

        token = reponse.split("#");


        if(token[0].equals("CONSULT"))
        {
            if(!token[1].equals("-1"))
            {
               int idd  = id;
               String intitule = token[2];
               float prix = Float.parseFloat(token[3]);
               int stock = Integer.parseInt(token[4]);
               String image  = token[5];

               System.out.println(image);
               article = new article(idd,intitule,prix,stock,image);
               articleCourant = article;
            }
        }
    }

    public void setArticleCourant(article articleCourant) {
        this.articleCourant = articleCourant;
    }

    public article getArticleCourant() {
        return articleCourant;
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