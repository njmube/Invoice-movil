package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.TipoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by DavidLeonardo on 25/4/2016.
 */
public class ClienteRestTipoComprobanteElectronico {

    private static ServicioTipoComprobanteElectronico servicioTipoComprobanteElectronico;


    public static ServicioTipoComprobanteElectronico getServicioTipoComprobanteElectronico() {
        if (servicioTipoComprobanteElectronico == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioTipoComprobanteElectronico = client.create(ServicioTipoComprobanteElectronico.class);
        }
        return servicioTipoComprobanteElectronico;
    }

    public interface ServicioTipoComprobanteElectronico {

        @GET("tiposComprobanteElectronico/obtenerTiposComprobante")
        Call<List<TipoComprobanteElectronico>> obtenerTiposComprobante();
    }
}
