package ec.bigdata.facturaelectronicamovil.anotacion;

import com.mobsandgeeks.saripaar.AnnotationRule;

import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

/**
 * Created by DavidLeonardo on 11/8/2016.
 */
public class IdentificacionRegla extends AnnotationRule<Identificacion, String> {

    protected IdentificacionRegla(Identificacion identificacion) {
        super(identificacion);
    }


    @Override
    public boolean isValid(String valor) {
        return Utilidades.validarIdentificacion(valor);
    }
}
