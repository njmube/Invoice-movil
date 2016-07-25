package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.interfaz.ServicioUsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
}
