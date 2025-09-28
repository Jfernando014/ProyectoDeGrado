package UnitTest;

import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Programa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PRUEBA: Yo como un docente necesito pruebas unitarias de control de intentos, 
 * con la finalidad de asegurar el correcto conteo de versiones.
 */
@DisplayName("Pruebas de Docente - Control de Intentos")
class DocenteControlIntentosTest {
    
    @Test
    @DisplayName("Estudiante con intentos de práctica e investigación")
    void testEstudianteConIntentosIniciales() {
        Programa programa = new Programa(1, "Ingeniería de Sistemas");
        Estudiante estudiante = new Estudiante(
            programa,
            2,
            1, 
            "Maria", 
            "López", 
            "3001112222", 
            "maria.lopez@unicauca.edu.co", 
            "SecurePass123"
        );
        
        assertEquals(2, estudiante.getCantidadIntentosPractica());
        assertEquals(1, estudiante.getCantidadIntentosInvestigacion());
    }
    
    @Test
    @DisplayName("Incrementar intentos de práctica")
    void testIncrementarIntentosPractica() {
        Programa programa = new Programa(1, "Sistemas");
        Estudiante estudiante = new Estudiante(programa, "Juan", "Pérez", "3115558888", "juan@unicauca.edu.co", "pass");
        
        estudiante.incrementarIntentoPractica();
        assertEquals(1, estudiante.getCantidadIntentosPractica());
        
        estudiante.incrementarIntentoPractica();
        assertEquals(2, estudiante.getCantidadIntentosPractica());
    }
    
    @Test
    @DisplayName("Incrementar intentos de práctica - límite excedido")
    void testIncrementarIntentosPracticaLimite() {
        Programa programa = new Programa(1, "Sistemas");
        Estudiante estudiante = new Estudiante(programa, 3, 0, "Juan", "Pérez", "3115558888", "juan@unicauca.edu.co", "pass");
        
        assertThrows(IllegalStateException.class, () -> {
            estudiante.incrementarIntentoPractica();
        });
    }
    
    @Test
    @DisplayName("Validar puede enviar práctica")
    void testPuedeEnviarPractica() {
        Programa programa = new Programa(1, "Sistemas");
        Estudiante estudiante = new Estudiante(programa, "Juan", "Pérez", "3115558888", "juan@unicauca.edu.co", "pass");
        
        assertTrue(estudiante.puedeEnviarPractica());
        
        estudiante.setCantidadIntentosPractica(3);
        assertFalse(estudiante.puedeEnviarPractica());
    }
    
    @Test
    @DisplayName("Validar puede enviar investigación")
    void testPuedeEnviarInvestigacion() {
        Programa programa = new Programa(1, "Sistemas");
        Estudiante estudiante = new Estudiante(programa, "Maria", "Gómez", "3226669999", "maria@unicauca.edu.co", "pass");
        
        assertTrue(estudiante.puedeEnviarInvestigacion());
        
        estudiante.setCantidadIntentosInvestigacion(3);
        assertFalse(estudiante.puedeEnviarInvestigacion());
    }
    
    @Test
    @DisplayName("Setter de intentos inválido - excede máximo")
    void testSetterIntentosExcedeMaximo() {
        Programa programa = new Programa(1, "Sistemas");
        Estudiante estudiante = new Estudiante(programa, "Juan", "Pérez", "3115558888", "juan@unicauca.edu.co", "pass");
        
        assertThrows(IllegalArgumentException.class, () -> {
            estudiante.setCantidadIntentosPractica(4);
        });
    }
}
