package co.edu.unicauca.Vista;


import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Factorys.RepositoryFactory;
import co.edu.unicauca.Repository.DepartamentoRepository;
import co.edu.unicauca.Repository.PersonaRepository;
import co.edu.unicauca.Repository.ProgramaRepository;
import co.edu.unicauca.Services.DepartamentoService;
import co.edu.unicauca.Services.PersonaService;
import co.edu.unicauca.Services.ProgramaService;
import co.edu.unicauca.Util.Cargo;
import co.edu.unicauca.main.Main;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */

public class UserRegisterController {
    @FXML
    TextField textFieldCorreoElectronico,textFieldNombre,textFieldApellido,textFieldCelular;
    @FXML
    PasswordField passwordFieldContrasenia;
    @FXML
    ComboBox<Cargo> comboBoxCargo;
    @FXML
    ComboBox<Programa> comboBoxPrograma;
    @FXML
    ComboBox<Departamento> comboBoxDepartamento;
    @FXML
    Text textoAviso;
    
    
    
    
    
    public void irALogin() throws IOException
    {
        
        Main.goLogin();
    
    }
    public void initialize() {
        RepositoryFactory<ProgramaRepository> repositoryProgramaFactory = new RepositoryFactory(ProgramaRepository.class);
        RepositoryFactory<DepartamentoRepository> repositoryDepartamentoFactory = new RepositoryFactory(DepartamentoRepository.class);
        
        DepartamentoService departamentoService = new DepartamentoService(repositoryDepartamentoFactory.getInstance("SQLite"));
        
        ProgramaService programaService = new ProgramaService(repositoryProgramaFactory.getInstance("SQLite"));
        
        try{
        
        comboBoxCargo.setItems(FXCollections.observableArrayList(Cargo.values()));
        comboBoxCargo.getSelectionModel().selectFirst(); // opcional
        comboBoxPrograma.setItems(FXCollections.observableArrayList(programaService.obtenerTodos()));
        
        comboBoxPrograma.setCellFactory(param -> new ListCell<Programa>() {
            @Override
            protected void updateItem(Programa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombrePrograma());
            }
        });
        
        comboBoxPrograma.setButtonCell(new ListCell<Programa>() {
            @Override
            protected void updateItem(Programa item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione programa" : item.getNombrePrograma());
            }
        });
        comboBoxDepartamento.setItems(FXCollections.observableArrayList(departamentoService.obtenerTodos()));
        comboBoxDepartamento.setCellFactory(param -> new ListCell<Departamento>() {
            @Override
            protected void updateItem(Departamento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombreDepartamento());
            }
        });
        comboBoxDepartamento.setButtonCell(new ListCell<Departamento>() {
            @Override
            protected void updateItem(Departamento item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleccione programa" : item.getNombreDepartamento());
            }
        });
        comboBoxDepartamento.getSelectionModel().selectFirst();
        comboBoxPrograma.getSelectionModel().selectFirst();
        
