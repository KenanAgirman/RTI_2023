package Protocol;

import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.rsa.BCRSAPrivateCrtKey;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class test {
    public static void main(String[] args) throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("clePriveeClient.ser"))) {
            Object obj = ois.readObject();
            if (obj instanceof BCRSAPrivateCrtKey) {
                BCRSAPrivateCrtKey bouncyCastlePrivateKey = (BCRSAPrivateCrtKey) obj;
                // Utilisez la clé privée de Bouncy Castle selon vos besoins
            } else {
                System.err.println("Le type de la clé privée n'est pas pris en charge : " + obj.getClass().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }

}
