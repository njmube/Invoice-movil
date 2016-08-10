package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ec.bigdata.facturaelectronicamovil.R;

public class OpcionesRepositorio extends AppCompatActivity {
    private ListView listViewOpcionesRepositorio;
    final String[] opcionesRepositorio = new String[]{"Comprobantes Emitidos Autorizados", "Comprobantes Emitidos No Autorizados", "Comprobantes Recibidos", "Comprobantes Devueltos"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_repositorio);

        listViewOpcionesRepositorio = (ListView) findViewById(R.id.list_view_opciones_repositorio);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, opcionesRepositorio) {
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View view = super.getView(position, convertView, parent);
                        // Initialize a TextView for ListView each Item
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                        tv.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.black));
                        return view;
                    }
                };
        listViewOpcionesRepositorio.setAdapter(arrayAdapter);
        listViewOpcionesRepositorio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), RepositorioComprobantesEmitidosAutorizados.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), RepositorioComprobantesEmitidosNoAutorizados.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    default:
                        break;
                }
            }
        });

    }
}
