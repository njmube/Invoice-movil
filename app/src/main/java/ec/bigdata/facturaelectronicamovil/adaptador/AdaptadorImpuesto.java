package ec.bigdata.facturaelectronicamovil.adaptador;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ec.bigdata.facturaelectronicamovil.modelo.TarifasImpuesto;

/**
 * Created by DavidLeonardo on 6/6/2016.
 */
public class AdaptadorImpuesto implements JsonDeserializer<TarifasImpuesto> {

    @Override
    public TarifasImpuesto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;


        TarifasImpuesto tarifasImpuesto = new TarifasImpuesto(
                jsonObject.get("codigoTarifaImpuesto").getAsString()
                , jsonObject.get("descripcionTarifaImpuesto").getAsString()
                , jsonObject.get("porcentajeTarifaImpuesto").getAsString()
                , jsonObject.get("variableTarifaImpuesto").getAsBoolean());

        JsonObject jsonObjectTipoImpuesto = (JsonObject) jsonObject.get("tipoImpuesto");

        tarifasImpuesto.setTipoImpuesto(jsonObjectTipoImpuesto.get("impuesto").getAsString());

        return tarifasImpuesto;
    }
}
