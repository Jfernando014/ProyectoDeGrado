package co.edu.unicauca.DTO;

/**
 *
 * @author J. Fernando
 */

public class ProyectoResumen {
    private int idProyecto;
    private String titulo;
    private String estado;
    private String archivoAdjunto;
    private String fechaDeSubida;

    public ProyectoResumen(int idProyecto, String titulo, String estado, String archivoAdjunto, String fechaDeSubida) {
        this.idProyecto = idProyecto;
        this.titulo = titulo;
        this.estado = estado;
        this.archivoAdjunto = archivoAdjunto;
        this.fechaDeSubida = fechaDeSubida;
    }

    public int getIdProyecto() { return idProyecto; }
    public String getTitulo() { return titulo; }
    public String getEstado() { return estado; }
    public String getArchivoAdjunto() { return archivoAdjunto; }
    public String getFechaDeSubida() { return fechaDeSubida; }

    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setArchivoAdjunto(String archivoAdjunto) { this.archivoAdjunto = archivoAdjunto; }
    public void setFechaDeSubida(String fechaDeSubida) { this.fechaDeSubida = fechaDeSubida; }
}
