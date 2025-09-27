package co.edu.unicauca.Services;

import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Observer.Subject;

import co.edu.unicauca.Repository.PersonaRepository;
import co.edu.unicauca.Util.Encriptador;
import co.edu.unicauca.Util.Validador;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class PersonaService extends Subject{
    
    PersonaRepository personaRepository;
    Persona persona;
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
        
    }
    
    public Persona iniciarSesion(String correoElectronico, String contrasenia) throws UnsupportedEncodingException, Exception {
        System.out.println(correoElectronico);
        if (!Validador.esCorreoValido("unicauca.edu.co", correoElectronico))
            return null;
        
        this.persona =  personaRepository.getOne(correoElectronico);
        
        if (persona == null) {
            return null;
        }

        String clave = "1234567890ABCDEF";  
        byte[] iv = "abcdefghijklmnop".getBytes("UTF-8");

        if (Encriptador.decriptar(clave, iv, persona.getContrasenia()).equals(contrasenia)) {
            
            persona.setIsLogged(true);
            
            this.notifyAllObserves();
            
            return persona;
            
        }
        
        return null;
    }
    public List<Persona> buscarPorId(int id)
    {
        
        return null;
    
    }
    public Persona getPersonaId(int id)
    {
    
        return personaRepository.getOne(id);
    
    }
    
    public Persona getPersona()
    {
        return persona;
    
    }
    
    public String registrar(Persona persona) throws UnsupportedEncodingException, Exception {
        if (!Validador.esCorreoValido("unicauca.edu.co", persona.getCorreoElectronico()))
            return "Correo invalido";

        if (!Validador.esContraseniaCorrecta(persona.getContrasenia())) {
            return "Formato de contrasenia invalido recuerde que debe llevar por lo menos un caracter especial una mayuscula y un digito";
        }

        persona.setContrasenia(Encriptador.encriptar(persona.getContrasenia()));
        
        if(personaRepository.save(persona))
        {
            return "Registro completado";
        }
        
        return "Se encontro un registro con el mismo correo electronico";
    }
    
}
