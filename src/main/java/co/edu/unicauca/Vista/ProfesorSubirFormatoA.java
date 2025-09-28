package co.edu.unicauca.Vista;

import co.edu.unicauca.Factorys.RepositoryFactory;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.FormatoA;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @FXML controller class
 *
 * @author 
 */
public class ProfesorSubirFormatoA implements Observer {

    @FXML
    private Button botonSubirArchivo;
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

    public void enviarFormato() throws Exception {
        RepositoryFactory<ProyectoRepository> repositoryFactory = new RepositoryFactory<>(ProyectoRepository.class);
        ProyectoService proyectoService = new ProyectoService(repositoryFactory.getInstance("SQLite"));
        LocalDate hoy = LocalDate.now();
        String fecha = hoy.format(DateTimeFormatter.ISO_DATE);

        Estudiante estudiante1 = new Estudiante();
        Estudiante estudiante2 = new Estudiante();
        estudiante1.setId(Integer.parseInt(textFieldEstudiante.getText()));
        estudiante2.setId(Integer.parseInt(textFieldEstudiante1.getText()));

        List<Estudiante> listaEstudiantes = new ArrayList<>();
        listaEstudiantes.add(estudiante1);
        listaEstudiantes.add(estudiante2);

        Profesor coodirector = new Profesor();
        coodirector.setId(Integer.parseInt(textFieldCoodirector.getText()));

        List<Profesor> listaProfesores = new ArrayList<>();
        listaProfesores.add(this.profesor);
        listaProfesores.add(coodirector);

        proyectoService.subirFormato(new FormatoA(
                textFieldTituloProyecto.getText(),
                textFieldObjetivoGeneral.getText(),
                textAreaObjetivosEspecificos.getText(),
                "PENDIENTE",
                comboBoxModalidad.getValue(),
                fecha,
                archivo.getName(),
                listaEstudiantes,
                listaProfesores
        ));

        archivo = null;
        imagenArchivoPlano.setVisible(true);
        imagenPdf.setVisible(false);
        textNombreArchivo.setText("Agrega un archivo PDF de máximo 20MB");
        vaciarCampos();
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
        PersonaService personaService = (PersonaService) o;
        if (personaService.getPersona() instanceof Profesor) {
            profesor = (Profesor) personaService.getPersona();
        }
    }
}
