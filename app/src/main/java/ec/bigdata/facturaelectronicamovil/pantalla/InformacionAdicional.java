package ec.bigdata.facturaelectronicamovil.pantalla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterInformacionAdicional;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

public class InformacionAdicional extends AppCompatActivity implements Validator.ValidationListener {

    private Toolbar toolbar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    @NotEmpty(message = "El nombre es requerido.")
    private EditText editTextNombre;

    @NotEmpty(message = "El valor es requerido.")
    private EditText editTextValor;

    private ListView listViewDetallesAdicionales;

    private Button buttonContinuar;

    private Button buttonAgregarDetalleAdicional;

    private ArrayAdapterInformacionAdicional arrayAdapterInformacionAdicional;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalList;

    private ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional informacionAdicional;

    private Validator validator;

    private int identificadorDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_adicional);

        toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_informacion_adicional));

        editTextNombre = (EditText) findViewById(R.id.edit_text_nombre_informacion_adicional);

        editTextValor = (EditText) findViewById(R.id.edit_text_valor_informacion_adicional);

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);

        buttonAgregarDetalleAdicional = (Button) findViewById(R.id.button_agregar_informacion_adicional);

        listViewDetallesAdicionales = (ListView) findViewById(R.id.list_view_informacion_adicional);

        //Cabecera
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.cabecera_list_view_informacion_adicional, listViewDetallesAdicionales, false);

        listViewDetallesAdicionales.addHeaderView(headerView, null, false);

        informacionAdicionalList = new ArrayList<>();

        arrayAdapterInformacionAdicional = new ArrayAdapterInformacionAdicional(getApplicationContext(), informacionAdicionalList);

        listViewDetallesAdicionales.setAdapter(arrayAdapterInformacionAdicional);

        validator = new Validator(this);
        validator.setValidationListener(this);

        buttonAgregarDetalleAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);
        buttonContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DetallesFactura.class);
                intent.putExtra(String.valueOf(Codigos.CODIGO_VALIDACION_INFORMACION_ADICIONAL), informacionAdicionalList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            identificadorDetalle = bundle.getInt(String.valueOf(Codigos.CODIGO_ID_DETALLE));
        }

    }

    @Override
    public void onValidationSucceeded() {

        if (informacionAdicionalList.size() < 15) {
            informacionAdicional = new ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional();
            informacionAdicional.setNombre(editTextNombre.getText().toString());
            informacionAdicional.setValor(editTextValor.getText().toString());
            informacionAdicionalList.add(informacionAdicional);
            arrayAdapterInformacionAdicional.notifyDataSetChanged();
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "El máximo es de 15 detalles en información adicional por comprobante.");
        }
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
