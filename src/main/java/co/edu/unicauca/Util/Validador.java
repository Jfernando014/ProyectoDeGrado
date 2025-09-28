package co.edu.unicauca.Util;

import java.io.File;
import java.util.regex.Pattern;

/**
 * Clase utilitaria para validaciones
 */
public class Validador {
    
    public static boolean esCorreoValido(String formatoDeCorreo, String correoElectronico) {
        if (correoElectronico == null || formatoDeCorreo == null)
            return false;

        String[] palabras = correoElectronico.split("@");
        if (palabras.length < 2)
            return false;

        return palabras[1].equals(formatoDeCorreo);
    }
    
    public static boolean esContraseniaCorrecta(String contrasenia) {
        return esLongitudValida(contrasenia) && 
               tieneMayuscula(contrasenia) && 
               tieneCaracterEspecial(contrasenia) && 
               tieneDigito(contrasenia);
    }
    
    private static boolean esLongitudValida(String contrasenia) {
        return contrasenia != null && contrasenia.length() >= 6;
    }

    private static boolean tieneMayuscula(String contrasenia) {
        return contrasenia != null && Pattern.compile("[A-Z]").matcher(contrasenia).find();
    }

    private static boolean tieneCaracterEspecial(String contrasenia) {
        return contrasenia != null && Pattern.compile("[^a-zA-Z0-9]").matcher(contrasenia).find();
    }

    private static boolean tieneDigito(String contrasenia) {
        return contrasenia != null && Pattern.compile("[0-9]").matcher(contrasenia).find();
    }
    
    public static boolean validarArchivo(File archivoAValidar) {
        if (archivoAValidar == null || !archivoAValidar.exists() || !archivoAValidar.isFile()) {
            return false;
        }
       
        String nombreArchivo = archivoAValidar.getName().toLowerCase();
        if (!nombreArchivo.endsWith(".pdf")) {
            return false;
        }
       
        long tamañoMaximo = ConstantesValidacion.MAX_ARCHIVO_MB * 1024 * 1024;
        long tamañoArchivo = archivoAValidar.length();
       
        return tamañoArchivo <= tamañoMaximo;
    }
    
    // NUEVAS VALIDACIONES
    
    public static boolean esNombreValido(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        
        String nombreTrim = nombre.trim();
        boolean longitudValida = nombreTrim.length() >= ConstantesValidacion.MIN_LONGITUD_NOMBRE && 
                                nombreTrim.length() <= ConstantesValidacion.MAX_LONGITUD_NOMBRE;
        boolean soloLetras = nombreTrim.matches(ConstantesValidacion.PATRON_SOLO_LETRAS);
        
        return longitudValida && soloLetras;
    }
    
    public static boolean esCelularValido(String celular) {
        return celular != null && celular.matches(ConstantesValidacion.PATRON_CELULAR);
    }
    
    public static boolean esCorreoValido(String correo) {
        return correo != null && correo.matches(ConstantesValidacion.PATRON_CORREO);
    }
    
    public static boolean esTituloValido(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        
        String tituloTrim = titulo.trim();
        return tituloTrim.length() >= ConstantesValidacion.MIN_LONGITUD_TITULO && 
               tituloTrim.length() <= ConstantesValidacion.MAX_LONGITUD_TITULO;
    }
    
    public static boolean esObjetivoValido(String objetivo) {
        if (objetivo == null || objetivo.trim().isEmpty()) {
            return false;
        }
        
        String objetivoTrim = objetivo.trim();
        return objetivoTrim.length() >= ConstantesValidacion.MIN_LONGITUD_OBJETIVO && 
               objetivoTrim.length() <= ConstantesValidacion.MAX_LONGITUD_OBJETIVO;
    }
    
    public static boolean esObjetivoEspecificoValido(String objetivoEspecifico) {
        if (objetivoEspecifico == null || objetivoEspecifico.trim().isEmpty()) {
            return false;
        }
        
        String objetivoTrim = objetivoEspecifico.trim();
        return objetivoTrim.length() >= ConstantesValidacion.MIN_LONGITUD_OBJETIVO_ESPECIFICO && 
               objetivoTrim.length() <= ConstantesValidacion.MAX_LONGITUD_OBJETIVO_ESPECIFICO;
    }
    
    public static boolean esEstadoValido(String estado) {
        if (estado == null) return false;
        
        for (String estadoValido : ConstantesValidacion.ESTADOS_PROYECTO) {
            if (estadoValido.equals(estado)) {
                return true;
            }
        }
        return false;
    }
    
    public static void validarPersona(String nombre, String apellido, String celular, String correo, String contrasenia) {
        if (!esNombreValido(nombre)) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_NOMBRE_OBLIGATORIO);
        }
        
        if (!esNombreValido(apellido)) {
            throw new IllegalArgumentException("El apellido " + ConstantesValidacion.MSG_NOMBRE_LONGITUD.toLowerCase());
        }
        
        if (!esCelularValido(celular)) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CELULAR_INVALIDO);
        }
        
        if (!esCorreoValido(correo)) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CORREO_INVALIDO);
        }
        
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CONTRASENIA_OBLIGATORIA);
        }
        
        if (!esContraseniaCorrecta(contrasenia)) {
            throw new IllegalArgumentException("La contraseña no cumple con los requisitos de seguridad");
        }
    }
}
