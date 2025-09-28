package co.edu.unicauca.Models;

import java.util.List;


public class Profesor extends Persona {
    
    private Departamento departamento;
    private List<FormatoA> listaProyectos;

    public Profesor(Departamento departamento, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        setDepartamento(departamento);
    }

    public Profesor() {
        // Constructor por defecto
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        if (departamento == null) {
            throw new IllegalArgumentException("El departamento es obligatorio");
        }
        this.departamento = departamento;
    }

    public List<FormatoA> getListaProyectos() {
        return listaProyectos;
    }

    public void setListaProyectos(List<FormatoA> listaProyectos) {
        if (listaProyectos == null) {
            throw new IllegalArgumentException("La lista de proyectos no puede ser nula");
        }
        this.listaProyectos = listaProyectos;
    }
}