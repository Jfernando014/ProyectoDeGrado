package co.edu.unicauca.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */

public class ConexionSQLite {
    
    private static final String URL = "jdbc:sqlite:mi_base.db"; 
    // Se creará en la raíz del proyecto
    private static Connection instance;

    private ConexionSQLite() {
    }
    
    
    public static Connection getInstance() throws Exception {
        try {
            
            if(instance == null || instance.isClosed())
            {
                instance = DriverManager.getConnection(URL);
            }
            
            return instance;
        } catch (SQLException e) {
            System.out.println("Error en ConexionSQLite "+ e.getMessage());
            throw new Exception("Error al conectar con la conexion con la base de datos");
        }
    }
    public static void desconectar() throws Exception {
    if (instance != null) {
        try {
            if (!instance.isClosed()) instance.close();
        } catch (SQLException e) {
            System.out.println("Error en ConexionSQLite "+ e.getMessage());
            throw new Exception("Error al cerrar la conexion con la base de datos");
            
        }
    }
}
}
