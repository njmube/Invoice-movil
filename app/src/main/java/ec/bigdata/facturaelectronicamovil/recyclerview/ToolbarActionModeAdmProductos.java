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
public class ToolbarActionModeAdmProductos implements ActionMode.Callback {
    private Context context;
    private RecyclerViewAdapterAdmProductos recyclerViewAdapterAdmProductos;

    public ToolbarActionModeAdmProductos(Context context, RecyclerViewAdapterAdmProductos recyclerViewAdapterAdmProductos) {
        this.context = context;
        this.recyclerViewAdapterAdmProductos = recyclerViewAdapterAdmProductos;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_adm_productos, menu);//Infla el menú en ActionMode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // A veces, el menú no será visible por lo que para ello se necesita configurar manualmente su visibilidad en este método
        // Así que aquí se muestra el menú de acciones de acuerdo a los niveles de SDK
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_edicion_producto), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_compartir_producto), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        } else {
            menu.findItem(R.id.item_edicion_producto).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.findItem(R.id.item_compartir_producto).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        AdministracionProductosActividad administracionProductosActividad = (AdministracionProductosActividad) this.context;
        AdministracionProductosFragmento administracionProductosFragmento = (AdministracionProductosFragmento) administracionProductosActividad.obtenerFragment();
        switch (item.getItemId()) {
            case R.id.item_edicion_producto:
                administracionProductosFragmento.editarProducto();
                mode.finish();
                break;
            case R.id.item_compartir_producto:
                administracionProductosFragmento.compartirProductos();
                mode.finish();
                break;

        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerViewAdapterAdmProductos.removerSeleccion();
        AdministracionProductosActividad administracionProductosActividad = (AdministracionProductosActividad) this.context;
        AdministracionProductosFragmento administracionProductosFragmento = (AdministracionProductosFragmento) administracionProductosActividad.obtenerFragment();
        administracionProductosFragmento.setNullToActionMode();
    }
}
