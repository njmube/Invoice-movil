package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by DavidLeonardo on 4/5/2016.
 */
public class ClienteRestSecuencial {
    private static ServicioSecuencial servicioSecuencial;

    public static ServicioSecuencial getServicioSecuencial() {
        if (servicioSecuencial == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
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

        @GET("secuencial/obtenerSecuencialesPorEmpresa/{idEmpresa}")
        Call<List<Secuencial>> obtenerSecuencialesPorEmpresa(@Path("idEmpresa") String idEmpresa);


        @PUT("secuencial/actualizarEstablecimiento/{id}/{codigoEstablecmiento}/{puntoEmision}/{direccion}")
        Call<ResponseBody> actualizarEstablecimiento(@Path("id") String idEmpresa, @Path("codigoEstablecmiento") String codigoEstablecmiento,
                                                     @Path("puntoEmision") String puntoEmision, @Path("direccion") String direccion);
    }
}
