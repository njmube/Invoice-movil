package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 22/7/2016.
 */
public class Archivo implements java.io.Serializable {

    private Integer idArchivo;
    private String respuestaSricomprobanteElectronicoArchivo;
    private String comprobanteFirmadoComprobanteElectronicoArchivo;

    public Archivo() {
    }

    public Archivo(String respuestaSricomprobanteElectronicoArchivo, String comprobanteFirmadoComprobanteElectronicoArchivo) {

        this.respuestaSricomprobanteElectronicoArchivo = respuestaSricomprobanteElectronicoArchivo;
        this.comprobanteFirmadoComprobanteElectronicoArchivo = comprobanteFirmadoComprobanteElectronicoArchivo;
    }

    public Integer getIdArchivo() {
        return this.idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public String getRespuestaSricomprobanteElectronicoArchivo() {
        return respuestaSricomprobanteElectronicoArchivo;
    }

    public void setRespuestaSricomprobanteElectronicoArchivo(String respuestaSricomprobanteElectronicoArchivo) {
        this.respuestaSricomprobanteElectronicoArchivo = respuestaSricomprobanteElectronicoArchivo;
    }

    public String getComprobanteFirmadoComprobanteElectronicoArchivo() {
        return comprobanteFirmadoComprobanteElectronicoArchivo;
    }

    public void setComprobanteFirmadoComprobanteElectronicoArchivo(String comprobanteFirmadoComprobanteElectronicoArchivo) {
        this.comprobanteFirmadoComprobanteElectronicoArchivo = comprobanteFirmadoComprobanteElectronicoArchivo;
    }
}