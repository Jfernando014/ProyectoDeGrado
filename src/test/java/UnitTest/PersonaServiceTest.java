package UnitTest;


import co.edu.unicauca.Services.PersonaService;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Repository.PersonaRepository;
import co.edu.unicauca.Util.Encriptador;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de PersonaService con Validaciones")
class PersonaServiceTest {
    
    @Mock
    private PersonaRepository personaRepository;
    
    @InjectMocks
    private PersonaService personaService;
    
    private Estudiante estudianteValido;
    private Profesor profesorValido;
    
    @BeforeEach
    void setUp() {
        Programa programa = new Programa(1, "Ingeniería de Sistemas");
        Departamento departamento = new Departamento(1, "Sistemas");
        
        estudianteValido = new Estudiante(
            programa, 
            "Carlos", 
            "Ramírez", 
            "3115558888", 
            "carlos.ramirez@unicauca.edu.co", 
            "Password123!"
        );
        
        profesorValido = new Profesor(
            departamento,
            "Ana",
            "Gómez",
            "3226669999",
            "ana.gomez@unicauca.edu.co",
            "Profesor123!"
        );
    }
    
    @Test
    @DisplayName("Registro exitoso de estudiante con datos válidos")
    void testRegistroEstudianteExitoso() throws Exception {
        when(personaRepository.save(any(Estudiante.class))).thenReturn(true);
        when(personaRepository.getOne(anyString())).thenReturn(null);
        
        String resultado = personaService.registrar(estudianteValido);
        
        assertEquals("Registro completado", resultado);
        verify(personaRepository, times(1)).save(estudianteValido);
        
       
        assertNotEquals("Password123!", estudianteValido.getContrasenia());
        assertTrue(estudianteValido.getContrasenia().length() > 10);
    }
    
    @Test
    @DisplayName("Registro fallido - correo ya existe")
    void testRegistroCorreoExistente() throws Exception {
        when(personaRepository.getOne("carlos.ramirez@unicauca.edu.co")).thenReturn(estudianteValido);
        
        String resultado = personaService.registrar(estudianteValido);
        
        assertEquals("Ya existe un usuario registrado con este correo electrónico", resultado);
        verify(personaRepository, never()).save(any());
    }
    
    @Test
    @DisplayName("Registro fallido - nombre inválido (debería lanzar excepción en constructor)")
    void testRegistroNombreInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
           
            new Estudiante(
                new Programa(1, "Sistemas"),
                "C", 
                "Ramírez",
                "3115558888",
                "test@unicauca.edu.co",
                "Password123!"
            );
        });
    }
    
    @Test
    @DisplayName("Registro fallido - celular inválido (debería lanzar excepción en constructor)")
    void testRegistroCelularInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Estudiante(
                new Programa(1, "Sistemas"),
                "Carlos",
                "Ramírez",
                "123", // Celular inválido - DEBE fallar
                "test@unicauca.edu.co",
                "Password123!"
            );
        });
    }
    
  @Test
@DisplayName("Registro fallido - contraseña débil")
void testRegistroContraseniaDebil() {
    // Crear estudiante con contraseña que NO cumple requisitos
    Programa programa = new Programa(1, "Sistemas");
    Estudiante estudianteDebil = new Estudiante(
        programa,
        "Juan",
        "Pérez",
        "3115558888",
        "juan@unicauca.edu.co",
        "solominusculas" // Contraseña sin mayúsculas, números ni caracteres especiales
    );
    
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        personaService.registrar(estudianteDebil);
    });
    
    assertTrue(exception.getMessage().contains("contraseña") || 
               exception.getMessage().contains("seguridad"));
    
    verifyNoInteractions(personaRepository);
}
    
    @Test
    @DisplayName("Inicio de sesión exitoso")
    void testInicioSesionExitoso() throws Exception {
        // Crear una copia del estudiante con contraseña encriptada
        Estudiante estudianteEncriptado = new Estudiante(
            estudianteValido.getPrograma(),
            estudianteValido.getNombre(),
            estudianteValido.getApellido(),
            estudianteValido.getCelular(),
            estudianteValido.getCorreoElectronico(),
            Encriptador.encriptar("Password123!") // Contraseña encriptada
        );
        
        when(personaRepository.getOne("carlos.ramirez@unicauca.edu.co")).thenReturn(estudianteEncriptado);
        
        var resultado = personaService.iniciarSesion("carlos.ramirez@unicauca.edu.co", "Password123!");
        
        assertNotNull(resultado);
        assertTrue(resultado.getIsLogged());
    }
    
    @Test
    @DisplayName("Inicio de sesión fallido - correo vacío")
    void testInicioSesionCorreoVacio() {
        assertThrows(IllegalArgumentException.class, () -> {
            personaService.iniciarSesion("", "Password123!");
        });
    }
    
    @Test
    @DisplayName("Inicio de sesión fallido - contraseña vacía")
    void testInicioSesionContraseniaVacia() {
        assertThrows(IllegalArgumentException.class, () -> {
            personaService.iniciarSesion("test@unicauca.edu.co", "");
        });
    }
    
    @Test
    @DisplayName("Inicio de sesión fallido - contraseña incorrecta")
    void testInicioSesionContraseniaIncorrecta() throws Exception {
        // Crear estudiante con contraseña encriptada
        Estudiante estudianteEncriptado = new Estudiante(
            estudianteValido.getPrograma(),
            estudianteValido.getNombre(),
            estudianteValido.getApellido(),
            estudianteValido.getCelular(),
            estudianteValido.getCorreoElectronico(),
            Encriptador.encriptar("Password123!") 
        );
        
        when(personaRepository.getOne("carlos.ramirez@unicauca.edu.co")).thenReturn(estudianteEncriptado);
        
        assertThrows(IllegalArgumentException.class, () -> {
            personaService.iniciarSesion("carlos.ramirez@unicauca.edu.co", "ContraseñaIncorrecta");
        });
    }
    
    @Test
    @DisplayName("Cerrar sesión exitoso")
    void testCerrarSesion() throws Exception {
        // Configurar sesión activa primero
        Estudiante estudianteEncriptado = new Estudiante(
            estudianteValido.getPrograma(),
            estudianteValido.getNombre(),
            estudianteValido.getApellido(),
            estudianteValido.getCelular(),
            estudianteValido.getCorreoElectronico(),
            Encriptador.encriptar("Password123!")
        );
        
        when(personaRepository.getOne("carlos.ramirez@unicauca.edu.co")).thenReturn(estudianteEncriptado);
        
        // Iniciar sesión
        var resultado = personaService.iniciarSesion("carlos.ramirez@unicauca.edu.co", "Password123!");
        assertNotNull(resultado);
        assertTrue(resultado.getIsLogged());
        
        // Cerrar sesión
        personaService.cerrarSesion();
        
        assertFalse(personaService.validarSesionActiva());
    }
    
    @Test
    @DisplayName("Encriptación y desencriptación funcionan correctamente")
    void testEncriptacionFunciona() {
        String textoOriginal = "Password123!";
        String textoEncriptado = Encriptador.encriptar(textoOriginal);
        String textoDesencriptado = Encriptador.decriptar(textoEncriptado);
        
        assertNotNull(textoEncriptado);
        assertNotEquals(textoOriginal, textoEncriptado);
        assertEquals(textoOriginal, textoDesencriptado);
    }
}
