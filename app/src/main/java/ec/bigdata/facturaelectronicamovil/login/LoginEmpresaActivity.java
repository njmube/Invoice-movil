package ec.bigdata.facturaelectronicamovil.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.menu.NavigationDrawer;
import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestEmpresa;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.PreferenciasUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.ResourceUtils;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginEmpresaActivity extends AppCompatActivity {

    private static final String TAG = LoginEmpresaActivity.class.getSimpleName();

    private EditText editTextNombreUsuario;

    private EditText editTextClave;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ProgressDialog progressDialog;

    PreferenciasUsuario preferenciasUsuario;

    private String nombreUsuario;
    private String claveUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empresa);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        preferenciasUsuario = new PreferenciasUsuario(getApplicationContext());


        editTextNombreUsuario = (EditText) findViewById(R.id.edit_text_nombre_usuario_empresa);
        editTextClave = (EditText) findViewById(R.id.edit_text_clave_usuario_empresa);

        HashMap<String, String> stringHashMap = preferenciasUsuario.obtenerDetallesSesionUsuario();

        nombreUsuario = stringHashMap.get(PreferenciasUsuario.LLAVE_NOMBRE_USUARIO);
        claveUsuario = stringHashMap.get(PreferenciasUsuario.LLAVE_CONTRASENIA);
        if (nombreUsuario != null && claveUsuario != null) {
            validarUsuario(nombreUsuario, claveUsuario);
        }
        editTextClave.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                int idIniciarSesion = getResources().getInteger(R.integer.id_boton_iniciar_sesion);
                return id == idIniciarSesion;
            }
        });
        Button buttonValidarLogin = (Button) findViewById(R.id.button_validar_login_empresa);
        buttonValidarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombreUsuario = editTextNombreUsuario.getText().toString();
                claveUsuario = editTextClave.getText().toString();
                validarUsuario(nombreUsuario, claveUsuario);
            }
        });
    }

    /**
     * Método que valida los campos de nombre de usuario y contraseña
     */
    private void validarUsuario(final String nombreUsuario, final String claveUsuario) {

        // Reset errores.
        editTextNombreUsuario.setError(null);

        editTextClave.setError(null);


        boolean cancel = false;
        View focusView = null;

        if (nombreUsuario != null && claveUsuario != null && !nombreUsuario.trim().equals("") && !claveUsuario.trim().equals("")) {

            ClienteRestEmpresa.ServicioEmpresa servicioEmpresa = ClienteRestEmpresa.getServicioEmpresa();

            progressDialog = DialogProgreso.mostrarDialogProgreso(LoginEmpresaActivity.this);

            Call<ClienteEmpresa> clienteEmpresaCall = servicioEmpresa.validarEmpresa(nombreUsuario, claveUsuario);

            clienteEmpresaCall.enqueue(new Callback<ClienteEmpresa>() {
                @Override
                public void onResponse(Call<ClienteEmpresa> call, Response<ClienteEmpresa> response) {
                    if (response.isSuccessful()) {

                        ClienteEmpresa clienteEmpresa = response.body();
//TODO Revisar forma de guardar sesión del usuario
                        preferenciasUsuario.createSesionUsuario(nombreUsuario, claveUsuario);

                        if (clienteEmpresa.getEstadoClienteEmpresa().equals(Boolean.TRUE)) {

                            //Ambiente de la aplicación
                            claseGlobalUsuario.setAmbiente(getResources().getString(R.string.ambiente));
                            //Tipo de emisión
                            claseGlobalUsuario.setTipoEmision(getResources().getString(R.string.tipo_emision));
                            //Tipos de comprobante
                            claseGlobalUsuario.setTiposComprobantes(new LinkedHashMap<String, String>(ResourceUtils.getHashMapResource(getApplicationContext(), R.xml.tipos_comprobante)));
                            //Impuestos
                            claseGlobalUsuario.setImpuestos(new LinkedHashMap<String, String>(ResourceUtils.getHashMapResource(getApplicationContext(), R.xml.impuestos)));

                            claseGlobalUsuario.setMoneda(getResources().getString(R.string.moneda));
                            //Información de la empresa
                            claseGlobalUsuario.setIdEmpresa(clienteEmpresa.getIdClienteEmpresa());
                            claseGlobalUsuario.setNombreUsuario(clienteEmpresa.getNombreUsuarioClienteEmpresa());
                            claseGlobalUsuario.setClaveUsuario(clienteEmpresa.getClaveUsuarioClienteEmpresa());
                            claseGlobalUsuario.setRazonSocial(clienteEmpresa.getRazonSocialClienteEmpresa());
                            claseGlobalUsuario.setNombreComercial(clienteEmpresa.getNombreComercialClienteEmpresa());
                            claseGlobalUsuario.setDireccionMatriz(clienteEmpresa.getDireccionClienteEmpresa());
                            claseGlobalUsuario.setCorreoPrincipal(clienteEmpresa.getCorreoPrincipalClienteEmpresa());
                            claseGlobalUsuario.setTelefonoPrincipal(clienteEmpresa.getTelefonoPrincipalClienteEmpresa());
                            claseGlobalUsuario.setIdPerfil(String.valueOf(clienteEmpresa.getPerfil().getIdPerfil()));
                            claseGlobalUsuario.setTipoPerfil(clienteEmpresa.getPerfil().getNombrePerfil());
                            claseGlobalUsuario.setObligadoLlevarContabilidad(clienteEmpresa.getObligadoContabilidadClienteEmpresa());
                            if (clienteEmpresa.getNumeroResolucionClienteEmpresa() != null) {
                                claseGlobalUsuario.setNumeroResolucion(clienteEmpresa.getNumeroResolucionClienteEmpresa());
                            } else {
                                claseGlobalUsuario.setNumeroResolucion("");
                            }
                            claseGlobalUsuario.setTipoUsuario(Utilidades.USUARIO_EMPRESA);
                            progressDialog.dismiss();
                            iniciarIntent();

                        } else {
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Su cuenta ha sido desactivada.");

                            editTextNombreUsuario.requestFocus();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ClienteEmpresa> call, Throwable t) {
                    call.cancel();
                    progressDialog.dismiss();
                }
            });


        } else if (nombreUsuario == null && claveUsuario == null) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Por favor ingrese su nombre de usuario y clave para continuar.");

            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (nombreUsuario.equals("") && claveUsuario.equals("")) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Por favor ingrese su nombre de usuario y clave para continuar.");

            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (nombreUsuario.equals("")) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Por favor ingrese su nombre de usuario para continuar.");

            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (claveUsuario.equals("")) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Por favor ingrese su clave para continuar.");

            focusView = editTextClave;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        }
    }

    private void iniciarIntent() {
        Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
