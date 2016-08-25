package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.CorreoAdicional;
import ec.bigdata.facturaelectronicamovil.modelo.CorreoCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DavidLeonardo on 2/8/2016.
 */
public class ClienteRestCorreoAdicionalCliente {

    private static ServicioCorreoAdicional servicioCorreoAdicional;

    public static ServicioCorreoAdicional getServicioCliente() {
        if (servicioCorreoAdicional == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
            servicioCorreoAdicional = client.create(ClienteRestCorreoAdicionalCliente.ServicioCorreoAdicional.class);
        }
        return servicioCorreoAdicional;
    }

    public interface ServicioCorreoAdicional {

        @GET("correo-adicional/correos-por-id/{idCliente}")
        Call<List<CorreoAdicional>> obtenerCorreosAdicionalesPorCliente(@Path("idCliente") Integer idCliente);

        @GET("correo-adicional/correos-por-identificacion/{empresaAsociado}/{identificacionCliente}")
        Call<List<CorreoCliente>> obtenerCorreosElectronicosPorIdentificacionCliente(@Path("empresaAsociado") String empresaAsociado, @Path("identificacionCliente") String identificacionCliente);

        @POST("correo-adicional/correos/{idCliente}/{tipoCliente}/{correoAdicional}")
        Call<ResponseBody> guardarCorreoAdicional(@Path("idCliente") Integer idCliente, @Path("tipoCliente") Boolean tipoCliente, @Query(value = "correoAdicional") String correoAdicional);

        @HTTP(method = "DELETE", path = "correo-adicional/correos/{correosAdicionales}", hasBody = true)
        Call<ResponseBody> eliminarCorreosAdicionales(@Body List<CorreoAdicional> correosAdicionales);

    }
}
