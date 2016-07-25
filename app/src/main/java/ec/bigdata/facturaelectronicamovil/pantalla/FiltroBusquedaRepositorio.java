package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

public class FiltroBusquedaRepositorio extends AppCompatActivity {

    private Spinner spinnerPeriodoFechas;

    private Spinner spinnerTiposComprobante;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private String secuencialSeleccionado;

    private String identificacionReceptorSeleccionado;

    private String tipoComprobanteSeleccionado;

    private Date fechaInicio;

    private Date fechaFin;

    private Button buttonAplicarFiltro;

    private TextView textViewMensajePeriodoFechasSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_busqueda_repositorio);
        secuencialSeleccionado = "";
        identificacionReceptorSeleccionado = "";
        tipoComprobanteSeleccionado = "";

        buttonAplicarFiltro = (Button) findViewById(R.id.button_aplicar_filtro);
        buttonAplicarFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RepositorioComprobantesEmitidos.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_SECUENCIAL), secuencialSeleccionado);
                intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_RECEPTOR), identificacionReceptorSeleccionado);
                intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_TIPO_COMPROBANTE), tipoComprobanteSeleccionado);
                intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_INICIO), fechaInicio);
                intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_FINAL), fechaFin);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_filtro_busqueda_comprobantes));
        textViewMensajePeriodoFechasSeleccionado = (TextView) findViewById(R.id.text_view_periodo_fechas_seleccionado);
        spinnerPeriodoFechas = (Spinner) findViewById(R.id.spinner_periodo_fechas);
        String periodoFechas[] = getResources().getStringArray(R.array.periodo_fechas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_dropdown_item, periodoFechas);

        spinnerPeriodoFechas.setAdapter(adapter);
        String tiposComprobanteArrayString[] = new String[6];
        spinnerTiposComprobante = (Spinner) findViewById(R.id.spinner_tipos_comprobante);
        LinkedHashMap<String, String> tiposComprobante = claseGlobalUsuario.getTiposComprobantes();
        int i = 0;
        tiposComprobanteArrayString[0] = "Todos";
        for (Map.Entry<String, String> entry : tiposComprobante.entrySet()) {
            tiposComprobanteArrayString[i] = entry.getKey();
            i++;
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.spinner_dropdown_item, tiposComprobanteArrayString);

        spinnerTiposComprobante.setAdapter(adapter);
        spinnerTiposComprobante.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        tipoComprobanteSeleccionado = "";
                        break;
                    case 1:
                        tipoComprobanteSeleccionado = claseGlobalUsuario.getTiposComprobantes().get("FACTURA");
                        break;
                    case 2:
                        tipoComprobanteSeleccionado = claseGlobalUsuario.getTiposComprobantes().get("NOTA DE CRÉDITO");
                        break;
                    case 3:
                        tipoComprobanteSeleccionado = claseGlobalUsuario.getTiposComprobantes().get("NOTA DE DÉBITO");
                        break;
                    case 4:
                        tipoComprobanteSeleccionado = claseGlobalUsuario.getTiposComprobantes().get("GUÍA DE REMISIÓN");
                        break;
                    case 5:
                        tipoComprobanteSeleccionado = claseGlobalUsuario.getTiposComprobantes().get("COMPROBANTE DE RETENCIÓN");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerPeriodoFechas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar calendario = Calendar.getInstance();
                Calendar calendarioFinalActual = Calendar.getInstance();
                fechaFin = calendarioFinalActual.getTime();
                String item = (String) parent.getItemAtPosition(position);
                switch (position) {
                    case 0:

                        textViewMensajePeriodoFechasSeleccionado.setText(item);
                        break;
                    case 1:
                        calendario.set(Calendar.HOUR_OF_DAY, 0);
                        calendario.set(Calendar.MINUTE, 0);
                        calendario.set(Calendar.SECOND, 0);
                        fechaInicio = calendario.getTime();
                        textViewMensajePeriodoFechasSeleccionado.setText(item);
                        break;
                    case 2:
                        int semana = calendario.get(Calendar.WEEK_OF_YEAR);
                        semana--;
                        calendario.set(Calendar.WEEK_OF_YEAR, semana);
                        calendario.set(Calendar.HOUR_OF_DAY, 0);
                        calendario.set(Calendar.MINUTE, 0);
                        calendario.set(Calendar.SECOND, 0);
                        fechaInicio = calendario.getTime();
                        textViewMensajePeriodoFechasSeleccionado.setText(item);
                        break;
                    case 3:
                        int mes = calendario.get(Calendar.MONTH);
                        mes--;
                        calendario.set(Calendar.MONTH, mes);
                        calendario.set(Calendar.DAY_OF_MONTH, 1);
                        calendario.set(Calendar.HOUR_OF_DAY, 0);
                        calendario.set(Calendar.MINUTE, 0);
                        calendario.set(Calendar.SECOND, 0);
                        fechaInicio = calendario.getTime();
                        calendarioFinalActual.set(Calendar.DAY_OF_MONTH, 1);
                        calendarioFinalActual.add(Calendar.DAY_OF_MONTH, -1);
                        fechaInicio = calendarioFinalActual.getTime();
                        textViewMensajePeriodoFechasSeleccionado.setText(item);
                        break;
                    case 4:
                        mes = calendario.get(Calendar.MONTH);
                        mes = mes - 6;
                        calendario.set(Calendar.MONTH, mes);
                        calendario.set(Calendar.DAY_OF_MONTH, 1);
                        calendario.set(Calendar.HOUR_OF_DAY, 0);
                        calendario.set(Calendar.MINUTE, 0);
                        calendario.set(Calendar.SECOND, 0);
                        fechaInicio = calendario.getTime();
                        textViewMensajePeriodoFechasSeleccionado.setText(item);
                        break;
                    case 5:
                        Intent intent = new Intent(getApplicationContext(), FiltroRangoFechas.class);
                        startActivityForResult(intent, Codigos.CODIGO_FILTRO_BUSQUEDA_RANGO_FECHAS);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Codigos.CODIGO_FILTRO_BUSQUEDA_RANGO_FECHAS) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    fechaInicio = (Date) (data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_INICIO)));
                    fechaFin = (Date) (data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_FINAL)));
                    textViewMensajePeriodoFechasSeleccionado.setText("Periodo personalizado:" + ec.bigdata.utilidades.Utilidades.obtenerFechaFormatoddMMyyyy(fechaInicio) + "-:" + ec.bigdata.utilidades.Utilidades.obtenerFechaFormatoddMMyyyy(fechaFin));
                }
            }
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
        Intent intent = new Intent(getApplicationContext(), RepositorioComprobantesEmitidos.class);
        setResult(RESULT_CANCELED, intent);
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
