package co.edu.unicauca.Vista;

import co.edu.unicauca.DTO.ProyectoResumen;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Services.ConsultaProyectoService;
import co.edu.unicauca.Observer.Observer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

/**
 *
 * @author J. Fernando
 */

public class EstudianteMisProyectos implements Observer {

    // === AJUSTA ESTA RUTA a donde guardas los PDFs ===
    private static final String RUTA_BASE_PDFS = "uploads"; // p.ej. "C:/proyectos/pdfs"

    @FXML private Label lblUsuario;
    @FXML private TableView<ProyectoResumen> tabla;
    @FXML private TableColumn<ProyectoResumen, String> colTitulo;
    @FXML private TableColumn<ProyectoResumen, String> colEstado;
    @FXML private TableColumn<ProyectoResumen, ProyectoResumen> colDetalles;

    private final ConsultaProyectoService service = new ConsultaProyectoService();
    private Estudiante estudiante; // estudiante logueado
    private final ObservableList<ProyectoResumen> items = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // columnas
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // placeholder cuando no hay filas
        tabla.setPlaceholder(new Label("Sin proyectos cargados"));

        // Columna "Detalles" con dos acciones: Ver info y Abrir PDF
        colDetalles.setCellFactory(col -> new TableCell<>() {
            private final Button btnInfo = new Button("🔍");
            private final Button btnPdf = new Button("📄");
            private final HBox box = new HBox(8, btnInfo, btnPdf);

            {
                btnInfo.setOnAction(e -> {
                    ProyectoResumen p = getTableView().getItems().get(getIndex());
                    mostrarDetalle(p);
                });
                btnPdf.setOnAction(e -> {
                    ProyectoResumen p = getTableView().getItems().get(getIndex());
                    abrirPDF(p);
                });
                btnInfo.getStyleClass().add("btn-detalle");
                btnPdf.getStyleClass().add("btn-detalle");
            }

            @Override
            protected void updateItem(ProyectoResumen p, boolean empty) {
                super.updateItem(p, empty);
                setGraphic(empty ? null : box);
            }
        });

        tabla.setItems(items);
    }

    /** Llamar al entrar a esta pantalla para establecer el estudiante logueado */
    public void setEstudiante(Estudiante est) {
        this.estudiante = est;
        if (lblUsuario != null && est != null) {
            lblUsuario.setText(est.getNombre() + " " + est.getApellido());
        }
        cargarProyectos();
    }

    private void cargarProyectos() {
        items.clear();
        if (estudiante == null || estudiante.getId() == 0) return;

        try {
            List<ProyectoResumen> lista = service.listarProyectosDeEstudiante(estudiante.getId());
            items.addAll(lista);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "No se pudieron cargar tus proyectos: " + ex.getMessage()).showAndWait();
        }
    }

    private void mostrarDetalle(ProyectoResumen p) {
        String msg =
            "ID Proyecto: " + p.getIdProyecto() + "\n" +
            "Título: " + p.getTitulo() + "\n" +
            "Estado: " + p.getEstado() + "\n" +
            "Fecha de subida: " + p.getFechaDeSubida() + "\n" +
            "Archivo: " + p.getArchivoAdjunto();
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText("Detalle del proyecto");
        a.showAndWait();
    }

    private void abrirPDF(ProyectoResumen p) {
        try {
            File f = new File(RUTA_BASE_PDFS, p.getArchivoAdjunto());
            if (!f.exists()) {
                new Alert(Alert.AlertType.WARNING,
                        "No se encontró el archivo:\n" + f.getAbsolutePath() +
                        "\n\nAjusta RUTA_BASE_PDFS en el controller.").showAndWait();
                return;
            }
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(f);
            } else {
                new Alert(Alert.AlertType.WARNING, "No es posible abrir el archivo en este sistema.").showAndWait();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "No se pudo abrir el PDF: " + ex.getMessage()).showAndWait();
        }
    }

    // Si tu flujo usa Observer para recibir el estudiante:
    @Override
    public void update(Object o) {
        if (o instanceof Estudiante) {
            setEstudiante((Estudiante) o);
        }
    }
}
