package co.edu.unicauca.Vista;

import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Observer.Observer;
import co.edu.unicauca.Repository.Implementation.ProyectoSaver;
import co.edu.unicauca.Util.ConstantesValidacion;
import co.edu.unicauca.Util.Tipo;
import co.edu.unicauca.Util.Validador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller: Subir Formato A (Docente)
 */
public class ProfesorSubirFormatoA implements Observer {

    // --- UI ---
    @FXML private Label lblUsuario;
    @FXML private Button botonSubirArchivo;

    @FXML private Pane PanelSubirFormatoA, PaneSeleccionarModalidad;
    @FXML private ComboBox<Tipo> comboBoxModalidad;
    @FXML private Text textNombreArchivo;
    @FXML private ImageView imagenArchivoPlano, imagenPdf;

    @FXML private TextField textFieldTituloProyecto;
    @FXML private TextField textFieldDirector;       // <— AÑADIDO: ID del director
    @FXML private TextField textFieldCoodirector;
    @FXML private TextField textFieldObjetivoGeneral;
    @FXML private TextField textFieldEstudiante;
    @FXML private TextField textFieldEstudiante1;

    @FXML private TextArea textAreaObjetivosEspecificos;

    // --- Estado ---
    private Profesor profesor;        // profesor en sesión (opcional, si lo inyectas)
    private File archivo;

    // --- Estilo de error ---
    private static final String ERROR_CLASS = "error-field";

    // ================== Helpers de UI ==================
    private void limpiarMarcasError() {
        Control[] controls = new Control[] {
            textFieldTituloProyecto, textFieldDirector, textFieldCoodirector,
            textFieldEstudiante, textFieldEstudiante1,
            textFieldObjetivoGeneral, textAreaObjetivosEspecificos, comboBoxModalidad
        };
        for (Control c : controls) {
            if (c != null) c.getStyleClass().remove(ERROR_CLASS);
        }
    }

    private void marcarError(Control c) {
        if (c != null && !c.getStyleClass().contains(ERROR_CLASS)) {
            c.getStyleClass().add(ERROR_CLASS);
        }
    }

    private boolean esVacio(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** Valida campos mínimos de la pantalla (no reglas de negocio). */
    private List<String> validarUI() {
        List<String> errores = new ArrayList<>();

        if (esVacio(textFieldTituloProyecto.getText())) {
            errores.add("El TÍTULO del proyecto es obligatorio.");
            marcarError(textFieldTituloProyecto);
        }
        if (esVacio(textFieldEstudiante.getText())) {
            errores.add("El ESTUDIANTE principal es obligatorio (ID).");
            marcarError(textFieldEstudiante);
        }
        if (comboBoxModalidad.getValue() == null) {
            errores.add("La MODALIDAD es obligatoria.");
            marcarError(comboBoxModalidad);
        }
        if (esVacio(textFieldCoodirector.getText())) {
            errores.add("El CODIRECTOR es obligatorio (ID).");
            marcarError(textFieldCoodirector);
        }

        // Director: debe venir del TextField o de la sesión
        if (esVacio(textFieldDirector.getText()) &&
            (this.profesor == null || this.profesor.getId() == 0)) {
            errores.add("El DIRECTOR es obligatorio (ID o sesión de profesor).");
            marcarError(textFieldDirector);
        }

        // Archivo
        if (archivo == null) {
            errores.add("Debes adjuntar un archivo PDF (máx. " + ConstantesValidacion.MAX_ARCHIVO_MB + " MB).");
            marcarError(botonSubirArchivo);
        }

        // Numéricos
        try { if (!esVacio(textFieldDirector.getText())) Integer.parseInt(textFieldDirector.getText()); }
        catch (NumberFormatException e) { errores.add("El ID del DIRECTOR debe ser numérico."); marcarError(textFieldDirector); }

        try { if (!esVacio(textFieldCoodirector.getText())) Integer.parseInt(textFieldCoodirector.getText()); }
        catch (NumberFormatException e) { errores.add("El ID del CODIRECTOR debe ser numérico."); marcarError(textFieldCoodirector); }

        try { if (!esVacio(textFieldEstudiante.getText())) Integer.parseInt(textFieldEstudiante.getText()); }
        catch (NumberFormatException e) { errores.add("El ID del ESTUDIANTE debe ser numérico."); marcarError(textFieldEstudiante); }

        if (!esVacio(textFieldEstudiante1.getText())) {
            try { Integer.parseInt(textFieldEstudiante1.getText()); }
            catch (NumberFormatException e) { errores.add("El ID del segundo ESTUDIANTE debe ser numérico."); marcarError(textFieldEstudiante1); }
        }

        return errores;
    }

    /** Muestra un Alert con lista de errores (viñetas). */
    private void mostrarErrores(List<String> errores, String titulo, String encabezado) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        TextArea area = new TextArea("• " + String.join("\n• ", errores));
        area.setEditable(false);
        area.setWrapText(true);
        area.setPrefRowCount(Math.min(errores.size() + 1, 12));
        alert.getDialogPane().setContent(area);
        alert.showAndWait();
    }

