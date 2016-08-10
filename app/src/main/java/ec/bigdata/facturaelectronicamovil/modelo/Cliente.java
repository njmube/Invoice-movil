package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 6/5/2016.
 */
public class Cliente implements java.io.Serializable {


    private Integer idCliente;
    private String identificacionCliente;
    private String nombreCliente;
    private String apellidoCliente;
    private String razonSocialCliente;
    private String direccionCliente;
    private String correoElectronicoCliente;
    private String telefonoCliente;
    private boolean estadoCliente;
    private Boolean tipoCliente;

    public Cliente() {
    }

    public Integer getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }


    public String getIdentificacionCliente() {
        return this.identificacionCliente;
    }

    public void setIdentificacionCliente(String identificacionCliente) {
        this.identificacionCliente = identificacionCliente;
    }

    public String getNombreCliente() {
        return this.nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccionCliente() {
        return this.direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getCorreoElectronicoCliente() {
        return this.correoElectronicoCliente;
    }

    public void setCorreoElectronicoCliente(String correoElectronicoCliente) {
        this.correoElectronicoCliente = correoElectronicoCliente;
    }

    public String getTelefonoCliente() {
        return this.telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public boolean isEstadoCliente() {
        return this.estadoCliente;
    }

    public void setEstadoCliente(boolean estadoCliente) {
        this.estadoCliente = estadoCliente;
    }


    public String getApellidoCliente() {
        return apellidoCliente;
    }

    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    public Boolean getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(Boolean tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getRazonSocialCliente() {
        return razonSocialCliente;
    }

    public void setRazonSocialCliente(String razonSocialCliente) {
        this.razonSocialCliente = razonSocialCliente;
    }
}


