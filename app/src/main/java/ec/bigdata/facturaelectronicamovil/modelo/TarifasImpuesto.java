package ec.bigdata.facturaelectronicamovil.modelo;

/**
 * Created by DavidLeonardo on 6/6/2016.
 */
public class TarifasImpuesto implements java.io.Serializable {


    private Integer idtarifaImpuesto;
    private String codigoTarifaImpuesto;
    private String descripcionTarifaImpuesto;
    private String porcentajeTarifaImpuesto;
    private boolean variableTarifaImpuesto;
    private String tipoImpuesto;

    public TarifasImpuesto() {
    }

    public TarifasImpuesto(String codigoTarifaImpuesto, String descripcionTarifaImpuesto, String porcentajeTarifaImpuesto, boolean variableTarifaImpuesto) {

        this.codigoTarifaImpuesto = codigoTarifaImpuesto;
        this.descripcionTarifaImpuesto = descripcionTarifaImpuesto;
        this.porcentajeTarifaImpuesto = porcentajeTarifaImpuesto;
        this.variableTarifaImpuesto = variableTarifaImpuesto;
    }

    public Integer getIdtarifaImpuesto() {
        return this.idtarifaImpuesto;
    }

    public void setIdtarifaImpuesto(Integer idtarifaImpuesto) {
        this.idtarifaImpuesto = idtarifaImpuesto;
    }

    public String getCodigoTarifaImpuesto() {
        return this.codigoTarifaImpuesto;
    }

    public void setCodigoTarifaImpuesto(String codigoTarifaImpuesto) {
        this.codigoTarifaImpuesto = codigoTarifaImpuesto;
    }

    public String getDescripcionTarifaImpuesto() {
        return this.descripcionTarifaImpuesto;
    }

    public void setDescripcionTarifaImpuesto(String descripcionTarifaImpuesto) {
        this.descripcionTarifaImpuesto = descripcionTarifaImpuesto;
    }

    public String getPorcentajeTarifaImpuesto() {
        return this.porcentajeTarifaImpuesto;
    }

    public void setPorcentajeTarifaImpuesto(String porcentajeTarifaImpuesto) {
        this.porcentajeTarifaImpuesto = porcentajeTarifaImpuesto;
    }

    public boolean isVariableTarifaImpuesto() {
        return this.variableTarifaImpuesto;
    }

    public void setVariableTarifaImpuesto(boolean variableTarifaImpuesto) {
        this.variableTarifaImpuesto = variableTarifaImpuesto;
    }

    public String getTipoImpuesto() {
        return tipoImpuesto;
    }

    public void setTipoImpuesto(String tipoImpuesto) {
        this.tipoImpuesto = tipoImpuesto;
    }
}
