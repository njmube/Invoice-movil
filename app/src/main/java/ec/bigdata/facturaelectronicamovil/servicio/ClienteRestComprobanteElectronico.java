package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by DavidLeonardo on 11/6/2016.
 */
public class ClienteRestComprobanteElectronico {

    private static ServicioComprobanteElectronico servicioComprobanteElectronico;

    public static ServicioComprobanteElectronico getServicioComprobanteElectronico() {
        if (servicioComprobanteElectronico == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)

                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioComprobanteElectronico = client.create(ClienteRestComprobanteElectronico.ServicioComprobanteElectronico.class);
        }
        return servicioComprobanteElectronico;
    }

    public interface ServicioComprobanteElectronico {

        @Multipart
        @POST("comprobante-electronico/firma-autorizacion")
        Call<ResponseBody> archivo(@Part MultipartBody.Part file, @Query(value = "idEmpresa", encoded = true) String idEmpresa);
    }
}
