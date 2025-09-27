package co.edu.unicauca.Vista;


import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Services.PersonaService;
import co.edu.unicauca.main.Main;
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
 * @author Karzo
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
    @FXML
            

    
    public void initialize()
    {      
        
    }
    public void irARegistrarse() throws IOException {
        
        Main.setRoot("UserRegister");
    }   
    
    @FXML
    public void iniciarSesion() throws UnsupportedEncodingException, IOException, Exception
    {
        
        persona = personaService.iniciarSesion(textFieldCorreoElectronico.getText(), passwordFieldContrasenia.getText());
        
        if(persona == null)
        {
            textCorreoOContraseniaIncorrecto.setText("CORREO O CONTRASEÑA INCORRECTOS");
        }else
        {
            
            if(persona instanceof Profesor)
            {
                Main.goProfesor();
            }
            if(persona instanceof Coordinador)
            {
               Main.setRoot("FormatosCoordinador");
            }
            
        }
        
        
    }

    public void setPersonaService(PersonaService personaService) {
        this.personaService = personaService;
    }
    
}
