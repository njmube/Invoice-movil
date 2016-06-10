package ec.bigdata.facturaelectronicamovil.modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DavidLeonardo on 25/4/2016.
 */
public class Ambiente implements java.io.Serializable {


    private int idAmbiente;
    private String tipoAmbiente;
    private Set asignacionConfiguracions = new HashSet(0);

    public Ambiente() {
    }


    public Ambiente(int idAmbiente, String tipoAmbiente) {
        this.idAmbiente = idAmbiente;
        this.tipoAmbiente = tipoAmbiente;
    }

    public Ambiente(int idAmbiente, String tipoAmbiente, Set asignacionConfiguracions) {
        this.idAmbiente = idAmbiente;
        this.tipoAmbiente = tipoAmbiente;
        this.asignacionConfiguracions = asignacionConfiguracions;
    }

    public int getIdAmbiente() {
        return this.idAmbiente;
    }

    public void setIdAmbiente(int idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    public String getTipoAmbiente() {
        return this.tipoAmbiente;
    }

    public void setTipoAmbiente(String tipoAmbiente) {
        this.tipoAmbiente = tipoAmbiente;
    }

    public Set getAsignacionConfiguracions() {
        return this.asignacionConfiguracions;
    }

    public void setAsignacionConfiguracions(Set asignacionConfiguracions) {
        this.asignacionConfiguracions = asignacionConfiguracions;
    }


}
