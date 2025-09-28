package co.edu.unicauca.Models;
import co.edu.unicauca.Util.ConstantesValidacion;

public class Persona {
    private int id;
    private String nombre;
    private String apellido;
    private String celular;
    private String correoElectronico;
    private String contrasenia;
    private boolean isLogged;
    
    public Persona() {
    }

    public Persona(String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        setNombre(nombre);
        setApellido(apellido);
        setCelular(celular);
        setCorreoElectronico(correoElectronico);
        setContrasenia(contrasenia);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_NOMBRE_OBLIGATORIO);
        }
        
        String nombreTrim = nombre.trim();
        if (nombreTrim.length() < ConstantesValidacion.MIN_LONGITUD_NOMBRE || 
            nombreTrim.length() > ConstantesValidacion.MAX_LONGITUD_NOMBRE) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_NOMBRE_LONGITUD);
        }
        
        if (!nombreTrim.matches(ConstantesValidacion.PATRON_SOLO_LETRAS)) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_NOMBRE_SOLO_LETRAS);
        }
        
        this.nombre = nombreTrim;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }
        
        String apellidoTrim = apellido.trim();
        if (apellidoTrim.length() < ConstantesValidacion.MIN_LONGITUD_NOMBRE || 
            apellidoTrim.length() > ConstantesValidacion.MAX_LONGITUD_NOMBRE) {
            throw new IllegalArgumentException("El apellido " + ConstantesValidacion.MSG_NOMBRE_LONGITUD.toLowerCase());
        }
        
        if (!apellidoTrim.matches(ConstantesValidacion.PATRON_SOLO_LETRAS)) {
            throw new IllegalArgumentException("El apellido solo puede contener letras y espacios");
        }
        
        this.apellido = apellidoTrim;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        if (celular == null || celular.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CELULAR_OBLIGATORIO);
        }
        
        String celularTrim = celular.trim();
        if (!celularTrim.matches(ConstantesValidacion.PATRON_CELULAR)) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CELULAR_INVALIDO);
        }
        
        this.celular = celularTrim;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CORREO_OBLIGATORIO);
        }
        
        String correoTrim = correoElectronico.trim();
        if (!correoTrim.matches(ConstantesValidacion.PATRON_CORREO)) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CORREO_INVALIDO);
        }
        
        this.correoElectronico = correoTrim;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_CONTRASENIA_OBLIGATORIA);
        }
        this.contrasenia = contrasenia;
    }

    public boolean getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(boolean isLogged) {
        this.isLogged = isLogged;
    }
}