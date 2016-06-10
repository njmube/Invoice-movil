package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.Ambiente;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by DavidLeonardo on 26/4/2016.
 */
public class ClienteRestAmbiente {
    private static ServicioAmbiente servicioAmbiente;

    public static ServicioAmbiente getServicioAmbiente() {
        if (servicioAmbiente == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioAmbiente = client.create(ServicioAmbiente.class);
        }
        return servicioAmbiente;
    }

    public interface ServicioAmbiente {

        @GET("ambiente/obtenerAmbientes")
        Call<List<Ambiente>> obtenerAmbientes();

    }
}
