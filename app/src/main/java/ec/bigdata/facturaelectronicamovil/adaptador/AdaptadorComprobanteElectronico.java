package ec.bigdata.facturaelectronicamovil.adaptador;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class AdaptadorComprobanteElectronico implements JsonDeserializer<ComprobanteElectronico> {
    @Override
    public ComprobanteElectronico deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObjectComprobanteElectronico = (JsonObject) json;

        JsonObject jsonObjectReceptor = (JsonObject) jsonObjectComprobanteElectronico.get("receptor");
        String rucReceptor = "";
        String razonSocialReceptor = "";
        String tipoPagoComprobanteElectronico = "";
        String numeroAutorizacion = "";
        if (jsonObjectReceptor != null) {
            rucReceptor = jsonObjectReceptor.get("rucReceptor").getAsString();
            razonSocialReceptor = jsonObjectReceptor.get("razonSocialReceptor").getAsString();
        }
        if (!jsonObjectComprobanteElectronico.get("tipoPagoComprobanteElectronico").isJsonNull()) {
            tipoPagoComprobanteElectronico = jsonObjectComprobanteElectronico.get("tipoPagoComprobanteElectronico").getAsString();
        }
        if (!jsonObjectComprobanteElectronico.get("numeroAutorizacion").isJsonNull()) {
            numeroAutorizacion = jsonObjectComprobanteElectronico.get("numeroAutorizacion").getAsString();
        }

        ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico comprobanteElectronico = new
                ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico(
                jsonObjectComprobanteElectronico.get("idComprobanteElectronico").getAsInt()
                , jsonObjectComprobanteElectronico.get("tipoComprobanteElectronico").getAsString()
                , jsonObjectComprobanteElectronico.get("claveAcceso").getAsString()
                , jsonObjectComprobanteElectronico.get("codigoEstablecimiento").getAsString()
                , jsonObjectComprobanteElectronico.get("puntoEmision").getAsString()
                , jsonObjectComprobanteElectronico.get("secuencial").getAsString()
                , numeroAutorizacion
                , jsonObjectComprobanteElectronico.get("rucEmisor").getAsString()
                , jsonObjectComprobanteElectronico.get("nombreComercialEmisor").getAsString()
                , jsonObjectComprobanteElectronico.get("ambienteComprobanteElectronico").getAsString()
                , jsonObjectComprobanteElectronico.get("estadoComprobanteElectronico").getAsString()
                , jsonObjectComprobanteElectronico.get("mensajeComprobanteElectronico").getAsString()
                , jsonObjectComprobanteElectronico.get("fechaEmision").getAsString()
                , jsonObjectComprobanteElectronico.get("fechaAutorizacion").getAsString()
                , tipoPagoComprobanteElectronico
                , rucReceptor, razonSocialReceptor);
        return comprobanteElectronico;
    }
}
