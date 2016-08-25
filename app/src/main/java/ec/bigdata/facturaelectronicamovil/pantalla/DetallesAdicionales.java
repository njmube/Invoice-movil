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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.List;

import ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterInformacionAdicional;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;

public class DetallesAdicionales extends AppCompatActivity implements Validator.ValidationListener, DialogConfirmacion.DialogConfirmacionComunicacion {

    private ClaseGlobalUsuario claseGlobalUsuario;

    @NotEmpty(message = "El nombre es requerido.")
    private EditText editTextNombre;

    @NotEmpty(message = "El valor es requerido.")
    private EditText editTextValor;

    private ListView listViewDetallesAdicionales;

    private Button buttonContinuar;

    private Button buttonAgregarDetalleAdicional;

    private ArrayAdapterInformacionAdicional arrayAdapterInformacionAdicional;

    private ArrayList<InformacionAdicional> informacionAdicionalList;

    private InformacionAdicional informacionAdicional;

    private Validator validator;

    private int identificadorDetalle;

    private DialogFragment dialogConfirmacionBorradoDetallesAdicionales;

    private ActionMode actionMode;

    private List<InformacionAdicional> detallesAdicionalesEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_adicionales);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar_compuesta

        //Enable the Up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_detalle_adicional));

        editTextNombre = (EditText) findViewById(R.id.edit_text_nombre_detalle_adicional);

        editTextValor = (EditText) findViewById(R.id.edit_text_valor_detalle_adicional);

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);

        buttonAgregarDetalleAdicional = (Button) findViewById(R.id.button_agregar_detalle_adicional);

        listViewDetallesAdicionales = (ListView) findViewById(R.id.list_view_detalles_adicionales);

        //Cabecera
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.cabecera_list_view_informacion_adicional, listViewDetallesAdicionales, false);

        listViewDetallesAdicionales.addHeaderView(headerView, null, false);

        informacionAdicionalList = new ArrayList<>();

        arrayAdapterInformacionAdicional = new ArrayAdapterInformacionAdicional(getApplicationContext(), informacionAdicionalList);

        listViewDetallesAdicionales.setAdapter(arrayAdapterInformacionAdicional);
        //Vista vacía.
        View viewVistaVacia = findViewById(R.id.text_view_vista_vacia);
        listViewDetallesAdicionales.setEmptyView(viewVistaVacia);


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
                intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_DETALLES_ADICIONALES), informacionAdicionalList);
                intent.putExtra(String.valueOf(Codigos.CODIGO_ID_DETALLE), identificadorDetalle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            identificadorDetalle = bundle.getInt(String.valueOf(Codigos.CODIGO_ID_DETALLE));
            List<InformacionAdicional> informacionAdicionalListGuardado = (ArrayList<InformacionAdicional>) bundle.getSerializable(String.valueOf(Codigos.CODIGO_LISTA_DETALLES_ADICIONALES));
            if (informacionAdicionalListGuardado != null && !informacionAdicionalListGuardado.isEmpty()) {
                informacionAdicionalList.addAll(informacionAdicionalListGuardado);
                arrayAdapterInformacionAdicional.notifyDataSetChanged();
            }
        }

        //Se activa el modo de selección múltiple.
        listViewDetallesAdicionales.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewDetallesAdicionales.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int totalSeleccionado;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    totalSeleccionado++;
                    arrayAdapterInformacionAdicional.setNuevaSeleccion(position - 1, checked);
                } else {
                    totalSeleccionado--;
                    arrayAdapterInformacionAdicional.quitarSeleccion(position - 1);
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
                        dialogConfirmacionBorradoDetallesAdicionales = DialogConfirmacion.newInstance(
                                getResources().getString(R.string.titulo_confirmacion),
                                getResources().getString(R.string.mensaje_confirmacion_eliminado_detalles_adicionales), 1, Boolean.FALSE);
                        dialogConfirmacionBorradoDetallesAdicionales.show(fragmentManager, "DialogConfirmacionBorradoDetallesAdicionales");
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                arrayAdapterInformacionAdicional.limpiarSeleccion();
            }
        });

        listViewDetallesAdicionales.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewDetallesAdicionales.setItemChecked(position, !arrayAdapterInformacionAdicional.estaPosicionActualSeleccionada(position - 1));
                return false;
            }
        });

    }

    private List<InformacionAdicional> seleccionarDetallesAdicionales() {
        List<InformacionAdicional> informacionAdicionalList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>(arrayAdapterInformacionAdicional.obtenerPosicionActualSeleccionada());
        for (Integer i : ids) {
            informacionAdicionalList.add(this.informacionAdicionalList.get(i));
        }
        return informacionAdicionalList;
    }
    @Override
    public void onValidationSucceeded() {
        if (informacionAdicionalList.size() < 3) {
            informacionAdicional = new InformacionAdicional();
            informacionAdicional.setNombre(editTextNombre.getText().toString());
            informacionAdicional.setValor(editTextValor.getText().toString());
            informacionAdicionalList.add(informacionAdicional);
            arrayAdapterInformacionAdicional.notifyDataSetChanged();
        } else {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Se permiten máximo tres detalles adicionales por detalle.");
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
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

    @Override
    public void presionarBotonSI(int idDialog) {
        switch (idDialog) {
            case 1:
                detallesAdicionalesEliminar = seleccionarDetallesAdicionales();
                informacionAdicionalList.removeAll(detallesAdicionalesEliminar);
                arrayAdapterInformacionAdicional.notifyDataSetChanged();
                break;
        }
        actionMode.finish();
    }

    @Override
    public void presionarBotonCancelar(int idDialog) {
        actionMode.finish();
    }
}
