package Protocol;

import ServeurGenerique.Requete;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Date;

public class RequeteLoginS implements Requete {
    private String login;
    private long temps;
    private double alea;
    private byte[] digest; // digest envoyé

    public RequeteLoginS(String login,String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        this.login = login;
        Security.addProvider(new BouncyCastleProvider());

        // Construction du sel
        this.temps = new Date().getTime();
        this.alea = Math.random();

        // Construction du digest salé
        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        digest = md.digest();
    }

    public String getLogin() { return login; }

    public boolean VerifyPassword(String password) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        // Construction du digest local
        MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
        md.update(login.getBytes());
        md.update(password.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(temps);
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        byte[] digestLocal = md.digest();

        // Comparaison digest reçu et digest local
        return MessageDigest.isEqual(digest,digestLocal);
    }
}
