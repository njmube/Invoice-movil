package ec.bigdata.facturaelectronicamovil.adaptador;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import ec.bigdata.facturaelectronicamovil.R;
import ec.bigdata.facturaelectronicamovil.modelo.CorreoAdicional;

/**
 * Created by DavidLeonardo on 2/8/2016.
 */
public class AdaptadorCorreoAdicionalCliente<T> extends ArrayAdapter<CorreoAdicional> {

    private HashMap<Integer, Boolean> selecccionados;


    public AdaptadorCorreoAdicionalCliente(Context context, List<CorreoAdicional> correoAdicionalList) {
        super(context, 0, correoAdicionalList);

        selecccionados = new HashMap<Integer, Boolean>();
    }

    public void setNuevaSeleccion(int position, boolean value) {
        selecccionados.put(position, value);
        notifyDataSetChanged();
    }

    public boolean estaPosicionActualSeleccionada(int position) {
        Boolean result = selecccionados.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> obtenerPosicionActualSeleccionada() {
        return selecccionados.keySet();
    }

    public void quitarSeleccion(int position) {
        selecccionados.remove(position);
        notifyDataSetChanged();
    }

    public void limpiarSeleccion() {
        selecccionados = new HashMap<>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Obteniendo una instancia del inflater
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Salvando la referencia del View de la fila
        View listItemView = convertView;

        //Comprobando si el View no existe.
        if (null == convertView) {
            //Si no existe, entonces inflarlo
            listItemView = inflater.inflate(
                    R.layout.estilo_list_view_correos_adicionales_cliente,
                    parent,
                    false);
        }
        //Obteniendo instancias de los TextView
        TextView correoAdicional = (TextView) listItemView.findViewById(R.id.text_view_correo_adicional_cliente);

        TextView tipoCliente = (TextView) listItemView.findViewById(R.id.text_view_tipo_cliente_correo_adicional);

        //Obteniendo instancia del CorreoAdicional en la posición actual
        CorreoAdicional item = getItem(position);
        correoAdicional.setText(item.getCorreoElectronicoCorreoAdicional());
        tipoCliente.setText(item.getTipoClienteCorreoAdicional().equals(Boolean.TRUE) ? "CLIENTE" : "PROVEEDOR");

        //Color cuando se seleccione un item
        listItemView.setBackgroundColor(selecccionados.get(position) != null ? 0x9934B5E4
                : Color.TRANSPARENT);

        return listItemView;
    }
}
