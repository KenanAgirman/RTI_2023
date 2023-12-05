package GenereClesRSA;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.*;

public class GenereClesRSA {
    public static void main(String args[]) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        Security.addProvider(new BouncyCastleProvider());

        // Génération des clés
        KeyPairGenerator genCles = KeyPairGenerator.getInstance("RSA","BC");
        genCles.initialize(512,new SecureRandom()); // 512 par exemple
        KeyPair deuxCles = genCles.generateKeyPair();
        PublicKey clePublique = deuxCles.getPublic();
        PrivateKey clePrivee = deuxCles.getPrivate();
        System.out.println(" *** Cle publique generee = " + clePublique);
        System.out.println(" *** Cle privee generee = " + clePrivee);

        // Sérialisation des clés dans des fichiers différents
        ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream("clePubliqueServeur.ser"));
        oos1.writeObject(clePublique);
        oos1.close();
        System.out.println("Sérialisation de la clé publique dans le fichier clePubliqueServeur.ser");

        ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream("clePriveeServeur.ser"));
        oos2.writeObject(clePrivee);
        oos2.close();
        System.out.println("Sérialisation de la clé privée dans le fichier clePriveeServeur.ser");
    }
}
