package UnitTest;

import co.edu.unicauca.Models.FormatoA;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Util.Tipo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PRUEBA: Yo como un estudiante necesito pruebas unitarias de consulta, 
 * con la finalidad de asegurar el correcto despliegue del estado.
 */
@DisplayName("Pruebas de Estudiante - Consulta de Estado")
class EstudianteConsultaEstadoTest {
    
    private Estudiante estudiante;
    private FormatoA proyecto;
    
    @BeforeEach
    void setUp() {
        Programa programa = new Programa(1, "Ingeniería de Sistemas");
        estudiante = new Estudiante(
            programa,
            "Maria",
            "Gomez",
            "9876543213",
            "maria.gomez@unicauca.edu.co",
            "Password123!"
        );
        
        List<Estudiante> estudiantes = Arrays.asList(estudiante);
        
        proyecto = new FormatoA(
            "Sistema de Gestión Académica",
            "Automatizar procesos académicos",
            "Implementar módulos de registro",
            "En primera evaluación Formato A",
            Tipo.Investigacion,
            "2024-01-15",
            "documento.pdf",
            estudiantes,
            Arrays.asList() // Sin profesores para simplificar
        );
    }
    
    @Test
    @DisplayName("Consulta de estado de proyecto")
    void testConsultaEstadoProyecto() {
        assertNotNull(proyecto.getEstado());
        assertEquals("En primera evaluación Formato A", proyecto.getEstado());
    }
    
    @Test
    @DisplayName("Consulta de diferentes estados de proyecto")
    void testConsultaDiferentesEstados() {
        // Verificar todos los estados posibles
        String[] estados = {
            "En primera evaluación Formato A",
            "En segunda evaluación Formato A", 
            "En tercera evaluación Formato A",
            "Aprobado Formato A",
            "Rechazado Formato A",
            "Pendiente de revisión"
        };
        
        for (String estado : estados) {
            proyecto.setEstado(estado);
            assertEquals(estado, proyecto.getEstado());
        }
    }
    
    @Test
    @DisplayName("Consulta de información completa del proyecto")
    void testConsultaInformacionCompletaProyecto() {
        assertEquals("Sistema de Gestión Académica", proyecto.getTitulo());
        assertEquals("Automatizar procesos académicos", proyecto.getObjetivo());
        assertEquals("Implementar módulos de registro", proyecto.getObjetivoEspecifico());
        assertEquals(Tipo.Investigacion, proyecto.getTipo());
        assertEquals("2024-01-15", proyecto.getFechaDeSubida());
        assertEquals("documento.pdf", proyecto.getArchivoAdjunto());
    }
    
    @Test
    @DisplayName("Proyecto con múltiples estudiantes")
    void testProyectoConMultiplesEstudiantes() {
        Programa programa = new Programa(1, "Sistemas");
        Estudiante estudiante1 = new Estudiante(programa, "Juan", "Pérez", "1111111111", "juan@unicauca.edu.co", "pass");
        Estudiante estudiante2 = new Estudiante(programa, "Ana", "López", "2222222222", "ana@unicauca.edu.co", "pass");
        
        List<Estudiante> estudiantes = Arrays.asList(estudiante1, estudiante2);
        proyecto.setEstudiantes(estudiantes);
        
        assertEquals(2, proyecto.getEstudiantes().size());
        
        // Verificar información de estudiantes
        for (Estudiante est : proyecto.getEstudiantes()) {
            assertNotNull(est.getNombre());
            assertNotNull(est.getCorreoElectronico());
            assertNotNull(est.getPrograma());
        }
    }
    
    @Test
    @DisplayName("Actualización de estado del proyecto")
    void testActualizacionEstadoProyecto() {
        proyecto.setEstado("En segunda evaluación Formato A");
        proyecto.setFechaRevision("2024-01-25");
        
        assertEquals("En segunda evaluación Formato A", proyecto.getEstado());
        assertEquals("2024-01-25", proyecto.getFechaRevision());
    }
}
