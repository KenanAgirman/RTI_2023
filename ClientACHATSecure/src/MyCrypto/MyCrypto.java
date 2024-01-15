package MyCrypto;

import javax.crypto.*;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class MyCrypto {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static String encodePublicKey(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String encodePrivateKey(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static PublicKey decodePublicKey(String encodedPublicKey) throws Exception {
        byte[] publicKeyBytes = Base64.getDecoder().decode(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey decodePrivateKey(String encodedPrivateKey) throws Exception {
        byte[] privateKeyBytes = Base64.getDecoder().decode(encodedPrivateKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        return keyFactory.generatePrivate(keySpec);
    }

    public static byte[] encryptRSA(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }


    public static String decryptRSA(byte[] encryptedData, PrivateKey privateKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }

    public static byte[] CryptSymDES(SecretKey cle,byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrementE = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }

    public static byte[] DecryptSymDES(SecretKey cle,byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrementD = Cipher.getInstance("DES/ECB/PKCS5Padding","BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }

    public static byte[] CryptAsymRSA(PublicKey cle,byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrementE = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        chiffrementE.init(Cipher.ENCRYPT_MODE, cle);
        return chiffrementE.doFinal(data);
    }
    public static byte[] DecryptAsymRSA(PrivateKey cle,byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher chiffrementD = Cipher.getInstance("RSA/ECB/PKCS1Padding","BC");
        chiffrementD.init(Cipher.DECRYPT_MODE, cle);
        return chiffrementD.doFinal(data);
    }


}

