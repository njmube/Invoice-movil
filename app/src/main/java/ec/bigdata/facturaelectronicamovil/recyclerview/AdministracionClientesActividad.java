package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import de.cketti.mailto.EmailIntentBuilder;
import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogEnvioCorreo;
import ec.bigdata.facturaelectronicamovil.dialogs.DialogSpinner;
import ec.bigdata.facturaelectronicamovil.interfaz.AdmClientesComunicacionFragmentInterface;
import ec.bigdata.facturaelectronicamovil.modelo.Cliente;
import ec.bigdata.facturaelectronicamovil.pantalla.ActualizacionCliente;
import ec.bigdata.facturaelectronicamovil.pantalla.NuevoCliente;
import ec.bigdata.facturaelectronicamovil.personalizacion.MensajePersonalizado;
import ec.bigdata.facturaelectronicamovil.utilidad.ClaseGlobalUsuario;
import ec.bigdata.facturaelectronicamovil.utilidad.Codigos;
import ec.bigdata.facturaelectronicamovil.utilidad.Utilidades;

public class AdministracionClientesActividad extends AppCompatActivity implements DialogSpinner.DialogSpinnerComunicacion, DialogEnvioCorreo.DialogEnvioCorreoComunicacion {

    private AdministracionClientesFragmento administracionClientesFragmento;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FloatingActionButton floatingActionButton;
    private DialogFragment dialogFragmentSpinnerTelefonosCliente;
    private ClaseGlobalUsuario claseGlobalUsuario;
    private Cliente clienteSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_adm_clientes);
        claseGlobalUsuario = (ClaseGlobalUsuario) getApplicationContext();
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button_ir_inicio);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdmClientesComunicacionFragmentInterface admClientesComunicacionFragmentInterface
                        = administracionClientesFragmento;
                admClientesComunicacionFragmentInterface.irAInicio();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_simple);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView tituloToolbar = (TextView) toolbar.findViewById(R.id.text_view_titulo_toolbar_simple);

        tituloToolbar.setText(getResources().getString(R.string.titulo_administracion_clientes));

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        administracionClientesFragmento = new AdministracionClientesFragmento();
        fragmentTransaction.replace(R.id.frame_layout_adm_clientes, administracionClientesFragmento, "AdmClientesFragment");
        fragmentTransaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nuevo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.item_nuevo:
                Intent intent = new Intent(this, NuevoCliente.class);
                startActivityForResult(intent, Codigos.CODIGO_CLIENTE_NUEVO);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(administracionClientesFragmento);
        fragmentTransaction.commit();
        this.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    public Fragment obtenerFragment() {
        return administracionClientesFragmento;
    }

    public void compartirCliente(List<Cliente> clientesSeleccionados) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Utilidades.formaterListaClientesACompartir(clientesSeleccionados));
        startActivity(Intent.createChooser(sharingIntent, "Compartir información de clientes."));
    }

    public void editarCliente(Cliente clienteSeleccionado) {
        Intent intent = new Intent(this, ActualizacionCliente.class);
        intent.putExtra(String.valueOf(Codigos.CODIGO_CLIENTE_SELECCIONADO), clienteSeleccionado);
        startActivityForResult(intent, Codigos.CODIGO_ADM_CLIENTES_SELECCIONADO);
        overridePendingTransition(R.anim.left_in, R.anim.left_out);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Codigos.CODIGO_ADM_CLIENTES_SELECCIONADO) {
            if (resultCode == RESULT_OK) {
                ec.bigdata.facturaelectronicamovil.modelo.Cliente clienteSeleccionado = (ec.bigdata.facturaelectronicamovil.modelo.Cliente) data.getExtras().getSerializable(String.valueOf(Codigos.CODIGO_CLIENTE_ACTUALIZADO));
                if (clienteSeleccionado != null) {
                    administracionClientesFragmento.actualizarClienteRecyclerView(clienteSeleccionado);
                }
            }
        } else if (requestCode == Codigos.CODIGO_CLIENTE_NUEVO) {
            if (resultCode == RESULT_OK) {
                //TODO: Definir que se hace en el recyclerview cuando se registra un nuevo cliente.
            }
        }
    }


    public void cargarTelefonosCliente(String[] telefonosCliente) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragmentSpinnerTelefonosCliente = DialogSpinner.newInstance(getResources().getString(R.string.titulo_telefonos_cliente), telefonosCliente);
        dialogFragmentSpinnerTelefonosCliente.show(fragmentManager, "DialogTelefonosCliente");
    }

    @Override
    public void enviarItemSeleccionado(String itemSeleccionado) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + itemSeleccionado));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    public void mostrarDialogEnvioCorreoCliente(Cliente cliente) {

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        dialogFragmentSpinnerTelefonosCliente = DialogEnvioCorreo.newInstance(cliente.getCorreoElectronicoCliente());
        dialogFragmentSpinnerTelefonosCliente.show(fragmentManager, "DialogEnvioCorreoCliente");
    }

    @Override
    public void enviarCorreo(String correoElectronicoPara, String asunto, String contenido) {
        boolean resultado = EmailIntentBuilder.from(this)
                .to(correoElectronicoPara)
                .subject(asunto)
                .body(contenido)
                .start();
        if (!resultado) {
            MensajePersonalizado.mostrarToastPersonalizado(getApplicationContext(), getLayoutInflater(), MensajePersonalizado.TOAST_ADVERTENCIA, "No se ha encontrado una aplicación para el envío de correos.");
        }
    }
}
