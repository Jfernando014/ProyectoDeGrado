package co.edu.unicauca.Models;

import java.util.List;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class Profesor extends Persona{
    
    Departamento departamento;
    List<FormatoA> listaProyectos;

    public Profesor(Departamento departamento, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        this.departamento = departamento;
    }

    public Profesor() {
        
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<FormatoA> getListaProyectos() {
        return listaProyectos;
    }

    public void setListaProyectos(List<FormatoA> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }
    
    

    
  
}