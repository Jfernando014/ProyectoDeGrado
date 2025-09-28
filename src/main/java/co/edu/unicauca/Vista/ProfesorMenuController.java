package co.edu.unicauca.Vista;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import co.edu.unicauca.main.Main;

public class ProfesorMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si la necesitas
    }

    @FXML
    private void irASubirFormatoA(MouseEvent event) throws IOException {
        System.out.println("➡️ Navegando a Subir Formato A...");
        Main.setRoot("ProfesorSubirFormatoA"); // carga ProfesorSubirFormatoA.fxml
    }
}
