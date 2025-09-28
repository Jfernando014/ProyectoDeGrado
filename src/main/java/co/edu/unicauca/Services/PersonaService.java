package co.edu.unicauca.Services;

import co.edu.unicauca.Models.Persona;
import co.edu.unicauca.Observer.Subject;
import co.edu.unicauca.Repository.PersonaRepository;
import co.edu.unicauca.Util.Encriptador;
import co.edu.unicauca.Util.Validador;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class PersonaService extends Subject {
    
    private PersonaRepository personaRepository;
    private Persona persona;
    
    public PersonaService(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }
    
    public Persona iniciarSesion(String correoElectronico, String contrasenia) throws UnsupportedEncodingException, Exception {
        // Validar parámetros de entrada
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio");
        }
        
        if (contrasenia == null || contrasenia.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        String correoTrim = correoElectronico.trim();
        String contraseniaTrim = contrasenia.trim();
        
        System.out.println(correoTrim);
        
        // Validar formato de correo unicauca
        if (!Validador.esCorreoValido("unicauca.edu.co", correoTrim)) {
            throw new IllegalArgumentException("El correo electrónico debe ser del dominio unicauca.edu.co");
        }
        
        // Buscar persona en el repositorio
        this.persona = personaRepository.getOne(correoTrim);
        
        if (persona == null) {
            throw new IllegalArgumentException("No se encontró un usuario con ese correo electrónico");
        }

        // Desencriptar y validar contraseña
        String clave = "1234567890ABCDEF";  
        byte[] iv = "abcdefghijklmnop".getBytes("UTF-8");

        String contraseniaDesencriptada = Encriptador.decriptar(persona.getContrasenia());
        
        if (contraseniaDesencriptada == null || !contraseniaDesencriptada.equals(contraseniaTrim)) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }
        
        // Establecer sesión y notificar observadores
        persona.setIsLogged(true);
        this.notifyAllObserves(persona);
        
        return persona;
    }
    
    public List<Persona> buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        // Implementar búsqueda por ID si es necesario
        return null;
    }
    
    public Persona getPersonaId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
        return personaRepository.getOne(id);
    }
    
    public Persona getPersona() {
        return persona;
    }
    
    public String registrar(Persona persona) throws UnsupportedEncodingException, Exception {
        // Validar persona no nula
        if (persona == null) {
            throw new IllegalArgumentException("La persona no puede ser nula");
        }
        
        // Validar campos obligatorios usando Validador
        Validador.validarPersona(
            persona.getNombre(), 
            persona.getApellido(), 
            persona.getCelular(), 
            persona.getCorreoElectronico(), 
            persona.getContrasenia()
        );
        
        // Validar formato de correo unicauca
        if (!Validador.esCorreoValido("unicauca.edu.co", persona.getCorreoElectronico())) {
            return "Correo invalido: debe ser del dominio unicauca.edu.co";
        }

        // Validar formato de contraseña
        if (!Validador.esContraseniaCorrecta(persona.getContrasenia())) {
            return "Formato de contrasenia invalido recuerde que debe llevar por lo menos un caracter especial una mayuscula y un digito";
        }

        // Verificar si el correo ya existe
        try {
            Persona personaExistente = personaRepository.getOne(persona.getCorreoElectronico());
            if (personaExistente != null) {
                return "Ya existe un usuario registrado con este correo electrónico";
            }
        } catch (Exception e) {
            // Si hay error al buscar, continuar con el registro
            System.out.println("Error al verificar correo existente: " + e.getMessage());
        }

        // Encriptar contraseña antes de guardar
        String contraseniaEncriptada = Encriptador.encriptar(persona.getContrasenia());
        persona.setContrasenia(contraseniaEncriptada);
        
        // Guardar en repositorio
        if (personaRepository.save(persona)) {
            return "Registro completado";
        }
        
        return "Error al guardar el registro en la base de datos";
    }
    
    public void cerrarSesion() {
        if (this.persona != null) {
            this.persona.setIsLogged(false);
            this.persona = null;
            this.notifyAllObserves();
        }
    }
    
    public boolean validarSesionActiva() {
        return this.persona != null && this.persona.getIsLogged();
    }
}
