package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ec.bigdata.comprobanteelectronico.esquema.factura.Pagos;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.AdaptadorFormasPago;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.ResourceUtils;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

public class FormasPago extends AppCompatActivity implements Validator.ValidationListener, DialogConfirmacion.DialogConfirmacionComunicacion {

    private Spinner spinnerFormasPago;
    @NotEmpty(message = "El total de la forma de pago es requerida.")
    private EditText editTextTotalFormaPago;

    @NotEmpty(message = "El plazo de la forma de pago es requerida.")
    private EditText editTextPlazoFormaPago;

    @NotEmpty(message = "La unidad de tiempo de la forma de pago es requerida.")
    private EditText editTextUnidadTiempoFormaPago;

    private ListView listViewFormasPago;

    private TextView textViewSumaTotalFormasPago;

    private Button buttonContinuar;

    private Button buttonAgregar;

    private DialogFragment dialogFragmentConfirmacionEliminadoFormasPago;

    private AdaptadorFormasPago adaptadorFormasPago;

    private ActionMode actionMode;

    private ArrayList<Pagos> pagos;

    private Pagos pago;

    private Validator validator;

    private String totalFormaPago;

    private String plazoFormaPago;

    private String unidadTiempoFormaPago;

    private LinkedHashMap<String, String> formasPago;

    private LinkedHashMap<String, String> nombresFormaPagoAgregadas;

    private String nombreFormaPagoSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formas_pago);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_formas_pago));

        validator = new Validator(this);
        validator.setValidationListener(this);

        spinnerFormasPago = (Spinner) findViewById(R.id.spinner_formas_pago);
        editTextTotalFormaPago = (EditText) findViewById(R.id.edit_text_total_forma_pago);
        editTextPlazoFormaPago = (EditText) findViewById(R.id.edit_text_plazo_forma_pago);
        editTextUnidadTiempoFormaPago = (EditText) findViewById(R.id.edit_text_unidad_tiempo_forma_pago);
        textViewSumaTotalFormasPago = (TextView) findViewById(R.id.text_view_suma_totales_formas_pago);
        listViewFormasPago = (ListView) findViewById(R.id.list_view_formas_pago_factura);

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonAgregar = (Button) findViewById(R.id.button_agregar_forma_pago);


        pagos = new ArrayList<>();
        nombresFormaPagoAgregadas = new LinkedHashMap<>();

        //Cabecera
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.cabecera_list_view_formas_pago, listViewFormasPago, false);

        listViewFormasPago.addHeaderView(headerView, null, false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey(String.valueOf(Codigos.CODIGO_LISTA_FORMAS_PAGO_SELECCIONADA))) {
                pagos = (ArrayList<Pagos>) bundle.getSerializable(String.valueOf(Codigos.CODIGO_LISTA_FORMAS_PAGO_SELECCIONADA));
            }
        }

        adaptadorFormasPago = new AdaptadorFormasPago(getApplicationContext(), pagos);
        listViewFormasPago.setAdapter(adaptadorFormasPago);

        //Vista vacía.
        View viewVistaVacia = findViewById(R.id.text_view_vista_vacia);
        listViewFormasPago.setEmptyView(viewVistaVacia);

        formasPago = new LinkedHashMap<String, String>(ResourceUtils.getHashMapResource(getApplicationContext(), R.xml.formas_pago));

        String[] stringArraySpinnerFormasPago = new String[formasPago.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : formasPago.entrySet()) {
            stringArraySpinnerFormasPago[i] = entry.getKey();
            i++;
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringArraySpinnerFormasPago);
        spinnerFormasPago.setAdapter(arrayAdapter);
        spinnerFormasPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                nombreFormaPagoSeleccionada = (String) parent.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nada
            }
        });

        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
                if (pagos != null && !pagos.isEmpty()) {
                    intent.putExtra(String.valueOf(Codigos.CODIGO_FORMAS_PAGO), pagos);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        //Se activa el modo de selección múltiple.
        listViewFormasPago.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewFormasPago.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int totalSeleccionado;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    totalSeleccionado++;
                    adaptadorFormasPago.setNuevaSeleccion(position - 1, checked);
                } else {
                    totalSeleccionado--;
                    adaptadorFormasPago.quitarSeleccion(position - 1);
                }
                mode.setTitle(totalSeleccionado + " seleccionados");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                actionMode = mode;
                totalSeleccionado = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_borrar, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_borrar:
                        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                        dialogFragmentConfirmacionEliminadoFormasPago = DialogConfirmacion.newInstance(
                                getResources().getString(R.string.titulo_confirmacion),
                                getResources().getString(R.string.mensaje_confirmacion_eliminado_formas_pago) + " " + Utilidades.formatearFormasPagoEliminacion(seleccionarNombreFormaPago()), 1, Boolean.TRUE);
                        dialogFragmentConfirmacionEliminadoFormasPago.show(fragmentManager, "DialogConfirmacionBorradoFormasPago");
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                adaptadorFormasPago.limpiarSeleccion();
            }
        });
        listViewFormasPago.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewFormasPago.setItemChecked(position - 1, !adaptadorFormasPago.estaPosicionActualSeleccionada(position - 1));
                return false;
            }
        });
    }

    private List<String> seleccionarNombreFormaPago() {
        List<String> pagosArrayList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>(adaptadorFormasPago.obtenerPosicionActualSeleccionada());
        for (Integer i : ids) {
            pagosArrayList.add(nombresFormaPagoAgregadas.get(this.pagos.get(i).getFormaPago()));
        }
        return pagosArrayList;
    }

    private List<Pagos> seleccionarPagos() {
        List<Pagos> pagosArrayList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>(adaptadorFormasPago.obtenerPosicionActualSeleccionada());
        for (Integer i : ids) {
            pagosArrayList.add(this.pagos.get(i));
        }
        return pagosArrayList;
    }

    private void calcularTotalSumaFormasPago() {
        BigDecimal total = BigDecimal.ZERO;
        for (Pagos pago : pagos) {
            total = total.add(new BigDecimal(pago.getTotal()));
        }
        textViewSumaTotalFormasPago.setText("Total:" + total);
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

    @Override
    public void onValidationSucceeded() {
        plazoFormaPago = editTextPlazoFormaPago.getText().toString();
        totalFormaPago = editTextTotalFormaPago.getText().toString();
        unidadTiempoFormaPago = editTextUnidadTiempoFormaPago.getText().toString();
        pago = new Pagos();
        pago.setFormaPago(formasPago.get(nombreFormaPagoSeleccionada));
        pago.setPlazo(plazoFormaPago);
        pago.setTotal(totalFormaPago);
        pago.setUnidadTiempo(unidadTiempoFormaPago);
        pagos.add(pago);
        nombresFormaPagoAgregadas.put(pago.getFormaPago(), nombreFormaPagoSeleccionada);
        adaptadorFormasPago.notifyDataSetChanged();
        calcularTotalSumaFormasPago();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {

                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, message);
            }
        }
    }

    @Override
    public void presionarBotonSI(int idDialog) {
        switch (idDialog) {
            case 1:
                pagos.removeAll(seleccionarPagos());
                adaptadorFormasPago.notifyDataSetChanged();
                break;
        }
        actionMode.finish();
        calcularTotalSumaFormasPago();

    }

    @Override
    public void presionarBotonCancelar(int idDialog) {
        actionMode.finish();
    }
}
