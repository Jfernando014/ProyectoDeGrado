package co.edu.unicauca.Repository.Implementation;

import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Repository.PersonaRepository;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Util.Cargo;
import static co.edu.unicauca.Util.Cargo.COORDINADOR;
import static co.edu.unicauca.Util.Cargo.ESTUDIANTE;
import static co.edu.unicauca.Util.Cargo.PROFESOR;
import co.edu.unicauca.database.ConexionSQLite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class PersonaRepositorySQLite implements PersonaRepository{

    @Override
    public List<Persona> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public Persona getOne(int id)
    {
        Connection conexionDB = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            

            // Primero determinar el tipo de persona usando la función existente
            Cargo cargo = getCargo(id);
            conexionDB = ConexionSQLite.getInstance();
            if (cargo == null) {
                return null; // No existe en ninguna tabla
            }

            // Obtener datos básicos de Persona
            String sqlPersona = """
                SELECT idPersona, nombre, apellido, correoElectronico, contrasenia, celular 
                FROM Persona 
                WHERE idPersona = ?
            """;

            stmt = conexionDB.prepareStatement(sqlPersona);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                return null; 
            }

            // Datos básicos comunes
            int idPersona = rs.getInt("idPersona");
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String correo = rs.getString("correoElectronico");
            String contrasenia = rs.getString("contrasenia");
            String celular = rs.getString("celular");

            // Cerrar recursos para reutilizar
            rs.close();
            stmt.close();

            // Cargar datos específicos según el cargo
            switch (cargo) {
                case ESTUDIANTE:
                    return cargarEstudiante(conexionDB, idPersona, nombre, apellido, correo, contrasenia, celular);

                case PROFESOR:
                    return cargarProfesor(conexionDB, idPersona, nombre, apellido, correo, contrasenia, celular);

                case COORDINADOR:
                    return cargarCoordinador(conexionDB, idPersona, nombre, apellido, correo, contrasenia, celular);

                default:
                    // Persona básica (sin rol específico)
                    Persona persona = new Persona();
                    persona.setId(idPersona);
                    persona.setNombre(nombre);
                    persona.setApellido(apellido);
                    persona.setCorreoElectronico(correo);
                    persona.setContrasenia(contrasenia);
                    persona.setCelular(celular);
                    return persona;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexionDB != null) conexionDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    
    }
    @Override
    public Persona getOne(String correoElectronico) {
        Connection conexionDB = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            

            // Primero determinar el tipo de persona usando la función existente
            Cargo cargo = getCargo(correoElectronico);
            conexionDB = ConexionSQLite.getInstance();
            if (cargo == null) {
                return null; // No existe en ninguna tabla
            }

            // Obtener datos básicos de Persona
            String sqlPersona = """
                SELECT idPersona, nombre, apellido, correoElectronico, contrasenia, celular 
                FROM Persona 
                WHERE correoElectronico = ?
            """;

            stmt = conexionDB.prepareStatement(sqlPersona);
            stmt.setString(1, correoElectronico);
            rs = stmt.executeQuery();

            if (!rs.next()) {
                return null; 
            }

            // Datos básicos comunes
            int idPersona = rs.getInt("idPersona");
            String nombre = rs.getString("nombre");
            String apellido = rs.getString("apellido");
            String correo = rs.getString("correoElectronico");
            String contrasenia = rs.getString("contrasenia");
            String celular = rs.getString("celular");

            // Cerrar recursos para reutilizar
            rs.close();
            stmt.close();

            // Cargar datos específicos según el cargo
            switch (cargo) {
                case ESTUDIANTE:
                    return cargarEstudiante(conexionDB, idPersona, nombre, apellido, correo, contrasenia, celular);

                case PROFESOR:
                    return cargarProfesor(conexionDB, idPersona, nombre, apellido, correo, contrasenia, celular);

                case COORDINADOR:
                    return cargarCoordinador(conexionDB, idPersona, nombre, apellido, correo, contrasenia, celular);

                default:
                    // Persona básica (sin rol específico)
                    Persona persona = new Persona();
                    persona.setId(idPersona);
                    persona.setNombre(nombre);
                    persona.setApellido(apellido);
                    persona.setCorreoElectronico(correo);
                    persona.setContrasenia(contrasenia);
                    persona.setCelular(celular);
                    return persona;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexionDB != null) conexionDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Métodos auxiliares para cargar datos específicos
    private Estudiante cargarEstudiante(Connection conexionDB, int idPersona, String nombre, 
                                       String apellido, String correo, String contrasenia, 
                                       String celular) throws SQLException {

        String sql = """
            SELECT idPrograma
            FROM Estudiante 
            WHERE idEstudiante = ?
        """;

        try (PreparedStatement stmt = conexionDB.prepareStatement(sql)) {
            stmt.setInt(1, idPersona);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setId(idPersona);
                    estudiante.setNombre(nombre);
                    estudiante.setApellido(apellido);
                    estudiante.setCorreoElectronico(correo);
                    estudiante.setContrasenia(contrasenia);
                    estudiante.setCelular(celular);
                    estudiante.setPrograma(new Programa(rs.getInt("idPrograma")));
                    return estudiante;
                }
            }
        }
        return null;
    }

    private Profesor cargarProfesor(Connection conexionDB, int idPersona, String nombre, 
                                   String apellido, String correo, String contrasenia, 
                                   String celular) throws SQLException {

        String sql = """
            SELECT idDepartamento
            FROM Profesor 
            WHERE idProfesor = ?
        """;

        try (PreparedStatement stmt = conexionDB.prepareStatement(sql)) {
            stmt.setInt(1, idPersona);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Profesor profesor = new Profesor();
                    profesor.setId(idPersona);
                    profesor.setNombre(nombre);
                    profesor.setApellido(apellido);
                    profesor.setCorreoElectronico(correo);
                    profesor.setContrasenia(contrasenia);
                    profesor.setCelular(celular);
                    profesor.setDepartamento(new Departamento(rs.getInt("idDepartamento")));

                    return profesor;
                }
            }
        }
        return null;
    }

    private Coordinador cargarCoordinador(Connection conexionDB, int idPersona, String nombre, 
                                         String apellido, String correo, String contrasenia, 
                                         String celular) throws SQLException {

        String sql = """
            SELECT idDepartamento
            FROM Coordinador 
            WHERE idCoordinador = ?
        """;

        try (PreparedStatement stmt = conexionDB.prepareStatement(sql)) {
            stmt.setInt(1, idPersona);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Coordinador coordinador = new Coordinador();
                    coordinador.setId(idPersona);
                    coordinador.setNombre(nombre);
                    coordinador.setApellido(apellido);
                    coordinador.setCorreoElectronico(correo);
                    coordinador.setContrasenia(contrasenia);
                    coordinador.setCelular(celular);
                    coordinador.setDepartamento(new Departamento(rs.getInt("idDepartamento")));
                    
                    return coordinador;
                }
            }
        }
        return null;
    }
    
    private Cargo getCargo(String correoElectronico) throws Exception {
        Connection conexionDB = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexionDB = ConexionSQLite.getInstance();

            // Consulta para verificar en Estudiante
            String sqlEstudiante = "SELECT 1 FROM Estudiante WHERE correoElectronico = ?";
            stmt = conexionDB.prepareStatement(sqlEstudiante);
            stmt.setString(1, correoElectronico);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Cargo.ESTUDIANTE;
            }

            // Consulta para verificar en Profesor
            String sqlProfesor = "SELECT 1 FROM Profesor WHERE correoElectronico = ?";
            stmt = conexionDB.prepareStatement(sqlProfesor);
            stmt.setString(1, correoElectronico);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Cargo.PROFESOR;
            }

            // Consulta para verificar en Coordinador
            String sqlCoordinador = "SELECT 1 FROM Coordinador WHERE correoElectronico = ?";
            stmt = conexionDB.prepareStatement(sqlCoordinador);
            stmt.setString(1, correoElectronico);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Cargo.COORDINADOR;
            }

            // Si no encuentra en ninguna tabla
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexionDB != null) ConexionSQLite.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private Cargo getCargo(int id) throws Exception {
        Connection conexionDB = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            conexionDB = ConexionSQLite.getInstance();

            // Consulta para verificar en Estudiante
            String sqlEstudiante = "SELECT 1 FROM Estudiante WHERE idEstudiante = ?";
            stmt = conexionDB.prepareStatement(sqlEstudiante);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Cargo.ESTUDIANTE;
            }

            // Consulta para verificar en Profesor
            String sqlProfesor = "SELECT 1 FROM Profesor WHERE idProfesor = ?";
            stmt = conexionDB.prepareStatement(sqlProfesor);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Cargo.PROFESOR;
            }

            // Consulta para verificar en Coordinador
            String sqlCoordinador = "SELECT 1 FROM Coordinador WHERE idCoordinador = ?";
            stmt = conexionDB.prepareStatement(sqlCoordinador);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Cargo.COORDINADOR;
            }

            // Si no encuentra en ninguna tabla
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexionDB != null) ConexionSQLite.desconectar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    public boolean save(Persona object) throws Exception {
        
        Connection conexionDB = null;
        
        try {
            conexionDB = ConexionSQLite.getInstance();
            conexionDB.setAutoCommit(false);
        } catch (Exception ex) {
            System.out.println("Error al conectar a la base de datos en save personaRepositorySql");
            throw new Exception("Error al conectar a la base de datos");
        }
        try
        {
            
            PreparedStatement statementPersona = conexionDB.prepareStatement("insert into persona(nombre,apellido,correoElectronico,contrasenia,celular) values(?,?,?,?,?)");
            statementPersona.setString(1,object.getNombre());
            statementPersona.setString(2, object.getApellido());
            statementPersona.setString(3, object.getCorreoElectronico());
            statementPersona.setString(4, object.getContrasenia());
            statementPersona.setString(5, object.getCelular());
            
            int filasAfectadas = statementPersona.executeUpdate();
        
            if (filasAfectadas == 0) {
                System.out.println("Error en filas afectadas persona");
                throw new SQLException("Error al registrarse ya se encuentra una persona registrada.");
            }

            int idPersona;
            try (ResultSet generatedKeys = statementPersona.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idPersona = generatedKeys.getInt(1);
                } else {
                    System.out.println("error al obtener id");
                    throw new SQLException("Error al obtener ID de persona.");
                }
            }
            object.setId(idPersona);
            if(object instanceof Estudiante)
            {
                saveEstudiante((Estudiante) object);   
                
            }else if(object instanceof Profesor)
            {
                saveProfesor((Profesor) object);
            }else if(object instanceof Coordinador)
            {
                saveCoordinador((Coordinador) object);
            
            }
            conexionDB.commit();
            return true;
            
        }catch(Exception e)
        {   
            System.out.println("Error " + e.getMessage());
            conexionDB.rollback();
            return false;
        }finally
        {
            ConexionSQLite.desconectar();
        }
    }
    private boolean saveEstudiante(Estudiante estudiante) throws Exception
    {   
        try
        {
            Connection conexionDB = ConexionSQLite.getInstance();
            PreparedStatement statementPersona = conexionDB.prepareStatement("insert into estudiante(idEstudiante,idPrograma,correoElectronico) values(?,?,?)");
            statementPersona.setInt(1, estudiante.getId());
            statementPersona.setInt(2, estudiante.getPrograma().getCodigoDePrograma());
            statementPersona.setString(3, estudiante.getCorreoElectronico());
            
            
            int filasAfectadas = statementPersona.executeUpdate();
            
            if (filasAfectadas == 0) {
                
                throw new SQLException("Error al registrar en estudiante.");
            }
            return true;
        }catch(Exception e)
        {
            throw new Exception("Error al guardar estudiante");
        }
        
    }
    private boolean saveCoordinador(Coordinador coordinador) throws Exception
    {   
        try
        {
            Connection conexionDB = ConexionSQLite.getInstance();
            PreparedStatement statementPersona = conexionDB.prepareStatement("insert into coordinador(idCoordinador,idDepartamento,correoElectronico) values(?,?,?)");
            statementPersona.setInt(1, coordinador.getId());
            statementPersona.setInt(2, coordinador.getDepartamento().getCodigoDepartamento());
            statementPersona.setString(3, coordinador.getCorreoElectronico());
            
            
            int filasAfectadas = statementPersona.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("Error al registrar en coordinador.");
            }
            return true;
        }catch(Exception e)
        {
            throw new Exception("Error al guardar coordinador");
        }
        
    }
    private boolean saveProfesor(Profesor profesor) throws Exception
    {   
        try
        {
            Connection conexionDB = ConexionSQLite.getInstance();
            PreparedStatement statementPersona = conexionDB.prepareStatement("insert into profesor(idProfesor,idDepartamento,correoElectronico) values(?,?,?)");
            statementPersona.setInt(1, profesor.getId());
            statementPersona.setInt(2, profesor.getDepartamento().getCodigoDepartamento());
            statementPersona.setString(3, profesor.getCorreoElectronico());
            
            
            int filasAfectadas = statementPersona.executeUpdate();
            
            if (filasAfectadas == 0) {
                throw new SQLException("Error al registrar en profesor.");
            }
            return true;
        }catch(Exception e)
        {
            throw new Exception("Error al guardar profesor");
        }
        
    }


    
}
