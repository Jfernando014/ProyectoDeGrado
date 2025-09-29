
package co.edu.unicauca.Services;

import co.edu.unicauca.DTO.ProyectoResumen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author J. Fernando
 */

public class ConsultaProyectoService {
    // ajusta la ruta si tu BD es otra
    private static final String SQLITE_URL = "jdbc:sqlite:mi_base.db";

    public List<ProyectoResumen> listarProyectosDeEstudiante(int idEstudiante) throws Exception {
        String sql = """
            SELECT p.idProyecto, p.titulo, p.estado, p.archivoAdjunto, p.fechaDeSubida
            FROM Proyecto p
            INNER JOIN ProyectosEstudiante pe ON pe.idProyecto = p.idProyecto
            WHERE pe.idEstudiante = ?
            ORDER BY p.idProyecto DESC
        """;

        List<ProyectoResumen> out = new ArrayList<>();

        try (Connection c = DriverManager.getConnection(SQLITE_URL)) {
            try (Statement st = c.createStatement()) { st.execute("PRAGMA foreign_keys = ON"); }
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, idEstudiante);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new ProyectoResumen(
                                rs.getInt("idProyecto"),
                                rs.getString("titulo"),
                                rs.getString("estado"),
                                rs.getString("archivoAdjunto"),
                                rs.getString("fechaDeSubida")
                        ));
                    }
                }
            }
        }
        return out;
    }
}
