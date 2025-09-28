package co.edu.unicauca.Vista;

import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Models.Estudiante; 
import co.edu.unicauca.Services.PersonaService;
import co.edu.unicauca.main.Main;
import co.edu.unicauca.Factorys.RepositoryFactory;
import co.edu.unicauca.Repository.PersonaRepository;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class UserLoginController {
    
    PersonaService personaService;
    Persona persona;
    
    @FXML
    TextField textFieldCorreoElectronico;
    
    @FXML
    PasswordField passwordFieldContrasenia;
    
    @FXML
    Text textCorreoOContraseniaIncorrecto;
    
    public void initialize() {      
        System.out.println("=== UserLoginController.initialize() ===");
        
        // ✅ INICIALIZAR personaService SI ES NULL
        if (personaService == null) {
            System.out.println("personaService es null - inicializando...");
            try {
                RepositoryFactory<PersonaRepository> factory = new RepositoryFactory<>(PersonaRepository.class);
                personaService = new PersonaService(factory.getInstance("SQLite"));
                System.out.println("✅ PersonaService inicializado en initialize()");
            } catch (Exception e) {
                System.err.println("❌ Error inicializando PersonaService: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("✅ personaService ya estaba inicializado");
        }
        
        // Limpiar mensajes de error
        if (textCorreoOContraseniaIncorrecto != null) {
            textCorreoOContraseniaIncorrecto.setText("");
        }
        
        // Limpiar campos
        if (textFieldCorreoElectronico != null) {
            textFieldCorreoElectronico.setText("");
        }
        if (passwordFieldContrasenia != null) {
            passwordFieldContrasenia.setText("");
        }
        
        System.out.println("=== Fin initialize() ===");
    }
    
    public void irARegistrarse() throws IOException {
        System.out.println("Navegando a registro...");
        Main.setRoot("UserRegister");
    }   
    
    @FXML
    public void iniciarSesion() throws UnsupportedEncodingException, IOException, Exception {
        System.out.println("=== INICIANDO SESIÓN ===");
        System.out.println("Correo: " + textFieldCorreoElectronico.getText());
        System.out.println("Password length: " + passwordFieldContrasenia.getText().length());
        
        // ✅ VERIFICAR QUE personaService NO SEA NULL
        if (personaService == null) {
            System.err.println("❌ ERROR: personaService es null en iniciarSesion()");
            textCorreoOContraseniaIncorrecto.setText("Error interno del sistema");
            return;
        }
        System.out.println("✅ personaService no es null");
        
        // ✅ VERIFICAR CAMPOS VACÍOS
        if (textFieldCorreoElectronico.getText().isEmpty() || passwordFieldContrasenia.getText().isEmpty()) {
            System.err.println("❌ Campos vacíos");
            textCorreoOContraseniaIncorrecto.setText("Complete todos los campos");
            return;
        }
        System.out.println("✅ Campos completos");
        
        try {
            System.out.println("🔍 Llamando a personaService.iniciarSesion()...");
            persona = personaService.iniciarSesion(textFieldCorreoElectronico.getText(), passwordFieldContrasenia.getText());
            System.out.println("✅ personaService.iniciarSesion() completado");
            
            if (persona == null) {
                System.err.println("❌ Login fallido - persona es null");
                textCorreoOContraseniaIncorrecto.setText("CORREO O CONTRASEÑA INCORRECTOS");
            } else {
                System.out.println("✅ Login exitoso!");
                System.out.println("Tipo de persona: " + persona.getClass().getSimpleName());
                System.out.println("Nombre: " + persona.getNombre());
                
                if (persona instanceof Profesor) {
                    System.out.println("🔵 Navegando a Mnu Profesor...");
                    Main.setRoot("ProfesorMenu");
                } else if (persona instanceof Coordinador) {
                    System.out.println("🔵 Navegando a vista Coordinador...");
                    Main.setRoot("FormatosCoordinador");
                } else if (persona instanceof Estudiante) {
                    System.out.println("🔵 Navegando a vista Estudiante...");
                    Main.setRoot("UserStudent");
                } else {
                    System.out.println("⚠️ Tipo de usuario no manejado: " + persona.getClass().getSimpleName());
                    textCorreoOContraseniaIncorrecto.setText("Tipo de usuario no soportado");
                }
            }
        } catch (Exception e) {
            System.err.println("❌ EXCEPCIÓN en iniciarSesion(): " + e.getMessage());
            e.printStackTrace();
            textCorreoOContraseniaIncorrecto.setText("Error durante el inicio de sesión");
        }
        
        System.out.println("=== FIN INICIAR SESIÓN ===");
    }

    public void setPersonaService(PersonaService personaService) {
        System.out.println("=== setPersonaService() llamado ===");
        this.personaService = personaService;
        System.out.println("✅ PersonaService seteado via setter");
        if (personaService == null) {
            System.err.println("❌ WARNING: Se está seteando un personaService null!");
        }
    }
}