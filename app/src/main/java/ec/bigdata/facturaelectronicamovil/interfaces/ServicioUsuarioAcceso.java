package ec.bigdata.facturaelectronicamovil.interfaces;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.modelo.UsuarioAcceso;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DavidLeonardo on 5/5/2016.
 */
public interface ServicioUsuarioAcceso {

    @GET("usuarioAcceso/obtenerUsuarios")
    Call<List<UsuarioAcceso>> obtenerUsuarios();

    @GET("usuarioAcceso/validarUsuarioObject/{nombreUsuario}/{claveUsuario}")
    Call<UsuarioAcceso> validarUsuarioAcceso(@Path("nombreUsuario") String _nombreUsuario, @Path("claveUsuario") String _claveUsuario);

    @GET("usuarioAcceso/obtenerUsuarioPorCorreo/{correoUsuario}")
    Call<UsuarioAcceso> obtenerUsuarioPorCorreo(@Path("correoUsuario") String _correoUsuario);

    @PUT("usuarioAcceso/actualizarNombres/{idUsuario}/{nombres}")
    Call<ResponseBody> actualizarNombres(@Path("idUsuario") String _idUsuario, @Path("nombres") String _nombres);

    @PUT("usuarioAcceso/actualizarApellidos/{idUsuario}/{apellidos}")
    Call<ResponseBody> actualizarApellidos(@Path("idUsuario") String _idUsuario, @Path("apellidos") String _apellidos);

    @PUT("usuarioAcceso/actualizarCorreoPrincipal/{idUsuario}/{correoPrincipal}")
    Call<ResponseBody> actualizarCorreoPrincipal(@Path("idUsuario") String _idUsuario, @Path("correoPrincipal") String _correoPrincipal);

    @PUT("usuarioAcceso/actualizarUsuario/{idUsuario}/{nombres}/{apellidos}/{correoPrincipal}/{correoAdicional}/{telefonoPrincipal}/{telefonoAdicional}")
    Call<ResponseBody> actualizarUsuario(@Query(value = "idUsuario", encoded = true) String _idUsuario,
                                         @Query(value = "nombres", encoded = true) String _nombres,
                                         @Query(value = "apellidos", encoded = true) String _apellidos,
                                         @Query(value = "correoPrincipal", encoded = true) String correoPrincipal,
                                         @Query(value = "correoAdicional", encoded = true) String correoAdicional,
                                         @Query(value = "telefonoPrincipal", encoded = true) String telefonoPrincipal,
                                         @Query(value = "telefonoAdicional", encoded = true) String telefonoAdicional);

}