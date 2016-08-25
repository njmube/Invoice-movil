package ec.bigdata.facturaelectronicamovil.recyclerview;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import ec.bigdata.facturaelectronicamovil.R;

/**
 * Created by DavidLeonardo on 26/7/2016.
 */
public class ToolbarActionModeAdmClientes implements ActionMode.Callback {
    private Context context;
    private RecyclerViewAdapterAdmClientes recyclerViewAdapterAdmClientes;


    public ToolbarActionModeAdmClientes(Context context, RecyclerViewAdapterAdmClientes recyclerViewAdapterAdmClientes) {
        this.context = context;
        this.recyclerViewAdapterAdmClientes = recyclerViewAdapterAdmClientes;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_adm_clientes, menu);//Infla el menú en ActionMode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // A veces, el menú no será visible por lo que para ello se necesita configurar manualmente su visibilidad en este método
        // Así que aquí se muestra el menú de acciones de acuerdo a los niveles de SDK
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_edicion_cliente), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_llamar_cliente), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_enviar_correo_cliente), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

        } else {
            menu.findItem(R.id.item_edicion_cliente).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.findItem(R.id.item_llamar_cliente).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.findItem(R.id.item_enviar_correo_cliente).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        AdministracionClientesActividad administracionClientes = (AdministracionClientesActividad) this.context;
        AdministracionClientesFragmento administracionClientesFragmento = (AdministracionClientesFragmento) administracionClientes.obtenerFragment();
        switch (item.getItemId()) {
            case R.id.item_edicion_cliente:
                administracionClientesFragmento.editarCliente();
                mode.finish();
                break;
            case R.id.item_llamar_cliente:
                administracionClientesFragmento.llamarCliente();
                mode.finish();
                break;
            case R.id.item_compartir_cliente:
                administracionClientesFragmento.compartirCliente();
                mode.finish();
                break;
            case R.id.item_enviar_correo_cliente:
                administracionClientesFragmento.enviarCorreoCliente();
                mode.finish();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerViewAdapterAdmClientes.removerSeleccion();
        AdministracionClientesActividad administracionClientes = (AdministracionClientesActividad) this.context;
        AdministracionClientesFragmento administracionClientesFragmento = (AdministracionClientesFragmento) administracionClientes.obtenerFragment();
        administracionClientesFragmento.setNullToActionMode();
    }
}
