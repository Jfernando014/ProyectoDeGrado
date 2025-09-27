package co.edu.unicauca.Models;
/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class Departamento {
    private int codigoDepartamento;
    private String nombreDepartamento;
    private Facultad facultad;

    public Departamento(int codigoDepartamento, String nombreDepartamento, Facultad facultad) {
        this.codigoDepartamento = codigoDepartamento;
        this.nombreDepartamento = nombreDepartamento;
        this.facultad = facultad;
    }

    public Departamento() {
    }

    public Departamento(int codigoDepartamento, String nombreDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
        this.nombreDepartamento = nombreDepartamento;
    }

    public Departamento(int codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public int getCodigoDepartamento() {
        return codigoDepartamento;
    }

    public void setCodigoDepartamento(int codigoDepartamento) {
        this.codigoDepartamento = codigoDepartamento;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
    
}