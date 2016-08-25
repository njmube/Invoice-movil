package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.UsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DavidLeonardo on 25/4/2016.
 */
public class ClienteRestUsuarioAcceso {

    private static ServicioUsuarioAcceso servicioUsuarioAcceso;


    public static ServicioUsuarioAcceso getServicioUsuarioAcceso() {
        if (servicioUsuarioAcceso == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioUsuarioAcceso = client.create(ServicioUsuarioAcceso.class);
        }
        return servicioUsuarioAcceso;
    }

    public interface ServicioUsuarioAcceso {

        @GET("usuarioAcceso")
        Call<List<UsuarioAcceso>> obtenerUsuarios();

        @GET("usuarioAcceso/{nombreUsuario}/{claveUsuario}")
        Call<UsuarioAcceso> validarUsuarioAcceso(@Path("nombreUsuario") String _nombreUsuario, @Path("claveUsuario") String _claveUsuario);

        @GET("usuarioAcceso/usuarioCorreo/{correoUsuario}")
        Call<UsuarioAcceso> obtenerUsuarioPorCorreo(@Path("correoUsuario") String _correoUsuario);

        @PUT("usuarioAcceso/{idUsuario}/{nombres}/{apellidos}/{correoPrincipal}/{correoAdicional}/{telefonoPrincipal}/{telefonoAdicional}")
        Call<ResponseBody> actualizarUsuario(@Query(value = "idUsuario", encoded = true) String _idUsuario,
                                             @Query(value = "nombres", encoded = true) String _nombres,
                                             @Query(value = "apellidos", encoded = true) String _apellidos,
                                             @Query(value = "correoPrincipal", encoded = true) String correoPrincipal,
                                             @Query(value = "correoAdicional", encoded = true) String correoAdicional,
                                             @Query(value = "telefonoPrincipal", encoded = true) String telefonoPrincipal,
                                             @Query(value = "telefonoAdicional", encoded = true) String telefonoAdicional);

    }
}
