package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 22/7/2016.
 */
public class Archivo implements java.io.Serializable {

    private Integer idArchivo;
    private byte[] respuestaSricomprobanteElectronicoArchivo;
    private byte[] comprobanteFirmadoComprobanteElectronicoArchivo;

    public Archivo() {
    }

    public Archivo(byte[] respuestaSricomprobanteElectronicoArchivo, byte[] comprobanteFirmadoComprobanteElectronicoArchivo) {

        this.respuestaSricomprobanteElectronicoArchivo = respuestaSricomprobanteElectronicoArchivo;
        this.comprobanteFirmadoComprobanteElectronicoArchivo = comprobanteFirmadoComprobanteElectronicoArchivo;
    }

    public Integer getIdArchivo() {
        return this.idArchivo;
    }

    public void setIdArchivo(Integer idArchivo) {
        this.idArchivo = idArchivo;
    }

    public byte[] getRespuestaSricomprobanteElectronicoArchivo() {
        return this.respuestaSricomprobanteElectronicoArchivo;
    }

    public void setRespuestaSricomprobanteElectronicoArchivo(byte[] respuestaSricomprobanteElectronicoArchivo) {
        this.respuestaSricomprobanteElectronicoArchivo = respuestaSricomprobanteElectronicoArchivo;
    }

    public byte[] getComprobanteFirmadoComprobanteElectronicoArchivo() {
        return this.comprobanteFirmadoComprobanteElectronicoArchivo;
    }

    public void setComprobanteFirmadoComprobanteElectronicoArchivo(byte[] comprobanteFirmadoComprobanteElectronicoArchivo) {
        this.comprobanteFirmadoComprobanteElectronicoArchivo = comprobanteFirmadoComprobanteElectronicoArchivo;
    }
}