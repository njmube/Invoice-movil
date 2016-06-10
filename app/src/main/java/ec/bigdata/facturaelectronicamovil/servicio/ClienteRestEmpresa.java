package ec.bigdata.facturaelectronicamovil.servicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.interfaces.ServicioEmpresa;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DavidLeonardo on 28/4/2016.
 */
public class ClienteRestEmpresa {
    private static ServicioEmpresa servicioEmpresa;


    public static ServicioEmpresa getServicioEmpresa() {
        if (servicioEmpresa == null) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
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

}
