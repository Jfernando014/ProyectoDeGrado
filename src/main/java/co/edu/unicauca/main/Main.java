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
 * @author Karzo (David Santiago Arias Narvaez)
 */

public class Main extends Application {

    private static Scene scene;
    private static Parent profesorRoot;
    private static Parent loginRoot;
    private ProfesorSubirFormatoA profesorSubirFormatoCrtl;

    public static Parent getProfesorRoot() {
        return profesorRoot;
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(new javafx.scene.Group(), 1920, 1080);
        stage.setScene(scene);
        stage.show();

        InitDB.crearTablas();
        RepositoryFactory<PersonaRepository> factoryPersona = new RepositoryFactory<>(PersonaRepository.class);
        PersonaService personaService = new PersonaService(factoryPersona.getInstance("SQLite"));
        
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/ProfesorSubirFormatoA.fxml"));
        profesorRoot = loader.load();          
        profesorSubirFormatoCrtl = loader.getController();
        personaService.addObserver(profesorSubirFormatoCrtl);

        
        loader = new FXMLLoader(Main.class.getResource("/fxml/UserLogin.fxml"));
        loginRoot = loader.load();              
        UserLoginController uc = loader.getController();
        uc.setPersonaService(personaService);

        scene.setRoot(loginRoot);

    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    public static void setRoot(Parent root)
    {
        scene.setRoot(root);
    }
    public static void goProfesor()
    {
        scene.setRoot(profesorRoot);
    }
    public static void goLogin()
    {
        scene.setRoot(loginRoot);
    }
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) throws IOException {
        
        launch();
        
    }

}