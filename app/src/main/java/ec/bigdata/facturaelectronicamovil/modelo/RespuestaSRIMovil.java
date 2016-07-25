package ec.bigdata.facturaelectronicamovil.modelo;

import java.io.Serializable;

/**
 * Created by DavidLeonardo on 5/7/2016.
 */
public class RespuestaSRIMovil implements Serializable {

    private String numeroComprobante;
    private String estado;
    private String claveAcceso;
    private String numeroAutorizacion;
    private String mensaje;
    private String mensajeAdicional;

    public RespuestaSRIMovil() {
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensajeAdicional() {
        return mensajeAdicional;
    }

    public void setMensajeAdicional(String mensajeAdicional) {
        this.mensajeAdicional = mensajeAdicional;
    }


}
