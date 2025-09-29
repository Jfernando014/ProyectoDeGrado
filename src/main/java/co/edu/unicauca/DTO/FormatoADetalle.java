package co.edu.unicauca.DTO;

public class FormatoADetalle {
    private int idProyecto;
    private String titulo;
    private String objetivo;
    private String objetivoEspecifico;
    private String estado;
    private String tipo;
    private String fechaDeSubida;
    private String archivoAdjunto;
    private String director;
    private String codirector;
    private String estudiantes; // "Juan Portilla - David Arias"

    public FormatoADetalle(int idProyecto, String titulo, String objetivo, String objetivoEspecifico,
                           String estado, String tipo, String fechaDeSubida, String archivoAdjunto,
                           String director, String codirector, String estudiantes) {
        this.idProyecto = idProyecto;
        this.titulo = titulo;
        this.objetivo = objetivo;
        this.objetivoEspecifico = objetivoEspecifico;
        this.estado = estado;
        this.tipo = tipo;
        this.fechaDeSubida = fechaDeSubida;
        this.archivoAdjunto = archivoAdjunto;
        this.director = director;
        this.codirector = codirector;
        this.estudiantes = estudiantes;
    }

    public int getIdProyecto() { return idProyecto; }
    public String getTitulo() { return titulo; }
    public String getObjetivo() { return objetivo; }
    public String getObjetivoEspecifico() { return objetivoEspecifico; }
    public String getEstado() { return estado; }
    public String getTipo() { return tipo; }
    public String getFechaDeSubida() { return fechaDeSubida; }
    public String getArchivoAdjunto() { return archivoAdjunto; }
    public String getDirector() { return director; }
    public String getCodirector() { return codirector; }
    public String getEstudiantes() { return estudiantes; }
}
