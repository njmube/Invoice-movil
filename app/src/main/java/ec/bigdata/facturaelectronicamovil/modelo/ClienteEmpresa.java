package ec.bigdata.facturaelectronicamovil.modelo;

import java.util.Date;

/**
 * Created by DavidLeonardo on 28/4/2016.
 */
public class ClienteEmpresa implements java.io.Serializable {


    private String idClienteEmpresa;
    private Perfil perfil;
    private String nombreUsuarioClienteEmpresa;
    private String claveUsuarioClienteEmpresa;
    private String nombreComercialClienteEmpresa;
    private String razonSocialClienteEmpresa;
    private String direccionClienteEmpresa;
    private String correoPrincipalClienteEmpresa;
    private String telefonoPrincipalClienteEmpresa;
    private Boolean obligadoContabilidadClienteEmpresa;
    private String numeroResolucionClienteEmpresa;
    private byte[] logoClienteEmpresa;
    private Date fechaRegistroClienteEmpresa;
    private Boolean estadoClienteEmpresa;
    private String tipoUsuarioClienteEmpresa;


    public ClienteEmpresa() {
    }


    public ClienteEmpresa(String idClienteEmpresa, String nombreUsuarioClienteEmpresa, String claveUsuarioClienteEmpresa) {
        this.idClienteEmpresa = idClienteEmpresa;
        this.nombreUsuarioClienteEmpresa = nombreUsuarioClienteEmpresa;
        this.claveUsuarioClienteEmpresa = claveUsuarioClienteEmpresa;
    }

    public ClienteEmpresa(String idClienteEmpresa, String nombreUsuarioClienteEmpresa, String claveUsuarioClienteEmpresa, String nombreComercialClienteEmpresa, String razonSocialClienteEmpresa, String direccionClienteEmpresa, String correoPrincipalClienteEmpresa, String telefonoPrincipalClienteEmpresa, Boolean obligadoContabilidadClienteEmpresa, String numeroResolucionClienteEmpresa, byte[] logoClienteEmpresa, Date fechaRegistroClienteEmpresa, Boolean estadoClienteEmpresa, String tipoUsuarioClienteEmpresa) {
        this.idClienteEmpresa = idClienteEmpresa;
        this.nombreUsuarioClienteEmpresa = nombreUsuarioClienteEmpresa;
        this.claveUsuarioClienteEmpresa = claveUsuarioClienteEmpresa;
        this.nombreComercialClienteEmpresa = nombreComercialClienteEmpresa;
        this.razonSocialClienteEmpresa = razonSocialClienteEmpresa;
        this.direccionClienteEmpresa = direccionClienteEmpresa;
        this.correoPrincipalClienteEmpresa = correoPrincipalClienteEmpresa;
        this.telefonoPrincipalClienteEmpresa = telefonoPrincipalClienteEmpresa;
        this.obligadoContabilidadClienteEmpresa = obligadoContabilidadClienteEmpresa;
        this.numeroResolucionClienteEmpresa = numeroResolucionClienteEmpresa;
        this.logoClienteEmpresa = logoClienteEmpresa;
        this.fechaRegistroClienteEmpresa = fechaRegistroClienteEmpresa;
        this.estadoClienteEmpresa = estadoClienteEmpresa;
        this.tipoUsuarioClienteEmpresa = tipoUsuarioClienteEmpresa;

    }

    public String getIdClienteEmpresa() {
        return this.idClienteEmpresa;
    }

    public void setIdClienteEmpresa(String idClienteEmpresa) {
        this.idClienteEmpresa = idClienteEmpresa;
    }

    public Perfil getPerfil() {
        return this.perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public String getNombreUsuarioClienteEmpresa() {
        return this.nombreUsuarioClienteEmpresa;
    }

    public void setNombreUsuarioClienteEmpresa(String nombreUsuarioClienteEmpresa) {
        this.nombreUsuarioClienteEmpresa = nombreUsuarioClienteEmpresa;
    }

    public String getClaveUsuarioClienteEmpresa() {
        return this.claveUsuarioClienteEmpresa;
    }

    public void setClaveUsuarioClienteEmpresa(String claveUsuarioClienteEmpresa) {
        this.claveUsuarioClienteEmpresa = claveUsuarioClienteEmpresa;
    }

    public String getNombreComercialClienteEmpresa() {
        return this.nombreComercialClienteEmpresa;
    }

    public void setNombreComercialClienteEmpresa(String nombreComercialClienteEmpresa) {
        this.nombreComercialClienteEmpresa = nombreComercialClienteEmpresa;
    }

    public String getRazonSocialClienteEmpresa() {
        return this.razonSocialClienteEmpresa;
    }

    public void setRazonSocialClienteEmpresa(String razonSocialClienteEmpresa) {
        this.razonSocialClienteEmpresa = razonSocialClienteEmpresa;
    }

    public String getDireccionClienteEmpresa() {
        return this.direccionClienteEmpresa;
    }

    public void setDireccionClienteEmpresa(String direccionClienteEmpresa) {
        this.direccionClienteEmpresa = direccionClienteEmpresa;
    }

    public String getCorreoPrincipalClienteEmpresa() {
        return this.correoPrincipalClienteEmpresa;
    }

    public void setCorreoPrincipalClienteEmpresa(String correoPrincipalClienteEmpresa) {
        this.correoPrincipalClienteEmpresa = correoPrincipalClienteEmpresa;
    }

    public String getTelefonoPrincipalClienteEmpresa() {
        return this.telefonoPrincipalClienteEmpresa;
    }

    public void setTelefonoPrincipalClienteEmpresa(String telefonoPrincipalClienteEmpresa) {
        this.telefonoPrincipalClienteEmpresa = telefonoPrincipalClienteEmpresa;
    }

    public Boolean getObligadoContabilidadClienteEmpresa() {
        return this.obligadoContabilidadClienteEmpresa;
    }

    public void setObligadoContabilidadClienteEmpresa(Boolean obligadoContabilidadClienteEmpresa) {
        this.obligadoContabilidadClienteEmpresa = obligadoContabilidadClienteEmpresa;
    }

    public String getNumeroResolucionClienteEmpresa() {
        return this.numeroResolucionClienteEmpresa;
    }

    public void setNumeroResolucionClienteEmpresa(String numeroResolucionClienteEmpresa) {
        this.numeroResolucionClienteEmpresa = numeroResolucionClienteEmpresa;
    }

    public byte[] getLogoClienteEmpresa() {
        return this.logoClienteEmpresa;
    }

    public void setLogoClienteEmpresa(byte[] logoClienteEmpresa) {
        this.logoClienteEmpresa = logoClienteEmpresa;
    }

    public Date getFechaRegistroClienteEmpresa() {
        return this.fechaRegistroClienteEmpresa;
    }

    public void setFechaRegistroClienteEmpresa(Date fechaRegistroClienteEmpresa) {
        this.fechaRegistroClienteEmpresa = fechaRegistroClienteEmpresa;
    }

    public Boolean getEstadoClienteEmpresa() {
        return this.estadoClienteEmpresa;
    }

    public void setEstadoClienteEmpresa(Boolean estadoClienteEmpresa) {
        this.estadoClienteEmpresa = estadoClienteEmpresa;
    }

    public String getTipoUsuarioClienteEmpresa() {
        return this.tipoUsuarioClienteEmpresa;
    }

    public void setTipoUsuarioClienteEmpresa(String tipoUsuarioClienteEmpresa) {
        this.tipoUsuarioClienteEmpresa = tipoUsuarioClienteEmpresa;
    }

}
