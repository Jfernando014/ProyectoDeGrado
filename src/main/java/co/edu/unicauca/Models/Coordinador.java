package co.edu.unicauca.Models;

import java.util.List;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class Coordinador extends Persona {
    
    Departamento departamento;
    List<FormatoA> listaProyectos;

    public Coordinador(Departamento departamento, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        this.departamento = departamento;
    }

    public Coordinador() {
       
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
