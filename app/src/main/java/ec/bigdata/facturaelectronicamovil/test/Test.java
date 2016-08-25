package ec.bigdata.facturaelectronicamovil.test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestArchivo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DavidLeonardo on 25/4/2016.
 */
public class Test {
    private static final String TAG = Test.class.getSimpleName();
    public static void main(String args[]) throws Exception {

        ClienteRestArchivo.ServicioArchivo servicioArchivo = ClienteRestArchivo.getServicioArchivo();
        Call<ResponseBody> responseBodyCall = servicioArchivo.obtenerZipRIDERespuestaSRI(18);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    JsonParser parser = new JsonParser();
                    JsonObject o = null;
                    try {
                        String s = new String(response.body().bytes());
                        System.out.println(s);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });

    }
}
