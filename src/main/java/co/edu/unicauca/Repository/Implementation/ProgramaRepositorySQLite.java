package co.edu.unicauca.Repository.Implementation;

import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Repository.ProgramaRepository;
import co.edu.unicauca.database.ConexionSQLite;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class ProgramaRepositorySQLite implements ProgramaRepository{

    @Override
    public List<Programa> getAll() throws Exception {
        String sql = "SELECT * FROM PROGRAMA";
        List<Programa> programas = new ArrayList<>();

        try (Connection conexionDB = ConexionSQLite.getInstance();
             Statement stmt = conexionDB.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Programa programa = new Programa();

                programa.setCodigoDePrograma(rs.getInt("idPrograma"));
                programa.setNombrePrograma(rs.getString("nombre"));
                programa.setDepartamento(new Departamento(rs.getInt("idDepartamento")));

                programas.add(programa);
            }

            return programas;

        } catch (SQLException e) {
            throw new Exception("Error al obtener los programas: " + e.getMessage(), e);
        }finally
        {
            ConexionSQLite.desconectar();
        }
    }
    
    @Override
    public Programa getOne(Integer id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean save(Programa object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
