package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 4/5/2016.
 */
public class MenuEstructuraFacturaElectronica {

    private String titulo;
    private Integer validacion;

    public MenuEstructuraFacturaElectronica(String titulo, Integer validacion) {
        this.titulo = titulo;
        this.validacion = validacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getValidacion() {
        return validacion;
    }

    public void setValidacion(Integer validacion) {
        this.validacion = validacion;
    }
}
