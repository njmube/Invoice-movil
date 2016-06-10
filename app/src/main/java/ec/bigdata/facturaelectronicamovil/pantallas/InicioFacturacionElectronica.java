package ec.bigdata.facturaelectronicamovil.pantallas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterEstructuraFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.modelo.MenuEstructuraFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.Codigos;

public class InicioFacturacionElectronica extends AppCompatActivity {
    private ListView componentesFacturaElectronica;

    //final String[] componentesFactura = new String[] { "Información Tributaria","Información Factura", "Detalles", "Información Adicional"};
    private List<MenuEstructuraFacturaElectronica> componentesFactura;

    private ArrayAdapterEstructuraFacturaElectronica adaptador;

    ClaseGlobalUsuario claseGlobalUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_facturacion_electronica);
        componentesFacturaElectronica = (ListView) findViewById(R.id.list_view_componentes_factura_electronica);
        cargarListView();

      /*Adaptador con Strings
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, componentesFactura) {
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);
                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                return view;
            }
        };
        componentesFacturaElectronica.setAdapter(adapter);
*/
        componentesFacturaElectronica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), InformacionEmisor.class);
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_INFORMACION_EMISOR);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), Establecimiento.class);
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_ESTABLECIMIENTO);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), Cliente.class);
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;

                    case 3:
                        intent = new Intent(getApplicationContext(), DetallesFactura.class);
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_INFORMACION_DETALLE);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    default:
                        break;
                }
            }
        });

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Validación de información tributaria
        if (requestCode == Codigos.CODIGO_VALIDACION_INFORMACION_EMISOR) {
            if (resultCode == RESULT_OK) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_EMISOR).setValidacion(R.drawable.ic_check_circle);
                adaptador.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_EMISOR).setValidacion(R.drawable.ic_error);
                adaptador.notifyDataSetChanged();
            }
        }
        //Validación de establecimiento
        if (requestCode == Codigos.CODIGO_VALIDACION_ESTABLECIMIENTO) {
            if (resultCode == RESULT_OK) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_ESTABLECIMIENTO).setValidacion(R.drawable.ic_check_circle);
                adaptador.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_ESTABLECIMIENTO).setValidacion(R.drawable.ic_error);
                adaptador.notifyDataSetChanged();
            }
        }
        //Validación de información factura
        if (requestCode == Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE) {
            if (resultCode == RESULT_OK) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE).setValidacion(R.drawable.ic_check_circle);
                adaptador.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE).setValidacion(R.drawable.ic_error);
                adaptador.notifyDataSetChanged();
            }
        }
        /*if(data!=null) {
            Secuencial secuencial = (Secuencial) data.getExtras().getSerializable(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL);

            if (secuencial != null) {

                Toast.makeText(InicioFacturacionElectronica.this, "Secuencial OK.", Toast.LENGTH_SHORT).show();
            }
            ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_VALIDACION_CLIENTE));
            if (cliente != null) {
                Toast.makeText(InicioFacturacionElectronica.this, "Cliente OK.", Toast.LENGTH_SHORT).show();
            }

            ec.bigdata.facturaelectronicamovil.modelo.Producto producto = (ec.bigdata.facturaelectronicamovil.modelo.Producto) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_VALIDACION_PRODUCTO));

            if (producto != null) {

                Toast.makeText(InicioFacturacionElectronica.this, "Producto OK.", Toast.LENGTH_SHORT).show();
            }
        }*/
        if (claseGlobalUsuario.getSecuencialAFacturar() != null) {
            Toast.makeText(InicioFacturacionElectronica.this, "Secuencial OK.", Toast.LENGTH_SHORT).show();
        }
        if (claseGlobalUsuario.getClienteAFacturar() != null) {
            Toast.makeText(InicioFacturacionElectronica.this, "Cliente OK.", Toast.LENGTH_SHORT).show();
        }
        if (claseGlobalUsuario.getProductoAFacturar() != null) {
            Toast.makeText(InicioFacturacionElectronica.this, "Producto OK.", Toast.LENGTH_SHORT).show();
        }

    }

    private void cargarListView() {
        componentesFactura = new ArrayList<>();
        componentesFactura.add(new MenuEstructuraFacturaElectronica("Información Emisor", R.drawable.ic_error));
        componentesFactura.add(new MenuEstructuraFacturaElectronica("Información Tributaria", R.drawable.ic_error));
        componentesFactura.add(new MenuEstructuraFacturaElectronica("Cliente", R.drawable.ic_error));
        componentesFactura.add(new MenuEstructuraFacturaElectronica("Detalles", R.drawable.ic_error));
        componentesFactura.add(new MenuEstructuraFacturaElectronica("Información Adicional", R.drawable.ic_error));

        //Inicializar el adaptador con la fuente de datos
        adaptador = new ArrayAdapterEstructuraFacturaElectronica<MenuEstructuraFacturaElectronica>(
                this,
                componentesFactura);

        //Relacionando la lista con el adaptador
        componentesFacturaElectronica.setAdapter(adaptador);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
