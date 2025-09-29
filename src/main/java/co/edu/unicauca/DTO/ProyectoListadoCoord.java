package co.edu.unicauca.DTO;
/**
 *
 * @author J. Fernando
 */
public class ProyectoListadoCoord {
    private int idProyecto;
    private String titulo;
    private String estado;
    private String estudiantes;     // "Juan Portilla - David Arias"
    private String archivoAdjunto;
    private String fechaDeSubida;

    public ProyectoListadoCoord(int idProyecto, String titulo, String estado,
                                String estudiantes, String archivoAdjunto, String fechaDeSubida) {
        this.idProyecto = idProyecto;
        this.titulo = titulo;
        this.estado = estado;
        this.estudiantes = estudiantes;
        this.archivoAdjunto = archivoAdjunto;
        this.fechaDeSubida = fechaDeSubida;
    }

    public int getIdProyecto() { return idProyecto; }
    public String getTitulo() { return titulo; }
    public String getEstado() { return estado; }
    public String getEstudiantes() { return estudiantes; }
    public String getArchivoAdjunto() { return archivoAdjunto; }
    public String getFechaDeSubida() { return fechaDeSubida; }

    public void setIdProyecto(int v) { idProyecto = v; }
    public void setTitulo(String v) { titulo = v; }
    public void setEstado(String v) { estado = v; }
    public void setEstudiantes(String v) { estudiantes = v; }
    public void setArchivoAdjunto(String v) { archivoAdjunto = v; }
    public void setFechaDeSubida(String v) { fechaDeSubida = v; }
}
