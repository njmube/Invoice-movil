package ec.bigdata.facturaelectronicamovil.servicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
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
 * Created by DavidLeonardo on 28/4/2016.
 */
public class ClienteRestEmpresa {
    private static ServicioEmpresa servicioEmpresa;

    public static ServicioEmpresa getServicioEmpresa() {
        if (servicioEmpresa == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(40, TimeUnit.SECONDS)
                    .connectTimeout(40, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            servicioEmpresa = client.create(ServicioEmpresa.class);
        }
        return servicioEmpresa;
    }

    public interface ServicioEmpresa {

        @GET("empresa/empresaNombreUsuario/{nombreUsuario}")
        Call<ClienteEmpresa> obtenerEmpresaPorNombreUsuario(@Path("nombreUsuario") String _nombreUsuario);

        @GET("empresa/empresaValidacion/{nombreUsuario}/{claveUsuario}")
        Call<ClienteEmpresa> validarEmpresa(@Path("nombreUsuario") String _nombreUsuario, @Path("claveUsuario") String claveUsuario);

        @GET("empresa/empresaCorreoElectronico/{correoElectronico}")
        Call<ClienteEmpresa> obtenerClienteEmpresaPorCorreo(@Path("correoElectronico") String _correoElectronico);

        @PUT("empresa/empresaLlevaContabilidad/{idEmpresa}/{llevaContabilidad}")
        Call<ResponseBody> actualizarObligadoLlevarContabilidad(@Path("idEmpresa") String idEmpresa, @Path("llevaContabilidad") boolean llevaContabilidad);


        @PUT("empresa/empresaActualizacion/{idEmpresa}/{nombreComercial}/{razonSocial}/{direccion}/{correoElectronicoPrincipal}/{telefonoPrincipal}/{numeroResolucion}")
        Call<ResponseBody> actualizarEmpresa(@Path("idEmpresa") String idEmpresa
                , @Query(value = "nombreComercial", encoded = true) String nombreComercial
                , @Query(value = "razonSocial", encoded = true) String razonSocial
                , @Query(value = "direccion", encoded = true) String direccion
                , @Query(value = "telefonoPrincipal", encoded = true) String telefonoPrincipal
                , @Query(value = "correoElectronicoPrincipal", encoded = true) String correoElectronicoPrincipal
                , @Query(value = "numeroResolucion", encoded = true) String numeroResolucion

        );

        @PUT("empresa/cambioContrasenia/{idEmpresa}/{contraseniaActual}/{nuevaContrasenia}")
        Call<ResponseBody> actualizarContrasenia(@Path("idEmpresa") String idEmpresa, @Query(value = "contraseniaActual", encoded = true) String contraseniaActual, @Query(value = "nuevaContrasenia", encoded = true) String nuevaContrasenia);

    }
}
