package co.edu.unicauca.Vista;

import co.edu.unicauca.DTO.ProyectoListadoCoord;
import co.edu.unicauca.Services.CoordinadorProyectoService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;   // ← usamos Text

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class CoordinadorListadoController {

    private static final String RUTA_BASE_PDFS = "uploads";

    @FXML private TableView<ProyectoListadoCoord> tabla;
    @FXML private TableColumn<ProyectoListadoCoord, String> colEstudiante;
    @FXML private TableColumn<ProyectoListadoCoord, String> colTitulo;
    @FXML private TableColumn<ProyectoListadoCoord, ProyectoListadoCoord> colDetalles;

    @FXML private Text txtTitulo;    // ← corregido
    @FXML private Text txtSeccion;   // ← corregido

    private final CoordinadorProyectoService service = new CoordinadorProyectoService();
    private final ObservableList<ProyectoListadoCoord> items = FXCollections.observableArrayList();
    private String filtroEstado = "PENDIENTE";

    @FXML
    public void initialize() {
        colEstudiante.setCellValueFactory(new PropertyValueFactory<>("estudiantes"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tabla.setPlaceholder(new Label("Sin proyectos " + filtroEstado.toLowerCase()));

        colDetalles.setCellFactory(col -> new TableCell<>() {
            private final Button btnInfo = new Button("🔍");
            private final Button btnPdf  = new Button("📄");
            private final Button btnOk   = new Button("✅");
            private final Button btnNo   = new Button("❌");
            private final HBox boxPend   = new HBox(8, btnInfo, btnPdf, btnOk, btnNo);
            private final HBox boxOtros  = new HBox(8, btnInfo, btnPdf);

            {
                btnInfo.setOnAction(e -> {
                    ProyectoListadoCoord p = getTableView().getItems().get(getIndex());
                    mostrarDetalle(p);
                });
                btnPdf.setOnAction(e -> {
                    ProyectoListadoCoord p = getTableView().getItems().get(getIndex());
                    abrirPDF(p);
                });
                btnOk.setOnAction(e -> cambiarEstado("Aprobado"));
                btnNo.setOnAction(e -> cambiarEstado("Rechazado"));
            }

            private void cambiarEstado(String nuevo) {
                ProyectoListadoCoord p = getTableView().getItems().get(getIndex());
                try {
                    service.actualizarEstado(p.getIdProyecto(), nuevo);
                    cargar(filtroEstado);
                } catch (Exception ex) {
                    new Alert(Alert.AlertType.ERROR, "No se pudo actualizar el estado: " + ex.getMessage()).showAndWait();
                }
            }

            @Override
            protected void updateItem(ProyectoListadoCoord p, boolean empty) {
                super.updateItem(p, empty);
                setGraphic(empty || p == null
                        ? null
                        : (filtroEstado.equalsIgnoreCase("PENDIENTE") ? boxPend : boxOtros));
            }
        });

        tabla.setItems(items);
    }

    public void setFiltro(String estado) {
        this.filtroEstado = estado;
        txtSeccion.setText(nombreBonito(estado));
        tabla.setPlaceholder(new Label("Sin proyectos " + estado.toLowerCase()));
        cargar(estado);
    }

    private void cargar(String estado) {
        items.clear();
        try {
            List<ProyectoListadoCoord> lista = service.listarPorEstado(estado);
            items.addAll(lista);
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "No se pudo cargar: " + ex.getMessage()).showAndWait();
        }
    }

    private void mostrarDetalle(ProyectoListadoCoord p) {
        String msg = "ID: " + p.getIdProyecto() + "\n"
                + "Estudiantes: " + p.getEstudiantes() + "\n"
                + "Título: " + p.getTitulo() + "\n"
                + "Estado: " + p.getEstado() + "\n"
                + "Fecha: " + p.getFechaDeSubida() + "\n"
                + "Archivo: " + p.getArchivoAdjunto();
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText("Formato A");
        a.showAndWait();
    }

    private void abrirPDF(ProyectoListadoCoord p) {
        try {
            File f = new File(RUTA_BASE_PDFS, p.getArchivoAdjunto());
            if (!f.exists()) {
                new Alert(Alert.AlertType.WARNING,
                        "No se encontró el archivo:\n" + f.getAbsolutePath()
                                + "\n\nAjusta RUTA_BASE_PDFS en el controller.").showAndWait();
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

    private String nombreBonito(String e) {
        e = e.toUpperCase();
        if (e.startsWith("PENDIENT")) return "Proyectos de grado pendientes";
        if (e.startsWith("APROBAD"))  return "Aprobados";
        if (e.startsWith("RECHAZAD")) return "Rechazados";
        return e;
    }
}
