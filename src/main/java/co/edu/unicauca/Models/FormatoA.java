package co.edu.unicauca.Models;

import co.edu.unicauca.Util.Tipo;
import co.edu.unicauca.Util.ConstantesValidacion;
import java.util.List;


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

    public FormatoA(int idProyecto, String titulo, String objetivo, String objetivoEspecifico, String estado, 
                   Tipo tipo, String fechaDeSubida, String fechaRevision, String archivoAdjunto, 
                   List<Estudiante> estudiantes, List<Profesor> profesores, Coordinador coordinador) {
        setIdProyecto(idProyecto);
        setTitulo(titulo);
        setObjetivo(objetivo);
        setObjetivoEspecifico(objetivoEspecifico);
        setEstado(estado);
        setTipo(tipo);
        setFechaDeSubida(fechaDeSubida);
        setFechaRevision(fechaRevision);
        setArchivoAdjunto(archivoAdjunto);
        setEstudiantes(estudiantes);
        setProfesores(profesores);
        setCoordinador(coordinador);
    }

    public FormatoA(String titulo, String objetivo, String objetivoEspecifico, String estado, Tipo tipo, 
                   String fechaDeSubida, String archivoAdjunto, List<Estudiante> estudiantes, List<Profesor> profesores) {
        setTitulo(titulo);
        setObjetivo(objetivo);
        setObjetivoEspecifico(objetivoEspecifico);
        setEstado(estado);
        setTipo(tipo);
        setFechaDeSubida(fechaDeSubida);
        setArchivoAdjunto(archivoAdjunto);
        setEstudiantes(estudiantes);
        setProfesores(profesores);
        this.coordinador = null;
    }
    
    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        if (idProyecto < 0) {
            throw new IllegalArgumentException("El ID del proyecto no puede ser negativo");
        }
        this.idProyecto = idProyecto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_TITULO_OBLIGATORIO);
        }
        
        String tituloTrim = titulo.trim();
        if (tituloTrim.length() < ConstantesValidacion.MIN_LONGITUD_TITULO || 
            tituloTrim.length() > ConstantesValidacion.MAX_LONGITUD_TITULO) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_TITULO_LONGITUD);
        }
        
        this.titulo = tituloTrim;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        if (objetivo == null || objetivo.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_OBJETIVO_OBLIGATORIO);
        }
        
        String objetivoTrim = objetivo.trim();
        if (objetivoTrim.length() < ConstantesValidacion.MIN_LONGITUD_OBJETIVO || 
            objetivoTrim.length() > ConstantesValidacion.MAX_LONGITUD_OBJETIVO) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_OBJETIVO_LONGITUD);
        }
        
        this.objetivo = objetivoTrim;
    }

    public String getObjetivoEspecifico() {
        return objetivoEspecifico;
    }

    public void setObjetivoEspecifico(String objetivoEspecifico) {
        if (objetivoEspecifico == null || objetivoEspecifico.trim().isEmpty()) {
            throw new IllegalArgumentException("El objetivo específico es obligatorio");
        }
        
        String objetivoTrim = objetivoEspecifico.trim();
        if (objetivoTrim.length() < ConstantesValidacion.MIN_LONGITUD_OBJETIVO_ESPECIFICO || 
            objetivoTrim.length() > ConstantesValidacion.MAX_LONGITUD_OBJETIVO_ESPECIFICO) {
            throw new IllegalArgumentException("El objetivo específico debe tener entre " + 
                ConstantesValidacion.MIN_LONGITUD_OBJETIVO_ESPECIFICO + " y " + 
                ConstantesValidacion.MAX_LONGITUD_OBJETIVO_ESPECIFICO + " caracteres");
        }
        
        this.objetivoEspecifico = objetivoTrim;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        
        boolean estadoValido = false;
        for (String estadoValidoItem : ConstantesValidacion.ESTADOS_PROYECTO) {
            if (estadoValidoItem.equals(estado)) {
                estadoValido = true;
                break;
            }
        }
        
        if (!estadoValido) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_ESTADO_INVALIDO + ": " + estado);
        }
        
        this.estado = estado;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_TIPO_OBLIGATORIO);
        }
        this.tipo = tipo;
    }

    public String getFechaDeSubida() {
        return fechaDeSubida;
    }

    public void setFechaDeSubida(String fechaDeSubida) {
        if (fechaDeSubida == null || fechaDeSubida.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de subida es obligatoria");
        }
        this.fechaDeSubida = fechaDeSubida;
    }

    public String getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(String fechaRevision) {
        this.fechaRevision = fechaRevision; // Puede ser null inicialmente
    }

    public String getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(String archivoAdjunto) {
        if (archivoAdjunto == null || archivoAdjunto.trim().isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_ARCHIVO_OBLIGATORIO);
        }
        
        if (!archivoAdjunto.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("El archivo adjunto debe ser un PDF");
        }
        
        this.archivoAdjunto = archivoAdjunto;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        if (estudiantes == null || estudiantes.isEmpty()) {
            throw new IllegalArgumentException(ConstantesValidacion.MSG_ESTUDIANTES_OBLIGATORIOS);
        }
        
        // Validar límites según el tipo de proyecto
        if (this.tipo != null) {
            if (this.tipo == Tipo.PracticaProfesional && estudiantes.size() > ConstantesValidacion.MAX_ESTUDIANTES_PRACTICA) {
                throw new IllegalArgumentException(ConstantesValidacion.MSG_MAX_ESTUDIANTES_PRACTICA);
            }
            
            if (this.tipo == Tipo.Investigacion && estudiantes.size() > ConstantesValidacion.MAX_ESTUDIANTES_INVESTIGACION) {
                throw new IllegalArgumentException(ConstantesValidacion.MSG_MAX_ESTUDIANTES_INVESTIGACION);
            }
        }
        
        this.estudiantes = estudiantes;
    }

    public List<Profesor> getProfesores() {
        return profesores;
    }

    public void setProfesores(List<Profesor> profesores) {
        if (profesores == null) {
            throw new IllegalArgumentException("La lista de profesores no puede ser nula");
        }
        this.profesores = profesores;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador; // Puede ser null inicialmente
    }
    
    public void validarIntegridad() {
        // Validar que todos los campos obligatorios estén presentes
        if (this.titulo == null || this.objetivo == null || this.objetivoEspecifico == null ||
            this.estado == null || this.tipo == null || this.fechaDeSubida == null ||
            this.archivoAdjunto == null || this.estudiantes == null || this.profesores == null) {
            throw new IllegalStateException("Todos los campos obligatorios deben estar completos");
        }
        
        // Validar límites de estudiantes según el tipo
        if (this.tipo == Tipo.PracticaProfesional && this.estudiantes.size() > ConstantesValidacion.MAX_ESTUDIANTES_PRACTICA) {
            throw new IllegalStateException(ConstantesValidacion.MSG_MAX_ESTUDIANTES_PRACTICA);
        }
        
        if (this.tipo == Tipo.Investigacion && this.estudiantes.size() > ConstantesValidacion.MAX_ESTUDIANTES_INVESTIGACION) {
            throw new IllegalStateException(ConstantesValidacion.MSG_MAX_ESTUDIANTES_INVESTIGACION);
        }
    }
}