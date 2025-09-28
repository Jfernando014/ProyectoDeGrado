package UnitTest;

import co.edu.unicauca.Services.ProyectoService;
import co.edu.unicauca.Models.FormatoA;
import co.edu.unicauca.Models.Estudiante;
import co.edu.unicauca.Models.Profesor;
import co.edu.unicauca.Models.Coordinador;
import co.edu.unicauca.Models.Programa;
import co.edu.unicauca.Models.Departamento;
import co.edu.unicauca.Util.Tipo;
import co.edu.unicauca.Repository.ProyectoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * PRUEBA: Yo como un docente necesito pruebas unitarias de persistencia, 
 * con la finalidad de validar que la información se guarde correctamente.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de Docente - Persistencia Formato A")
class DocenteTest {
    
    @Mock
    private ProyectoRepository proyectoRepository;
    
    @InjectMocks
    private ProyectoService proyectoService;
    
    private FormatoA formatoAValido;
    
    @BeforeEach
    void setUp() {
        Programa programa = new Programa(1, "Ingeniería de Sistemas");
        Departamento departamento = new Departamento(1, "Sistemas");
        
        Estudiante estudiante = new Estudiante(programa, "Carlos", "Ramírez", "3115558888", "carlos@unicauca.edu.co", "pass");
        Profesor profesor = new Profesor(departamento, "Ana", "Gómez", "3226669999", "ana@unicauca.edu.co", "pass");
        Coordinador coordinador = new Coordinador(departamento, "Luis", "Martínez", "3337770000", "luis@unicauca.edu.co", "pass");
        
        List<Estudiante> estudiantes = Arrays.asList(estudiante);
        List<Profesor> profesores = Arrays.asList(profesor);
        
        formatoAValido = new FormatoA(
            1,
            "Sistema de Gestión Académica",
            "Automatizar procesos académicos de la universidad",
            "Implementar módulos de registro, calificaciones y seguimiento",
            "Pendiente de revisión",
            Tipo.Investigacion,
            "2024-01-15",
            null,
            "documento.pdf",
            estudiantes,
            profesores,
            coordinador
        );
    }
    
    @Test
    @DisplayName("Guardado completo de Formato A con todos los datos")
    void testGuardadoCompletoFormatoA() throws Exception {
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(true);
        
        boolean resultado = proyectoService.subirFormato(formatoAValido);
        
        assertTrue(resultado);
        verify(proyectoRepository, times(1)).save(formatoAValido);
    }
    
    @Test
    @DisplayName("Validar integridad de datos en Formato A")
    void testIntegridadDatosFormatoA() {
        // Verificar todos los campos
        assertEquals(1, formatoAValido.getIdProyecto());
        assertEquals("Sistema de Gestión Académica", formatoAValido.getTitulo());
        assertEquals("Automatizar procesos académicos de la universidad", formatoAValido.getObjetivo());
        assertEquals("Implementar módulos de registro, calificaciones y seguimiento", formatoAValido.getObjetivoEspecifico());
        assertEquals("Pendiente de revisión", formatoAValido.getEstado());
        assertEquals(Tipo.Investigacion, formatoAValido.getTipo());
        assertEquals("2024-01-15", formatoAValido.getFechaDeSubida());
        assertEquals("documento.pdf", formatoAValido.getArchivoAdjunto());
        assertEquals(1, formatoAValido.getEstudiantes().size());
        assertEquals(1, formatoAValido.getProfesores().size());
        assertNotNull(formatoAValido.getCoordinador());
    }
    
    @Test
    @DisplayName("Guardado fallido de Formato A - Error en repositorio")
    void testGuardadoFallidoFormatoA() throws Exception {
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(false);
        
        boolean resultado = proyectoService.subirFormato(formatoAValido);
        
        assertFalse(resultado);
        verify(proyectoRepository, times(1)).save(formatoAValido);
    }
    
    @Test
    @DisplayName("Formato A con diferentes tipos de proyecto")
    void testFormatoAConDiferentesTipos() throws Exception {
        // Proyecto de Investigación
        FormatoA formatoInvestigacion = new FormatoA(
            "Proyecto Investigación",
            "Objetivo investigación",
            "Objetivo específico investigación",
            "Pendiente de revisión",
            Tipo.Investigacion,
            "2024-01-15",
            "investigacion.pdf",
            formatoAValido.getEstudiantes(),
            formatoAValido.getProfesores()
        );
        
        // Proyecto de Práctica Laboral
        FormatoA formatoPractica = new FormatoA(
            "Proyecto Práctica",
            "Objetivo práctica",
            "Objetivo específico práctica", 
            "Pendiente de revisión",
            Tipo.PracticaProfesional,
            "2024-01-15",
            "practica.pdf",
            formatoAValido.getEstudiantes(),
            formatoAValido.getProfesores()
        );
        
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(true);
        
       
        boolean resultado1 = proyectoService.subirFormato(formatoInvestigacion);
        boolean resultado2 = proyectoService.subirFormato(formatoPractica);
        
        assertTrue(resultado1);
        assertTrue(resultado2);
        verify(proyectoRepository, times(2)).save(any(FormatoA.class));
    }
}
