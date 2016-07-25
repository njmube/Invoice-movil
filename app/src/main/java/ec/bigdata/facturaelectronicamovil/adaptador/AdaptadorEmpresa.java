package ec.bigdata.facturaelectronicamovil.adaptador;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import ec.bigdata.facturaelectronicamovil.modelo.Perfil;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

/**
 * Created by DavidLeonardo on 28/4/2016.
 */
public class AdaptadorEmpresa implements JsonDeserializer<ClienteEmpresa> {

    @Override
    public ClienteEmpresa deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jsonObject = (JsonObject) json;

        //Validaciones para posibles valores nulos
        byte[] logoEmpresa = null;
        String numeroResolucion = "";
        try {
            numeroResolucion = jsonObject.get("numeroResolucionClienteEmpresa").getAsString();
            logoEmpresa = jsonObject.get("logoClienteEmpresa").getAsJsonArray().toString().getBytes();
        } catch (Exception e) {
            numeroResolucion = "";
        }
        ClienteEmpresa clienteEmpresa = new ClienteEmpresa(
                jsonObject.get("idClienteEmpresa").getAsString()
                , jsonObject.get("nombreUsuarioClienteEmpresa").getAsString()
                , jsonObject.get("claveUsuarioClienteEmpresa").getAsString()
                , jsonObject.get("nombreComercialClienteEmpresa").getAsString()
                , jsonObject.get("razonSocialClienteEmpresa").getAsString()
                , jsonObject.get("direccionClienteEmpresa").getAsString()
                , jsonObject.get("correoPrincipalClienteEmpresa").getAsString()
                , jsonObject.get("telefonoPrincipalClienteEmpresa").getAsString()
                , jsonObject.get("obligadoContabilidadClienteEmpresa").getAsBoolean()
                , numeroResolucion
                , logoEmpresa
                , Utilidades.obtenerFechaDateFormatoddMMyyyy(jsonObject.get("fechaRegistroClienteEmpresa").getAsString())
                , jsonObject.get("estadoClienteEmpresa").getAsBoolean()
                , jsonObject.get("tipoUsuarioClienteEmpresa").getAsString());

        JsonObject json_object_perfil = (JsonObject) jsonObject.get("perfil");
        Perfil perfil = new Perfil(json_object_perfil.get("id_perfil").getAsInt(),
                json_object_perfil.get("codigo_perfil").getAsString(),
                json_object_perfil.get("nombre_perfil").getAsString(), json_object_perfil.get("descripcion_perfil").getAsString(), json_object_perfil.get("estado_perfil").getAsBoolean());

        clienteEmpresa.setPerfil(perfil);


        return clienteEmpresa;
    }
}
