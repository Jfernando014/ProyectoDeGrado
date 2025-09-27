package co.edu.unicauca.Models;
/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class Facultad
{   
    private int codigoFacultad;
    private String nombreFacultad;

    public Facultad(int codigoFacultad, String nombreFacultad) {
        this.codigoFacultad = codigoFacultad;
        this.nombreFacultad = nombreFacultad;
    }

    public Facultad(int codigoFacultad) {
        this.codigoFacultad = codigoFacultad;
    }
    
    public int getCodigoFacultad() {
        return codigoFacultad;
    }

    public void setCodigoFacultad(int codigoFacultad) {
        this.codigoFacultad = codigoFacultad;
    }

    public String getNombreFacultad() {
        return nombreFacultad;
    }

    public void setNombreFacultad(String nombreFacultad) {
        this.nombreFacultad = nombreFacultad;
    }
    
} 