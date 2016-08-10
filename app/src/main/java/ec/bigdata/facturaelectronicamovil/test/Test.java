package ec.bigdata.facturaelectronicamovil.test;

import java.util.List;

import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCliente;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 25/4/2016.
 */
public class Test {
    private static final String TAG = Test.class.getSimpleName();
    public static void main(String args[]) throws Exception {

        /*ClienteRestArchivo.ServicioArchivo servicioArchivo = ClienteRestArchivo.getServicioArchivo();

        Call<ResponseBody> responseBodyCall = servicioArchivo.obtenerArchivoPorBytes(10);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    JsonParser parser = new JsonParser();
                    JsonObject o = null;
                    String s = null;
                    try {
                        s = new String(response.body().bytes());
                        Log.i("RESPUESTA", s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i("ERROR", t.getMessage());
            }
        });*/
        ClienteRestCliente.ServicioCliente servicioCliente = ClienteRestCliente.getServicioCliente();

        Call<List<Cliente>> listCall = servicioCliente.obtenerClientesPorEmpresaAsociado("1792547164001", 0, 1);
        listCall.enqueue(new Callback<List<Cliente>>() {
            @Override
            public void onResponse(Call<List<Cliente>> call, Response<List<Cliente>> response) {
                if (response.isSuccessful()) {
                    List<Cliente> clientes = response.body();
                    if (clientes != null) {
                        for (Cliente cliente : clientes) {
                            System.out.println(cliente.getNombreCliente());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Cliente>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
