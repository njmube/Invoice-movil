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
public class ToolbarActionModeComprobantesEmitidos implements ActionMode.Callback {
    private Context context;
    private RecyclerViewAdapterComprobantesEmitidos recyclerViewAdapterComprobantesEmitidos;


    public ToolbarActionModeComprobantesEmitidos(Context context, RecyclerViewAdapterComprobantesEmitidos recyclerViewAdapterComprobantesEmitidos) {
        this.context = context;
        this.recyclerViewAdapterComprobantesEmitidos = recyclerViewAdapterComprobantesEmitidos;

    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.menu_comprobantes_emitidos, menu);//Infla el menú en ActionMode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        // A veces, el menú no será visible por lo que para ello se necesita configurar manualmente su visibilidad en este método
        // Así que aquí se muestra el menú de acciones de acuerdo a los niveles de SDK
        if (Build.VERSION.SDK_INT < 11) {
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_informacion_comprobante), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_reenviar_comprobante), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_guardar_comprobante), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            MenuItemCompat.setShowAsAction(menu.findItem(R.id.item_compartir_comprobante), MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);

        } else {
            menu.findItem(R.id.item_informacion_comprobante).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.findItem(R.id.item_reenviar_comprobante).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.findItem(R.id.item_guardar_comprobante).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
            menu.findItem(R.id.item_compartir_comprobante).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        ComprobantesEmitidosAutorizados comprobantesEmitidosAutorizados = (ComprobantesEmitidosAutorizados) this.context;
        ComprobantesEmitidosAutorizadosFragmento comprobantesEmitidosAutorizadosFragmento = (ComprobantesEmitidosAutorizadosFragmento) comprobantesEmitidosAutorizados.obtenerFragment();
        switch (item.getItemId()) {
            case R.id.item_informacion_comprobante:
                comprobantesEmitidosAutorizadosFragmento.mostrarDetalleComprobante();
                mode.finish();
                break;
            case R.id.item_reenviar_comprobante:
                comprobantesEmitidosAutorizadosFragmento.reenviarComprobante();
                mode.finish();
                break;
            case R.id.item_guardar_comprobante:
                comprobantesEmitidosAutorizadosFragmento.descargarComprobanteElectronico();
                mode.finish();
                break;
            case R.id.item_compartir_comprobante:
                comprobantesEmitidosAutorizadosFragmento.compartirComprobante();
                mode.finish();
                break;

        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerViewAdapterComprobantesEmitidos.removerSeleccion();
        ComprobantesEmitidosAutorizados comprobantesEmitidosAutorizados = (ComprobantesEmitidosAutorizados) this.context;
        ComprobantesEmitidosAutorizadosFragmento comprobantesEmitidosAutorizadosFragmento = (ComprobantesEmitidosAutorizadosFragmento) comprobantesEmitidosAutorizados.obtenerFragment();
        comprobantesEmitidosAutorizadosFragmento.setNullToActionMode();
    }
}
