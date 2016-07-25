package ec.bigdata.facturaelectronicamovil.pantalla;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.Archivo;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestArchivo;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ClienteRestArchivo.ServicioArchivo servicioArchivo = ClienteRestArchivo.getServicioArchivo();
        Call<Archivo> archivoCall = servicioArchivo.obtenerArchivoPorComprobante(10);
        archivoCall.enqueue(new Callback<Archivo>() {
            @Override
            public void onResponse(Call<Archivo> call, Response<Archivo> response) {
                if (response.isSuccessful()) {
                    Archivo a = response.body();
                    String s = new String(a.getRespuestaSricomprobanteElectronicoArchivo());
                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_INFORMACION, s);
                }
            }

            @Override
            public void onFailure(Call<Archivo> call, Throwable t) {

            }
        });
    }
}
