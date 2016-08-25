package ec.bigdata.facturaelectronicamovil.servicio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by DavidLeonardo on 23/8/2016.
 */
public class ClienteRestNotificacion {

    private static ServicioNotificacionComprobante servicioNotificacionComprobante;

    public static ServicioNotificacionComprobante getServicioNotificacionComprobante() {
        if (servicioNotificacionComprobante == null) {
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
            servicioNotificacionComprobante = client.create(ServicioNotificacionComprobante.class);
        }
        return servicioNotificacionComprobante;
    }

    public interface ServicioNotificacionComprobante {
        @POST("notificacion-comprobante/reenvio/{idComprobante}/{correosDestinatarios}")
        Call<ResponseBody> reenviarComprobanteEmitidoAutorizado(@Path("idComprobante") Integer idComprobante, @Body List<String> correosDestinatarios);
    }
}
