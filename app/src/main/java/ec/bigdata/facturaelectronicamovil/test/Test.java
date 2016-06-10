package ec.bigdata.facturaelectronicamovil.test;

import ec.bigdata.facturaelectronicamovil.interfaces.ServicioEmpresa;
import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestEmpresa;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 25/4/2016.
 */
public class Test {
    public static void main(String args[]) throws Exception {

        ServicioEmpresa servicioEmpresa = ClienteRestEmpresa.getServicioEmpresa();

        Call<ClienteEmpresa> responseBodyCall = servicioEmpresa.validarEmpresa("bigdata", "bigdata");

        responseBodyCall.enqueue(new Callback<ClienteEmpresa>() {
            @Override
            public void onResponse(Call<ClienteEmpresa> call, Response<ClienteEmpresa> response) {
                System.out.print(response.body().getClaveUsuarioClienteEmpresa());
            }

            @Override
            public void onFailure(Call<ClienteEmpresa> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
}
