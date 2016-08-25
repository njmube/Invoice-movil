package ec.bigdata.facturaelectronicamovil.pantalla;

import android.app.ProgressDialog;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.ArrayAdapterInformacionAdicional;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.modelo.CorreoAdicional;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCorreoAdicionalCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformacionAdicional extends AppCompatActivity implements Validator.ValidationListener, DialogConfirmacion.DialogConfirmacionComunicacion {


    @NotEmpty(message = "El nombre es requerido.")
    private EditText editTextNombre;

    @NotEmpty(message = "El valor es requerido.")
    private EditText editTextValor;

    private ListView listViewDetallesAdicionales;

    private Switch switchInformacionAdicionalPersonalCliente;

    private Switch switchCorreosAdicionalesCliente;

    private Button buttonContinuar;

    private Button buttonAgregarDetalleAdicional;

    private ArrayAdapterInformacionAdicional arrayAdapterInformacionAdicional;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalList;

    private ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional informacionAdicional;

    private Validator validator;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado;

    private ClienteRestCorreoAdicionalCliente.ServicioCorreoAdicional servicioCorreoAdicional;

    private List<CorreoAdicional> correosAdicionales;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalListCorreosAdicionales;

    private ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalListPersonalCliente;


    private ClaseGlobalUsuario claseGlobalUsuario;

    private ActionMode actionMode;

    private DialogFragment dialogFragmentEliminarInfoAdicional;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_adicional);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_informacion_adicional));

        switchInformacionAdicionalPersonalCliente = (Switch) findViewById(R.id.switch_informacion_adicional_personal_cliente);

        switchCorreosAdicionalesCliente = (Switch) findViewById(R.id.switch_correos_adicionales_cliente);

        editTextNombre = (EditText) findViewById(R.id.edit_text_nombre_informacion_adicional);

        editTextValor = (EditText) findViewById(R.id.edit_text_valor_informacion_adicional);

        buttonContinuar = (Button) toolbar.findViewById(R.id.button_continuar);

        buttonAgregarDetalleAdicional = (Button) findViewById(R.id.button_agregar_informacion_adicional);

        listViewDetallesAdicionales = (ListView) findViewById(R.id.list_view_informacion_adicional);

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        servicioCorreoAdicional = ClienteRestCorreoAdicionalCliente.getServicioCliente();

        //Cabecera
        ViewGroup headerView = (ViewGroup) getLayoutInflater().inflate(R.layout.cabecera_list_view_informacion_adicional, listViewDetallesAdicionales, false);

        listViewDetallesAdicionales.addHeaderView(headerView, null, false);

        informacionAdicionalList = new ArrayList<>();

        informacionAdicionalListCorreosAdicionales = new ArrayList<>();

        informacionAdicionalListPersonalCliente = new ArrayList<>();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            if (bundle.containsKey(String.valueOf(Codigos.CODIGO_LISTA_INFORMACION_ADICIONAL_SELECCIONADA))) {
                informacionAdicionalList = (ArrayList<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional>) bundle.getSerializable(String.valueOf(Codigos.CODIGO_LISTA_INFORMACION_ADICIONAL_SELECCIONADA));
            }


            if (bundle.containsKey(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO))) {
                clienteSeleccionado = (Cliente) bundle.getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO));
                if (clienteSeleccionado != null) {
                    progressDialog = DialogProgreso.mostrarDialogProgreso(InformacionAdicional.this);
                    Call<List<CorreoAdicional>> listCall = servicioCorreoAdicional.obtenerCorreosAdicionalesPorCliente(clienteSeleccionado.getIdCliente());
                    listCall.enqueue(new Callback<List<CorreoAdicional>>() {
                        @Override
                        public void onResponse(Call<List<CorreoAdicional>> call, Response<List<CorreoAdicional>> response) {
                            if (response.isSuccessful()) {
                                correosAdicionales = response.body();
                                if (correosAdicionales != null && !correosAdicionales.isEmpty()) {
                                    for (int i = 0; i < correosAdicionales.size(); i++) {

                                        ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional informacionAdicional = new ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional();
                                        informacionAdicional.setNombre("CORREO_ADICIONAL" + i);
                                        informacionAdicional.setValor(correosAdicionales.get(i).getCorreoElectronicoCorreoAdicional());
                                        informacionAdicionalListCorreosAdicionales.add(informacionAdicional);
                                    }
                                }

                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<List<CorreoAdicional>> call, Throwable t) {
                            call.cancel();
                            progressDialog.dismiss();
                        }

                    });

                    if (clienteSeleccionado.getTelefonoCliente() != null && !clienteSeleccionado.getTelefonoCliente().equals("")) {
                        ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional informacionAdicional = new ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional();
                        informacionAdicional.setNombre("TELEFONO");
                        informacionAdicional.setValor(clienteSeleccionado.getTelefonoCliente());
                        informacionAdicionalListPersonalCliente.add(informacionAdicional);
                    }

                    if (clienteSeleccionado.getCorreoElectronicoCliente() != null && !clienteSeleccionado.getCorreoElectronicoCliente().equals("")) {
                        ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional informacionAdicional = new ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional();
                        informacionAdicional.setNombre("CORREO_PRINCIPAL");
                        informacionAdicional.setValor(clienteSeleccionado.getCorreoElectronicoCliente());
                        informacionAdicionalListPersonalCliente.add(informacionAdicional);
                    }
                }
            }
        }

        arrayAdapterInformacionAdicional = new ArrayAdapterInformacionAdicional(getApplicationContext(), informacionAdicionalList);

        listViewDetallesAdicionales.setAdapter(arrayAdapterInformacionAdicional);
        //Vista vacía
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
                intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_INFORMACION_ADICIONAL_SELECCIONADA), informacionAdicionalList);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
        switchCorreosAdicionalesCliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (informacionAdicionalListCorreosAdicionales != null && !informacionAdicionalListCorreosAdicionales.isEmpty()) {
                        for (ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional i : informacionAdicionalListCorreosAdicionales) {
                            if (informacionAdicionalList.size() < 15) {
                                informacionAdicionalList.add(i);

                            } else {
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "El máximo es de 15 detalles en información adicional por comprobante.");
                            }
                        }
                        Set<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> linkedHashSet = new LinkedHashSet<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional>();
                        linkedHashSet.addAll(informacionAdicionalList);
                        informacionAdicionalList.clear();
                        informacionAdicionalList.addAll(linkedHashSet);
                        arrayAdapterInformacionAdicional.notifyDataSetChanged();
                    }
                } else {
                    informacionAdicionalList.removeAll(informacionAdicionalListCorreosAdicionales);
                    arrayAdapterInformacionAdicional.notifyDataSetChanged();
                }


            }
        });

        switchInformacionAdicionalPersonalCliente.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    if (clienteSeleccionado != null) {
                        for (ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional i : informacionAdicionalListPersonalCliente) {
                            if (informacionAdicionalList.size() < 15) {
                                informacionAdicionalList.add(i);

                            } else {
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "El máximo es de 15 detalles en información adicional por comprobante.");
                            }
                        }
                        Set<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> linkedHashSet = new LinkedHashSet<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional>();
                        linkedHashSet.addAll(informacionAdicionalList);
                        informacionAdicionalList.clear();
                        informacionAdicionalList.addAll(linkedHashSet);
                        arrayAdapterInformacionAdicional.notifyDataSetChanged();
                    }
                } else {
                    informacionAdicionalList.removeAll(informacionAdicionalListPersonalCliente);
                    arrayAdapterInformacionAdicional.notifyDataSetChanged();
                }
            }
        });


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
                        dialogFragmentEliminarInfoAdicional = DialogConfirmacion.newInstance(
                                getResources().getString(R.string.titulo_confirmacion),
                                getResources().getString(R.string.mensaje_confirmacion_eliminado_detalles_adicionales), 1, Boolean.FALSE);
                        dialogFragmentEliminarInfoAdicional.show(fragmentManager, "DialogEliminarInfoAdicional");
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
                listViewDetallesAdicionales.setItemChecked(position - 1, !arrayAdapterInformacionAdicional.estaPosicionActualSeleccionada(position));
                return false;
            }
        });
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

    private List<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> seleccionarInformacionAdicional() {
        List<ec.bigdata.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional> informacionAdicionalArrayList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>(arrayAdapterInformacionAdicional.obtenerPosicionActualSeleccionada());
        for (Integer i : ids) {
            informacionAdicionalArrayList.add(this.informacionAdicionalList.get(i));
        }
        return informacionAdicionalArrayList;
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
        Intent intent = new Intent(getApplicationContext(), InicioFacturacionElectronica.class);
        intent.putExtra(String.valueOf(Codigos.CODIGO_LISTA_INFORMACION_ADICIONAL_SELECCIONADA), informacionAdicionalList);

        setResult(RESULT_OK, intent);
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    public void presionarBotonSI(int identificadorDialog) {
        informacionAdicionalList.removeAll(seleccionarInformacionAdicional());
        arrayAdapterInformacionAdicional.notifyDataSetChanged();
        actionMode.finish();
    }

    @Override
    public void presionarBotonCancelar(int identificadorDialog) {
        actionMode.finish();
    }
}
