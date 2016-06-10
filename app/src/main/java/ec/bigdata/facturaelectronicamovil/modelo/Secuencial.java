package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 4/5/2016.
 */
public class Secuencial implements java.io.Serializable {


    private Integer idSecuencial;
    private ClienteEmpresa clienteEmpresa;
    private String secuencialFacturaSecuencial;
    private String secuencialNotaCreditoSecuencial;
    private String secuencialNotaDebitoSecuencial;
    private String secuencialGuiaRemisionSecuencial;
    private String secuencialRetencionSecuencial;
    private String puntoEmisionSecuencial;
    private String codigoEstablecimientoSecuencial;
    private String direccionSecuencial;
    private Boolean estadoSecuencial;
    private String ambienteSecuencial;

    public Secuencial() {
    }


    public Secuencial(ClienteEmpresa clienteEmpresa) {
        this.clienteEmpresa = clienteEmpresa;
    }

    public Secuencial(ClienteEmpresa clienteEmpresa, String secuencialFacturaSecuencial, String secuencialNotaCreditoSecuencial, String secuencialNotaDebitoSecuencial, String secuencialGuiaRemisionSecuencial, String secuencialRetencionSecuencial, String puntoEmisionSecuencial, String codigoEstablecimientoSecuencial, String direccionSecuencial, Boolean estadoSecuencial, String ambienteSecuencial) {
        this.clienteEmpresa = clienteEmpresa;
        this.secuencialFacturaSecuencial = secuencialFacturaSecuencial;
        this.secuencialNotaCreditoSecuencial = secuencialNotaCreditoSecuencial;
        this.secuencialNotaDebitoSecuencial = secuencialNotaDebitoSecuencial;
        this.secuencialGuiaRemisionSecuencial = secuencialGuiaRemisionSecuencial;
        this.secuencialRetencionSecuencial = secuencialRetencionSecuencial;
        this.puntoEmisionSecuencial = puntoEmisionSecuencial;
        this.codigoEstablecimientoSecuencial = codigoEstablecimientoSecuencial;
        this.direccionSecuencial = direccionSecuencial;
        this.estadoSecuencial = estadoSecuencial;
        this.ambienteSecuencial = ambienteSecuencial;
    }

    public Integer getIdSecuencial() {
        return this.idSecuencial;
    }

    public void setIdSecuencial(Integer idSecuencial) {
        this.idSecuencial = idSecuencial;
    }

    public ClienteEmpresa getClienteEmpresa() {
        return this.clienteEmpresa;
    }

    public void setClienteEmpresa(ClienteEmpresa clienteEmpresa) {
        this.clienteEmpresa = clienteEmpresa;
    }

    public String getSecuencialFacturaSecuencial() {
        return this.secuencialFacturaSecuencial;
    }

    public void setSecuencialFacturaSecuencial(String secuencialFacturaSecuencial) {
        this.secuencialFacturaSecuencial = secuencialFacturaSecuencial;
    }

    public String getSecuencialNotaCreditoSecuencial() {
        return this.secuencialNotaCreditoSecuencial;
    }

    public void setSecuencialNotaCreditoSecuencial(String secuencialNotaCreditoSecuencial) {
        this.secuencialNotaCreditoSecuencial = secuencialNotaCreditoSecuencial;
    }

    public String getSecuencialNotaDebitoSecuencial() {
        return this.secuencialNotaDebitoSecuencial;
    }

    public void setSecuencialNotaDebitoSecuencial(String secuencialNotaDebitoSecuencial) {
        this.secuencialNotaDebitoSecuencial = secuencialNotaDebitoSecuencial;
    }

    public String getSecuencialGuiaRemisionSecuencial() {
        return this.secuencialGuiaRemisionSecuencial;
    }

    public void setSecuencialGuiaRemisionSecuencial(String secuencialGuiaRemisionSecuencial) {
        this.secuencialGuiaRemisionSecuencial = secuencialGuiaRemisionSecuencial;
    }

    public String getSecuencialRetencionSecuencial() {
        return this.secuencialRetencionSecuencial;
    }

    public void setSecuencialRetencionSecuencial(String secuencialRetencionSecuencial) {
        this.secuencialRetencionSecuencial = secuencialRetencionSecuencial;
    }

    public String getPuntoEmisionSecuencial() {
        return this.puntoEmisionSecuencial;
    }

    public void setPuntoEmisionSecuencial(String puntoEmisionSecuencial) {
        this.puntoEmisionSecuencial = puntoEmisionSecuencial;
    }

    public String getCodigoEstablecimientoSecuencial() {
        return this.codigoEstablecimientoSecuencial;
    }

    public void setCodigoEstablecimientoSecuencial(String codigoEstablecimientoSecuencial) {
        this.codigoEstablecimientoSecuencial = codigoEstablecimientoSecuencial;
    }

    public String getDireccionSecuencial() {
        return this.direccionSecuencial;
    }

    public void setDireccionSecuencial(String direccionSecuencial) {
        this.direccionSecuencial = direccionSecuencial;
    }

    public Boolean getEstadoSecuencial() {
        return this.estadoSecuencial;
    }

    public void setEstadoSecuencial(Boolean estadoSecuencial) {
        this.estadoSecuencial = estadoSecuencial;
    }

    public String getAmbienteSecuencial() {
        return this.ambienteSecuencial;
    }

    public void setAmbienteSecuencial(String ambienteSecuencial) {
        this.ambienteSecuencial = ambienteSecuencial;
    }
}