package ec.bigdata.facturaelectronicamovil.modelo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by DavidLeonardo on 26/4/2016.
 */
public class TipoComprobanteElectronico implements java.io.Serializable {


    private Integer idTipoComprobanteElectronico;
    private String codigoTipoComprobanteElectronico;
    private String nombreTipoComprobanteElectronico;
    private Set comprobanteElectronicos = new HashSet(0);

    public TipoComprobanteElectronico() {
    }


    public TipoComprobanteElectronico(String codigoTipoComprobanteElectronico, String nombreTipoComprobanteElectronico) {
        this.codigoTipoComprobanteElectronico = codigoTipoComprobanteElectronico;
        this.nombreTipoComprobanteElectronico = nombreTipoComprobanteElectronico;
    }

    public TipoComprobanteElectronico(String codigoTipoComprobanteElectronico, String nombreTipoComprobanteElectronico, Set comprobanteElectronicos) {
        this.codigoTipoComprobanteElectronico = codigoTipoComprobanteElectronico;
        this.nombreTipoComprobanteElectronico = nombreTipoComprobanteElectronico;
        this.comprobanteElectronicos = comprobanteElectronicos;
    }

    public Integer getIdTipoComprobanteElectronico() {
        return this.idTipoComprobanteElectronico;
    }

    public void setIdTipoComprobanteElectronico(Integer idTipoComprobanteElectronico) {
        this.idTipoComprobanteElectronico = idTipoComprobanteElectronico;
    }

    public String getCodigoTipoComprobanteElectronico() {
        return this.codigoTipoComprobanteElectronico;
    }

    public void setCodigoTipoComprobanteElectronico(String codigoTipoComprobanteElectronico) {
        this.codigoTipoComprobanteElectronico = codigoTipoComprobanteElectronico;
    }

    public String getNombreTipoComprobanteElectronico() {
        return this.nombreTipoComprobanteElectronico;
    }

    public void setNombreTipoComprobanteElectronico(String nombreTipoComprobanteElectronico) {
        this.nombreTipoComprobanteElectronico = nombreTipoComprobanteElectronico;
    }

    public Set getComprobanteElectronicos() {
        return this.comprobanteElectronicos;
    }

    public void setComprobanteElectronicos(Set comprobanteElectronicos) {
        this.comprobanteElectronicos = comprobanteElectronicos;
    }

}
