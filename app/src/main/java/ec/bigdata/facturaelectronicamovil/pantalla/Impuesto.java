package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterImpuestoComprobante;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterTarifasImpuesto;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogImpuestosICE;
import ec.bigdata.facturaelectronicamovil.modelo.TarifasImpuesto;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestImpuesto;
import ec.bigdata.facturaelectronicamovil.utilidad.Calculos;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import retrofit2.Call;
import retrofit2.Response;

public class Impuesto extends AppCompatActivity implements DialogImpuestosICE.DialogImpuestosICEComunicacion {

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestImpuesto.ServicioImpuesto servicioImpuesto;

    private Spinner spinnerTipoImpuestos;

    private Spinner spinnerTarifaImpuestos;

    private EditText editTextBaseImponibleICE;

    private EditText editTextPorcentajeICE;

    private TextView textViewValorICE;

    private ListView listViewImpuestos;

    private ArrayList<ImpuestoComprobanteElectronico> impuestoComprobanteElectronicoArrayList;

    private ArrayAdapterImpuestoComprobante arrayAdapterImpuestoComprobante;

    private String tipoImpuestoSeleccionado;

    private List<TarifasImpuesto> tarifasImpuestos;

    private BigDecimal baseImponibleDetalle;

    private ImpuestoComprobanteElectronico impuestoComprobanteElectronicoSeleccionado;

    private TarifaImpuestoAsyncTask impuestoAsyncTask;

    private ImpuestoComprobanteElectronico impuestoComprobanteElectronico;

    private TarifasImpuesto tarifaImpuesto;

    private int identificadorDetalle;

    private Button buttonAgregarImpuesto;

