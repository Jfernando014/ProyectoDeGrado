
package co.edu.unicauca.Models;

import co.edu.unicauca.Util.Tipo;
import java.util.List;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class FormatoA {
    private int idProyecto;
    private String titulo;
    private String objetivo;
    private String objetivoEspecifico;
    private String estado;
    private Tipo tipo;
    private String fechaDeSubida;    
    private String fechaRevision;    
    private String archivoAdjunto;  
    private List<Estudiante> estudiantes;
    private List<Profesor> profesores;
    private Coordinador coordinador;

    public FormatoA(int idProyecto, String titulo, String objetivo, String objetivoEspecifico, String estado, Tipo tipo, String fechaDeSubida, String fechaRevision, String archivoAdjunto, List<Estudiante> estudiantes, List<Profesor> profesores, Coordinador coordinador) {
        this.idProyecto = idProyecto;
        this.titulo = titulo;
        this.objetivo = objetivo;
        this.objetivoEspecifico = objetivoEspecifico;
        this.estado = estado;
        this.tipo = tipo;
        this.fechaDeSubida = fechaDeSubida;
        this.fechaRevision = fechaRevision;
        this.archivoAdjunto = archivoAdjunto;
        this.estudiantes = estudiantes;
        this.profesores = profesores;
        this.coordinador = coordinador;
    }

    public FormatoA(String titulo, String objetivo, String objetivoEspecifico, String estado, Tipo tipo, String fechaDeSubida, String archivoAdjunto, List<Estudiante> estudiantes, List<Profesor> profesores) {
        this.titulo = titulo;
        this.objetivo = objetivo;
        this.objetivoEspecifico = objetivoEspecifico;
        this.estado = estado;
        this.tipo = tipo;
        this.fechaDeSubida = fechaDeSubida;
        this.archivoAdjunto = archivoAdjunto;
        this.estudiantes = estudiantes;
        this.profesores = profesores;
    }
    
    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getObjetivoEspecifico() {
        return objetivoEspecifico;
    }

    public void setObjetivoEspecifico(String objetivoEspecifico) {
        this.objetivoEspecifico = objetivoEspecifico;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getFechaDeSubida() {
        return fechaDeSubida;
    }

    public void setFechaDeSubida(String fechaDeSubida) {
        this.fechaDeSubida = fechaDeSubida;
    }

    public String getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(String fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(String archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        this.profesores = profesores;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }
    
}
