package ec.bigdata.facturaelectronicamovil.anotacion;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by DavidLeonardo on 11/8/2016.
 */
@ValidateUsing(IdentificacionRegla.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Identificacion {

    int messageResId() default -1;

    String message() default "Error en la identificación";

    int sequence() default -1;

}
