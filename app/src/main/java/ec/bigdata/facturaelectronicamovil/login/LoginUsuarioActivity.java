package ec.bigdata.facturaelectronicamovil.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.adaptador.AdaptadorUsuario;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogProgreso;
import ec.bigdata.facturaelectronicamovil.menu.NavigationDrawer;
import ec.bigdata.facturaelectronicamovil.modelo.UsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.servicio.ClienteRestUsuarioAcceso;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginUsuarioActivity extends AppCompatActivity {

    private static final String TAG = "LoginUsuarioActivity";

    private EditText editTextNombreUsuario;

    private EditText editTextClave;

    private ClaseGlobalUsuario claseGlobalUsuario;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_login_usuario);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();

        editTextNombreUsuario = (EditText) findViewById(R.id.edit_text_nombre_usuario_persona);
        editTextClave = (EditText) findViewById(R.id.edit_text_clave_usuario_persona);
        editTextClave.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                int id_iniciar_sesion = getResources().getInteger(R.integer.id_boton_iniciar_sesion);
                if (id == id_iniciar_sesion) {
                    validarUsuario();
                    return true;
                }
                return false;
            }
        });
        Button buttonValidarLogin = (Button) findViewById(R.id.button_validar_login_persona);
        buttonValidarLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validarUsuario();
            }
        });


    }

    /**
     * Método que valida los campos de nombre de usuario y contraseña
     */
    private void validarUsuario() {

        // Reset errores.
        editTextNombreUsuario.setError(null);
        editTextClave.setError(null);


        String nombre_usuario = editTextNombreUsuario.getText().toString();
        String password = editTextClave.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (nombre_usuario != null && password != null && !nombre_usuario.equals("") && !password.equals("")) {
            Gson gson = new GsonBuilder().registerTypeAdapter(UsuarioAcceso.class, new AdaptadorUsuario()).setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(5, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Utilidades.obtenerURLWebService()).addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();

            ClienteRestUsuarioAcceso.ServicioUsuarioAcceso servicioUsuarioAcceso = retrofit.create(ClienteRestUsuarioAcceso.ServicioUsuarioAcceso.class);
            Call<UsuarioAcceso> call = servicioUsuarioAcceso.validarUsuarioAcceso(nombre_usuario, password);
            progressDialog = DialogProgreso.mostrarDialogProgreso(LoginUsuarioActivity.this);
            call.enqueue(new Callback<UsuarioAcceso>() {
                @Override
                public void onResponse(Call<UsuarioAcceso> call, Response<UsuarioAcceso> response) {
                    if (response.isSuccessful()) {
                        UsuarioAcceso usuarioAcceso = response.body();
                        if (usuarioAcceso.getEstadoUsuario().equals("1")) {

                            claseGlobalUsuario.setIdUsuario(String.valueOf(usuarioAcceso.getIdUsuario()));
                            claseGlobalUsuario.setIdentificacionUsuario(usuarioAcceso.getIdentificacionUsuario());
                            claseGlobalUsuario.setNombreUsuario(usuarioAcceso.getNombreUsuarioAcceso());
                            claseGlobalUsuario.setClaveUsuario(usuarioAcceso.getClaveUsuarioAcceso());
                            claseGlobalUsuario.setNombres(usuarioAcceso.getNombreUsuario());
                            claseGlobalUsuario.setApellidos(usuarioAcceso.getApellidoUsuario());
                            claseGlobalUsuario.setCorreoPrincipal(usuarioAcceso.getCorreoPrincipalUsuario());
                            claseGlobalUsuario.setTipoUsuario(Utilidades.USUARIO_PERSONA);
                            if (usuarioAcceso.getCorreoAdicionalUsuario() != null) {
                                claseGlobalUsuario.setCorreoAdicional(usuarioAcceso.getCorreoAdicionalUsuario());
                            } else {
                                claseGlobalUsuario.setCorreoAdicional("");
                            }
                            claseGlobalUsuario.setTelefonoPrincipal(usuarioAcceso.getTelefonoPrincipalUsuario());
                            if (usuarioAcceso.getTelefonoAdicionalUsuario() != null) {
                                claseGlobalUsuario.setTelefonoAdicional(usuarioAcceso.getTelefonoAdicionalUsuario());
                            } else {
                                claseGlobalUsuario.setTelefonoAdicional("0");
                            }
                            claseGlobalUsuario.setIdPerfil(String.valueOf(usuarioAcceso.getPerfil().getIdPerfil()));
                            claseGlobalUsuario.setTipoPerfil(usuarioAcceso.getPerfil().getNombrePerfil());

                            progressDialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), NavigationDrawer.class);
                            startActivity(intent);
                            finish();
                        } else {
                            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Su cuenta ha sido desactivada.");
                            editTextNombreUsuario.requestFocus();
                        }
                    } else {
                        MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "El usuario no existe.");

                        editTextNombreUsuario.requestFocus();

                    }
                }

                @Override
                public void onFailure(Call<UsuarioAcceso> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });


        } else if (nombre_usuario == null && password == null) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "Por favor ingrese su nombre de usuario y clave para continuar.");
            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (nombre_usuario.equals("") && password.equals("")) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Por favor ingrese su nombre de usuario y clave para continuar.");
            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (nombre_usuario.equals("")) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Por favor ingrese su nombre de usuario para continuar.");
            focusView = editTextNombreUsuario;
            cancel = true;
        } else if (password.equals("")) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ERROR, "Por favor ingrese su clave para continuar.");

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

