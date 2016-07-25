package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Predicate;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterEstructuraFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.facturacion.UtilidadesFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.modelo.MenuEstructuraFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;

public class InicioFacturacionElectronica extends AppCompatActivity implements DialogConfirmacion.ConfirmacionDialogListener {

    private ListView listViewComponentesFacturaElectronica;

    private List<MenuEstructuraFacturaElectronica> componentesFactura;

    private ArrayAdapterEstructuraFacturaElectronica adaptador;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private Secuencial secuencialSeleccionado;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.factura.Detalle> detallesSeleccionados;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalArrayList;

    private ImplementacionFactura implementacionFactura;

    private Button buttonContinuarResumenFacturaElectronica;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_facturacion_electronica);
        listViewComponentesFacturaElectronica = (ListView) findViewById(R.id.list_view_componentes_factura_electronica);
        cargarListView();
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        listViewComponentesFacturaElectronica.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_TRIBUTARIA);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), Cliente.class);
                        if (clienteSeleccionado != null) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
                        }
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;

                    case 3:
                        intent = new Intent(getApplicationContext(), DetallesFactura.class);
                        if (detallesSeleccionados != null && !detallesSeleccionados.isEmpty()) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_DETALLES_FACTURA), detallesSeleccionados);
                        }
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_INFORMACION_DETALLE);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), InformacionAdicional.class);
                        startActivityForResult(intent, Codigos.CODIGO_VALIDACION_INFORMACION_ADICIONAL);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    default:
                        break;
                }
            }
        });

        buttonContinuarResumenFacturaElectronica = (Button) findViewById(R.id.button_continuar_resumen_factura_electronica);
        buttonContinuarResumenFacturaElectronica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificacion = validarVerificacionComponentesFactura();
                if (verificacion.equals("")) {
                    //TODO continua al resumen de la factura y verificación de RIDE
                    UtilidadesFacturaElectronica utilidadesFacturaElectronica = new UtilidadesFacturaElectronica(claseGlobalUsuario, secuencialSeleccionado, clienteSeleccionado, informacionAdicionalArrayList);
                    implementacionFactura = utilidadesFacturaElectronica.generarFacturaElectronica(detallesSeleccionados);
                    Intent intent = new Intent(getApplicationContext(), ResumenFacturaElectronica.class);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_FACTURA_ELECTRONICA), implementacionFactura);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, getResources().getString(R.string.etiqueta_valor_noverificado).concat(verificacion));
                }
            }
        });
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
        if (requestCode == Codigos.CODIGO_VALIDACION_TRIBUTARIA) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    secuencialSeleccionado = (Secuencial) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL));
                }
                componentesFactura.get(Codigos.CODIGO_VALIDACION_TRIBUTARIA).setValidacion(R.drawable.ic_check_circle);
                adaptador.notifyDataSetChanged();
            } else if (resultCode == RESULT_CANCELED) {
                componentesFactura.get(Codigos.CODIGO_VALIDACION_TRIBUTARIA).setValidacion(R.drawable.ic_error);
                adaptador.notifyDataSetChanged();
            }
        }
        //Validación de información factura
        if (requestCode == Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
                    componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_CLIENTE).setValidacion(R.drawable.ic_check_circle);
                    adaptador.notifyDataSetChanged();
                }

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        //Validación de información de detalles
        if (requestCode == Codigos.CODIGO_VALIDACION_INFORMACION_DETALLE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    detallesSeleccionados = (ArrayList<Detalle>) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_LISTA_DETALLES_FACTURA));
                    componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_DETALLE).setValidacion(R.drawable.ic_check_circle);
                    adaptador.notifyDataSetChanged();
                }

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
        //Validación de información adicional
        if (requestCode == Codigos.CODIGO_VALIDACION_INFORMACION_ADICIONAL) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    informacionAdicionalArrayList = (ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional>) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_VALIDACION_INFORMACION_ADICIONAL));
                    componentesFactura.get(Codigos.CODIGO_VALIDACION_INFORMACION_ADICIONAL).setValidacion(R.drawable.ic_check_circle);
                    adaptador.notifyDataSetChanged();
                }

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private String validarVerificacionComponentesFactura() {
        StringBuilder stringBuilder = new StringBuilder();
        for (MenuEstructuraFacturaElectronica menuEstructuraFacturaElectronica : componentesFactura) {

            if (menuEstructuraFacturaElectronica.getValidacion().equals(R.drawable.ic_error)) {
                stringBuilder.append(menuEstructuraFacturaElectronica.getTitulo());
            }
        }
        return stringBuilder.toString();
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
        listViewComponentesFacturaElectronica.setAdapter(adaptador);

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_borrar_factura_electronica:


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Predicate condicion = new Predicate() {

            @Override
            public boolean evaluate(Object o) {
                return ((MenuEstructuraFacturaElectronica) o).getValidacion().equals(R.drawable.ic_check_circle);
            }
        };
        if (!CollectionUtils.select(componentesFactura, condicion).isEmpty()) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = DialogConfirmacion.newInstance(
                    getResources().getString(R.string.titulo_cancelar_inicio_factura),
                    getResources().getString(R.string.mensaje_continuar_cancelar_inicio_factura));
            dialogFragment.show(fragmentManager, "DialogConfirmacion");
        } else {
            this.finish();
        }
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void presionarBotonSI() {
        this.finish();
    }

    @Override
    public void presionarBotonCancelar() {

    }
}
