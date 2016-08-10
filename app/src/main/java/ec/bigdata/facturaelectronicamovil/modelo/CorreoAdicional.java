package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 2/8/2016.
 */
public class CorreoAdicional implements java.io.Serializable {


    private Integer idCorreoAdicional;
    private String correo;

    public CorreoAdicional() {
    }

    public CorreoAdicional(String correo) {
        this.correo = correo;
    }

    public CorreoAdicional(Integer idCorreoAdicional, String correo) {
        this.idCorreoAdicional = idCorreoAdicional;
        this.correo = correo;
    }

    public Integer getIdCorreoAdicional() {
        return this.idCorreoAdicional;
    }

    public void setIdCorreoAdicional(Integer idCorreoAdicional) {
        this.idCorreoAdicional = idCorreoAdicional;
    }

    public String getCorreo() {
        return this.correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

}
