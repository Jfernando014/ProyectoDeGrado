package co.edu.unicauca.Util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */

public class Encriptador {

    public static String encriptar(String value) {
        try {
            
            String clave = "1234567890ABCDEF";  
            byte[] iv = "abcdefghijklmnop".getBytes("UTF-8");
            
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, sks, new IvParameterSpec(iv));
            
            byte[] encriptado = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encriptado);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decriptar(String clave, byte[] iv, String encriptado) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            SecretKeySpec sks = new SecretKeySpec(clave.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, sks, new IvParameterSpec(iv));

            byte[] dec = cipher.doFinal(Base64.getDecoder().decode(encriptado));
            return new String(dec);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
