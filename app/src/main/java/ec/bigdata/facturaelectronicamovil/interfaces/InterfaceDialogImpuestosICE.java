package ec.bigdata.facturaelectronicamovil.interfaces;

import java.io.Serializable;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;

/**
 * Created by DavidLeonardo on 9/6/2016.
 */
public interface InterfaceDialogImpuestosICE extends Serializable {
    void enviarTarifaImpuesto(ImpuestoComprobanteElectronico impuestoComprobanteElectronico);
}