    private Button botonContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impuesto);
        //Se gira la pantalla modo horizontal-LANDSCAPE
        switch (getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_impuesto));

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        servicioImpuesto = ClienteRestImpuesto.getServicioImpuesto();

        spinnerTipoImpuestos = (Spinner) findViewById(R.id.spinner_tipos_impuestos);

        spinnerTarifaImpuestos = (Spinner) findViewById(R.id.spinner_tarifas_impuestos);

        editTextBaseImponibleICE = (EditText) findViewById(R.id.edit_text_base_imponible_ice);

        editTextPorcentajeICE = (EditText) findViewById(R.id.edit_text_porcentaje_ice);

        textViewValorICE = (TextView) findViewById(R.id.text_view_valor_ice);

        listViewImpuestos = (ListView) findViewById(R.id.list_view_impuestos);

        buttonAgregarImpuesto = (Button) findViewById(R.id.button_agregar_impuesto);

        //Cabecera
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.cabecera_list_view_impuestos, listViewImpuestos, false);

        listViewImpuestos.addHeaderView(headerView, null, false);

        //Se llena los impuestos que se reciben del detalle de la factura
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            impuestoComprobanteElectronicoArrayList = new ArrayList<>();
            impuestoComprobanteElectronicoArrayList = (ArrayList<ImpuestoComprobanteElectronico>) bundle.getSerializable(String.valueOf(Codigos.CODIGO_LISTA_IMPUESTOS));
            if (impuestoComprobanteElectronicoArrayList != null && !impuestoComprobanteElectronicoArrayList.isEmpty()) {
                arrayAdapterImpuestoComprobante = new ArrayAdapterImpuestoComprobante(getApplicationContext(), impuestoComprobanteElectronicoArrayList);
                listViewImpuestos.setAdapter(arrayAdapterImpuestoComprobante);
                //Se toma la base imponible del detalle, que se recibe al inicio

                baseImponibleDetalle = new BigDecimal(impuestoComprobanteElectronicoArrayList.get(0).getBaseImponible());
            }
            identificadorDetalle = bundle.getInt(String.valueOf(Codigos.CODIGO_ID_DETALLE));
        }

        botonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        botonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (impuestoComprobanteElectronicoArrayList != null && !impuestoComprobanteElectronicoArrayList.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), DetallesFactura.class);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_IMPUESTOS), impuestoComprobanteElectronicoArrayList);
                    intent.putExtra(String.valueOf(Codigos.CODIGO_ID_DETALLE), identificadorDetalle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        buttonAgregarImpuesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tarifaImpuesto != null) {
                    impuestoComprobanteElectronico = new ImpuestoComprobanteElectronico();
                    impuestoComprobanteElectronico.setCodigo(claseGlobalUsuario.getImpuestos().get(tipoImpuestoSeleccionado));
                    impuestoComprobanteElectronico.setCodigoPorcentaje(tarifaImpuesto.getCodigoTarifaImpuesto());
                    impuestoComprobanteElectronico.setTarifa(tarifaImpuesto.getPorcentajeTarifaImpuesto());
                    impuestoComprobanteElectronico.setBaseImponible(baseImponibleDetalle.toString());
                    impuestoComprobanteElectronico.setValor(Calculos.obtenerValor(baseImponibleDetalle.toString(), tarifaImpuesto.getPorcentajeTarifaImpuesto()));
                    validarImpuesto(impuestoComprobanteElectronico);
                }
            }
        });
        String[] tiposImpuestos = getResources().getStringArray(R.array.impuestos);
        String[] tiposImpuestosFactura = new String[4];

        tiposImpuestosFactura[0] = tiposImpuestos[1];
        tiposImpuestosFactura[1] = tiposImpuestos[2];
        tiposImpuestosFactura[2] = tiposImpuestos[3];
        tiposImpuestosFactura[3] = tiposImpuestos[4];


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_dropdown_item, tiposImpuestosFactura);

        spinnerTipoImpuestos.setAdapter(adapter);

        spinnerTipoImpuestos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                impuestoAsyncTask = new TarifaImpuestoAsyncTask();
                tipoImpuestoSeleccionado = (String) parent.getItemAtPosition(position);
                switch (tipoImpuestoSeleccionado) {
                    case "IVA":
                        spinnerTarifaImpuestos.setVisibility(View.VISIBLE);
                        cargarSpinnerTarifasImpuestos(1);
                        break;
                    case "ICE":
                        spinnerTarifaImpuestos.setVisibility(View.GONE);
                        cargarListaTarifas(3);
                        if (tarifasImpuestos != null && !tarifasImpuestos.isEmpty()) {
                            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                            DialogFragment dialogFragment = DialogImpuestosICE.newInstance(new ArrayList<>(tarifasImpuestos));
                            dialogFragment.show(fragmentManager, "DialogImpuestosICE");
                        }
                        break;
                    case "IRBPNR":

                        spinnerTarifaImpuestos.setVisibility(View.VISIBLE);
                        break;
                    case "ISD":

                        spinnerTarifaImpuestos.setVisibility(View.VISIBLE);
                        cargarSpinnerTarifasImpuestos(4);
                        break;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTarifaImpuestos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tarifaImpuesto = (TarifasImpuesto) parent.getSelectedItem();

                /*nackbar.make(view, tarifasImpuesto.getDescripcionTarifaImpuesto(), Snackbar.LENGTH_SHORT)
                      .show();*/

                /* TODO Ver utilidad de orientación de pantalla
                switch (getResources().getConfiguration().orientation){
                    case Configuration.ORIENTATION_PORTRAIT:
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case Configuration.ORIENTATION_LANDSCAPE:
                        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listViewImpuestos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                impuestoComprobanteElectronicoSeleccionado = (ImpuestoComprobanteElectronico) parent.getItemAtPosition(position);
                PopupMenu popup = new PopupMenu(Impuesto.this, view, Gravity.RIGHT);
                popup.getMenuInflater().inflate(R.menu.menu_detalle_impuesto, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_borrar_impuesto:
                                impuestoComprobanteElectronicoArrayList.remove(impuestoComprobanteElectronicoSeleccionado);
                                arrayAdapterImpuestoComprobante.notifyDataSetChanged();
                                break;

                        }
                        return true;
                    }

                });
                popup.show();
                return true;
            }
        });
    }

    private void cargarListaTarifas(Integer tipoImpuesto) {
        try {
            tarifasImpuestos = impuestoAsyncTask.execute(tipoImpuesto).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void cargarSpinnerTarifasImpuestos(Integer tipoImpuesto) {

        cargarListaTarifas(tipoImpuesto);
        ArrayAdapterTarifasImpuesto arrayAdapterImpuesto = new ArrayAdapterTarifasImpuesto(getApplicationContext(), tarifasImpuestos);
        spinnerTarifaImpuestos.setAdapter(arrayAdapterImpuesto);
    }

    @Override
    public void cargarTarifaImpuesto(ImpuestoComprobanteElectronico impuestoComprobanteElectronico) {
        validarImpuesto(impuestoComprobanteElectronico);
    }

    private void validarImpuesto(ImpuestoComprobanteElectronico impuestoComprobanteElectronico) {
        if (impuestoComprobanteElectronicoArrayList != null && !impuestoComprobanteElectronicoArrayList.isEmpty()) {
            for (int i = 0; i < impuestoComprobanteElectronicoArrayList.size(); i++) {
                //Se valida si el impuesto ya está agregado, si lo está se elimina.
                if (impuestoComprobanteElectronicoArrayList.get(i).getCodigo().equals(impuestoComprobanteElectronico.getCodigo())) {
                    impuestoComprobanteElectronicoArrayList.remove(i);
                }
            }

        }
        //Se recibe el impuesto seleccionado del dialog y se actualiza la lista de impuestos.
        impuestoComprobanteElectronicoArrayList.add(impuestoComprobanteElectronico);
        arrayAdapterImpuestoComprobante.notifyDataSetChanged();
        spinnerTarifaImpuestos.setVisibility(View.VISIBLE);
        spinnerTipoImpuestos.setSelection(0);

        //Se actualiza la lista de impuestos en la clase global
        claseGlobalUsuario.setImpuestoComprobanteElectronicoList(impuestoComprobanteElectronicoArrayList);
    }

    private class TarifaImpuestoAsyncTask extends AsyncTask<Integer, Integer, List<TarifasImpuesto>> {
        private List<TarifasImpuesto> tarifasImpuestos;

        public TarifaImpuestoAsyncTask() {
            tarifasImpuestos = new ArrayList<>();
        }

        @Override
        protected List<TarifasImpuesto> doInBackground(Integer... params) {
            Call<List<TarifasImpuesto>> listCall = null;
            listCall = servicioImpuesto.obtenerTarifasImpuestoPorTipoImpuesto(params[0]);
            try {
                Response<List<TarifasImpuesto>> response = listCall.execute();
                if (response.isSuccessful()) {
                    tarifasImpuestos = response.body();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tarifasImpuestos;
        }


        @Override
        protected void onPostExecute(List<TarifasImpuesto> result) {

        }
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
