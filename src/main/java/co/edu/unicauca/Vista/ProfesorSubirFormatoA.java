package co.edu.unicauca.Vista;

import co.edu.unicauca.Factorys.RepositoryFactory;
import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.FormatoA;
import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Observer.Observer;
import co.edu.unicauca.Repository.ProyectoRepository;
import co.edu.unicauca.Services.PersonaService;
import co.edu.unicauca.Services.ProyectoService;
import co.edu.unicauca.Util.Tipo;
import co.edu.unicauca.Util.Validador;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import javafx.scene.control.Label;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;

/**
 * @FXML controller class
 *
 * @author 
 */
public class ProfesorSubirFormatoA implements Observer {

    @FXML

    Label lblUsuario;
    @FXML
    Button botonSubirArchivo;

    @FXML
    private Pane PanelSubirFormatoA, PaneSeleccionarModalidad;
    @FXML
    private ComboBox<Tipo> comboBoxModalidad;
    @FXML
    private Text textNombreArchivo;
    @FXML
    private ImageView imagenArchivoPlano, imagenPdf;
    @FXML
    private TextField textFieldTituloProyecto, textFieldCoodirector, textFieldObjetivoGeneral,
            textFieldEstudiante, textFieldEstudiante1;
    @FXML
    private TextArea textAreaObjetivosEspecificos;

    private Profesor profesor = null;
    private File archivo;
    private File archivoSubido;
    
    private static final String ERROR_CLASS = "error-field";
    
