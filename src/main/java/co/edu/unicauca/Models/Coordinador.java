package co.edu.unicauca.Models;

import java.util.List;


public class Coordinador extends Persona {
    
    private Departamento departamento;
    private List<FormatoA> listaProyectos;

    public Coordinador(Departamento departamento, String nombre, String apellido, String celular, String correoElectronico, String contrasenia) {
        super(nombre, apellido, celular, correoElectronico, contrasenia);
        setDepartamento(departamento);
    }

    public Coordinador() {
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
    
    public boolean puedeAprobarProyecto(FormatoA formato) {
        if (formato == null) {
            return false;
        }
        
        // El coordinador solo puede aprobar proyectos en tercera evaluación
        return "En tercera evaluación Formato A".equals(formato.getEstado());
    }
    
    public boolean puedeRechazarProyecto(FormatoA formato) {
        if (formato == null) {
            return false;
        }
        
        // El coordinador puede rechazar proyectos en cualquier estado de evaluación
        String estado = formato.getEstado();
        return estado.startsWith("En ") && estado.contains("evaluación");
    }
}
