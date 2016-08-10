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

        @GET("empresa/obtenerEmpresaPorIdNombreUsuario/{nombreUsuario}")
        Call<ClienteEmpresa> obtenerEmpresaPorIdNombreUsuario(@Path("nombreUsuario") String _nombreUsuario);

        @GET("empresa/validarEmpresa/{nombreUsuario}/{claveUsuario}")
        Call<ClienteEmpresa> validarEmpresa(@Path("nombreUsuario") String _nombreUsuario, @Path("claveUsuario") String claveUsuario);

        @GET("empresa/obtenerClienteEmpresaPorCorreo/{correoElectronico}")
        Call<ClienteEmpresa> obtenerClienteEmpresaPorCorreo(@Path("correoElectronico") String _correoElectronico);

        @PUT("empresa/actualizarObligadoLlevarContabilidad/{id}/{llevaContabilidad}")
        Call<ResponseBody> actualizarObligadoLlevarContabilidad(@Path("id") String id, @Path("llevaContabilidad") boolean llevaContabilidad);

        @PUT("empresa/actualizarEmpresa/{id}/{nombreComercial}/{razonSocial}/{direccion}/{correoPrincipal}/{telefonoPrincipal}/{numeroResolucion}")
        Call<ResponseBody> actualizarEmpresa(@Path("id") String id
                , @Path("nombreComercial") String nombreComercial
                , @Path("razonSocial") String razonSocial
                , @Path("direccion") String direccion
                , @Path("correoPrincipal") String correoPrincipal
                , @Path("telefonoPrincipal") String telefonoPrincipal
                , @Path("numeroResolucion") String numeroResolucion
        );

        @PUT("empresa/actualizarEmpresa/{id}/{nombreComercial}/{razonSocial}/{direccion}/{numeroResolucion}")
        Call<ResponseBody> actualizarEmpresa(@Query(value = "id", encoded = true) String id
                , @Query(value = "nombreComercial", encoded = true) String nombreComercial
                , @Query(value = "razonSocial", encoded = true) String razonSocial
                , @Query(value = "direccion", encoded = true) String direccion
                , @Query(value = "numeroResolucion", encoded = true) String numeroResolucion
        );
    }
}
