package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.Recurso;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by DavidLeonardo on 27/4/2016.
 */
public class ClienteRestRecursos {

    private static ServicioRecursos servicioRecursos;

    public static ServicioRecursos getServicioRecursos() {
        if (servicioRecursos == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioRecursos = client.create(ServicioRecursos.class);
        }
        return servicioRecursos;
    }

    public interface ServicioRecursos {
        @GET("recurso/obtenerPantallasPorPerfil/{idPerfil}")
        Call<List<Recurso>> obtenerPantallasPorPerfil(@Path("idPerfil") String _idPerfil);
    }
}
