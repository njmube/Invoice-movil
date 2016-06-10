package ec.bigdata.facturaelectronicamovil.pantallas;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.TipoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestTipoComprobanteElectronico;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpcionesFacturacion extends AppCompatActivity {

    private ListView listViewOpcionesFacturacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_facturacion);

        listViewOpcionesFacturacion = (ListView) findViewById(R.id.list_view_opciones_facturacion_electronica);

        ClienteRestTipoComprobanteElectronico.ServicioTipoComprobanteElectronico servicioTipoComprobanteElectronico = ClienteRestTipoComprobanteElectronico.getServicioTipoComprobanteElectronico();
        Call<List<TipoComprobanteElectronico>> call = servicioTipoComprobanteElectronico.obtenerTiposComprobante();
        call.enqueue(new Callback<List<TipoComprobanteElectronico>>() {
            @Override
            public void onResponse(Call<List<TipoComprobanteElectronico>> call, Response<List<TipoComprobanteElectronico>> response) {
                if (response.isSuccessful()) {

                    String tipos_comprobante = null;

                    List<TipoComprobanteElectronico> tipoComprobanteElectronicos = response.body();
                    List<String> tiposComprobantesNombres = new ArrayList<String>();
                    if (tipoComprobanteElectronicos != null && !tipoComprobanteElectronicos.isEmpty()) {
                        for (TipoComprobanteElectronico tce : tipoComprobanteElectronicos) {
                            tiposComprobantesNombres.add(tce.getNombreTipoComprobanteElectronico());
                        }
                    }
                    String[] arrayTiposComprobante = new String[tiposComprobantesNombres.size()];
                    arrayTiposComprobante = tiposComprobantesNombres.toArray(arrayTiposComprobante);

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, arrayTiposComprobante) {

                        public View getView(int position, View convertView, ViewGroup parent) {
                            // Get the Item from ListView
                            View view = super.getView(position, convertView, parent);

                            // Initialize a TextView for ListView each Item
                            TextView tv = (TextView) view.findViewById(android.R.id.text1);
                            if (Build.VERSION.SDK_INT < 23) {
                                tv.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Holo_Large);

                            } else {

                                tv.setTextAppearance(android.R.style.TextAppearance_Holo_Large);
                            }
                            tv.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));

                            return view;
                        }
                    };
                    listViewOpcionesFacturacion.setAdapter(adapter);

                    listViewOpcionesFacturacion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String item = (String) parent.getItemAtPosition(position);
                            switch (item) {
                                case "Factura":
                                    Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                                    break;
                                default:
                                    Toast.makeText(OpcionesFacturacion.this, item, Toast.LENGTH_SHORT).show();
                                    break;
                            }

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<List<TipoComprobanteElectronico>> call, Throwable t) {

            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
