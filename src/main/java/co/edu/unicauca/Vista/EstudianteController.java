package co.edu.unicauca.Vista;

import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.FormatoA;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Util.Tipo;
import co.edu.unicauca.main.Main;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class EstudianteController {

    @FXML
    private Label labelBienvenida;
    
    @FXML
    private Label labelPrograma;
    
    @FXML
    private Label labelTituloProyecto;
    
    @FXML
    private Label labelTipoProyecto;
    
    @FXML
    private Label labelEstadoProyecto;
    
    @FXML
    private Label labelDetalleEstado;
    
    @FXML
    private Label labelFechaActualizacion;
    
    @FXML
    private Label labelFechaSubida;
    
    @FXML
    private Label labelFechaRevision;
    
    @FXML
    private Label labelDirectores;
    
    @FXML
    private Button btnActualizar;
    
    @FXML
    private Button btnCerrarSesion;
    
    private Estudiante estudiante;
    
    public void initialize() {
        System.out.println("✅ VistaEstudianteController inicializado");
        actualizarFecha();
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        if (estudiante != null) {
            actualizarVista();
        }
    }
    
    private void actualizarVista() {
        // Información del estudiante
        if (labelBienvenida != null) {
            labelBienvenida.setText("Bienvenido, " + estudiante.getNombre() + " " + estudiante.getApellido());
        }
        
        if (labelPrograma != null) {
            Programa programa = estudiante.getPrograma();
            if (programa != null) {
                labelPrograma.setText("Programa: " + programa.getNombrePrograma());
            } else {
                labelPrograma.setText("Programa: No asignado");
            }
        }
        
        // Información del proyecto
        actualizarEstadoProyecto();
    }
    
    private void actualizarEstadoProyecto() {
        List<FormatoA> proyectos = estudiante.getProyectos();
        
        if (proyectos == null || proyectos.isEmpty()) {
            mostrarSinProyecto();
        } else {
            // Tomar el primer proyecto (asumiendo que un estudiante tiene uno principal)
            FormatoA proyecto = proyectos.get(0);
            mostrarInformacionProyecto(proyecto);
        }
        
        actualizarFecha();
    }
    
    private void mostrarSinProyecto() {
        labelTituloProyecto.setText("Título: No hay proyecto registrado");
        labelTipoProyecto.setText("Tipo: --");
        labelEstadoProyecto.setText("SIN PROYECTO");
        labelEstadoProyecto.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;");
        labelDetalleEstado.setText("No se ha subido ningún Formato A");
        
        // Limpiar información adicional
        if (labelFechaSubida != null) labelFechaSubida.setText("Fecha de subida: --");
        if (labelFechaRevision != null) labelFechaRevision.setText("Fecha de revisión: --");
        if (labelDirectores != null) labelDirectores.setText("Directores: --");
    }
    
    private void mostrarInformacionProyecto(FormatoA proyecto) {
        // Información básica del proyecto
        labelTituloProyecto.setText("Título: " + (proyecto.getTitulo() != null ? proyecto.getTitulo() : "Sin título"));
        
        // Tipo de proyecto
        Tipo tipo = proyecto.getTipo();
        labelTipoProyecto.setText("Tipo: " + (tipo != null ? tipo.toString() : "No especificado"));
        
        // Estado del proyecto (usando el estado real de la base de datos)
        String estado = proyecto.getEstado();
        if (estado == null || estado.isEmpty()) {
            estado = "Pendiente de revisión";
        }
        labelEstadoProyecto.setText(estado.toUpperCase());
        
        // Aplicar color según el estado
        aplicarColorEstado(estado);
        
        // Detalle del estado
        labelDetalleEstado.setText(obtenerDetalleEstado(estado));
        
        // Fechas
        if (labelFechaSubida != null) {
            labelFechaSubida.setText("Fecha de subida: " + 
                (proyecto.getFechaDeSubida() != null ? proyecto.getFechaDeSubida() : "--"));
        }
        
        if (labelFechaRevision != null) {
            labelFechaRevision.setText("Fecha de revisión: " + 
                (proyecto.getFechaRevision() != null ? proyecto.getFechaRevision() : "Pendiente"));
        }
        
        // Directores/profesores
        if (labelDirectores != null) {
            List<Profesor> profesores = proyecto.getProfesores();
            if (profesores != null && !profesores.isEmpty()) {
                StringBuilder directores = new StringBuilder("Directores: ");
                for (Profesor profesor : profesores) {
                    directores.append(profesor.getNombre()).append(" ").append(profesor.getApellido()).append(", ");
                }
                // Eliminar la última coma
                if (directores.length() > 2) {
                    directores.setLength(directores.length() - 2);
                }
                labelDirectores.setText(directores.toString());
            } else {
                labelDirectores.setText("Directores: No asignados");
            }
        }
    }
    
    private void aplicarColorEstado(String estado) {
        // Colores según el estado (ajusta según tus estados específicos)
        if (estado == null) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;");
            return;
        }
        
        String estadoLower = estado.toLowerCase();
        
        if (estadoLower.contains("aceptado") || estadoLower.contains("aprobado") || estadoLower.contains("aprobado formato a")) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #00aa00; -fx-font-weight: bold;"); // Verde
        } else if (estadoLower.contains("rechazado") || estadoLower.contains("rechazado formato a")) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #ff4444; -fx-font-weight: bold;"); // Rojo
        } else if (estadoLower.contains("primera evaluación") || estadoLower.contains("primera evaluacion")) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #ffaa00; -fx-font-weight: bold;"); // Naranja
        } else if (estadoLower.contains("segunda evaluación") || estadoLower.contains("segunda evaluacion")) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #ff8800; -fx-font-weight: bold;"); // Naranja oscuro
        } else if (estadoLower.contains("tercera evaluación") || estadoLower.contains("tercera evaluacion")) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #ff6600; -fx-font-weight: bold;"); // Naranja más oscuro
        } else if (estadoLower.contains("pendiente") || estadoLower.contains("en revisión") || estadoLower.contains("en revision")) {
            labelEstadoProyecto.setStyle("-fx-text-fill: #0088ff; -fx-font-weight: bold;"); // Azul
        } else {
            labelEstadoProyecto.setStyle("-fx-text-fill: #666666; -fx-font-weight: bold;"); // Gris
        }
    }
    
    private String obtenerDetalleEstado(String estado) {
        if (estado == null) {
            return "Estado no definido";
        }
        
        String estadoLower = estado.toLowerCase();
        
        if (estadoLower.contains("primera evaluación") || estadoLower.contains("primera evaluacion")) {
            return "Tu Formato A está en primera evaluación por el coordinador";
        } else if (estadoLower.contains("segunda evaluación") || estadoLower.contains("segunda evaluacion")) {
            return "Tu Formato A está en segunda evaluación por el comité";
        } else if (estadoLower.contains("tercera evaluación") || estadoLower.contains("tercera evaluacion")) {
            return "Tu Formato A está en evaluación final";
        } else if (estadoLower.contains("aceptado") || estadoLower.contains("aprobado")) {
            return "¡Felicidades! Tu Formato A ha sido aceptado. Puedes continuar con tu proyecto";
        } else if (estadoLower.contains("rechazado")) {
            return "Tu Formato A ha sido rechazado. Contacta al coordinador para más información";
        } else if (estadoLower.contains("pendiente") || estadoLower.contains("en revisión") || estadoLower.contains("en revision")) {
            return "Tu Formato A está pendiente de revisión";
        } else {
            return "Estado: " + estado;
        }
    }
    
    private void actualizarFecha() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        labelFechaActualizacion.setText("Última actualización: " + sdf.format(new Date()));
    }
    
    @FXML
    private void actualizarEstado() {
        System.out.println("🔄 Actualizando estado del proyecto");
        actualizarEstadoProyecto();
        labelFechaActualizacion.setText("Última actualización: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
    }
    
    @FXML
    private void cerrarSesion() throws IOException {
        System.out.println("🔒 Cerrando sesión de estudiante");
        Main.setRoot("UserLogin");
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
}