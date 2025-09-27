package co.edu.unicauca.main;

import co.edu.unicauca.Factorys.RepositoryFactory;
import co.edu.unicauca.Repository.PersonaRepository;
import co.edu.unicauca.Services.PersonaService;
import co.edu.unicauca.Vista.ProfesorSubirFormatoA;
import co.edu.unicauca.Vista.UserLoginController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import co.edu.unicauca.database.InitDB;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo (David Santiago Arias Narvaez)
 */

public class Main extends Application {

    private static Scene scene;
    private static Parent profesorRoot;
    private static Parent loginRoot;
    private static PersonaService personaService;

    public static Parent getProfesorRoot() {
        return profesorRoot;
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("🚀 Iniciando aplicación...");
        
        scene = new Scene(new javafx.scene.Group(), 1920, 1080);
        stage.setScene(scene);
        stage.show();

        InitDB.crearTablas();
        RepositoryFactory<PersonaRepository> factoryPersona = new RepositoryFactory<>(PersonaRepository.class);
        personaService = new PersonaService(factoryPersona.getInstance("SQLite"));
        System.out.println("✅ PersonaService creado");
        
        // Cargar vista de profesor
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/ProfesorSubirFormatoA.fxml"));
        profesorRoot = loader.load();          
        ProfesorSubirFormatoA profesorSubirFormatoCrtl = loader.getController();
        personaService.addObserver(profesorSubirFormatoCrtl);
        System.out.println("✅ Vista de profesor cargada");

        // Cargar login y conservar referencia
        loader = new FXMLLoader(Main.class.getResource("/fxml/UserLogin.fxml"));
        loginRoot = loader.load();              
        UserLoginController loginController = loader.getController();
        loginController.setPersonaService(personaService);
        System.out.println("✅ Vista de login cargada y service inyectado");

        scene.setRoot(loginRoot);
        System.out.println("✅ Escena configurada con login");
    }

    public static void setRoot(String fxml) throws IOException {
        System.out.println("🔄 setRoot('" + fxml + "')");
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/"+fxml + ".fxml"));
        Parent root = loader.load();
        
        // ✅ INYECTAR PERSONA SERVICE SI ES UN UserLoginController
        if (fxml.equals("UserLogin")) {
            UserLoginController controller = loader.getController();
            controller.setPersonaService(personaService);
            System.out.println("✅ Service inyectado en UserLoginController");
        }
        
        scene.setRoot(root);
        System.out.println("✅ Navegación completada a: " + fxml);
    }
    
    public static void setRoot(Parent root) {
        scene.setRoot(root);
    }
    
    public static void goProfesor() {
        System.out.println("🎓 goProfesor() llamado");
        if (profesorRoot != null) {
            scene.setRoot(profesorRoot);
            System.out.println("✅ Navegado a profesor usando root existente");
        } else {
            try {
                setRoot("ProfesorSubirFormatoA");
                System.out.println("✅ Navegado a profesor cargando fresh");
            } catch (IOException e) {
                System.err.println("❌ Error navegando a profesor: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    public static void goLogin() {
        System.out.println("🔐 goLogin() llamado");
        if (loginRoot != null) {
            try {
                // La forma más segura es cargar fresh pero conservando la lógica
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/UserLogin.fxml"));
                Parent newLoginRoot = loader.load();
                UserLoginController controller = loader.getController();
                controller.setPersonaService(personaService);
                
                // Actualizar la referencia
                loginRoot = newLoginRoot;
                scene.setRoot(loginRoot);
                System.out.println("✅ Login cargado fresh con service inyectado");
            } catch (IOException e) {
                System.err.println("❌ Error en goLogin(): " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                setRoot("UserLogin");
                System.out.println("✅ Login cargado via setRoot (loginRoot era null)");
            } catch (IOException e) {
                System.err.println("❌ Error cargando login: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("=== INICIANDO APLICACIÓN ===");
        launch();
    }
}