    private void limpiarMarcasError() {
        // agrega aquí todos los controles del formulario que quieres limpiar
        Control[] controls = new Control[] {
            textFieldTituloProyecto, textFieldCoodirector,
            textFieldEstudiante, textFieldEstudiante1,
            textFieldObjetivoGeneral, textAreaObjetivosEspecificos,
            comboBoxModalidad
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

    /**
     * Valida campos mínimos antes de construir el FormatoA.
     * Devuelve lista de errores de UI (sin tocar reglas de negocio aún).
     */
    private java.util.List<String> validarUI() {
        java.util.List<String> errores = new java.util.ArrayList<>();

        // Requeridos por tu pantalla (tienen * en el mockup)
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
            errores.add("El CODIRECTOR del proyecto es obligatorio (ID).");
            marcarError(textFieldCoodirector);
        }

        // Archivo PDF (tu Validador ya comprueba extensión/tamaño)
        if (archivo == null) {
            errores.add("Debes adjuntar un archivo PDF (máx. 20 MB).");
            marcarError(botonSubirArchivo);
        }

        // Valores numéricos
        try { if (!esVacio(textFieldEstudiante.getText())) Integer.parseInt(textFieldEstudiante.getText()); }
        catch (NumberFormatException e) { errores.add("El ID del ESTUDIANTE debe ser numérico."); marcarError(textFieldEstudiante); }

        if (!esVacio(textFieldEstudiante1.getText())) {
            try { Integer.parseInt(textFieldEstudiante1.getText()); }
            catch (NumberFormatException e) { errores.add("El ID del segundo ESTUDIANTE debe ser numérico."); marcarError(textFieldEstudiante1); }
        }

        try { if (!esVacio(textFieldCoodirector.getText())) Integer.parseInt(textFieldCoodirector.getText()); }
        catch (NumberFormatException e) { errores.add("El ID del CODIRECTOR debe ser numérico."); marcarError(textFieldCoodirector); }

        return errores;
    }

    /** Muestra un Alert con lista de errores (en viñetas) */
    private void mostrarErrores(java.util.List<String> errores, String titulo, String encabezado) {
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

    /**
     * Método para volver al menú de profesor
     */
    @FXML
    private void VolverAlMenu(MouseEvent event) throws IOException {
        System.out.println("⬅️ Volviendo al menú de Profesor...");
        Parent root = FXMLLoader.load(getClass().getResource("ProfesorMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        try {
            comboBoxModalidad.setItems(FXCollections.observableArrayList(Tipo.values()));
            comboBoxModalidad.getSelectionModel().selectFirst();
        } catch (Exception e) {
            System.out.println("Error en Profesor subir formato");
            e.printStackTrace();
        }
    }

    public void cambiarPaginaFormatoA() {
        comboBoxModalidad.setValue(comboBoxModalidad.getValue());
        PanelSubirFormatoA.setVisible(true);
        PaneSeleccionarModalidad.setVisible(false);
    }

    @FXML
    public void enviarFormato() throws Exception {
        // 1) Validación de UI (campos vacíos, tipos numéricos, archivo seleccionado)
        limpiarMarcasError();
        java.util.List<String> erroresUI = validarUI();
        if (!erroresUI.isEmpty()) {
            mostrarErrores(erroresUI, "Campos incompletos o inválidos",
                    "No se pudo subir el Formato A. Corrige lo siguiente:");
            return;
        }

        // 2) Validación de archivo por reglas existentes (extensión/tamaño)
        if (!Validador.validarArchivo(archivo)) {
            java.util.List<String> errs = java.util.List.of(
                "El archivo debe ser PDF y no superar los " + co.edu.unicauca.Util.ConstantesValidacion.MAX_ARCHIVO_MB + " MB."
            );
            mostrarErrores(errs, "Archivo no válido", "No se pudo subir el Formato A:");
            marcarError(botonSubirArchivo);
            return;
        }

        // 3) Construcción del DTO y reglas de negocio (ProyectoService ya valida títulos, objetivos, estado, tipo, alumnos, etc.)
        RepositoryFactory<ProyectoRepository> repositoryFactory = new RepositoryFactory<>(ProyectoRepository.class);
        ProyectoService proyectoService = new ProyectoService(repositoryFactory.getInstance("SQLite"));

        java.time.LocalDate hoy = java.time.LocalDate.now();
        String fecha = hoy.format(java.time.format.DateTimeFormatter.ISO_DATE);

        Estudiante estudiante1 = new Estudiante();
        estudiante1.setId(Integer.parseInt(textFieldEstudiante.getText()));

        Estudiante estudiante2 = new Estudiante();
        if (!esVacio(textFieldEstudiante1.getText())) {
            estudiante2.setId(Integer.parseInt(textFieldEstudiante1.getText()));
        }

        java.util.List<Estudiante> listaEstudiantes = new java.util.ArrayList<>();
        listaEstudiantes.add(estudiante1);
        if (!esVacio(textFieldEstudiante1.getText())) {
            listaEstudiantes.add(estudiante2);
        }

        Profesor coodirector = new Profesor();
        coodirector.setId(Integer.parseInt(textFieldCoodirector.getText()));

        java.util.List<Profesor> listaProfesores = new java.util.ArrayList<>();
        listaProfesores.add(this.profesor);     // el director logueado
        listaProfesores.add(coodirector);       // codirector

        try {
            proyectoService.subirFormato(new FormatoA(
                textFieldTituloProyecto.getText(),
                textFieldObjetivoGeneral.getText(),
                textAreaObjetivosEspecificos.getText(),
                "Pendiente de revisión",
                comboBoxModalidad.getValue(),
                fecha,
                archivo.getName(),
                listaEstudiantes,
                listaProfesores
            ));
        } catch (IllegalArgumentException e) {
            // 4) Mensajes de negocio ( vienen de ProyectoService / Validador )
            mostrarErrores(java.util.List.of(e.getMessage()),
                    "Validación de negocio", "Revisa las reglas del formato:");
            return;
        } catch (Exception e) {
            mostrarErrores(java.util.List.of("Error inesperado: " + e.getMessage()),
                    "Error", "No se pudo subir el Formato A:");
            return;
        }

        // 5) Reset UI + confirmación
        archivo = null;
        imagenArchivoPlano.setVisible(true);
        imagenPdf.setVisible(false);
        textNombreArchivo.setText("Agrega un archivo PDF de máximo 20MB");
        vaciarCampos();

        Alert ok = new Alert(Alert.AlertType.INFORMATION, "Formato A subido correctamente.");
        ok.setHeaderText("¡Listo!");
        ok.showAndWait();
    }


    public void vaciarCampos() {
        textFieldTituloProyecto.setText("");
        textFieldObjetivoGeneral.setText("");
        textAreaObjetivosEspecificos.setText("");
        textFieldCoodirector.setText("");
        textFieldEstudiante.setText("");
        textFieldEstudiante1.setText("");
    }

    public void subirDocumento() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Selecciona un archivo");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));

        File archivo = fc.showOpenDialog(botonSubirArchivo.getScene().getWindow());

        if (!Validador.validarArchivo(archivo)) {
            System.out.println("El archivo elegido no cumple con los parámetros requeridos");
        } else {
            textNombreArchivo.setText(archivo.getName());
            imagenArchivoPlano.setVisible(false);
            imagenPdf.setVisible(true);
            this.archivo = archivo;
        }
    }

    @Override
  public void update(Object o) {
    System.out.println("? ProfesorSubirFormatoA.update() llamado con: " + (o != null ? o.getClass().getSimpleName() : "null"));
    
    if (o instanceof Persona) {
        // Es una Persona (Estudiante, Profesor, Coordinador)
        Persona persona = (Persona) o;
        System.out.println("? Persona logueada: " + persona.getNombre() + " " + persona.getApellido());
        
        // Actualizar la UI según el tipo de persona
        actualizarUI(persona);
        
    } else if (o instanceof PersonaService) {
        // Es el servicio de persona (para otros casos)
        PersonaService service = (PersonaService) o;
        System.out.println("? PersonaService recibido");
        
    } else {
        System.out.println("? Tipo de objeto no reconocido: " + (o != null ? o.getClass().getName() : "null"));
    }
}

private void actualizarUI(Persona persona) {
    // Actualizar la interfaz según el tipo de persona
    if (persona instanceof Estudiante) {
        System.out.println("? Es un Estudiante");
        // Lógica para estudiante
    } else if (persona instanceof Profesor) {
        System.out.println("? Es un Profesor");
        // Lógica para profesor
    } else if (persona instanceof Coordinador) {
        System.out.println("? Es un Coordinador");
        // Lógica para coordinador
    }
    
    // Actualizar elementos de la UI
    if (lblUsuario != null) {
        lblUsuario.setText(persona.getNombre() + " " + persona.getApellido());
    }
    }   
}
