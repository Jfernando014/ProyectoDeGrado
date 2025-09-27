package co.edu.unicauca.Services;

import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Repository.DepartamentoRepository;
import java.util.List;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class DepartamentoService {
    DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }
    
    public List<Departamento> obtenerTodos() throws Exception
    {
        return departamentoRepository.getAll();
    
    }
}
