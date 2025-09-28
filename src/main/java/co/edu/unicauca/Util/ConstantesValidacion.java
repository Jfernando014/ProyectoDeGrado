package co.edu.unicauca.Util;

/**
 * Constantes para validaciones en el sistema
 */
public class ConstantesValidacion {
    
    // Límites de longitud
    public static final int MIN_LONGITUD_NOMBRE = 2;
    public static final int MAX_LONGITUD_NOMBRE = 100;
    public static final int MIN_LONGITUD_TITULO = 5;
    public static final int MAX_LONGITUD_TITULO = 200;
    public static final int MIN_LONGITUD_OBJETIVO = 10;
    public static final int MAX_LONGITUD_OBJETIVO = 1000;
    public static final int MIN_LONGITUD_OBJETIVO_ESPECIFICO = 10;
    public static final int MAX_LONGITUD_OBJETIVO_ESPECIFICO = 500;
    
    // Límites de negocio
    public static final int MAX_INTENTOS_PROYECTO = 3;
    public static final int MAX_ESTUDIANTES_INVESTIGACION = 2;
    public static final int MAX_ESTUDIANTES_PRACTICA = 1;
    public static final int MAX_ARCHIVO_MB = 20;
    
    // Patrones de validación
    public static final String PATRON_CELULAR = "\\d{10}";
    public static final String PATRON_CORREO = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PATRON_SOLO_LETRAS = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$";
    public static final String PATRON_ARCHIVO_PDF = "^.+\\.pdf$";
    
    // Estados válidos de proyectos
    public static final String[] ESTADOS_PROYECTO = {
        "Pendiente de revisión",
        "En primera evaluación Formato A",
        "En segunda evaluación Formato A", 
        "En tercera evaluación Formato A",
        "Aprobado Formato A",
        "Rechazado Formato A"
    };
    
    // Mensajes de error
    public static final String MSG_NOMBRE_OBLIGATORIO = "El nombre es obligatorio";
    public static final String MSG_NOMBRE_LONGITUD = "El nombre debe tener entre " + MIN_LONGITUD_NOMBRE + " y " + MAX_LONGITUD_NOMBRE + " caracteres";
    public static final String MSG_NOMBRE_SOLO_LETRAS = "El nombre solo puede contener letras y espacios";
    public static final String MSG_CORREO_OBLIGATORIO = "El correo electrónico es obligatorio";
    public static final String MSG_CORREO_INVALIDO = "El formato del correo electrónico es inválido";
    public static final String MSG_CELULAR_OBLIGATORIO = "El celular es obligatorio";
    public static final String MSG_CELULAR_INVALIDO = "El celular debe tener exactamente 10 dígitos";
    public static final String MSG_CONTRASENIA_OBLIGATORIA = "La contraseña es obligatoria";
    public static final String MSG_TITULO_OBLIGATORIO = "El título del proyecto es obligatorio";
    public static final String MSG_TITULO_LONGITUD = "El título debe tener entre " + MIN_LONGITUD_TITULO + " y " + MAX_LONGITUD_TITULO + " caracteres";
    public static final String MSG_OBJETIVO_OBLIGATORIO = "El objetivo general es obligatorio";
    public static final String MSG_OBJETIVO_LONGITUD = "El objetivo general debe tener entre " + MIN_LONGITUD_OBJETIVO + " y " + MAX_LONGITUD_OBJETIVO + " caracteres";
    public static final String MSG_ESTADO_INVALIDO = "Estado no válido";
    public static final String MSG_TIPO_OBLIGATORIO = "El tipo de proyecto es obligatorio";
    public static final String MSG_ARCHIVO_OBLIGATORIO = "El archivo adjunto es obligatorio";
    public static final String MSG_ESTUDIANTES_OBLIGATORIOS = "El proyecto debe tener al menos un estudiante";
    public static final String MSG_MAX_ESTUDIANTES_PRACTICA = "Los proyectos de práctica laboral solo permiten " + MAX_ESTUDIANTES_PRACTICA + " estudiante";
    public static final String MSG_MAX_ESTUDIANTES_INVESTIGACION = "Los proyectos de investigación permiten máximo " + MAX_ESTUDIANTES_INVESTIGACION + " estudiantes";
}