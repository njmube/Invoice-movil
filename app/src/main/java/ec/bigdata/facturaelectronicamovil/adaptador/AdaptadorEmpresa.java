package ec.bigdata.facturaelectronicamovil.adaptador;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import ec.bigdata.facturaelectronicamovil.modelo.Perfil;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;

/**
 * Created by DavidLeonardo on 28/4/2016.
 */
public class AdaptadorEmpresa implements JsonDeserializer<ClienteEmpresa> {

    @Override
    public ClienteEmpresa deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject json_object_empresa = (JsonObject) json;

        //Validaciones para posibles valores nulos
        byte[] logo_empresa = null;
        String numero_resolucion = "";
        try {
            numero_resolucion = json_object_empresa.get("numeroResolucionClienteEmpresa").getAsString();
            logo_empresa = json_object_empresa.get("logoClienteEmpresa").getAsJsonArray().toString().getBytes();
        } catch (Exception e) {
            numero_resolucion = "";
        }
        ClienteEmpresa clienteEmpresa = new ClienteEmpresa(
                json_object_empresa.get("idClienteEmpresa").getAsString()
                , json_object_empresa.get("nombreUsuarioClienteEmpresa").getAsString()
                , json_object_empresa.get("claveUsuarioClienteEmpresa").getAsString()
                , json_object_empresa.get("nombreComercialClienteEmpresa").getAsString()
                , json_object_empresa.get("razonSocialClienteEmpresa").getAsString()
                , json_object_empresa.get("direccionClienteEmpresa").getAsString()
                , json_object_empresa.get("correoPrincipalClienteEmpresa").getAsString()
                , json_object_empresa.get("telefonoPrincipalClienteEmpresa").getAsString()
                , json_object_empresa.get("obligadoContabilidadClienteEmpresa").getAsBoolean()
                , numero_resolucion
                , logo_empresa
                , Utilidades.obtenerFechaFormatoMySQL(json_object_empresa.get("fechaRegistroClienteEmpresa").getAsString())
                , json_object_empresa.get("estadoClienteEmpresa").getAsBoolean()
                , json_object_empresa.get("tipoUsuarioClienteEmpresa").getAsString());

        JsonObject json_object_perfil = (JsonObject) json_object_empresa.get("perfil");
        Perfil perfil = new Perfil(json_object_perfil.get("id_perfil").getAsInt(),
                json_object_perfil.get("codigo_perfil").getAsString(),
                json_object_perfil.get("nombre_perfil").getAsString(), json_object_perfil.get("descripcion_perfil").getAsString(), json_object_perfil.get("estado_perfil").getAsBoolean());

        clienteEmpresa.setPerfil(perfil);


        return clienteEmpresa;
    }
}
