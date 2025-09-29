package co.edu.unicauca.Services;

/**
 *
 * @author J. Fernando
 */

import co.edu.unicauca.DTO.FormatoADetalle;
import co.edu.unicauca.DTO.ProyectoListadoCoord;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoordinadorProyectoService {
    private static final String SQLITE_URL = "jdbc:sqlite:mi_base.db";

    /** estadoFilter: "PENDIENTE", "APROBADO", "RECHAZADO" (case-insensitive). */
    public List<ProyectoListadoCoord> listarPorEstado(String estadoFilter) throws Exception {
        // aceptamos "Pendiente de revisión" con LIKE
        String sql = """
            SELECT  p.idProyecto,
                    p.titulo,
                    p.estado,
                    COALESCE(GROUP_CONCAT(per.nombre || ' ' || per.apellido, ' - '), '') AS estudiantes,
                    p.archivoAdjunto,
                    p.fechaDeSubida
            FROM Proyecto p
            JOIN ProyectosEstudiante pe ON pe.idProyecto = p.idProyecto
            LEFT JOIN Persona per ON per.idPersona = pe.idEstudiante
            WHERE UPPER(p.estado) LIKE ?
            GROUP BY p.idProyecto, p.titulo, p.estado, p.archivoAdjunto, p.fechaDeSubida
            ORDER BY p.idProyecto DESC
        """;

        String like;
        switch (estadoFilter.toUpperCase()) {
            case "PENDIENTE" -> like = "PENDIENT%";   // cubre "Pendiente de revisión"
            case "APROBADO"  -> like = "APROBAD%";
            case "RECHAZADO" -> like = "RECHAZAD%";
            default -> like = estadoFilter.toUpperCase();
        }

        List<ProyectoListadoCoord> out = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(SQLITE_URL)) {
            try (PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, like);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        out.add(new ProyectoListadoCoord(
                                rs.getInt("idProyecto"),
                                rs.getString("titulo"),
                                rs.getString("estado"),
                                rs.getString("estudiantes"),
                                rs.getString("archivoAdjunto"),
                                rs.getString("fechaDeSubida")
                        ));
                    }
                }
            }
        }
        return out;
    }

    public void actualizarEstado(int idProyecto, String nuevoEstado) throws Exception {
        String sql = "UPDATE Proyecto SET estado = ? WHERE idProyecto = ?";
        try (Connection c = DriverManager.getConnection(SQLITE_URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idProyecto);
            ps.executeUpdate();
        }
    }
    
    public FormatoADetalle obtenerDetalle(int idProyecto) throws Exception {
        String sql = """
            SELECT p.idProyecto, p.titulo, p.objetivo, p.objetivoEspecifico,
                   p.estado, p.tipo, p.fechaDeSubida, p.archivoAdjunto,
                   (SELECT nombre || ' ' || apellido FROM Persona WHERE idPersona = pp.idDirector)   AS director,
                   (SELECT nombre || ' ' || apellido FROM Persona WHERE idPersona = pp.idCodirector) AS codirector,
                   COALESCE(GROUP_CONCAT(per.nombre || ' ' || per.apellido, ' - '), '') AS estudiantes
            FROM Proyecto p
            LEFT JOIN ProyectosProfesor pp    ON pp.idProyecto = p.idProyecto
            LEFT JOIN ProyectosEstudiante pe  ON pe.idProyecto = p.idProyecto
            LEFT JOIN Persona per             ON per.idPersona = pe.idEstudiante
            WHERE p.idProyecto = ?
            GROUP BY p.idProyecto, p.titulo, p.objetivo, p.objetivoEspecifico, p.estado, p.tipo, p.fechaDeSubida, p.archivoAdjunto, director, codirector
        """;

        try (java.sql.Connection c = java.sql.DriverManager.getConnection("jdbc:sqlite:mi_base.db");
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idProyecto);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new FormatoADetalle(
                        rs.getInt("idProyecto"),
                        rs.getString("titulo"),
                        rs.getString("objetivo"),
                        rs.getString("objetivoEspecifico"),
                        rs.getString("estado"),
                        rs.getString("tipo"),
                        rs.getString("fechaDeSubida"),
                        rs.getString("archivoAdjunto"),
                        rs.getString("director"),
                        rs.getString("codirector"),
                        rs.getString("estudiantes")
                    );
                }
            }
        }
        return null;
    }

    
}