    // ================== Navegación ==================
    @FXML
    private void VolverAlMenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/co/edu/unicauca/Vista/ProfesorMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // ================== Ciclo de vida ==================
    @FXML
    public void initialize() {
        try {
            comboBoxModalidad.setItems(FXCollections.observableArrayList(Tipo.values()));
            comboBoxModalidad.getSelectionModel().selectFirst();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cambiarPaginaFormatoA() {
        PanelSubirFormatoA.setVisible(true);
        PaneSeleccionarModalidad.setVisible(false);
    }

    // ================== Acción principal ==================
    @FXML
    public void enviarFormato() {
        // 1) UI
        limpiarMarcasError();
        List<String> erroresUI = validarUI();
        if (!erroresUI.isEmpty()) {
            mostrarErrores(erroresUI, "Campos incompletos o inválidos",
                    "No se pudo subir el Formato A. Corrige lo siguiente:");
            return;
        }

        // 2) Archivo
        if (!Validador.validarArchivo(archivo)) {
            mostrarErrores(List.of(
                "El archivo debe ser PDF y no superar los " + ConstantesValidacion.MAX_ARCHIVO_MB + " MB."
            ), "Archivo no válido", "No se pudo subir el Formato A:");
            marcarError(botonSubirArchivo);
            return;
        }

        // 3) Preparar datos (IDs)
        List<Integer> idsEstudiantes = new ArrayList<>();
        idsEstudiantes.add(Integer.parseInt(textFieldEstudiante.getText()));
        if (!esVacio(textFieldEstudiante1.getText())) {
            idsEstudiantes.add(Integer.parseInt(textFieldEstudiante1.getText()));
        }

        // Director: toma del campo o de la sesión
        Integer idDirector = null;
        if (!esVacio(textFieldDirector.getText())) {
            idDirector = Integer.parseInt(textFieldDirector.getText());
        } else if (this.profesor != null && this.profesor.getId() != 0) {
            idDirector = this.profesor.getId();
        } else {
            mostrarErrores(List.of("Debes indicar un DIRECTOR válido (ID o sesión activa)."),
                    "Sesión/Director faltante", "No se pudo subir el Formato A:");
            return;
        }

        Integer idCodirector = Integer.parseInt(textFieldCoodirector.getText());
        String fechaISO = LocalDate.now().toString();

        try {
            // 4) Guardar en BD (Proyecto + ProyectosProfesor + ProyectosEstudiante)
            int idProyecto = ProyectoSaver.saveFormatoACompletoFromUI(
                    textFieldTituloProyecto.getText(),
                    textFieldObjetivoGeneral.getText(),
                    textAreaObjetivosEspecificos.getText(),
                    "Pendiente de revisión",
                    comboBoxModalidad.getValue(),
                    fechaISO,
                    archivo.getName(),
                    idDirector,
                    idCodirector,
                    idsEstudiantes
            );

            // 5) Reset + OK
            archivo = null;
            imagenArchivoPlano.setVisible(true);
            imagenPdf.setVisible(false);
            textNombreArchivo.setText("Agrega un archivo PDF de máximo 20MB");
            vaciarCampos();

            new Alert(Alert.AlertType.INFORMATION,
                "Formato A subido correctamente. ID proyecto: " + idProyecto).showAndWait();

        } catch (IllegalArgumentException e) {
            mostrarErrores(List.of(e.getMessage()),
                    "Validación de negocio", "Revisa las reglas del formato:");
        } catch (Exception e) {
            mostrarErrores(List.of("Error inesperado: " + e.getMessage()),
                    "Error", "No se pudo subir el Formato A:");
        }
    }

    public void vaciarCampos() {
        textFieldTituloProyecto.setText("");
        textFieldDirector.setText("");
        textFieldCoodirector.setText("");
        textFieldObjetivoGeneral.setText("");
        textFieldEstudiante.setText("");
        textFieldEstudiante1.setText("");
        textAreaObjetivosEspecificos.setText("");
    }

    @FXML
    public void subirDocumento() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecciona un archivo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File elegido = fc.showOpenDialog(botonSubirArchivo.getScene().getWindow());
        if (!Validador.validarArchivo(elegido)) {
            System.out.println("El archivo elegido no cumple con los parámetros requeridos");
        } else {
            textNombreArchivo.setText(elegido.getName());
            imagenArchivoPlano.setVisible(false);
            imagenPdf.setVisible(true);
            this.archivo = elegido;
        }
    }

    // ================== Inyección / Observer ==================

    /** Inyéctalo al cargar el FXML si quieres usar la sesión del profesor. */
    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
        if (lblUsuario != null && profesor != null) {
            lblUsuario.setText(profesor.getNombre() + " " + profesor.getApellido());
        }
    }

    @Override
    public void update(Object o) {
        if (o instanceof Persona) {
            Persona persona = (Persona) o;
            if (persona instanceof Profesor) {
                this.profesor = (Profesor) persona;
            }
            if (lblUsuario != null) {
                lblUsuario.setText(persona.getNombre() + " " + persona.getApellido());
            }
        }
    }
}
