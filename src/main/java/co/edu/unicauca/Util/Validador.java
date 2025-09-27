package co.edu.unicauca.Util;

import java.io.File;
import java.util.regex.Pattern;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */

public class Validador {
    
    public static boolean esCorreoValido(String formatoDeCorreo,String correoElectronico)
    {
        
        if(correoElectronico == null)
            return false;

        String[] palabras = correoElectronico.split("@");
        if(palabras.length < 2)
            return false;

        if(palabras[1].equals(formatoDeCorreo))
        {
            return true;
        }
        return false;
    }
    
    public static boolean esContraseniaCorrecta(String contrasenia) {
       if(!esLongitudValida(contrasenia) || !tieneMayuscula(contrasenia) || !tieneCaracterEspecial(contrasenia) || !tieneDigito(contrasenia))
           return false;
        return true;
    }
    
    private static boolean esLongitudValida(String contrasenia) {
        return contrasenia.length() >= 6;
    }

    private static boolean tieneMayuscula(String contrasenia) {
        return Pattern.compile("[A-Z]").matcher(contrasenia).find();
    }

    private static boolean tieneCaracterEspecial(String contrasenia) {
        return Pattern.compile("[^a-zA-Z0-9]").matcher(contrasenia).find();
    }

    private static boolean tieneDigito(String contrasenia) {
        return Pattern.compile("[0-9]").matcher(contrasenia).find();
    }
    public static boolean validarArchivo(File archivoAValidar)
    {

       if (archivoAValidar == null || !archivoAValidar.exists() || !archivoAValidar.isFile()) {
           return false;
       }
       
       String nombreArchivo = archivoAValidar.getName().toLowerCase();
       if (!nombreArchivo.endsWith(".pdf")) {
           return false;
       }
       
       
       long tamañoMaximo = 20 * 1024 * 1024;
       long tamañoArchivo = archivoAValidar.length();
       
       if (tamañoArchivo > tamañoMaximo) {
           return false;
       }
       return true;
       
    }
}
