package ec.bigdata.facturaelectronicamovil.adaptador;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ec.bigdata.facturaelectronicamovil.modelo.Archivo;

/**
 * Created by DavidLeonardo on 22/7/2016.
 */
public class AdaptadorArchivo implements JsonDeserializer<Archivo> {
    @Override
    public Archivo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObjectArchivo = (JsonObject) json;
        byte[] byteRespuestaComprobanteElectronico = null;
        byte[] byteArchivoFirmadoComprobanteElectronico = null;
        try {
            byteArchivoFirmadoComprobanteElectronico = jsonObjectArchivo.get("comprobanteFirmadoComprobanteElectronicoArchivo").getAsString().getBytes();
            byteRespuestaComprobanteElectronico = jsonObjectArchivo.get("respuestaSricomprobanteElectronicoArchivo").getAsString().getBytes();
        } catch (Exception e) {

        }

        Archivo archivo = new Archivo(byteRespuestaComprobanteElectronico, byteArchivoFirmadoComprobanteElectronico);
        return archivo;
    }
}
