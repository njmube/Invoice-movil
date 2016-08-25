package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class ComprobanteElectronico implements java.io.Serializable {

    private Integer idComprobanteElectronico;
    private String tipoComprobanteElectronico;
    private String claveAccesoComprobanteElectronico;
    private String codigoEstablecimientoComprobanteElectronico;
    private String puntoEmisionComprobanteElectronico;
    private String secuencialComprobanteElectronico;
    private String numeroAutorizacionComprobanteElectronico;
    private String rucEmisorComprobanteElectronico;
    private String razonSocialEmisorComprobanteElectronico;
    private String ambienteComprobanteElectronico;
    private String estadoComprobanteElectronico;
    private String mensajeComprobanteElectronico;
    private String fechaEmisionComprobanteElectronico;
    private String fechaAutorizacionComprobanteElectronico;
    private String tipoPagoComprobanteElectronico;
    private String rucReceptor;
    private String razonSocialReceptor;

    public ComprobanteElectronico() {
    }

    public ComprobanteElectronico(Integer idComprobanteElectronico, String tipoComprobanteElectronico, String claveAccesoComprobanteElectronico, String codigoEstablecimientoComprobanteElectronico, String puntoEmisionComprobanteElectronico, String secuencialComprobanteElectronico, String numeroAutorizacionComprobanteElectronico, String rucEmisorComprobanteElectronico, String razonSocialEmisorComprobanteElectronico, String ambienteComprobanteElectronico, String estadoComprobanteElectronico, String mensajeComprobanteElectronico, String fechaEmisionComprobanteElectronico, String fechaAutorizacionComprobanteElectronico, String tipoPagoComprobanteElectronico, String rucReceptor, String razonSocialReceptor) {
        this.idComprobanteElectronico = idComprobanteElectronico;
        this.tipoComprobanteElectronico = tipoComprobanteElectronico;
        this.claveAccesoComprobanteElectronico = claveAccesoComprobanteElectronico;
        this.codigoEstablecimientoComprobanteElectronico = codigoEstablecimientoComprobanteElectronico;
        this.puntoEmisionComprobanteElectronico = puntoEmisionComprobanteElectronico;
        this.secuencialComprobanteElectronico = secuencialComprobanteElectronico;
        this.numeroAutorizacionComprobanteElectronico = numeroAutorizacionComprobanteElectronico;
        this.rucEmisorComprobanteElectronico = rucEmisorComprobanteElectronico;
        this.razonSocialEmisorComprobanteElectronico = razonSocialEmisorComprobanteElectronico;
        this.ambienteComprobanteElectronico = ambienteComprobanteElectronico;
        this.estadoComprobanteElectronico = estadoComprobanteElectronico;
        this.mensajeComprobanteElectronico = mensajeComprobanteElectronico;
        this.fechaEmisionComprobanteElectronico = fechaEmisionComprobanteElectronico;
        this.fechaAutorizacionComprobanteElectronico = fechaAutorizacionComprobanteElectronico;
        this.tipoPagoComprobanteElectronico = tipoPagoComprobanteElectronico;
        this.rucReceptor = rucReceptor;
        this.razonSocialReceptor = razonSocialReceptor;
    }

    public Integer getIdComprobanteElectronico() {
        return this.idComprobanteElectronico;
    }

    public void setIdComprobanteElectronico(Integer idComprobanteElectronico) {
        this.idComprobanteElectronico = idComprobanteElectronico;
    }

    public String getTipoComprobanteElectronico() {
        return this.tipoComprobanteElectronico;
    }

    public void setTipoComprobanteElectronico(String tipoComprobanteElectronico) {
        this.tipoComprobanteElectronico = tipoComprobanteElectronico;
    }

    public String getClaveAccesoComprobanteElectronico() {
        return this.claveAccesoComprobanteElectronico;
    }

    public void setClaveAccesoComprobanteElectronico(String claveAccesoComprobanteElectronico) {
        this.claveAccesoComprobanteElectronico = claveAccesoComprobanteElectronico;
    }

    public String getCodigoEstablecimientoComprobanteElectronico() {
        return this.codigoEstablecimientoComprobanteElectronico;
    }

    public void setCodigoEstablecimientoComprobanteElectronico(String codigoEstablecimientoComprobanteElectronico) {
        this.codigoEstablecimientoComprobanteElectronico = codigoEstablecimientoComprobanteElectronico;
    }

    public String getPuntoEmisionComprobanteElectronico() {
        return this.puntoEmisionComprobanteElectronico;
    }

    public void setPuntoEmisionComprobanteElectronico(String puntoEmisionComprobanteElectronico) {
        this.puntoEmisionComprobanteElectronico = puntoEmisionComprobanteElectronico;
    }

    public String getSecuencialComprobanteElectronico() {
        return this.secuencialComprobanteElectronico;
    }

    public void setSecuencialComprobanteElectronico(String secuencialComprobanteElectronico) {
        this.secuencialComprobanteElectronico = secuencialComprobanteElectronico;
    }

    public String getNumeroAutorizacionComprobanteElectronico() {
        return this.numeroAutorizacionComprobanteElectronico;
    }

    public void setNumeroAutorizacionComprobanteElectronico(String numeroAutorizacionComprobanteElectronico) {
        this.numeroAutorizacionComprobanteElectronico = numeroAutorizacionComprobanteElectronico;
    }

    public String getRucEmisorComprobanteElectronico() {
        return this.rucEmisorComprobanteElectronico;
    }

    public void setRucEmisorComprobanteElectronico(String rucEmisorComprobanteElectronico) {
        this.rucEmisorComprobanteElectronico = rucEmisorComprobanteElectronico;
    }

    public String getRazonSocialEmisorComprobanteElectronico() {
        return razonSocialEmisorComprobanteElectronico;
    }

    public void setRazonSocialEmisorComprobanteElectronico(String razonSocialEmisorComprobanteElectronico) {
        this.razonSocialEmisorComprobanteElectronico = razonSocialEmisorComprobanteElectronico;
    }

    public String getAmbienteComprobanteElectronico() {
        return this.ambienteComprobanteElectronico;
    }

    public void setAmbienteComprobanteElectronico(String ambienteComprobanteElectronico) {
        this.ambienteComprobanteElectronico = ambienteComprobanteElectronico;
    }

    public String getEstadoComprobanteElectronico() {
        return this.estadoComprobanteElectronico;
    }

    public void setEstadoComprobanteElectronico(String estadoComprobanteElectronico) {
        this.estadoComprobanteElectronico = estadoComprobanteElectronico;
    }

    public String getMensajeComprobanteElectronico() {
        return this.mensajeComprobanteElectronico;
    }

    public void setMensajeComprobanteElectronico(String mensajeComprobanteElectronico) {
        this.mensajeComprobanteElectronico = mensajeComprobanteElectronico;
    }

    public String getFechaEmisionComprobanteElectronico() {
        return fechaEmisionComprobanteElectronico;
    }

    public void setFechaEmisionComprobanteElectronico(String fechaEmisionComprobanteElectronico) {
        this.fechaEmisionComprobanteElectronico = fechaEmisionComprobanteElectronico;
    }

    public String getFechaAutorizacionComprobanteElectronico() {
        return fechaAutorizacionComprobanteElectronico;
    }

    public void setFechaAutorizacionComprobanteElectronico(String fechaAutorizacionComprobanteElectronico) {
        this.fechaAutorizacionComprobanteElectronico = fechaAutorizacionComprobanteElectronico;
    }

    public String getTipoPagoComprobanteElectronico() {
        return this.tipoPagoComprobanteElectronico;
    }

    public void setTipoPagoComprobanteElectronico(String tipoPagoComprobanteElectronico) {
        this.tipoPagoComprobanteElectronico = tipoPagoComprobanteElectronico;
    }

    public String getRazonSocialReceptor() {
        return razonSocialReceptor;
    }

    public void setRazonSocialReceptor(String razonSocialReceptor) {
        this.razonSocialReceptor = razonSocialReceptor;
    }

    public String getRucReceptor() {
        return rucReceptor;
    }

    public void setRucReceptor(String rucReceptor) {
        this.rucReceptor = rucReceptor;
    }
}
