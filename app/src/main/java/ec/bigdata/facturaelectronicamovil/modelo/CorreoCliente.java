package ec.bigdata.facturaelectronicamovil.modelo;

import java.io.Serializable;

/**
 * Created by DavidLeonardo on 22/8/2016.
 */
public class CorreoCliente implements Serializable {

    private String correoCliente;

    public CorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

}
