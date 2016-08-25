package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 2/8/2016.
 */
public class CorreoAdicional implements java.io.Serializable {

    private Integer idCorreoAdicional;

    private String correoElectronicoCorreoAdicional;
    private Boolean tipoClienteCorreoAdicional;

    public CorreoAdicional() {
    }

    public CorreoAdicional(Integer idCorreoAdicional, String correoElectronicoCorreoAdicional, Boolean tipoClienteCorreoAdicional) {
        this.idCorreoAdicional = idCorreoAdicional;
        this.correoElectronicoCorreoAdicional = correoElectronicoCorreoAdicional;
        this.tipoClienteCorreoAdicional = tipoClienteCorreoAdicional;
    }

    public Integer getIdCorreoAdicional() {
        return this.idCorreoAdicional;
    }

    public void setIdCorreoAdicional(Integer idCorreoAdicional) {
        this.idCorreoAdicional = idCorreoAdicional;
    }

    public String getCorreoElectronicoCorreoAdicional() {
        return this.correoElectronicoCorreoAdicional;
    }

    public void setCorreoElectronicoCorreoAdicional(String correoElectronicoCorreoAdicional) {
        this.correoElectronicoCorreoAdicional = correoElectronicoCorreoAdicional;
    }

    public Boolean getTipoClienteCorreoAdicional() {
        return this.tipoClienteCorreoAdicional;
    }

    public void setTipoClienteCorreoAdicional(Boolean tipoClienteCorreoAdicional) {
        this.tipoClienteCorreoAdicional = tipoClienteCorreoAdicional;
    }

}
