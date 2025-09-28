
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


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;


/**
 *@FXML controller class
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class ProfesorSubirFormatoA implements Observer{
    @FXML
    Label lblUsuario;
    @FXML
    Button botonSubirArchivo;
    @FXML
    Pane PanelSubirFormatoA,PaneSeleccionarModalidad;
    @FXML
    ComboBox<Tipo> comboBoxModalidad;
    @FXML
    Text textNombreArchivo;
    @FXML
    ImageView imagenArchivoPlano,imagenPdf;
    @FXML
    TextField textFieldTituloProyecto,textFieldCoodirector,textFieldObjetivoGeneral,textFieldEstudiante,textFieldEstudiante1;
    @FXML
    TextArea textAreaObjetivosEspecificos;
    Profesor profesor=null;
    File archivo;
    
    
    private File archivoSubido;
    
    public void initialize()
    {
        try{
            
            comboBoxModalidad.setItems(FXCollections.observableArrayList(Tipo.values()));
            comboBoxModalidad.getSelectionModel().selectFirst();
            
        }catch(Exception e)
        {
            System.out.println("Error en Profesor subir formato");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    public void cambiarPaginaFormatoA()
    {
        comboBoxModalidad.setValue(comboBoxModalidad.getValue());
        PanelSubirFormatoA.setVisible(true);
        PaneSeleccionarModalidad.setVisible(false);
    }
    
    public void enviarFormato() throws Exception
    {
        RepositoryFactory<ProyectoRepository> repositoryFactory = new RepositoryFactory(ProyectoRepository.class);
        ProyectoService proyectoService = new ProyectoService(repositoryFactory.getInstance("SQLite"));
        LocalDate hoy = LocalDate.now();
        String fecha = hoy.format(DateTimeFormatter.ISO_DATE);
        Estudiante estudiante1 = new Estudiante();
        Estudiante estudiante2 = new Estudiante();
        estudiante1.setId(Integer.parseInt( textFieldEstudiante.getText()));
        estudiante2.setId(Integer.parseInt(textFieldEstudiante1.getText()));
        List<Estudiante> listaEstudiantes = new ArrayList();
        listaEstudiantes.add(estudiante1);
        listaEstudiantes.add(estudiante2);
        
        Profesor coodirector = new Profesor();
        coodirector.setId(Integer.parseInt(textFieldCoodirector.getText()));
        List<Profesor> listaProfesores = new ArrayList();
        listaProfesores.add(this.profesor);
        listaProfesores.add(coodirector);
        
        proyectoService.subirFormato(new FormatoA(textFieldTituloProyecto.getText(),textFieldObjetivoGeneral.getText(),textAreaObjetivosEspecificos.getText(),"PENDIENTE",comboBoxModalidad.getValue(),fecha,archivo.getName(),listaEstudiantes,listaProfesores));
        
        
        archivo = null;
        imagenArchivoPlano.setVisible(true);
        imagenPdf.setVisible(false);
        textNombreArchivo.setText("Agrega un archivo PDF de maximo 20MB");
        vaciarCampos();
    }
    public void vaciarCampos()
    {
        textFieldTituloProyecto.setText("");
        textFieldObjetivoGeneral.setText("");
        textAreaObjetivosEspecificos.setText("");
        textFieldCoodirector.setText("");
        textFieldEstudiante.setText("");
        textFieldEstudiante1.setText("");
    }
    public void subirDocumento()
    {
        FileChooser fc = new FileChooser();
        
        fc.setTitle("Selecciona un archivo");
        
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        
        File archivo = fc.showOpenDialog(botonSubirArchivo.getScene().getWindow());
        
        if (!Validador.validarArchivo(archivo)){
            
            System.out.println("El archivo elegido no cumple con los parametros requeridos");
        }else
        {
            textNombreArchivo.setText(archivo.getName());
            imagenArchivoPlano.setVisible(false);
            imagenPdf.setVisible(true);
            
            this.archivo=archivo;
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
