package ec.bigdata.facturaelectronicamovil.servicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.adaptador.AdaptadorComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.modelo.ComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by DavidLeonardo on 8/7/2016.
 */
public class ClienteRestRepositorioComprobanteElectronico {

    private static ServicioRepositorioComprobanteElectronico servicioRepositorioComprobanteElectronico;

    public static ServicioRepositorioComprobanteElectronico getServicioRepositorioComprobanteElectronico() {
        if (servicioRepositorioComprobanteElectronico == null) {

            Gson gson = new GsonBuilder().registerTypeAdapter(ComprobanteElectronico.class, new AdaptadorComprobanteElectronico()).serializeNulls().create();
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            servicioRepositorioComprobanteElectronico = client.create(ServicioRepositorioComprobanteElectronico.class);
        }
        return servicioRepositorioComprobanteElectronico;
    }

    public interface ServicioRepositorioComprobanteElectronico {

        @GET("repositorio/comprobantes-emitidos/{rucEmpresaEmisor}/{estados}/{resultadoMinimo}/{resultadoMaximo}")
        Call<List<ComprobanteElectronico>> obtenerComprobantesElectronicosEmitidosPorEmpresa(@Path("rucEmpresaEmisor") String rucEmpresaEmisor, @Field("estados") @QueryMap Map<String, String> estados, @Path("resultadoMinimo") Integer resultadoMinimo, @Path("resultadoMaximo") Integer resultadoMaximo);


        @GET("repositorio/comprobantes-emitidos-filtro-busqueda/{rucEmpresaEmisor}/{estados}/{fechaInicio}/{fechaFin}/{tipoComprobante}/{secuencial}/{identificacionReceptor}/{resultadoMinimo}/{resultadoMaximo}")
        Call<List<ComprobanteElectronico>> obtenerComprobantesElectronicosEmitidosPorEmpresaPorFiltroBusqueda(@Query(value = "rucEmpresaEmisor", encoded = true) String rucEmpresaEmisor,
                                                                                                              @QueryMap Map<String, String> estados,
                                                                                                              @Query(value = "fechaInicio", encoded = true) Date fechaInicio,
                                                                                                              @Query(value = "fechaFin", encoded = true) Date fechaFin,
                                                                                                              @Query(value = "tipoComprobante", encoded = true) String tipoComprobante,
                                                                                                              @Query(value = "secuencial", encoded = true) String secuencial,
                                                                                                              @Query(value = "identificacionReceptor", encoded = true) String identificacionReceptor,
                                                                                                              @Query(value = "resultadoMinimo", encoded = true) Integer resultadoMinimo,
                                                                                                              @Query(value = "resultadoMaximo", encoded = true) Integer resultadoMaximo);

    }
}
