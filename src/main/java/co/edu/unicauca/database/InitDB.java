package co.edu.unicauca.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */

public class InitDB {
    public static void crearTablas() {
    
        try {
            Connection conn = ConexionSQLite.getInstance();
            
            // Recomendado para SQLite: WAL y timeout para locks
            try (Statement s = conn.createStatement()) {
                s.execute("PRAGMA journal_mode = WAL;");
                s.execute("PRAGMA synchronous = NORMAL;");
                s.execute("PRAGMA foreign_keys = ON;");
                s.execute("PRAGMA busy_timeout = 5000;");
            }
            
            conn.setAutoCommit(false);
            
            try (Statement st = conn.createStatement()) {
                // ============ Núcleo académico ============
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Facultad (
                      idFacultad INTEGER PRIMARY KEY AUTOINCREMENT,
                      nombre     TEXT NOT NULL
                    );
                """);
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Departamento (
                      idDepartamento INTEGER PRIMARY KEY AUTOINCREMENT,
                      nombre         TEXT NOT NULL,
                      idFacultad     INTEGER NOT NULL,
                      FOREIGN KEY (idFacultad)
                        REFERENCES Facultad(idFacultad)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT
                    );
                """);
                //FALTA AGREGAR ID COORDINADOR PARA QUE NO SE REPITAN COORDINADORES EN DEPARTAMENTO
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Programa (
                      idPrograma     INTEGER PRIMARY KEY AUTOINCREMENT,
                      nombre         TEXT NOT NULL,
                      idDepartamento INTEGER NOT NULL,
                      FOREIGN KEY (idDepartamento)
                        REFERENCES Departamento(idDepartamento)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT
                    );
                """);

                // ============ Personas y roles ============
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Persona (
                      idPersona         INTEGER PRIMARY KEY AUTOINCREMENT,
                      nombre            TEXT NOT NULL,
                      apellido          TEXT NOT NULL,
                      correoElectronico TEXT NOT NULL UNIQUE,
                      contrasenia       TEXT NOT NULL,
                      celular           TEXT
                    );
                """);

                // ⬇️ AHORA con correoElectronico en Estudiante (FK a Persona)
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Estudiante (
                      idEstudiante                  INTEGER PRIMARY KEY,
                      idPrograma                    INTEGER NOT NULL,
                      correoElectronico             TEXT NOT NULL UNIQUE,
                      cantidadIntentosInvestigacion INTEGER DEFAULT 0,
                      cantidadIntentosPractica      INTEGER DEFAULT 0,
                      FOREIGN KEY (idEstudiante)
                        REFERENCES Persona(idPersona)
                        ON DELETE CASCADE,
                      FOREIGN KEY (correoElectronico)
                        REFERENCES Persona(correoElectronico)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
                      FOREIGN KEY (idPrograma)
                        REFERENCES Programa(idPrograma)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT
                    );
                """);

                // ⬇️ AHORA con correoElectronico en Profesor (FK a Persona)
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Profesor (
                      idProfesor       INTEGER PRIMARY KEY,
                      idDepartamento   INTEGER NOT NULL,
                      correoElectronico TEXT NOT NULL UNIQUE,
                      FOREIGN KEY (idProfesor)
                        REFERENCES Persona(idPersona)
                        ON DELETE CASCADE,
                      FOREIGN KEY (correoElectronico)
                        REFERENCES Persona(correoElectronico)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
                      FOREIGN KEY (idDepartamento)
                        REFERENCES Departamento(idDepartamento)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT
                    );
                """);

