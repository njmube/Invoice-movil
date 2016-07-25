package ec.bigdata.facturaelectronicamovil.interfaz;

import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by DavidLeonardo on 28/4/2016.
 */
public interface ServicioEmpresa {

    @GET("empresa/obtenerEmpresaPorIdNombreUsuario/{nombreUsuario}")
    Call<ClienteEmpresa> obtenerEmpresaPorIdNombreUsuario(@Path("nombreUsuario") String _nombreUsuario);

    @GET("empresa/validarEmpresa/{nombreUsuario}/{claveUsuario}")
    Call<ClienteEmpresa> validarEmpresa(@Path("nombreUsuario") String _nombreUsuario, @Path("claveUsuario") String claveUsuario);

    @GET("empresa/obtenerClienteEmpresaPorCorreo/{correoElectronico}")
    Call<ClienteEmpresa> obtenerClienteEmpresaPorCorreo(@Path("correoElectronico") String _correoElectronico);

    @PUT("empresa/actualizarObligadoLlevarContabilidad/{id}/{llevaContabilidad}")
    Call<ResponseBody> actualizarObligadoLlevarContabilidad(@Path("id") String id, @Path("llevaContabilidad") boolean llevaContabilidad);

    @PUT("empresa/actualizarEmpresa/{id}/{nombreComercial}/{razonSocial}/{direccion}/{correoPrincipal}/{telefonoPrincipal}/{numeroResolucion}")
    Call<ResponseBody> actualizarEmpresa(@Path("id") String id
            , @Path("nombreComercial") String nombreComercial
            , @Path("razonSocial") String razonSocial
            , @Path("direccion") String direccion
            , @Path("correoPrincipal") String correoPrincipal
            , @Path("telefonoPrincipal") String telefonoPrincipal
            , @Path("numeroResolucion") String numeroResolucion
    );

    @PUT("empresa/actualizarEmpresa/{id}/{nombreComercial}/{razonSocial}/{direccion}/{numeroResolucion}")
    Call<ResponseBody> actualizarEmpresa(@Query(value = "id", encoded = true) String id
            , @Query(value = "nombreComercial", encoded = true) String nombreComercial
            , @Query(value = "razonSocial", encoded = true) String razonSocial
            , @Query(value = "direccion", encoded = true) String direccion
            , @Query(value = "numeroResolucion", encoded = true) String numeroResolucion
    );
}