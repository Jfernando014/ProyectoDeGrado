package co.edu.unicauca.Services;

import co.edu.unicauca.Models.FormatoA;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Repository.ProyectoRepository;
import co.edu.unicauca.Util.Tipo;
import co.edu.unicauca.Util.Validador;
import java.util.List;


public class ProyectoService {
    private ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }
    
    public boolean subirFormato(FormatoA formato) throws Exception {
        // Validar formato no nulo
        if (formato == null) {
            throw new IllegalArgumentException("El formato no puede ser nulo");
        }
        
        // Validar integridad del formato
        formato.validarIntegridad();
        
        // Validaciones específicas del servicio
        validarFormatoA(formato);
        
        // Guardar en repositorio
        return proyectoRepository.save(formato);
    }
    
    private void validarFormatoA(FormatoA formato) {
        // Validar título
        if (!Validador.esTituloValido(formato.getTitulo())) {
            throw new IllegalArgumentException("El título del proyecto no es válido");
        }
        
        // Validar objetivo general
        if (!Validador.esObjetivoValido(formato.getObjetivo())) {
            throw new IllegalArgumentException("El objetivo general no es válido");
        }
        
        // Validar objetivo específico
        if (!Validador.esObjetivoEspecificoValido(formato.getObjetivoEspecifico())) {
            throw new IllegalArgumentException("El objetivo específico no es válido");
        }
        
        // Validar estado
        if (!Validador.esEstadoValido(formato.getEstado())) {
            throw new IllegalArgumentException("El estado del proyecto no es válido");
        }
        
        // Validar tipo
        if (formato.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de proyecto es obligatorio");
        }
        
        // Validar estudiantes
        List<Estudiante> estudiantes = formato.getEstudiantes();
        if (estudiantes == null || estudiantes.isEmpty()) {
            throw new IllegalArgumentException("El proyecto debe tener al menos un estudiante");
        }
        
        // Validar límites de estudiantes según el tipo
        if (formato.getTipo() == Tipo.PracticaProfesional && estudiantes.size() > 1) {
            throw new IllegalArgumentException("Los proyectos de práctica laboral solo permiten un estudiante");
        }
        
        if (formato.getTipo() == Tipo.Investigacion && estudiantes.size() > 2) {
            throw new IllegalArgumentException("Los proyectos de investigación permiten máximo 2 estudiantes");
        }
        
        // Validar que los estudiantes tengan intentos disponibles
        for (Estudiante estudiante : estudiantes) {
            if (formato.getTipo() == Tipo.PracticaProfesional && !estudiante.puedeEnviarPractica()) {
                throw new IllegalArgumentException("El estudiante " + estudiante.getNombre() + " no tiene intentos disponibles para práctica laboral");
            }
            
            if (formato.getTipo() == Tipo.Investigacion && !estudiante.puedeEnviarInvestigacion()) {
                throw new IllegalArgumentException("El estudiante " + estudiante.getNombre() + " no tiene intentos disponibles para investigación");
            }
        }
        
        // Validar archivo adjunto
        if (formato.getArchivoAdjunto() == null || formato.getArchivoAdjunto().trim().isEmpty()) {
            throw new IllegalArgumentException("El archivo adjunto es obligatorio");
        }
        
        if (!formato.getArchivoAdjunto().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("El archivo adjunto debe ser un archivo PDF");
        }
    }
    
    public boolean aprobarFormato(FormatoA formato, String fechaRevision) {
        if (formato == null) {
            throw new IllegalArgumentException("El formato no puede ser nulo");
        }
        
        if (fechaRevision == null || fechaRevision.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de revisión es obligatoria");
        }
        
        // Validar que el formato esté en estado aprobable
        String estadoActual = formato.getEstado();
        if (!"En tercera evaluación Formato A".equals(estadoActual)) {
            throw new IllegalStateException("Solo se pueden aprobar formatos en 'tercera evaluación'");
        }
        
        // Aprobar formato
        formato.setEstado("Aprobado Formato A");
        formato.setFechaRevision(fechaRevision);
        
        try {
            return proyectoRepository.save(formato);
        } catch (Exception e) {
            throw new RuntimeException("Error al aprobar el formato: " + e.getMessage(), e);
        }
    }
    
    public boolean rechazarFormato(FormatoA formato, String fechaRevision, String motivo) {
        if (formato == null) {
            throw new IllegalArgumentException("El formato no puede ser nulo");
        }
        
        if (fechaRevision == null || fechaRevision.trim().isEmpty()) {
            throw new IllegalArgumentException("La fecha de revisión es obligatoria");
        }
        
        if (motivo == null || motivo.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo del rechazo es obligatorio");
        }
        
        // Validar que el formato esté en estado rechazable
        String estadoActual = formato.getEstado();
        if (!estadoActual.startsWith("En ") || estadoActual.contains("Aprobado") || estadoActual.contains("Rechazado")) {
            throw new IllegalStateException("No se puede rechazar un formato en estado: " + estadoActual);
        }
        
        // Rechazar formato
        formato.setEstado("Rechazado Formato A");
        formato.setFechaRevision(fechaRevision);
        
        try {
            return proyectoRepository.save(formato);
        } catch (Exception e) {
            throw new RuntimeException("Error al rechazar el formato: " + e.getMessage(), e);
        }
    }
    
    public boolean avanzarEstado(FormatoA formato) {
        if (formato == null) {
            throw new IllegalArgumentException("El formato no puede ser nulo");
        }
        
        String estadoActual = formato.getEstado();
        String nuevoEstado;
        
        switch (estadoActual) {
            case "Pendiente de revisión":
                nuevoEstado = "En primera evaluación Formato A";
                break;
            case "En primera evaluación Formato A":
                nuevoEstado = "En segunda evaluación Formato A";
                break;
            case "En segunda evaluación Formato A":
                nuevoEstado = "En tercera evaluación Formato A";
                break;
            case "En tercera evaluación Formato A":
            case "Aprobado Formato A":
            case "Rechazado Formato A":
                throw new IllegalStateException("No se puede avanzar el estado desde: " + estadoActual);
            default:
                throw new IllegalStateException("Estado no reconocido: " + estadoActual);
        }
        
        formato.setEstado(nuevoEstado);
        
        try {
            return proyectoRepository.save(formato);
        } catch (Exception e) {
            throw new RuntimeException("Error al avanzar el estado del formato: " + e.getMessage(), e);
        }
    }
}
