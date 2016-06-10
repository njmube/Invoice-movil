package ec.bigdata.facturaelectronicamovil.servicio;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.modelo.Producto;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DavidLeonardo on 20/5/2016.
 */
public class ClienteRestProducto {

    private static ServicioProducto servicioProducto;

    public static ServicioProducto getServicioProducto() {
        if (servicioProducto == null) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService())
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
            servicioProducto = client.create(ClienteRestProducto.ServicioProducto.class);
        }
        return servicioProducto;
    }

    public interface ServicioProducto {

        @GET("producto/obtenerProductosPorEmpresaAsociado/{idEmpresa}")
        Call<List<Producto>> obtenerProductosPorEmpresaAsociado(@Path("idEmpresa") String idEmpresa);

        @GET("producto/obtenerProductoPorCodigoPrincipalPorEmpresaAsociado/{idEmpresa}/{codigoPrincipalProducto}")
        Call<Producto> obtenerProductoPorCodigoPrincipalPorEmpresaAsociado(@Path("idEmpresa") String idEmpresa, @Path("codigoPrincipalProducto") String codigoPrincipalProducto);

        @POST("producto/guardarProducto/{idEmpresa}/{codigoPrincipalProducto}/{codigoAuxiliarProducto}/{descripcionProducto}/{precioUnitarioProducto}")
        Call<ResponseBody> guardarProducto(@Query(value = "idEmpresa", encoded = true) String idEmpresa,
                                           @Query(value = "codigoPrincipalProducto", encoded = true) String codigoPrincipalProducto,
                                           @Query(value = "codigoAuxiliarProducto", encoded = true) String codigoAuxiliarProducto,
                                           @Query(value = "descripcionProducto", encoded = true) String descripcionProducto,
                                           @Query(value = "precioUnitarioProducto", encoded = true) String precioUnitarioProducto);

        @PUT("producto/actualizarProducto/{idEmpresa}/{idProducto}/{codigoAuxiliarProducto}/{descripcionProducto}/{precioUnitarioProducto}")
        Call<ResponseBody> actualizarProducto(@Query(value = "idEmpresa", encoded = true) String idEmpresa,
                                              @Query(value = "idProducto", encoded = true) Integer idProducto,
                                              @Query(value = "codigoAuxiliarProducto", encoded = true) String codigoAuxiliarProducto,
                                              @Query(value = "descripcionProducto", encoded = true) String descripcionProducto,
                                              @Query(value = "precioUnitarioProducto", encoded = true) String precioUnitarioProducto);
    }
}