                // ⬇️ AHORA con correoElectronico en Coordinador (FK a Persona)
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Coordinador (
                      idCoordinador     INTEGER PRIMARY KEY,
                      idDepartamento    INTEGER NOT NULL,
                      correoElectronico TEXT NOT NULL UNIQUE,
                      FOREIGN KEY (idCoordinador)
                        REFERENCES Persona(idPersona)
                        ON DELETE CASCADE,
                      FOREIGN KEY (correoElectronico)
                        REFERENCES Persona(correoElectronico)
                        ON UPDATE CASCADE
                        ON DELETE CASCADE,
                      FOREIGN KEY (idDepartamento)
                        REFERENCES Departamento(idDepartamento)
                        ON UPDATE CASCADE
                        ON DELETE RESTRICT
                    );
                """);

                // ============ Proyectos y relaciones ============
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Proyecto (
                      idProyecto         INTEGER PRIMARY KEY AUTOINCREMENT,
                      titulo             TEXT NOT NULL,
                      objetivo           TEXT,
                      objetivoEspecifico TEXT,
                      estado             TEXT NOT NULL,
                      tipo               TEXT NOT NULL,
                      fechaDeSubida      TEXT,
                      fechaRevision      TEXT,
                      archivoAdjunto     TEXT
                    );
                """);
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS Comentario (
                      idComentario  INTEGER PRIMARY KEY AUTOINCREMENT,
                      contenido     TEXT NOT NULL,
                      fecha         TEXT NOT NULL,
                      idProyecto    INTEGER NOT NULL,
                      idCoordinador INTEGER NOT NULL,
                      FOREIGN KEY (idProyecto)
                        REFERENCES Proyecto(idProyecto)
                        ON DELETE CASCADE,
                      FOREIGN KEY (idCoordinador)
                        REFERENCES Coordinador(idCoordinador)
                        ON DELETE CASCADE
                    );
                """);
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS ProyectosEstudiante (
                      idEstudiante INTEGER NOT NULL,
                      idProyecto   INTEGER NOT NULL,
                      PRIMARY KEY (idEstudiante, idProyecto),
                      FOREIGN KEY (idEstudiante)
                        REFERENCES Estudiante(idEstudiante)
                        ON DELETE CASCADE,
                      FOREIGN KEY (idProyecto)
                        REFERENCES Proyecto(idProyecto)
                        ON DELETE CASCADE
                    );
                """);
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS ProyectosProfesor (
                      idDirector   INTEGER NOT NULL,
                      idCodirector INTEGER,
                      idProyecto   INTEGER NOT NULL,
                      PRIMARY KEY (idDirector, idProyecto),
                      FOREIGN KEY (idCodirector)
                        REFERENCES Profesor(idProfesor)
                        ON DELETE SET NULL,
                      FOREIGN KEY (idDirector)
                        REFERENCES Profesor(idProfesor)
                        ON DELETE CASCADE,
                      FOREIGN KEY (idProyecto)
                        REFERENCES Proyecto(idProyecto)
                        ON DELETE CASCADE
                    );
                """);
                st.addBatch("""
                    CREATE TABLE IF NOT EXISTS ProyectosCoordinador (
                      idCoordinador INTEGER NOT NULL,
                      idProyecto    INTEGER NOT NULL,
                      PRIMARY KEY (idCoordinador, idProyecto),
                      FOREIGN KEY (idCoordinador)
                        REFERENCES Coordinador(idCoordinador)
                        ON DELETE CASCADE,
                      FOREIGN KEY (idProyecto)
                        REFERENCES Proyecto(idProyecto)
                        ON DELETE CASCADE
                    );
                """);

                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_persona_correo ON Persona(correoElectronico);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_profesor_departamento ON Profesor(idDepartamento);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_coordinador_departamento ON Coordinador(idDepartamento);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_estudiante_programa ON Estudiante(idPrograma);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_comentario_proyecto ON Comentario(idProyecto);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_rel_pe_proyecto ON ProyectosEstudiante(idProyecto);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_rel_pp_proyecto ON ProyectosProfesor(idProyecto);
                  """);
                st.addBatch("""
                      CREATE INDEX IF NOT EXISTS idx_rel_pc_proyecto ON ProyectosCoordinador(idProyecto);
                  """);

                // ============ Triggers ============
                st.addBatch("""
                    CREATE TRIGGER IF NOT EXISTS trg_pe_before_insert
                    BEFORE INSERT ON ProyectosEstudiante
                    FOR EACH ROW
                    BEGIN
                      SELECT CASE
                        WHEN (SELECT tipo FROM Proyecto WHERE idProyecto = NEW.idProyecto) = 'Investigacion'
                             AND (SELECT COUNT(*) FROM ProyectosEstudiante WHERE idProyecto = NEW.idProyecto) >= 2
                          THEN RAISE(ABORT, 'Proyecto de Investigacion permite maximo 2 estudiantes')
                        WHEN (SELECT tipo FROM Proyecto WHERE idProyecto = NEW.idProyecto) = 'Practica laboral'
                             AND (SELECT COUNT(*) FROM ProyectosEstudiante WHERE idProyecto = NEW.idProyecto) >= 1
                          THEN RAISE(ABORT, 'Proyecto de Practica laboral permite maximo 1 estudiante')
                      END;
                    END;
                """);
                st.addBatch("""
                    CREATE TRIGGER IF NOT EXISTS trg_proyecto_tipo_before_update
                    BEFORE UPDATE OF tipo ON Proyecto
                    FOR EACH ROW
                    BEGIN
                      SELECT CASE
                        WHEN NEW.tipo = 'Practica laboral'
                             AND (SELECT COUNT(*) FROM ProyectosEstudiante WHERE idProyecto = OLD.idProyecto) > 1
                          THEN RAISE(ABORT, 'No se puede cambiar a Practica laboral: hay >1 estudiante asignado')
                        WHEN NEW.tipo = 'Investigacion'
                             AND (SELECT COUNT(*) FROM ProyectosEstudiante WHERE idProyecto = OLD.idProyecto) > 2
                          THEN RAISE(ABORT, 'No se puede cambiar a Investigacion: hay >2 estudiantes asignados')
                      END;
                    END;
                """);
                
                st.executeBatch();
                
                // ============ INSERTAR DATOS INICIALES ============
                System.out.println("Insertando datos iniciales...");
                
                // Insertar Facultad de Ingeniería
                st.addBatch("INSERT OR IGNORE INTO Facultad (idFacultad, nombre) VALUES (1, 'Facultad de Ingeniería')");
                
                // Insertar los departamentos específicos que solicitaste
                st.addBatch("INSERT OR IGNORE INTO Departamento (idDepartamento, nombre, idFacultad) VALUES (1, 'Ingeniería de Sistemas', 1)");
                st.addBatch("INSERT OR IGNORE INTO Departamento (idDepartamento, nombre, idFacultad) VALUES (2, 'Ingeniería Electrónica y Telecomunicaciones', 1)");
                st.addBatch("INSERT OR IGNORE INTO Departamento (idDepartamento, nombre, idFacultad) VALUES (3, 'Automatica Industrial', 1)");
                st.addBatch("INSERT OR IGNORE INTO Departamento (idDepartamento, nombre, idFacultad) VALUES (4, 'Tecnología Telemática', 1)");
                
                // Insertar UN SOLO programa para cada departamento con el mismo nombre
                st.addBatch("INSERT OR IGNORE INTO Programa (idPrograma, nombre, idDepartamento) VALUES (1, 'Ingeniería de Sistemas', 1)");
                st.addBatch("INSERT OR IGNORE INTO Programa (idPrograma, nombre, idDepartamento) VALUES (2, 'Ingeniería Electrónica y Telecomunicaciones', 2)");
                st.addBatch("INSERT OR IGNORE INTO Programa (idPrograma, nombre, idDepartamento) VALUES (3, 'Automatica Industrial', 3)");
                st.addBatch("INSERT OR IGNORE INTO Programa (idPrograma, nombre, idDepartamento) VALUES (4, 'Tecnología Telemática', 4)");
                
                st.executeBatch();
                conn.commit();
                
                System.out.println("Base de datos inicializada correctamente con 4 departamentos y sus programas");
                ConexionSQLite.desconectar();
                
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            } 
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }
}

    

