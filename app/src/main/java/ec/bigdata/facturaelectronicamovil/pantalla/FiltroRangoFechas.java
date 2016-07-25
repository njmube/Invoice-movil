package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogFecha;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Personalizacion;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

/**
 * Created by DavidLeonardo on 13/7/2016.
 */
public class FiltroRangoFechas extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView textViewFechaInicial;

    private TextView textViewFechaFinal;

    private Button buttonDialogFechaInicial;

    private Button buttonDialogFechaFinal;

    private Button buttonAceptar;

    private Calendar calendarFechaInicial;

    private Calendar calendarFechaFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rango_fechas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);
        tituloToolbar.setText(getResources().getString(R.string.titulo_filtro_rango_fechas));

        textViewFechaInicial = (TextView) findViewById(R.id.text_view_fecha_inicial);
        textViewFechaFinal = (TextView) findViewById(R.id.text_view_fecha_final);
        buttonDialogFechaInicial = (Button) findViewById(R.id.button_dialog_fecha_inicial);
        buttonDialogFechaFinal = (Button) findViewById(R.id.button_dialog_fecha_final);

        buttonAceptar = (Button) findViewById(R.id.button_aceptar);
        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (calendarFechaInicial != null && calendarFechaFinal != null) {
                    if (calendarFechaInicial.before(calendarFechaFinal) || calendarFechaInicial.equals(calendarFechaFinal)) {
                        Intent intent = new Intent(getApplicationContext(), FiltroBusquedaRepositorio.class);
                        intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_INICIO), calendarFechaInicial.getTime());
                        intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_FINAL), calendarFechaFinal.getTime());
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, "La fecha de inicio debe ser menor a la fecha de fin.");
                    }
                } else {
                    Personalizacion.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), Personalizacion.TOAST_ERROR, "Debe seleccionar un rango de fechas.");

                }
            }
        });

        buttonDialogFechaInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DialogFecha().show(getSupportFragmentManager(), "DialogFechaInicial");
            }
        });


        buttonDialogFechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DialogFecha().show(getSupportFragmentManager(), "DialogFechaFinal");
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (Fragment f : fragmentList) {
            switch (f.getTag()) {
                case "DialogFechaInicial":
                    calendarFechaInicial = Calendar.getInstance();
                    calendarFechaInicial.set(year, monthOfYear, dayOfMonth);
                    textViewFechaInicial.setText(Utilidades.obtenerFechaStringFormatoddMMyyyy(calendarFechaInicial.getTime()));
                    break;
                case "DialogFechaFinal":
                    calendarFechaFinal = Calendar.getInstance();
                    calendarFechaFinal.set(year, monthOfYear, dayOfMonth);
                    textViewFechaFinal.setText(Utilidades.obtenerFechaStringFormatoddMMyyyy(calendarFechaFinal.getTime()));
                    break;
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
        Intent intent = new Intent(getApplicationContext(), FiltroBusquedaRepositorio.class);
        if (calendarFechaInicial != null && calendarFechaFinal != null) {
            intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_INICIO), calendarFechaInicial.getTime());
            intent.putExtra(String.valueOf(Codigos.CODIGO_FILTRO_BUSQUEDA_FECHA_FINAL), calendarFechaFinal.getTime());
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}