        if(comboBoxCargo.getValue().equals(Cargo.ESTUDIANTE))
        {
            comboBoxDepartamento.setVisible(false);
            comboBoxPrograma.setVisible(true);
        }else
        {
            comboBoxDepartamento.setVisible(true);
            comboBoxPrograma.setVisible(false);
        }
        
        
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    
    private void limpiarCampos() {
      textFieldCorreoElectronico.clear();
      textFieldNombre.clear();
      textFieldApellido.clear();
      textFieldCelular.clear();
      passwordFieldContrasenia.clear();
      textoAviso.setText(""); 

      if (comboBoxCargo.getItems() != null && !comboBoxCargo.getItems().isEmpty()) {
          comboBoxCargo.getSelectionModel().selectFirst(); 
      }

      textFieldCorreoElectronico.setStyle("-fx-alignment: CENTER;");
      textFieldNombre.setStyle("-fx-alignment: CENTER;");
      textFieldApellido.setStyle("-fx-alignment: CENTER;");
      textFieldCelular.setStyle("-fx-alignment: CENTER;");
      passwordFieldContrasenia.setStyle("-fx-alignment: CENTER;");
    }
    public void registrarse() throws UnsupportedEncodingException, IOException, Exception
    {
        RepositoryFactory<PersonaRepository> repositoryPersonaFactory = new RepositoryFactory(PersonaRepository.class);
        String resultado="";
        PersonaService personaService = new PersonaService(repositoryPersonaFactory.getInstance("SQLite"));
        
        if (validarCampos())
        {
            Persona personaARegistrar=null;
            if(comboBoxCargo.getValue()==Cargo.ESTUDIANTE)
            {
                personaARegistrar = new Estudiante(
                    new Programa(comboBoxPrograma.getValue().getCodigoDePrograma(),comboBoxPrograma.getValue().getNombrePrograma()),
                    textFieldNombre.getText(),
                    textFieldApellido.getText(),
                    textFieldCelular.getText(),
                    textFieldCorreoElectronico.getText(),
                    passwordFieldContrasenia.getText()
                );

            }
            else if(comboBoxCargo.getValue()==Cargo.PROFESOR)
            {
                personaARegistrar = new Profesor(
                    new Departamento(comboBoxDepartamento.getValue().getCodigoDepartamento(),comboBoxDepartamento.getValue().getNombreDepartamento()), 
                    textFieldNombre.getText(),
                    textFieldApellido.getText(),
                    textFieldCelular.getText(),
                    textFieldCorreoElectronico.getText(),
                    passwordFieldContrasenia.getText()
                );

            }
            else if(comboBoxCargo.getValue()==Cargo.COORDINADOR)
            {
                personaARegistrar = new Coordinador(
                    new Departamento(comboBoxDepartamento.getValue().getCodigoDepartamento(),comboBoxDepartamento.getValue().getNombreDepartamento()),
                    textFieldNombre.getText(),
                    textFieldApellido.getText(),
                    textFieldCelular.getText(),
                    textFieldCorreoElectronico.getText(),
                    passwordFieldContrasenia.getText()
                );

            }
            resultado = personaService.registrar(personaARegistrar);
            if(resultado.equals("Registro completado"))
            {
                textoAviso.setText(resultado);
                
                limpiarCampos();
                
                Main.setRoot("UserLogin");
                
            }if(resultado.equals("Formato de contrasenia invalido recuerde que debe llevar por lo menos un caracter especial una mayuscula y un digito"))
            {
                passwordFieldContrasenia.setStyle("-fx-text-fill: red;-fx-alignment: center;");
                textoAviso.setText(resultado);
            }if(resultado.equals("Correo invalido"))
            {
                textFieldCorreoElectronico.setStyle("-fx-text-fill: red;-fx-alignment: center;");
                textoAviso.setText(resultado);
            }
            
        }
        
    }
    private boolean validarCampos()
    {   
        boolean bandera = true;
        String resultado = "";
        if("".equals(textFieldNombre.getText()) )
        {
            textFieldNombre.setStyle("-fx-prompt-text-fill: red;-fx-alignment: center;");
            resultado+="FALTA EL CAMPO DE NOMBRE\n";
            bandera=false;
        }
        if("".equals(textFieldApellido.getText()) )
        {
            textFieldApellido.setStyle("-fx-prompt-text-fill: red;-fx-alignment: center;");
            resultado+="FALTA EL CAMPO DE APELLIDO\n";
            bandera=false;
        }
        if("".equals(textFieldCorreoElectronico.getText()))
        {
            textFieldCorreoElectronico.setStyle("-fx-prompt-text-fill: red;-fx-alignment: center;");
            resultado+="FALTA EL CAMPO DE CORREO ELECTRONICO\n";
            bandera=false;
        }
        if("".equals(passwordFieldContrasenia.getText()))
        {
            passwordFieldContrasenia.setStyle("-fx-prompt-text-fill: red;-fx-alignment: center;");
            resultado+="FALTA EL CAMPO DE CONTRASEÑA\n";
            bandera=false;
        }
        textoAviso.setText(resultado);
        return bandera;
    }
    public void cambiarDepartamentoPrograma()
    {
        
        if(comboBoxCargo.getValue().equals(Cargo.ESTUDIANTE))
        {
            comboBoxDepartamento.setVisible(false);
            comboBoxPrograma.setVisible(true);
            System.out.println("Cambiando combo box a programa");
        }
        else
        {
            comboBoxDepartamento.setVisible(true);
            comboBoxPrograma.setVisible(false);
            System.out.println("Cambiando combo box a departamento");
        }
    }
}
