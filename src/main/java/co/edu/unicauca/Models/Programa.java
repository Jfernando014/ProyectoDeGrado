package co.edu.unicauca.Models;

import co.edu.unicauca.Models.Departamento;
/**
 *
 * @author J.Fernando
 * @author Fabian Dorado
 * @author Karzo
 */
public class Programa
{
    private int codigoDePrograma;
    private String nombrePrograma;
    private Departamento departamento;

    public Programa(int codigoDePrograma, String nombrePrograma, Departamento departamento) {
        this.codigoDePrograma = codigoDePrograma;
        this.nombrePrograma = nombrePrograma;
        this.departamento = departamento;
    }

    public Programa(int codigoDePrograma, String nombrePrograma) {
        this.codigoDePrograma = codigoDePrograma;
        this.nombrePrograma = nombrePrograma;
    }

    public Programa() {
    }

    public Programa(int codigoDePrograma) {
        this.codigoDePrograma = codigoDePrograma;
    }
    
    public int getCodigoDePrograma() {
        return codigoDePrograma;
    }

    public void setCodigoDePrograma(int codigoDePrograma) {
        this.codigoDePrograma = codigoDePrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    

}