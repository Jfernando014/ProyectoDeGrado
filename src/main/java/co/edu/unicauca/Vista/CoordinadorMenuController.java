package co.edu.unicauca.Vista;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author J. Fernando
 */

public class CoordinadorMenuController {

    @FXML private void irPendientesClick(MouseEvent e) throws Exception {
        cargarLista("PENDIENTE", (Node) e.getSource());
    }

    @FXML private void irAprobadosClick(MouseEvent e) throws Exception {
        cargarLista("APROBADO", (Node) e.getSource());
    }

    @FXML private void irRechazadosClick(MouseEvent e) throws Exception {
        cargarLista("RECHAZADO", (Node) e.getSource());
    }

    private void cargarLista(String filtro, Node source) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CoordinadorListado.fxml"));
        Parent root = loader.load();

        CoordinadorListadoController ctrl = loader.getController();
        ctrl.setFiltro(filtro); // pasa "PENDIENTE" / "APROBADO" / "RECHAZADO"

        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
