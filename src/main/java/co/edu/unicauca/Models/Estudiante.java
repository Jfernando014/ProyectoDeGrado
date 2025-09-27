package co.edu.unicauca.Models;

import co.edu.unicauca.Models.Programa;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class Estudiante extends Persona {
    
    Programa programa;
    List<FormatoA> proyectos;
    int cantidadIntentosPractica;
    int cantidadIntentosInvestigacion;
    public Estudiante() {
    }

    public Estudiante(Programa programa, int cantidadIntentosPractica, int cantidadIntentosInvestigacion, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        this.programa = programa;
        this.cantidadIntentosPractica = cantidadIntentosPractica;
        this.cantidadIntentosInvestigacion = cantidadIntentosInvestigacion;
        proyectos = new LinkedList();
    }

    public Estudiante(Programa programa, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        this.programa = programa;
    }
    
    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public List<FormatoA> getProyectos() {
        return proyectos;
    }

    public void setProyectos(List<FormatoA> proyectos) {
        this.proyectos = proyectos;
    }

   


    
    
    
}
