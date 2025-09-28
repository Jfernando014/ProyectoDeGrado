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
 * Pruebas para ProyectoService con validaciones
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas de ProyectoService")
class ProyectoServiceTest {
    
    @Mock
    private ProyectoRepository proyectoRepository;
    
    @InjectMocks
    private ProyectoService proyectoService;
    
    private FormatoA formatoAValido;
    private Estudiante estudianteValido;
    
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
        
        Profesor profesor = new Profesor(departamento, "Ana", "Gómez", "3226669999", "ana@unicauca.edu.co", "pass");
        Coordinador coordinador = new Coordinador(departamento, "Luis", "Martínez", "3337770000", "luis@unicauca.edu.co", "pass");
        
        List<Estudiante> estudiantes = Arrays.asList(estudianteValido);
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
    @DisplayName("Subir formato exitoso con datos válidos")
    void testSubirFormatoExitoso() throws Exception {
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(true);
        
        boolean resultado = proyectoService.subirFormato(formatoAValido);
        
        assertTrue(resultado);
        verify(proyectoRepository, times(1)).save(formatoAValido);
    }
    
    @Test
    @DisplayName("Subir formato fallido - formato nulo")
    void testSubirFormatoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            proyectoService.subirFormato(null);
        });
    }
    
    @Test
    @DisplayName("Subir formato fallido - título inválido")
    void testSubirFormatoTituloInvalido() {
        formatoAValido.setTitulo("AB"); // Título muy corto
        
        assertThrows(IllegalArgumentException.class, () -> {
            proyectoService.subirFormato(formatoAValido);
        });
    }
    
    @Test
    @DisplayName("Subir formato fallido - sin estudiantes")
    void testSubirFormatoSinEstudiantes() {
        formatoAValido.setEstudiantes(Arrays.asList());
        
        assertThrows(IllegalArgumentException.class, () -> {
            proyectoService.subirFormato(formatoAValido);
        });
    }
    
    @Test
    @DisplayName("Subir formato fallido - archivo no PDF")
    void testSubirFormatoArchivoNoPDF() {
        formatoAValido.setArchivoAdjunto("documento.txt");
        
        assertThrows(IllegalArgumentException.class, () -> {
            proyectoService.subirFormato(formatoAValido);
        });
    }
    
    @Test
    @DisplayName("Aprobar formato exitoso")
    void testAprobarFormatoExitoso() throws Exception {
        formatoAValido.setEstado("En tercera evaluación Formato A");
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(true);
        
        boolean resultado = proyectoService.aprobarFormato(formatoAValido, "2024-01-30");
        
        assertTrue(resultado);
        assertEquals("Aprobado Formato A", formatoAValido.getEstado());
        assertEquals("2024-01-30", formatoAValido.getFechaRevision());
    }
    
    @Test
    @DisplayName("Aprobar formato fallido - estado incorrecto")
    void testAprobarFormatoEstadoIncorrecto() {
        formatoAValido.setEstado("Pendiente de revisión");
        
        assertThrows(IllegalStateException.class, () -> {
            proyectoService.aprobarFormato(formatoAValido, "2024-01-30");
        });
    }
    
    @Test
    @DisplayName("Rechazar formato exitoso")
    void testRechazarFormatoExitoso() throws Exception {
        formatoAValido.setEstado("En primera evaluación Formato A");
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(true);
        
        boolean resultado = proyectoService.rechazarFormato(formatoAValido, "2024-01-25", "Falta documentación");
        
        assertTrue(resultado);
        assertEquals("Rechazado Formato A", formatoAValido.getEstado());
        assertEquals("2024-01-25", formatoAValido.getFechaRevision());
    }
    
    @Test
    @DisplayName("Avanzar estado exitoso")
    void testAvanzarEstadoExitoso() throws Exception {
        formatoAValido.setEstado("Pendiente de revisión");
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(true);
        
        boolean resultado = proyectoService.avanzarEstado(formatoAValido);
        
        assertTrue(resultado);
        assertEquals("En primera evaluación Formato A", formatoAValido.getEstado());
    }
    
    @Test
    @DisplayName("Avanzar estado fallido - estado final")
    void testAvanzarEstadoEstadoFinal() {
        formatoAValido.setEstado("Aprobado Formato A");
        
        assertThrows(IllegalStateException.class, () -> {
            proyectoService.avanzarEstado(formatoAValido);
        });
    }
    
    @Test
    @DisplayName("Subir formato fallido - guardado en repositorio falla")
    void testSubirFormatoGuardadoFallido() throws Exception {
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(false);
        
        boolean resultado = proyectoService.subirFormato(formatoAValido);
        
        assertFalse(resultado);
        verify(proyectoRepository, times(1)).save(formatoAValido);
    }
    
    @Test
    @DisplayName("Rechazar formato fallido - guardado en repositorio falla")
    void testRechazarFormatoGuardadoFallido() throws Exception {
        formatoAValido.setEstado("En primera evaluación Formato A");
        when(proyectoRepository.save(any(FormatoA.class))).thenReturn(false);
        
        boolean resultado = proyectoService.rechazarFormato(formatoAValido, "2024-01-25", "Falta documentación");
        
        assertFalse(resultado);
        verify(proyectoRepository, times(1)).save(formatoAValido);
    }
}