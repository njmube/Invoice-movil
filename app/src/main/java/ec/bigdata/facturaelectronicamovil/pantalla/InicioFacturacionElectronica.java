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
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.factura.Detalle;
import ec.bigdata.comprobanteelectronico.esquema.factura.Pagos;
import ec.bigdata.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterEstructuraFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacionUnBoton;
import ec.bigdata.facturaelectronicamovil.facturacion.UtilidadesFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.modelo.MenuEstructuraFacturaElectronica;
import ec.bigdata.facturaelectronicamovil.modelo.Secuencial;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

public class InicioFacturacionElectronica extends AppCompatActivity implements DialogConfirmacion.DialogConfirmacionComunicacion, DialogConfirmacionUnBoton.DialogConfirmacionUnBotonComunicacion {

    private ListView listViewComponentesFacturaElectronicaObligatorios;

    private ListView listViewComponentesFacturaElectronicaOpcionales;

    private Button buttonContinuarResumenFacturaElectronica;

    private List<MenuEstructuraFacturaElectronica> componentesFacturaObligatorios;

    private List<MenuEstructuraFacturaElectronica> componentesFacturaOpcionales;

    private ArrayAdapterEstructuraFacturaElectronica adaptadorComponentesObligatorios;

    private ArrayAdapterEstructuraFacturaElectronica adaptadorComponentesOpcionales;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private Secuencial secuencialSeleccionado;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.factura.Detalle> detallesSeleccionados;

    private ArrayList<Pagos> formasPago;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalArrayList;


