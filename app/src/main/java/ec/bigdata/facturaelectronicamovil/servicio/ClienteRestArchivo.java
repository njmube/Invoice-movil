package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by DavidLeonardo on 22/7/2016.
 */
public class ClienteRestArchivo {
    private static ServicioArchivo servicioArchivo;

    public static ServicioArchivo getServicioArchivo() {
        if (servicioArchivo == null) {
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            servicioArchivo = client.create(ClienteRestArchivo.ServicioArchivo.class);
        }
        return servicioArchivo;
    }

    public interface ServicioArchivo {

        @GET("archivo/archivo-bytes/{idComprobanteElectronico}/{tipoArchivo}")
        Call<ResponseBody> obtenerArchivoPorBytesPorTipoArchivo(@Path("idComprobanteElectronico") Integer idComprobanteElectronico, @Path("tipoArchivo") Integer tipoArchivo);

        @GET("archivo/zip/{idComprobanteElectronico}")
            //@Streaming
        Call<ResponseBody> obtenerZipRIDERespuestaSRI(@Path("idComprobanteElectronico") Integer idComprobanteElectronico);
    }
}
