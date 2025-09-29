package co.edu.unicauca.Vista;

import co.edu.unicauca.DTO.FormatoADetalle;
import co.edu.unicauca.Services.CoordinadorProyectoService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;

public class CoordinadorFormatoAController {

    // AJUSTA si guardas PDFs en otra carpeta
    private static final String RUTA_BASE_PDFS = "uploads";

    @FXML private TextField txtTitulo;
    @FXML private TextField txtDirector;
    @FXML private TextField txtCodirector;
    @FXML private TextField txtObjetivo;
    @FXML private TextArea  txtObjetivoEspecifico;

    @FXML private TextArea  txtEstudiantes;
    @FXML private TextField txtFecha;
    @FXML private TextField txtModalidad;
    @FXML private TextField txtArchivo;

    private final CoordinadorProyectoService service = new CoordinadorProyectoService();
    private int idProyecto;
    private String filtroAnterior = "PENDIENTE";

    /** Llamado desde la lista */
    public void init(int idProyecto, String filtroAnterior) {
        this.idProyecto = idProyecto;
        if (filtroAnterior != null) this.filtroAnterior = filtroAnterior;
        cargar();
    }

    private void cargar() {
        try {
            FormatoADetalle f = service.obtenerDetalle(idProyecto);
            if (f == null) {
                new Alert(Alert.AlertType.WARNING, "No se encontró el proyecto " + idProyecto).showAndWait();
                return;
            }
            txtTitulo.setText(f.getTitulo());
            txtDirector.setText(nvl(f.getDirector()));
            txtCodirector.setText(nvl(f.getCodirector()));
            txtObjetivo.setText(nvl(f.getObjetivo()));
            txtObjetivoEspecifico.setText(nvl(f.getObjetivoEspecifico()));
            txtEstudiantes.setText(nvl(f.getEstudiantes()));
            txtFecha.setText(nvl(f.getFechaDeSubida()));
            txtModalidad.setText(nvl(f.getTipo()));
            txtArchivo.setText(nvl(f.getArchivoAdjunto()));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error cargando detalle: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void abrirPDF() {
        try {
            File f = new File(RUTA_BASE_PDFS, txtArchivo.getText());
            if (!f.exists()) {
                new Alert(Alert.AlertType.WARNING, "No se encontró el archivo:\n" + f.getAbsolutePath()).showAndWait();
                return;
            }
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().open(f);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el PDF: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void aprobar() {
        actualizarEstado("Aprobado");
    }

    @FXML
    private void rechazar() {
        actualizarEstado("Rechazado");
    }

    private void actualizarEstado(String nuevo) {
        try {
            service.actualizarEstado(idProyecto, nuevo);
            new Alert(Alert.AlertType.INFORMATION, "Proyecto " + nuevo + ".").showAndWait();
            volver();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo actualizar: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void volver() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CoordinadorListado.fxml"));
            Parent root = loader.load();
            CoordinadorListadoController ctrl = loader.getController();
            ctrl.setFiltro(filtroAnterior); // recarga la lista con el filtro anterior
            Stage stage = (Stage) txtTitulo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "No se pudo volver: " + e.getMessage()).showAndWait();
        }
    }

    private String nvl(String s) { return s == null ? "" : s; }
}