    private ImplementacionFactura implementacionFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_facturacion_electronica);
        listViewComponentesFacturaElectronicaObligatorios = (ListView) findViewById(R.id.list_view_componentes_factura_electronica_obligatorios);
        listViewComponentesFacturaElectronicaOpcionales = (ListView) findViewById(R.id.list_view_componentes_factura_electronica_opcionales);
        cargarListViewComponentesObligatorios();
        cargarListViewComponentesOpcionales();
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        listViewComponentesFacturaElectronicaObligatorios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), InformacionEmisor.class);
                        startActivityForResult(intent, Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_EMISOR);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), Establecimiento.class);
                        startActivityForResult(intent, Codigos.CODIGO_REQUEST_VALIDACION_TRIBUTARIA);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), Cliente.class);
                        if (clienteSeleccionado != null) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);

                        }
                        intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_FACTURA_ELECTRONICA), Boolean.TRUE);
                        startActivityForResult(intent, Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_CLIENTE);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;

                    case 3:
                        intent = new Intent(getApplicationContext(), DetallesFactura.class);
                        if (detallesSeleccionados != null && !detallesSeleccionados.isEmpty()) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_DETALLES_FACTURA), detallesSeleccionados);
                        }
                        startActivityForResult(intent, Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_DETALLE);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;
                    default:
                        break;
                }
            }
        });
        listViewComponentesFacturaElectronicaOpcionales.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), FormasPago.class);
                        if (formasPago != null && !formasPago.isEmpty()) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_FORMAS_PAGO_SELECCIONADA), formasPago);
                        }
                        startActivityForResult(intent, Codigos.CODIGO_REQUEST_VALIDACION_FORMAS_PAGO);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;


                    case 1:
                        intent = new Intent(getApplicationContext(), InformacionAdicional.class);
                        if (informacionAdicionalArrayList != null && !informacionAdicionalArrayList.isEmpty()) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_INFORMACION_ADICIONAL_SELECCIONADA), informacionAdicionalArrayList);
                        }

                        if (clienteSeleccionado != null) {
                            intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
                        }
                        startActivityForResult(intent, Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_ADICIONAL);
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
                List<String> validacion = validarVerificacionComponentesFactura();
                if (validacion.isEmpty()) {
                    if (implementacionFactura == null) {
                        UtilidadesFacturaElectronica utilidadesFacturaElectronica = new UtilidadesFacturaElectronica(claseGlobalUsuario, secuencialSeleccionado, clienteSeleccionado, formasPago, informacionAdicionalArrayList);
                        implementacionFactura = utilidadesFacturaElectronica.generarFacturaElectronica(detallesSeleccionados);
                    }
                    Intent intent = new Intent(getApplicationContext(), ResumenFacturaPrincipal.class);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_EXTRA_FACTURA_ELECTRONICA), implementacionFactura);
                    startActivityForResult(intent, Codigos.CODIGO_REQUEST_FACTURA_ELECTRONICA);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                } else {
                    String verificacion = validacion.size() > 1 ? StringUtils.join(validacion, ",") : validacion.get(0);
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, getResources().getString(R.string.etiqueta_valor_pendiente).concat(verificacion));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Validación de la información del emisor.
        if (requestCode == Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_EMISOR) {
            if (resultCode == RESULT_OK) {
                componentesFacturaObligatorios.get(0).setValidacion(R.drawable.ic_thumb_up_black_48dp);
            } else if (resultCode == RESULT_CANCELED) {
                componentesFacturaObligatorios.get(0).setValidacion(R.drawable.ic_thumb_down_black_48dp);
            }

        }
        //Validación de establecimiento
        if (requestCode == Codigos.CODIGO_REQUEST_VALIDACION_TRIBUTARIA) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    secuencialSeleccionado = (Secuencial) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_ESTABLECIMIENTO_ACTUAL));
                }
                componentesFacturaObligatorios.get(1).setValidacion(R.drawable.ic_thumb_up_black_48dp);

            } else if (resultCode == RESULT_CANCELED) {
                componentesFacturaObligatorios.get(1).setValidacion(R.drawable.ic_thumb_down_black_48dp);

            }
        }
        //Validación de información factura
        if (requestCode == Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_CLIENTE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
                    componentesFacturaObligatorios.get(2).setValidacion(R.drawable.ic_thumb_up_black_48dp);
                }
            } else if (resultCode == RESULT_CANCELED) {
                componentesFacturaObligatorios.get(2).setValidacion(R.drawable.ic_thumb_down_black_48dp);
            }
        }
        //Validación de información de detalles
        if (requestCode == Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_DETALLE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    detallesSeleccionados = (ArrayList<Detalle>) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_LISTA_DETALLES_FACTURA));
                    componentesFacturaObligatorios.get(3).setValidacion(R.drawable.ic_thumb_up_black_48dp);
                }

            } else if (resultCode == RESULT_CANCELED) {
                componentesFacturaObligatorios.get(3).setValidacion(R.drawable.ic_thumb_down_black_48dp);
            }
        }

        //Validación de formas de pago

        if (requestCode == Codigos.CODIGO_REQUEST_VALIDACION_FORMAS_PAGO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    formasPago = (ArrayList<Pagos>) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_LISTA_FORMAS_PAGO_SELECCIONADA));

                }
            }
        }

        //Validación de información adicional
        if (requestCode == Codigos.CODIGO_REQUEST_VALIDACION_INFORMACION_ADICIONAL) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    informacionAdicionalArrayList = (ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional>) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_LISTA_INFORMACION_ADICIONAL_SELECCIONADA));

                }
            }
        }
        if (requestCode == Codigos.CODIGO_REQUEST_FACTURA_ELECTRONICA) {
            if (resultCode == RESULT_OK) {
                implementacionFactura = (ImplementacionFactura) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_EXTRA_FACTURA_ELECTRONICA));
            }
        }
        adaptadorComponentesObligatorios.notifyDataSetChanged();
    }

    private List<String> validarVerificacionComponentesFactura() {
        List<String> componentesSinValidar = new ArrayList<>();
        for (MenuEstructuraFacturaElectronica menuEstructuraFacturaElectronica : componentesFacturaObligatorios) {

            if (menuEstructuraFacturaElectronica.getValidacion().equals(R.drawable.ic_thumb_down_black_48dp)) {
                componentesSinValidar.add(menuEstructuraFacturaElectronica.getTitulo());
            }
        }
        return componentesSinValidar;
    }

    private void cargarListViewComponentesObligatorios() {
        componentesFacturaObligatorios = new ArrayList<>();
        componentesFacturaObligatorios.add(new MenuEstructuraFacturaElectronica("Información Emisor", R.drawable.ic_thumb_down_black_48dp));
        componentesFacturaObligatorios.add(new MenuEstructuraFacturaElectronica("Información Tributaria", R.drawable.ic_thumb_down_black_48dp));
        componentesFacturaObligatorios.add(new MenuEstructuraFacturaElectronica("Cliente", R.drawable.ic_thumb_down_black_48dp));
        componentesFacturaObligatorios.add(new MenuEstructuraFacturaElectronica("Detalles", R.drawable.ic_thumb_down_black_48dp));

        //Inicializar el adaptador con la fuente de datos
        adaptadorComponentesObligatorios = new ArrayAdapterEstructuraFacturaElectronica<MenuEstructuraFacturaElectronica>(
                this,
                componentesFacturaObligatorios);

        //Relacionando la lista con el adaptador
        listViewComponentesFacturaElectronicaObligatorios.setAdapter(adaptadorComponentesObligatorios);

    }

    private void cargarListViewComponentesOpcionales() {
        componentesFacturaOpcionales = new ArrayList<>();
        componentesFacturaOpcionales.add(new MenuEstructuraFacturaElectronica("Formas de pago", R.drawable.ic_thumb_up_black_48dp));
        componentesFacturaOpcionales.add(new MenuEstructuraFacturaElectronica("Información Adicional", R.drawable.ic_thumb_up_black_48dp));

        //Inicializar el adaptador con la fuente de datos
        adaptadorComponentesOpcionales = new ArrayAdapterEstructuraFacturaElectronica<MenuEstructuraFacturaElectronica>(
                this,
                componentesFacturaOpcionales);

        //Relacionando la lista con el adaptador
        listViewComponentesFacturaElectronicaOpcionales.setAdapter(adaptadorComponentesOpcionales);

    }

    private void validarTotalesFormaPago() {
        BigDecimal totalFormasPago = BigDecimal.ZERO;
        BigDecimal totalFactura = BigDecimal.ZERO;
        totalFactura = new BigDecimal(implementacionFactura.getInformacionFactura().getImporteTotal());
        for (Pagos pago : formasPago) {
            totalFormasPago = totalFormasPago.add(new BigDecimal(pago.getTotal()));
        }
        if (totalFactura.compareTo(totalFormasPago) != 0) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = DialogConfirmacion.newInstance(
                    getResources().getString(R.string.titulo_cancelar_inicio_factura),
                    getResources().getString(R.string.mensaje_continuar_cancelar_inicio_factura), 1, Boolean.FALSE);
            dialogFragment.show(fragmentManager, "DialogConfirmacion");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_borrar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_borrar:
                validarBorrado();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void validarBorrado() {
        Predicate condicion = new Predicate() {

            @Override
            public boolean evaluate(Object o) {
                return ((MenuEstructuraFacturaElectronica) o).getValidacion().equals(R.drawable.ic_thumb_up_black_48dp);
            }
        };
        if (!CollectionUtils.select(componentesFacturaObligatorios, condicion).isEmpty() || !CollectionUtils.select(componentesFacturaOpcionales, condicion).isEmpty()) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            DialogFragment dialogFragment = DialogConfirmacion.newInstance(
                    getResources().getString(R.string.titulo_cancelar_inicio_factura),
                    getResources().getString(R.string.mensaje_continuar_cancelar_inicio_factura), 1, Boolean.FALSE);
            dialogFragment.show(fragmentManager, "DialogConfirmacionCancelarFactura");
        } else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        validarBorrado();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void presionarBotonSI(int idDialog) {
        switch (idDialog) {
            case 1:
                this.finish();
                break;
            case 2:
        }

    }

    @Override
    public void presionarBotonCancelar(int idDialog) {

    }

    @Override
    public void presionarBotonContinuar() {

    }
}
