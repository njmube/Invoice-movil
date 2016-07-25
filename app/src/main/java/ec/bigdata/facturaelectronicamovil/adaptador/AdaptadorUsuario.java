/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.bigdata.facturaelectronicamovil.adaptador;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import ec.bigdata.facturaelectronicamovil.modelo.Perfil;
import ec.bigdata.facturaelectronicamovil.modelo.UsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;


/**
 * @author DavidLeonardo
 */
public class AdaptadorUsuario implements JsonDeserializer<UsuarioAcceso> {

    @Override
    public UsuarioAcceso deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {


        JsonObject json_object_usuario = (JsonObject) json;

        UsuarioAcceso usuarioAcceso = new UsuarioAcceso(json_object_usuario.get("idUsuario").getAsInt(), json_object_usuario.get("identificacionUsuario").getAsString(), json_object_usuario.get("nombreUsuarioAcceso").getAsString(), json_object_usuario.get("claveUsuarioAcceso").getAsString(), json_object_usuario.get("nombreUsuario").getAsString(), json_object_usuario.get("apellidoUsuario").getAsString(), json_object_usuario.get("telefonoPrincipalUsuario").getAsString(), json_object_usuario.get("telefonoAdicionalUsuario") != null ? json_object_usuario.get("telefonoAdicionalUsuario").getAsString() : "", json_object_usuario.get("correoPrincipalUsuario").getAsString(), json_object_usuario.get("correoAdicionalUsuario") != null ? json_object_usuario.get("correoAdicionalUsuario").getAsString() : "", Utilidades.obtenerFechaFormatoyyyyMMddHHmmss(json_object_usuario.get("fechaRegistroUsuario").getAsString()), json_object_usuario.get("estadoUsuario").getAsString());

        JsonObject json_object_perfil = (JsonObject) json_object_usuario.get("perfil");
        Perfil perfil = new Perfil(json_object_perfil.get("id_perfil").getAsInt(),
                json_object_perfil.get("codigo_perfil").getAsString(),
                json_object_perfil.get("nombre_perfil").getAsString(), json_object_perfil.get("descripcion_perfil").getAsString(), json_object_perfil.get("estado_perfil").getAsBoolean());

        usuarioAcceso.setPerfil(perfil);


        return usuarioAcceso;
    }
}