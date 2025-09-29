package co.edu.unicauca.Repository.Implementation;

import co.edu.unicauca.Util.Tipo;

import java.sql.*;
import java.util.List;

public final class ProyectoSaver {

    private ProyectoSaver() {
    }

    // TODO: ajusta esta ruta a tu archivo .db real
    private static final String SQLITE_URL = "jdbc:sqlite:mi_base.db";

    /**
     * Inserta en Proyecto y enlaza profesores/estudiantes en una transacción.
     * Esquema usado: - Proyecto(titulo, objetivo, objetivoEspecifico, estado,
     * tipo, fechaDeSubida, archivoAdjunto) - ProyectosProfesor(idDirector,
     * idCodirector, idProyecto) - ProyectosEstudiante(idProyecto, idEstudiante)
     */
    public static int saveFormatoACompletoFromUI(
            String titulo,
            String objetivoGeneral, // -> objetivo
            String objetivosEspecificos, // -> objetivoEspecifico
            String estado,
            Tipo tipo,
            String fechaISO, // -> fechaDeSubida
            String nombreArchivoPdf, // -> archivoAdjunto
            Integer idDirector, // requerido
            Integer idCodirector, // opcional según tu BD
            List<Integer> idsEstudiantes
    ) throws Exception {

        final String sqlProyecto
                = "INSERT INTO Proyecto(titulo, objetivo, objetivoEspecifico, estado, tipo, fechaDeSubida, archivoAdjunto) "
                + "VALUES(?,?,?,?,?,?,?)";

        final String sqlProf
                = "INSERT INTO ProyectosProfesor(idDirector, idCodirector, idProyecto) VALUES(?,?,?)";

        final String sqlEst
                = "INSERT INTO ProyectosEstudiante(idProyecto, idEstudiante) VALUES(?,?)";

        Connection c = null;
        try {
            c = DriverManager.getConnection(SQLITE_URL);
            try (Statement st = c.createStatement()) {
                st.execute("PRAGMA foreign_keys = ON");
            }
            ensureJoinTables(c);

            c.setAutoCommit(false);

            // --- Validaciones FK dinámicas (según apunten tus FKs) ---
            if (idDirector == null || idDirector == 0) {
                throw new IllegalArgumentException("Debes indicar un DIRECTOR válido.");
            }

            validateFKValueExists(c, "ProyectosProfesor", "idDirector", idDirector, "DIRECTOR");

            if (idCodirector != null && idCodirector != 0) {
                validateFKValueExists(c, "ProyectosProfesor", "idCodirector", idCodirector, "CODIRECTOR");
            } else {
                idCodirector = null; // guardaremos NULL si la columna lo permite
            }

            if (idsEstudiantes != null) {
                for (Integer idEst : idsEstudiantes) {
                    if (idEst == null || idEst == 0) {
                        throw new IllegalArgumentException("ID de ESTUDIANTE inválido: " + idEst);
                    }
                    validateFKValueExists(c, "ProyectosEstudiante", "idEstudiante", idEst, "ESTUDIANTE");
                }
            }

            // --- 1) Insert principal ---
            int idProyecto;
            try (PreparedStatement ps = c.prepareStatement(sqlProyecto, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, titulo);
                ps.setString(2, objetivoGeneral);
                ps.setString(3, objetivosEspecificos);
                ps.setString(4, estado);
                ps.setString(5, tipo.name());
                ps.setString(6, fechaISO);
                ps.setString(7, nombreArchivoPdf);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs != null && rs.next()) {
                        idProyecto = rs.getInt(1);
                    } else {
                        try (Statement s2 = c.createStatement(); ResultSet rs2 = s2.executeQuery("SELECT last_insert_rowid()")) {
                            rs2.next();
                            idProyecto = rs2.getInt(1);
                        }
                    }
                }
            }

            // --- 2) Enlace profesores (UNA fila por proyecto) ---
            try (PreparedStatement ps = c.prepareStatement(sqlProf)) {
                ps.setInt(1, idDirector);
                if (idCodirector == null) {
                    ps.setNull(2, Types.INTEGER);
                } else {
                    ps.setInt(2, idCodirector);
                }
                ps.setInt(3, idProyecto);
                ps.executeUpdate();
            }

            // --- 3) Enlace estudiantes (0..n) ---
            if (idsEstudiantes != null && !idsEstudiantes.isEmpty()) {
                try (PreparedStatement ps = c.prepareStatement(sqlEst)) {
                    for (Integer idEst : idsEstudiantes) {
                        if (idEst == null || idEst == 0) {
                            continue;
                        }
                        ps.setInt(1, idProyecto);
                        ps.setInt(2, idEst);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
            }

            c.commit();
            return idProyecto;

        } catch (Exception ex) {
            if (c != null) try {
                c.rollback();
            } catch (Exception ignore) {
            }
            throw ex;
        } finally {
            if (c != null) {
                try {
                    c.setAutoCommit(true);
                } catch (Exception ignore) {
                }
                try {
                    c.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    // ===== Helpers =====
    private static void ensureJoinTables(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS ProyectosProfesor ("
                    + "  idDirector   INTEGER NOT NULL,"
                    + "  idCodirector INTEGER,"
                    + "  idProyecto   INTEGER NOT NULL"
                    + ")"
            );
            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS ProyectosEstudiante ("
                    + "  idProyecto   INTEGER NOT NULL,"
                    + "  idEstudiante INTEGER NOT NULL"
                    + ")"
            );
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_pp_proyecto ON ProyectosProfesor(idProyecto)");
            st.executeUpdate("CREATE INDEX IF NOT EXISTS idx_pe_proyecto ON ProyectosEstudiante(idProyecto)");
        }
    }

    /**
     * Valida contra la tabla/columna real donde apunta la FK (descubierta con
     * PRAGMA).
     */
    private static void validateFKValueExists(Connection c, String childTable, String childColumn,
            int value, String nombreHumano) throws SQLException {
        FKTarget target = fkTargetFor(c, childTable, childColumn);
        if (target == null) {
            return; // sin FK explícita, no validamos
        }
        if (!existsById(c, target.table, target.column, value)) {
            throw new IllegalArgumentException(nombreHumano + " (id=" + value + ") no existe en "
                    + target.table + "." + target.column);
        }
    }

    private static FKTarget fkTargetFor(Connection c, String childTable, String childColumn) throws SQLException {
        String sql = "PRAGMA foreign_key_list(" + childTable + ")";
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String from = rs.getString("from"); // columna hija
                String refTable = rs.getString("table");
                String to = rs.getString("to");     // columna padre
                if (childColumn.equalsIgnoreCase(from)) {
                    return new FKTarget(refTable, to);
                }
            }
        }
        return null;
    }

    private static boolean existsById(Connection c, String table, String idColumn, int id) throws SQLException {
        String sql = "SELECT 1 FROM " + table + " WHERE " + idColumn + " = ? LIMIT 1";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static final class FKTarget {

        final String table;
        final String column;

        FKTarget(String t, String c) {
            this.table = t;
            this.column = c;
        }
    }
}
