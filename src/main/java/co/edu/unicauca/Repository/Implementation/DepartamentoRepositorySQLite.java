package co.edu.unicauca.Repository.Implementation;

import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Models.Facultad;
import co.edu.unicauca.Repository.DepartamentoRepository;
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
public class DepartamentoRepositorySQLite implements DepartamentoRepository {
    
    @Override
    public List<Departamento> getAll() throws Exception {
        String sql = "SELECT * FROM DEPARTAMENTO";
        List<Departamento> departamentos = new ArrayList<>();

        try (Connection conexionDB = ConexionSQLite.getInstance();
             Statement stmt = conexionDB.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                
                Departamento departamento = new Departamento();
                departamento.setCodigoDepartamento(rs.getInt("idDepartamento"));
                departamento.setNombreDepartamento(rs.getString("nombre"));
                departamento.setFacultad(new Facultad(rs.getInt("idFacultad")));
                departamentos.add(departamento);
            }

            return departamentos;

        } catch (SQLException e) {
            throw new Exception("Error al obtener los departamentos: " + e.getMessage(), e);
        }finally
        {
            ConexionSQLite.desconectar();
        }
    }

    @Override
    public Departamento getOne(Integer id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean save(Departamento object) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
    
}
