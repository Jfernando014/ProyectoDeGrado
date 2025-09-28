package co.edu.unicauca.Util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Clase utilitaria para encriptación y desencriptación
 */
public class Encriptador {

    private static final String CLAVE = "1234567890ABCDEF";  
    private static final String IV = "abcdefghijklmnop";
    
    public static String encriptar(String value) {
        try {
            if (value == null) return null;
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec sks = new SecretKeySpec(CLAVE.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            
            cipher.init(Cipher.ENCRYPT_MODE, sks, ivSpec);
            
            byte[] encriptado = cipher.doFinal(value.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encriptado);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar: " + e.getMessage(), e);
        }
    }

    public static String decriptar(String encriptado) {
        try {
            if (encriptado == null) return null;
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec sks = new SecretKeySpec(CLAVE.getBytes("UTF-8"), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes("UTF-8"));
            
            cipher.init(Cipher.DECRYPT_MODE, sks, ivSpec);

            byte[] dec = cipher.doFinal(Base64.getDecoder().decode(encriptado));
            return new String(dec, "UTF-8");
        } catch (Exception ex) {
            throw new RuntimeException("Error al desencriptar: " + ex.getMessage(), ex);
        }
    }
}