package ec.bigdata.facturaelectronicamovil.pantalla;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.AdaptadorCorreoAdicionalCliente;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogConfirmacion;
import ec.bigdata.facturaelectronicamovil.modelo.CorreoAdicional;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestCorreoAdicionalCliente;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorreosAdicionalesCliente extends AppCompatActivity implements Validator.ValidationListener, DialogConfirmacion.DialogConfirmacionComunicacion {

    @Order(value = 1)
    @NotEmpty(message = "El correo electronico es requerido.", sequence = 1)
    @Email(message = "El correo electrónico no es válido.", sequence = 2)
    private EditText editTextCorreoAdicional;

    private Button buttonAgregarCorreoAdicional;

    private List<CorreoAdicional> correosAdicionales;

    private ListView listViewCorreosAdicionales;

    private AdaptadorCorreoAdicionalCliente adaptadorCorreoAdicionalCliente;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ClienteRestCorreoAdicionalCliente.ServicioCorreoAdicional servicioCorreoAdicional;

    private Validator validator;

    private ec.bigdata.facturaelectronicamovil.modelo.Cliente cliente;

    private DialogFragment dialogFragmentConfirmacionEliminadoCorreosAdicionales;

    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correos_adicionales_cliente);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_compuesta);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar);

        tituloToolbar.setText(getResources().getString(R.string.titulo_correos_adicionales_cliente));

        TextView textViewClienteAsociadoCorreosAdicionales = (TextView) findViewById(R.id.text_view_cliente_asociado_correos_adicionales);

        editTextCorreoAdicional = (EditText) findViewById(R.id.edit_text_correo_adicional_cliente);

        buttonAgregarCorreoAdicional = (Button) findViewById(R.id.button_agregar_correo_adicional);

        listViewCorreosAdicionales = (ListView) findViewById(R.id.list_view_correos_adicionales);

        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        servicioCorreoAdicional = ClienteRestCorreoAdicionalCliente.getServicioCliente();

        correosAdicionales = new ArrayList<>();

        validator = new Validator(this);
        validator.setValidationListener(this);

        //Se recibe el id del cliente

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cliente = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) bundle.getSerializable((String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO)));

            textViewClienteAsociadoCorreosAdicionales.setText(getResources().getString(R.string.titulo_cliente_seleccionado) + " " + Utilidades.obtenerNombreCliente(cliente));
        }
        Call<List<CorreoAdicional>> listCall = servicioCorreoAdicional.obtenerCorreosAdicionalesPorCliente(cliente.getIdCliente());
        listCall.enqueue(new Callback<List<CorreoAdicional>>() {
            @Override
            public void onResponse(Call<List<CorreoAdicional>> call, Response<List<CorreoAdicional>> response) {
                if (response.isSuccessful()) {
                    correosAdicionales = response.body();

                    adaptadorCorreoAdicionalCliente = new AdaptadorCorreoAdicionalCliente(getApplicationContext(), correosAdicionales);
                    listViewCorreosAdicionales.setAdapter(adaptadorCorreoAdicionalCliente);

                }
            }

            @Override
            public void onFailure(Call<List<CorreoAdicional>> call, Throwable t) {
                call.cancel();
            }
        });

        buttonAgregarCorreoAdicional.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        listViewCorreosAdicionales.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewCorreosAdicionales.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private int totalSeleccionado;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    totalSeleccionado++;
                    adaptadorCorreoAdicionalCliente.setNuevaSeleccion(position, checked);
                } else {
                    totalSeleccionado--;
                    adaptadorCorreoAdicionalCliente.quitarSeleccion(position);
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
                        dialogFragmentConfirmacionEliminadoCorreosAdicionales = DialogConfirmacion.newInstance(
                                getResources().getString(R.string.titulo_confirmacion),
                                getResources().getString(R.string.mensaje_confirmacion_eliminado_correos_adicionales) + " " + Utilidades.formatearCorreosAdicionalesEliminacion(seleccionarCorreosAdicionales()), Boolean.TRUE);
                        dialogFragmentConfirmacionEliminadoCorreosAdicionales.show(fragmentManager, "DialogConfirmacionBorradoCorreosAdicionales");
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                adaptadorCorreoAdicionalCliente.limpiarSeleccion();
            }
        });

        listViewCorreosAdicionales.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listViewCorreosAdicionales.setItemChecked(position, !adaptadorCorreoAdicionalCliente.estaPosicionActualSeleccionada(position));
                return false;
            }
        });

    }

    private List<CorreoAdicional> seleccionarCorreosAdicionales() {
        List<CorreoAdicional> correosAdicionales = new ArrayList<>();
        List<Integer> ids = new ArrayList<>(adaptadorCorreoAdicionalCliente.obtenerPosicionActualSeleccionada());
        for (Integer i : ids) {
            correosAdicionales.add(this.correosAdicionales.get(i));
        }
        return correosAdicionales;
    }

    private void agregarCorreo() {
        Call<ResponseBody> responseBodyCall = servicioCorreoAdicional.guardarCorreoAdicional(cliente.getIdCliente(), editTextCorreoAdicional.getText().toString());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    String contenido = null;
                    try {
                        contenido = new String(response.body().bytes());
                        JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                        if (jsonObject.get("estado").getAsBoolean() == true) {
                            correosAdicionales.add(new CorreoAdicional(jsonObject.get("identificador").getAsInt(), editTextCorreoAdicional.getText().toString()));
                            adaptadorCorreoAdicionalCliente.notifyDataSetChanged();
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Correo adicional agregado.");
                        } else {
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, jsonObject.get("mensajeError").getAsString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Error al guardar el correo adicional.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Error al guardar el correo adicional.");
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        agregarCorreo();
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

    @Override
    public void presionarBotonSI() {
        final List<CorreoAdicional> correoAdicionalList = seleccionarCorreosAdicionales();
        if (correoAdicionalList != null && !correoAdicionalList.isEmpty()) {
            Call<ResponseBody> responseBodyCall = servicioCorreoAdicional.eliminarCorreosAdicionales(correoAdicionalList);
            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        String contenido = null;
                        try {
                            contenido = new String(response.body().bytes());
                            JsonObject jsonObject = new JsonParser().parse(contenido).getAsJsonObject();
                            if (jsonObject.get("estado").getAsBoolean() == true) {
                                actionMode.finish();
                                correosAdicionales.removeAll(correoAdicionalList);
                                adaptadorCorreoAdicionalCliente.notifyDataSetChanged();
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_INFORMACION, "Correo adicionales eliminados.");
                            } else {
                                MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, jsonObject.get("mensajeError").getAsString());
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Correo adicionales no eliminados.");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    call.cancel();
                    MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Correo adicionales no eliminados.");
                }
            });
        }
    }

    @Override
    public void presionarBotonCancelar() {
        actionMode.finish();
    }
}
