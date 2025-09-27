package co.edu.unicauca.Services;

import co.edu.unicauca.Models.FormatoA;
import co.edu.unicauca.Repository.ProyectoRepository;

/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class ProyectoService {
    ProyectoRepository proyectoRepository;

    public ProyectoService(ProyectoRepository proyectoRepository) {
        this.proyectoRepository = proyectoRepository;
    }
    
    public boolean subirFormato(FormatoA formato) throws Exception
    {
        return proyectoRepository.save(formato);
    }
}
