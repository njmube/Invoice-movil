package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
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
 * Created by DavidLeonardo on 4/5/2016.
 */
public class ClienteRestSecuencial {
    private static ServicioSecuencial servicioSecuencial;

    public static ServicioSecuencial getServicioSecuencial() {
        if (servicioSecuencial == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(40, TimeUnit.SECONDS)
                    .connectTimeout(40, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioSecuencial = client.create(ServicioSecuencial.class);
        }
        return servicioSecuencial;
    }

    public interface ServicioSecuencial {

        @GET("establecimiento/establecimientos/{idEmpresa}")
        Call<List<Secuencial>> obtenerEstablecimientosPorEmpresa(@Path("idEmpresa") String idEmpresa);


        @PUT("establecimiento/establecimientos/{idEmpresa}/{idEstablecimiento}/{codigoEstablecimiento}/{puntoEmision}/{direccion}")
        Call<ResponseBody> actualizarEstablecimiento(@Path("idEmpresa") String idEmpresa, @Path("idEstablecimiento") Integer idEstablecimiento, @Path("codigoEstablecimiento") String codigoEstablecimiento,
                                                     @Path("puntoEmision") String puntoEmision, @Query(value = "direccion", encoded = true) String direccion);

        @PUT("establecimiento/establecimientos/{idEmpresa}/{codigoEstablecimiento}/{puntoEmision}/{direccion}")
        Call<ResponseBody> guardarEstablecimiento(@Path("idEmpresa") String idEmpresa, @Path("codigoEstablecimiento") String codigoEstablecimiento,
                                                     @Path("puntoEmision") String puntoEmision, @Query(value = "direccion", encoded = true) String direccion);
    }
}
