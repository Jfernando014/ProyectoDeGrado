package co.edu.unicauca.Models;

import java.util.LinkedList;
import java.util.List;
import co.edu.unicauca.Util.ConstantesValidacion;

public class Estudiante extends Persona {
    
    private Programa programa;
    private List<FormatoA> proyectos;
    private int cantidadIntentosPractica;
    private int cantidadIntentosInvestigacion;
    
    public Estudiante() {
        this.proyectos = new LinkedList<>();
        this.cantidadIntentosPractica = 0;
        this.cantidadIntentosInvestigacion = 0;
    }

    public Estudiante(Programa programa, int cantidadIntentosPractica, int cantidadIntentosInvestigacion, 
                     String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        setPrograma(programa);
        setCantidadIntentosPractica(cantidadIntentosPractica);
        setCantidadIntentosInvestigacion(cantidadIntentosInvestigacion);
        this.proyectos = new LinkedList<>();
    }

    public Estudiante(Programa programa, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        setPrograma(programa);
        this.proyectos = new LinkedList<>();
        this.cantidadIntentosPractica = 0;
        this.cantidadIntentosInvestigacion = 0;
    }
    
    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        if (programa == null) {
            throw new IllegalArgumentException("El programa es obligatorio");
        }
        this.programa = programa;
    }

    public List<FormatoA> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<FormatoA> proyectos) {
        if (proyectos == null) {
            throw new IllegalArgumentException("La lista de proyectos no puede ser nula");
        }
        this.proyectos = proyectos;
    }

    public int getCantidadIntentosPractica() {
        return cantidadIntentosPractica;
    }

    public void setCantidadIntentosPractica(int cantidadIntentosPractica) {
        if (cantidadIntentosPractica < 0) {
            throw new IllegalArgumentException("Los intentos de práctica no pueden ser negativos");
        }
        if (cantidadIntentosPractica > ConstantesValidacion.MAX_INTENTOS_PROYECTO) {
            throw new IllegalArgumentException("Máximo " + ConstantesValidacion.MAX_INTENTOS_PROYECTO + " intentos de práctica permitidos");
        }
        this.cantidadIntentosPractica = cantidadIntentosPractica;
    }

    public int getCantidadIntentosInvestigacion() {
        return cantidadIntentosInvestigacion;
    }

    public void setCantidadIntentosInvestigacion(int cantidadIntentosInvestigacion) {
        if (cantidadIntentosInvestigacion < 0) {
            throw new IllegalArgumentException("Los intentos de investigación no pueden ser negativos");
        }
        if (cantidadIntentosInvestigacion > ConstantesValidacion.MAX_INTENTOS_PROYECTO) {
            throw new IllegalArgumentException("Máximo " + ConstantesValidacion.MAX_INTENTOS_PROYECTO + " intentos de investigación permitidos");
        }
        this.cantidadIntentosInvestigacion = cantidadIntentosInvestigacion;
    }
    
    public void incrementarIntentoPractica() {
        if (this.cantidadIntentosPractica >= ConstantesValidacion.MAX_INTENTOS_PROYECTO) {
            throw new IllegalStateException("No se pueden realizar más intentos de práctica");
        }
        this.cantidadIntentosPractica++;
    }
    
    public void incrementarIntentoInvestigacion() {
        if (this.cantidadIntentosInvestigacion >= ConstantesValidacion.MAX_INTENTOS_PROYECTO) {
            throw new IllegalStateException("No se pueden realizar más intentos de investigación");
        }
        this.cantidadIntentosInvestigacion++;
    }
    
    public boolean puedeEnviarPractica() {
        return this.cantidadIntentosPractica < ConstantesValidacion.MAX_INTENTOS_PROYECTO;
    }
    
    public boolean puedeEnviarInvestigacion() {
        return this.cantidadIntentosInvestigacion < ConstantesValidacion.MAX_INTENTOS_PROYECTO;
    }
}

   


    
    
    

