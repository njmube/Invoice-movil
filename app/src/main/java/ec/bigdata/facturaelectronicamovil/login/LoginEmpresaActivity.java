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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.interfaces.ServicioEmpresa;
import ec.bigdata.facturaelectronicamovil.menu.NavigationDrawer;
import ec.bigdata.facturaelectronicamovil.modelo.ClienteEmpresa;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestEmpresa;
import ec.bigdata.facturaelectronicamovil.utilidades.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidades.ResourceUtils;
import ec.bigdata.facturaelectronicamovil.utilidades.Utilidades;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginEmpresaActivity extends AppCompatActivity {

    private static final String TAG = LoginEmpresaActivity.class.getSimpleName();

    private EditText editTextNombreUsuario;

    private EditText editTextClave;

    private ProgressBar progressBar;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empresa);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        editTextNombreUsuario = (EditText) findViewById(R.id.edit_text_nombre_usuario_empresa);
        editTextClave = (EditText) findViewById(R.id.edit_text_clave_usuario_empresa);
        editTextClave.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                int idIniciarSesion = getResources().getInteger(R.integer.id_boton_iniciar_sesion);
                if (id == idIniciarSesion) {
                    validarUsuario();
                    return true;
                }
                return false;
            }
        });
        Button buttonValidarLogin = (Button) findViewById(R.id.button_validar_login_empresa);
        buttonValidarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_login_empresa);

    }

    /**
     * Método que valida los campos de nombre de usuario y contraseña
     */
    private void validarUsuario() {

        // Reset errores.
        editTextNombreUsuario.setError(null);
        editTextClave.setError(null);


        String nombreUsuario = editTextNombreUsuario.getText().toString();
        String password = editTextClave.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (nombreUsuario != null && password != null && !nombreUsuario.equals("") && !password.equals("")) {

            ServicioEmpresa servicioEmpresa = ClienteRestEmpresa.getServicioEmpresa();

            progressDialog = ProgressDialog.show(LoginEmpresaActivity.this,
                    "Conectando",
                    "Espere por favor...");

            Call<ClienteEmpresa> clienteEmpresaCall = servicioEmpresa.validarEmpresa(nombreUsuario, password);

            clienteEmpresaCall.enqueue(new Callback<ClienteEmpresa>() {
                @Override
                public void onResponse(Call<ClienteEmpresa> call, Response<ClienteEmpresa> response) {
                    if (response.isSuccessful()) {

                        ClienteEmpresa clienteEmpresa = response.body();
                        if (clienteEmpresa.getEstadoClienteEmpresa().equals(Boolean.TRUE)) {

                            claseGlobalUsuario.setImpuestos(new LinkedHashMap<String, String>(ResourceUtils.getHashMapResource(getApplicationContext(), R.xml.impuestos)));
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
                            Intent intent_navigation_drawer = new Intent(getApplicationContext(), NavigationDrawer.class);
                            startActivity(intent_navigation_drawer);
                            finish();
                        } else {

                            Toast.makeText(LoginEmpresaActivity.this, "Su cuenta ha sido desactivada..", Toast.LENGTH_SHORT).show();
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


        } else if (nombreUsuario == null && password == null) {
            Toast.makeText(LoginEmpresaActivity.this, "Por favor ingrese su nombre de usuario y clave para continuar.", Toast.LENGTH_SHORT).show();
            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (nombreUsuario.equals("") && password.equals("")) {
            Toast.makeText(LoginEmpresaActivity.this, "Por favor ingrese su nombre de usuario y clave para continuar.", Toast.LENGTH_SHORT).show();
            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (nombreUsuario.equals("")) {
            Toast.makeText(LoginEmpresaActivity.this, "Por favor ingrese su nombre de usuario para continuar.", Toast.LENGTH_SHORT).show();
            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (password.equals("")) {
            Toast.makeText(LoginEmpresaActivity.this, "Por favor ingrese su clave para continuar.", Toast.LENGTH_SHORT).show();
            focusView = editTextClave;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
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
